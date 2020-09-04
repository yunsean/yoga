package com.yoga.utility.image;

import com.yoga.core.property.PropertiesService;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.StandardStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ImageConvertService {

    @Value("${file.image_magick_path:}")
    private String imageMagickPath;
    @Autowired
    private PropertiesService propertiesService;

    private static String osName = null;

    static {
        osName = System.getProperty("os.name").toLowerCase();
    }

    public String drawImg(String picPath, Integer width, Integer height) {
        IMOperation op = new IMOperation();
        op.addImage();
        op.resize(width, height);
        op.quality(85d);
        op.addImage();
        ConvertCmd cmd = new ConvertCmd(true); //IM4JAVA是同时支持ImageMagick和GraphicsMagick的，如果为true则使用GM，如果为false支持IM。
        if (osName.indexOf("win") >= 0) {
            cmd.setSearchPath(imageMagickPath);
        } else if (osName.indexOf("mac") >= 0) {
            cmd.setSearchPath("/usr/local/bin/");
        } else {
            cmd.setSearchPath(imageMagickPath);
        }
        cmd.setErrorConsumer(StandardStream.STDERR);
        try {
            boolean prefixAdded = false;
            if (new File(propertiesService.getFileLocalPath() + "/" + picPath).exists()) {
                prefixAdded = true;
                picPath = propertiesService.getFileLocalPath() + "/" + picPath;
            } else if (!new File(picPath).exists()) {
                return null;
            }
            int pos = picPath.indexOf(".");
            String resizedPath;
            if (pos > 0) {
                resizedPath = picPath.substring(0, pos) + (width == null ? "" : ("_" + width)) + (height == null ? "" : ("_" + height)) + picPath.substring(pos);
            } else {
                resizedPath = picPath.substring(0, pos) + (width == null ? "" : ("_" + width)) + (height == null ? "" : ("_" + height));
            }
            cmd.run(op, picPath, resizedPath);
            if (prefixAdded) return resizedPath.replace(propertiesService.getFileLocalPath(), "");
            else return resizedPath;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        }
        return null;
    }
}
