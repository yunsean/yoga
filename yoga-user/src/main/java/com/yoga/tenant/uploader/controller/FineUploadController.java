package com.yoga.tenant.uploader.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.core.data.tuple.TwoTuple;
import com.yoga.core.property.PropertiesService;
import com.yoga.tenant.uploader.dto.UploadDto;
import com.yoga.tenant.uploader.service.ImageConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.UUID;

@Controller("globalFineUploadController")
@RequestMapping("/uploader/fine")
public class FineUploadController extends BaseWebController {

    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";

    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private ImageConvertService imageConvertService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFiles(HttpServletRequest request, ModelMap model, UploadDto dto) {
        String result = "";
        try {
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            if (multipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                MultipartFile multiFile = multiRequest.getFile("qqfile");
                if (multiFile == null) multiFile = multiRequest.getFile("file");
                if (multiFile != null) {
                    TwoTuple<String, String> url = null;
                    int width = 0;
                    int height = 0;
                    if (dto.isResize()) {
                        width = dto.getWidth();
                        height = dto.getHeight();
                        if (width == 0) width = 1280;
                        if (height == 0) height = 720;
                    }
                    if (width != 0 && height != 0) {
                        String paths = request.getSession().getServletContext().getRealPath("/") + "uploadImage";
                        File pf = new File(paths);
                        if (!pf.exists()) {
                            pf.mkdir();
                        }
                        File file = new File(paths + "/" + System.currentTimeMillis() + multiFile.getOriginalFilename());
                        multiFile.transferTo(file);
                        String path = imageConvertService.drawImg(file.getPath(), width, height);
                        File efile = new File(path);
                        FileInputStream fis = new FileInputStream(efile);
                        byte[] file_buff = null;
                        if (fis != null) {
                            int len = fis.available();
                            file_buff = new byte[len];
                            fis.read(file_buff);
                        }
                        url = save(file_buff, multiFile.getOriginalFilename(), Long.valueOf(request.getParameter("tid")));
                        file.delete();
                        fis.close();
                        efile.delete();
                    } else {
                        url = save(multiFile.getBytes(), multiFile.getOriginalFilename(), Long.valueOf(request.getParameter("tid")));
                    }
                    if (url != null && !"".equals(url)) {
                        result = "{\"success\": \"ok\",\"result\": \"ok\",\"tempLink\":\"" + url.second + "\",\"url\":\"" + url.second + "\",\"file\":\"" + url.first + "\"}";
                    } else {
                        result = "{\"result\": \"failed\", \"message\": \"上传失败\"}";
                        model.put("state", ERROR);
                        model.put("msg", "上传失败");
                    }
                } else {
                    result = "{\"result\": \"failed\", \"message\": \"上传失败\"}";
                    model.put("state", ERROR);
                    model.put("msg", "上传失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "{\"result\": \"failed\", \"message\": \"" + e.getMessage() + "\"}";
            model.put("state", ERROR);
            model.put("msg", "上传失败");
        }
        return result;
    }

    private String produceFileName(String originalFileName) {
        if (originalFileName.lastIndexOf('.') > 0) {
            String suffix = originalFileName.substring(originalFileName.lastIndexOf('.'), originalFileName.length());
            String fileName = UUID.randomUUID().toString() + suffix;
            return fileName;
        } else {
            return originalFileName;
        }
    }

    private TwoTuple<String, String> save(byte[] bytes, String oldFilename, Long tenantId) throws IOException {
        String filename = "/" + produceFileName(oldFilename);
        String localUrl = propertiesService.getFileLocalPath(tenantId);
        localUrl = localUrl.replace("\\", "/");
        File path = new File(localUrl);
        if (!path.exists()) path.mkdirs();
        localUrl += filename;
        File newFile = new File(localUrl);
        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(newFile);
            writer.write(bytes);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        String url = localUrl;
        return new TwoTuple<>(localUrl, url.replace(propertiesService.getFileLocalPath().replace("\\", "/"), propertiesService.getFileRemotePath()));
    }
}
