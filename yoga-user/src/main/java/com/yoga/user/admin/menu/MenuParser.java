package com.yoga.user.admin.menu;

import com.yoga.core.utils.ParamUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.*;

public class MenuParser {
    public List<MenuItem> loadMenus(List<InputStream> files) {
        List<MenuItem> allItems = new ArrayList<>();
        for (InputStream file : files) {
            List<MenuItem> menuItems = loadMenus(file);
            if (ParamUtils.isNotEmpty(menuItems)) {
                allItems.addAll(menuItems);
            }
        }
        return allItems;
    }
    public List<MenuItem> loadMenus(InputStream file) {
        List<MenuItem> menuItems = new ArrayList<>();
        Document doc = null;
        try {
            SAXReader saxReader = new SAXReader();
            try {
                doc = saxReader.read(file);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            Element root = doc.getRootElement();
            @SuppressWarnings("unchecked")
            Iterator<Element> rootIte = root.elementIterator();
            while (rootIte.hasNext()) {
                Element element = rootIte.next();
                MenuItem menu = this.readMenu(element);
                @SuppressWarnings("unchecked")
                Iterator<Element> children = element.elementIterator();
                menu.setChildren(this.readMenus(children));
                menuItems.add(menu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menuItems;
    }
    private MenuItem readMenu(Element element) {
        int sort = Integer.parseInt(element.attributeValue("sort"));
        String code = element.attributeValue("code");
        String icon = element.attributeValue("icon");
        String name = element.attributeValue("name");
        String url = element.attributeValue("url");
        String remark = element.attributeValue("remark");
        boolean admin = "true".equals(element.attributeValue("admin"));
        return new MenuItem(sort, code, icon, name, url, remark, admin);
    }
    @SuppressWarnings("unchecked")
    private List<MenuItem> readMenus(Iterator<Element> elements) {
        List<MenuItem> menuList = new ArrayList<>();
        while (elements.hasNext()) {
            Element element = elements.next();
            MenuItem menu = this.readMenu(element);
            Iterator<Element> children = element.elementIterator();
            menu.setChildren(getModuleMenus(children));
            menuList.add(menu);
        }
        return menuList;
    }
    private List<MenuItem> getModuleMenus(Iterator<Element> elements) {
        List<MenuItem> menuList = new ArrayList<>();
        while (elements.hasNext()) {
            Element element = elements.next();
            MenuItem menu = readMenu(element);
            menuList.add(menu);
        }
        return menuList;
    }
}
