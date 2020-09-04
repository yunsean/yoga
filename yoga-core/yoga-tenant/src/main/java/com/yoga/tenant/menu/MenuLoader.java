package com.yoga.tenant.menu;

import com.yoga.core.data.ResourceLoader;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.spring.SpringContextUtil;
import com.yoga.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;

public class MenuLoader {
    static Logger logger = LoggerFactory.getLogger(MenuLoader.class);

    private static List<MenuItem> menuItems;
    private static volatile MenuLoader menuLoader;

    public static MenuLoader getInstance() {
        if (menuLoader == null) {
            synchronized (MenuLoader.class) {
                if (menuLoader == null) {
                    menuLoader = new MenuLoader();
                }
            }
        }
        return new MenuLoader();
    }

    public MenuLoader() {
        init();
    }
    private void init() {
        List<MenuItem> menuItems = new MenuParser().loadMenus(getMenuFiles());
        PropertiesService propertiesService = SpringContextUtil.getBean(PropertiesService.class);
        if (propertiesService != null) {
            String tenantAlias = propertiesService.getTenantAlias();
            menuItems.forEach(it-> replaceTenantAlias(it, tenantAlias));
        }
        this.menuItems = new ArrayList<>();
        for (MenuItem current : menuItems) {
            boolean found = false;
            for (MenuItem exist : this.menuItems) {
                if (exist.getName() != null && exist.getName().equals(current.getName())) {
                    exist.addChild(current.getChildren());
                    exist.setSort(Math.max(current.getSort(), exist.getSort()));
                    found = true;
                    break;
                }
            }
            if (!found) {
                this.menuItems.add(current);
            }
        }
        sortMenus(this.menuItems);
    }
    private void replaceTenantAlias(MenuItem item, String alias) {
        item.setName(item.getName().replace("租户", alias));
        if (CollectionUtils.isEmpty(item.getChildren())) return;
        item.getChildren().forEach(it-> replaceTenantAlias(it, alias));
    }
    private void sortMenus(List<MenuItem> menuItems) {
        if (menuItems == null) return;
        for (MenuItem item : menuItems) {
            sortMenus(item.getChildren());
        }
        Collections.sort(menuItems, new Comparator<MenuItem>() {
            @Override
            public int compare(MenuItem o1, MenuItem o2) {
                return o1.getSort() - o2.getSort();
            }
        });
    }

    private static InputStream getExternalMenuFile() {
        File file = new File("menu.xml");
        try {
            logger.info("加载根目录菜单:" + file.getAbsolutePath());
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.error("未找到根目录菜单");
        }
        return null;
    }
    private static List<InputStream> getMenuFiles() {
        List<InputStream> files = new ArrayList<>();
        InputStream externalMenu = getExternalMenuFile();
        if (null != externalMenu) {
            files.add(externalMenu);
        }
        Resource[] resources = ResourceLoader.getResources("classpath*:/menu.xml");
        if (null != resources && resources.length > 0) {
            for (Resource resource : resources) {
                try {
                    URL url = resource.getURL();
                    InputStream inputStream = null;
                    try {
                        JarURLConnection openStream = (JarURLConnection) url.openConnection();
                        inputStream = openStream.getInputStream();
                    } catch (Exception e) {
                        inputStream = url.openStream();
                    }
                    if (null == inputStream) {
                        continue;
                    }
                    files.add(inputStream);
                    logger.info("加载菜单资源目录：" + url.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return files;
    }

    public List<MenuItem> getAllMenus() {
        return getAllMenus(true);
    }
    public List<MenuItem> getAllMenus(boolean excludeEmptyUrl) {
        List<MenuItem> result = new ArrayList<>();
        for (MenuItem menu : menuItems) {
            MenuItem urlMenu = new MenuItem(menu);
            for (MenuItem sub : menu.getChildren()) {
                if (!excludeEmptyUrl || StringUtil.isNotBlank(sub.getUrl())) {
                    urlMenu.addChild(sub);
                }
            }
            if (urlMenu.hasChildren()) {
                result.add(urlMenu);
            }
        }
        return result;
    }
    public List<MenuItem> getMenuByModule(Set<String> modules, boolean urlOnly, boolean noConfig) {
        if (null == modules || modules.size() == 0) {
            return null;
        }
        List<MenuItem> result = new ArrayList<>();
        for (MenuItem menu : menuItems) {
            MenuItem validMenu = new MenuItem(menu);
            for (MenuItem sub : menu.getChildren()) {
                if (!modules.contains(sub.getCode())) continue;
                if (urlOnly && StringUtil.isBlank(sub.getUrl())) continue;
                if (noConfig && StringUtil.isBlank(sub.getUrl()) && (sub.getChildren() == null || sub.getChildren().size() < 1)) continue;
                validMenu.addChild(sub);
            }
            if (null != validMenu.getChildren() && validMenu.getChildren().size() > 0) {
                result.add(validMenu);
            }
        }
        return result;
    }
}
