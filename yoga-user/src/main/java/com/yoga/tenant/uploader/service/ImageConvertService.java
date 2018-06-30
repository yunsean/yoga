package com.yoga.tenant.uploader.service;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.StandardStream;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ImageConvertService {

    private String imageMagickPath;
    private static String osName = null;

    static {
        osName = System.getProperty("os.name").toLowerCase();
    }

    public String drawImg(String picPath, int width, int height) {
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
            String path = picPath.replaceAll("\\.", "_" + width + "_" + height + "\\.");
            System.out.println(picPath);
            cmd.run(op, picPath, path);
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getImageMagickPath() {
        return imageMagickPath;
    }
    public void setImageMagickPath(String imageMagickPath) {
        this.imageMagickPath = imageMagickPath;
    }
}
