package com.yoga.utility.uploader.controller;

import com.yoga.core.data.CommonResult;
import com.yoga.core.data.MapConverter;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.utils.TypeCastUtil;
import com.yoga.utility.uploader.dto.ZuiUploadDto;
import com.yoga.utility.uploader.model.UploadFile;
import com.yoga.utility.uploader.service.UploadService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ApiIgnore
@Controller("globalNginxUploadController")
@RequestMapping("/admin/system/uploader/nginx")
public class NginxUploadController {
    @Autowired
    private UploadService uploadService;

    @ApiIgnore
    @ResponseBody
    @RequestMapping("/auth")
    public void onAuth(HttpServletRequest request, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null || !subject.isAuthenticated()) response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        else response.setStatus(HttpServletResponse.SC_OK);
    }

    @ApiIgnore
    @ResponseBody
    @RequestMapping("/noauth")
    public CommonResult onNoAuth() {
        return new CommonResult(ResultConstants.ERROR_FORBIDDEN, "权限错误");
    }

    public static class FileItem {
        public String filename;
        public String filepath;
        public String remoteUrl;
        public String contentType;
        public String md5;
        public long fileSize;
        public long id;
    }

    @ApiIgnore
    @ResponseBody
    @PostMapping("/upload")
    public String onUpload(MultipartHttpServletRequest request, ZuiUploadDto dto) {
        try {
            Collection<Part> parts = request.getParts();
            List<FileItem> files = new ArrayList<>();
            FileItem fileItem = null;
            for (Part part : parts) {
                String name = part.getName();
                byte[] bytes = new byte[(int) part.getSize()];
                part.getInputStream().read(bytes);
                String value = new String(bytes);
                if (name.equals("file_name")) {
                    if (fileItem != null) files.add(fileItem);
                    fileItem = new FileItem();
                    fileItem.filename = value;
                } else if (name.equals("file_path")) {
                    fileItem.filepath = value;
                } else if (name.equals("file_content_type")) {
                    fileItem.contentType = value;
                } else if (name.equals("file_md5")) {
                    fileItem.md5 = value;
                } else if (name.equals("file_size")) {
                    fileItem.fileSize = TypeCastUtil.toLong(value);
                }
            }
            if (fileItem != null) files.add(fileItem);
            files.stream().forEach(fileItem1 -> {
                if (dto.isResize()) fileItem1.filepath = uploadService.resize(fileItem1.filepath, dto.getWidth(), dto.getHeight());
                UploadFile result = uploadService.add(dto.getTid(), fileItem1.filename, fileItem1.filepath, dto.getPurpose(), fileItem1.contentType, fileItem1.fileSize);
                fileItem1.remoteUrl = result.getRemoteUrl();
                fileItem1.id = result.getId();
            });
            CommonResult result = new CommonResult(new MapConverter<>((MapConverter.Converter<FileItem>)
                    (item, map) -> map.set("filename", item.filename)
                            .set("contentType", item.contentType)
                            .set("filepath", item.remoteUrl)
                            .set("md5", item.md5)
                            .set("size", item.fileSize)
                            .set("fileId", item.id))
                    .build(files));
            String sResult = result.toString();
            return sResult;
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(ResultConstants.ERROR_UNKNOWN, e.getMessage()).toString();
        }
    }
}
