package com.yoga.ueditor.controller;

import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.ConfigManager;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.utils.Utility;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequestMapping("/ueditor")
@Controller
public class UEditorController {

    @Autowired
    private PropertiesService propertiesService;

    private static ConfigManager configManager = null;
    private static ConfigManager getConfigManager(String configFile, String rootPath) {
        if (configManager == null) {
            synchronized (UEditorController.class) {
                if (configManager == null) {
                    if (rootPath == null) rootPath = "/";
                    if (rootPath.length() < 1 || rootPath.charAt(rootPath.length() - 1) != '/') rootPath += "/";
                    File file = new File(configFile);
                    if (!file.exists()) {
                        String realPath = Utility.getAppPath(UEditorController.class) + configFile;
                        file = new File(configFile);
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
}
