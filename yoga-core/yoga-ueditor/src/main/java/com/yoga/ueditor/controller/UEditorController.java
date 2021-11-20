package com.yoga.ueditor.controller;

import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.ConfigManager;
import com.yoga.core.property.PropertiesService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@ApiIgnore
@RequestMapping("/admin/ueditor")
@Controller
public class UEditorController {

    @Autowired
    private PropertiesService propertiesService;

    private static ConfigManager configManager = null;
    private static ConfigManager getConfigManager(String configFile, String rootPath) {
        if (configManager == null) {
            synchronized (UEditorController.class) {
                if (configManager == null) {
                    File file = new File("./" + configFile);
                    log.debug(file.getAbsolutePath() + " exist=" + file.exists());
                    if (!file.exists()) {
                        if (rootPath == null) rootPath = "/";
                        if (rootPath.length() < 1 || rootPath.charAt(rootPath.length() - 1) != '/') rootPath += "/";
                        file = new File(rootPath + configFile);
                        log.debug(file.getAbsolutePath() + " exist=" + file.exists());
                    }
                    if (!file.exists()) {
                        String realPath = getAppPath(UEditorController.class) + configFile;
                        file = new File(realPath);
                        log.debug(file.getAbsolutePath() + " exist=" + file.exists());
                    }
                    if (file.exists()) {
                        System.out.println("ueditor config file: " + file.getAbsolutePath());
                        configManager = ConfigManager.getInstance(file.getAbsolutePath(), rootPath);
                    }
                    if (configManager == null) {
                        InputStream inputStream = UEditorController.class.getResourceAsStream("/ueditor/config.json");
                        configManager = ConfigManager.getInstance(inputStream, rootPath);
                    }
                }
            }
        }
        return configManager;
    }

    @RequestMapping("/controller")
    @ResponseBody
    public void controller(HttpServletRequest request, HttpServletResponse response, String action) throws IOException, JSONException {
        request.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "text/html");
        try {
            ConfigManager configManager = getConfigManager("config.json", propertiesService.getFileLocalPath());
            String baseState = new ActionEnter(request, configManager).exec();
            PrintWriter writer = response.getWriter();
            writer.write(baseState);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/asset/**")
    public void asset(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String file = request.getRequestURI();
        if (file.startsWith("/ueditor/asset")) file = file.substring(14);
        file = propertiesService.getFileLocalPath() + file;
        Path path = Paths.get(file);
        String mimeType = "image/png";//Files.probeContentType(path);
        response.setHeader("Content-Type", mimeType);
        FileInputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = response.getOutputStream();
        try {
            int b = 0;
            byte[] buffer = new byte[512];
            while (true){
                b = inputStream.read(buffer);
                if (b == -1) break;
                outputStream.write(buffer, 0, b);
            }
        } finally {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
        }
    }

    private static String getAppPath(Class cls) {
        if (cls == null) throw new java.lang.IllegalArgumentException("参数不能为空！");
        ClassLoader loader = cls.getClassLoader();
        String clsName = cls.getName() + ".class";
        Package pack = cls.getPackage();
        String path = "";
        if (pack != null) {
            String packName = pack.getName();
            if (packName.startsWith("java.") || packName.startsWith("javax.")) throw new java.lang.IllegalArgumentException("请不要传送系统内置类！");
            clsName = clsName.substring(packName.length() + 1);
            if (packName.indexOf(".") < 0) {
                path = packName + "/";
            } else {
                int start = 0;
                int end = 0;
                end = packName.indexOf(".");
                while (end != -1) {
                    path = path + packName.substring(start, end) + "/";
                    start = end + 1;
                    end = packName.indexOf(".", start);
                }
                path = path + packName.substring(start) + "/";
            }
        }
        java.net.URL url = loader.getResource(path + clsName);
        String realPath = url.getPath();
        int pos = realPath.indexOf("file:");
        if (pos > -1) {
            realPath = realPath.substring(pos + 5);
        }
        pos = realPath.indexOf(path + clsName);
        realPath = realPath.substring(0, pos - 1);
        if (realPath.endsWith("!")) {
            realPath = realPath.substring(0, realPath.lastIndexOf("/"));
        }
        /*------------------------------------------------------------
        ClassLoader的getResource方法使用了utf-8对路径信息进行了编码，当路径
                   中存在中文和空格时，他会对这些字符进行转换，这样，得到的往往不是我们想要
                    的真实路径，在此，调用了URLDecoder的decode方法进行解码，以便得到原始的
                    中文及空格路径
        -------------------------------------------------------------*/
        try {
            realPath = java.net.URLDecoder.decode(realPath, "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (realPath.charAt(realPath.length() - 1) != '/') {
            realPath += "/";
        }
        return realPath;
    }
}
