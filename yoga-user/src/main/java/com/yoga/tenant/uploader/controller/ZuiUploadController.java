package com.yoga.tenant.uploader.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.utils.StrUtil;
import com.yoga.tenant.uploader.dto.DeleteDto;
import com.yoga.tenant.uploader.dto.UploadDto;
import com.yoga.tenant.uploader.model.UploadFile;
import com.yoga.tenant.uploader.model.UploadResult;
import com.yoga.tenant.uploader.service.UploadService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller("globalZuiUploadController")
@RequestMapping("/uploader/zui")
public class ZuiUploadController extends BaseWebController {

    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private UploadService uploadService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public UploadResult uploadFiles(HttpServletResponse response, UploadDto dto,
                                    @RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "uuid", required = false) String uuid,
                                    @RequestParam(value = "chunk", defaultValue = "0") int chunk,
                                    @RequestParam(value = "chunks", defaultValue = "1") int chunks,
                                    @RequestHeader(value = "adddate", required = false) Integer addDate,
                                    @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            if (StrUtil.hasBlank(name, uuid) || file == null) throw new Exception("无效的文件上传请求！");
            // 临时目录用来存放所有分片文件
            String tempFileDir = propertiesService.getFileTempPath() + File.separator + uuid;
            File parentFileDir = new File(tempFileDir);
            if (!parentFileDir.exists()) parentFileDir.mkdirs();
            // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台(默认每片为5M)
            File tempPartFile = new File(parentFileDir, name + "_" + chunk + ".part");
            FileUtils.copyInputStreamToFile(file.getInputStream(), tempPartFile);
            boolean uploadDone = true; // 是否全部上传完成，所有分片都存在才说明整个文件上传完成
            for (int i = 0; i < chunks && uploadDone; i++) {
                File partFile = new File(parentFileDir, name + "_" + i + ".part");
                if (!partFile.exists()) uploadDone = false;
            }
            if (uploadDone) {   //所有分片文件都上传完成，将所有分片文件合并到一个文件中
                File destTempFile = uploadService.generateFilePath(dto.getTid(), name, addDate);
                for (int i = 0; i < chunks; i++) {
                    File partFile = new File(parentFileDir, name + "_" + i + ".part");
                    FileOutputStream destTempfos = new FileOutputStream(destTempFile, true);
                    FileUtils.copyFile(partFile, destTempfos);
                    destTempfos.close();
                }
                FileUtils.deleteDirectory(parentFileDir); // 删除临时目录中的分片文件
                String path = destTempFile.getPath();
                if (dto.isResize()) path = uploadService.resize(path, dto.getWidth(), dto.getHeight());
                UploadFile uploadFile = uploadService.add(dto.getTid(), path, dto.getPurpose());
                return new UploadResult(uploadFile.getRemoteUrl(), uploadFile.getLocalPath(), uploadFile.getId());
            } else {
                if (chunk == chunks - 1) { // 最后一个分片仍然显示文件不全部存在，则代表临时文件创建失败
                    FileUtils.deleteDirectory(parentFileDir);
                    throw new Exception("创建临时文件失败！");
                }
            }
            return new UploadResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new UploadResult(ex.getMessage());
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteFile(DeleteDto dto) {
        try {
            uploadService.delete(dto.getTid(), dto.getFileId());
            return new CommonResult();
        } catch (Exception ex) {
            return new CommonResult(-1, ex.getMessage());
        }
    }
}
