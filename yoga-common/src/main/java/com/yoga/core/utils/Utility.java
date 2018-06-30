package com.yoga.core.utils;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

public class Utility {
    public static String getAppPath(Class cls) {
        if (cls == null) {
            throw new java.lang.IllegalArgumentException("参数不能为空！");
        }
        ClassLoader loader = cls.getClassLoader();
        String clsName = cls.getName() + ".class";
        Package pack = cls.getPackage();
        String path = "";
        if (pack != null) {
            String packName = pack.getName();
            if (packName.startsWith("java.") || packName.startsWith("javax.")) {
                throw new java.lang.IllegalArgumentException("请不要传送系统内置类！");
            }
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

    public static String getStartupPath(Class cls) {
        String realPath = cls.getClassLoader().getResource("").getFile();
        File file = new File(realPath);
        realPath = file.getAbsolutePath();
        try {
            realPath = URLDecoder.decode(realPath, "utf-8");
            file = new File(realPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File parentFile = file.getParentFile();
        return parentFile.getAbsolutePath();
    }

    public static String getResourcePath(Class cls, String resourceFile) {
        URL url = cls.getClass().getResource(resourceFile);
        String realPath = url.getPath();
        int pos = realPath.indexOf("file:");
        System.out.println("pos=" + pos);
        if (pos > -1) {
            realPath = realPath.substring(pos + 5);
        }
        try {
            realPath = java.net.URLDecoder.decode(realPath, "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return realPath;
    }

    public static String randomCode(int length){
        return  "" + ((long)((Math.random() * 9 + 1) * Math.pow(10, length - 1)));
    }
}
