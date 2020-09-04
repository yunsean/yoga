package com.yoga.utility.uploader.controller;

import com.yoga.core.base.BaseController;
import com.yoga.core.data.ApiResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.utils.StringUtil;
import com.yoga.utility.uploader.dto.FileDeleteDto;
import com.yoga.utility.uploader.dto.FileGetDto;
import com.yoga.utility.uploader.dto.ZuiUploadDto;
import com.yoga.utility.uploader.model.UploadFile;
import com.yoga.utility.uploader.vo.UploadResultVo;
import com.yoga.utility.uploader.service.UploadService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;

@ApiIgnore
@Controller("globalZuiUploadController")
@RequestMapping("/admin/system/uploader/zui")
public class ZuiUploadController extends BaseController {

    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private UploadService uploadService;

    @ApiOperation("ZUI文件上传")
    @RequiresAuthentication
    @PostMapping("/upload.json")
    @ResponseBody
    public UploadResultVo uploadFiles(ZuiUploadDto dto,
                                      @RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "uuid", required = false) String uuid,
                                      @RequestParam(value = "chunk", defaultValue = "0") int chunk,
                                      @RequestParam(value = "chunks", defaultValue = "1") int chunks,
                                      @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            if (StringUtil.hasBlank(name, uuid) || file == null) throw new Exception("无效的文件上传请求！");
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
                File destTempFile = uploadService.generateFilePath(dto.getTid(), name);
                for (int i = 0; i < chunks; i++) {
                    File partFile = new File(parentFileDir, name + "_" + i + ".part");
                    FileOutputStream destTempfos = new FileOutputStream(destTempFile, true);
                    FileUtils.copyFile(partFile, destTempfos);
                    destTempfos.close();
                }
                FileUtils.deleteDirectory(parentFileDir); // 删除临时目录中的分片文件
                String path = destTempFile.getPath();
                if (dto.isResize()) path = uploadService.resize(path, dto.getWidth(), dto.getHeight());
                UploadFile uploadFile = uploadService.add(dto.getTid(), name, path, dto.getPurpose(), null, null);
                return new UploadResultVo(uploadFile.getRemoteUrl(), uploadFile.getLocalPath(), uploadFile.getId(), uploadFile.getFilename(), uploadFile.getFileSize());
            } else {
                if (chunk == chunks - 1) { // 最后一个分片仍然显示文件不全部存在，则代表临时文件创建失败
                    FileUtils.deleteDirectory(parentFileDir);
                    throw new Exception("创建临时文件失败！");
                }
            }
            return new UploadResultVo();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new UploadResultVo(ex.getMessage());
        }
    }

    @ApiOperation("删除文件")
    @DeleteMapping("/delete.json")
    @ResponseBody
    public ApiResult deleteFile(@ModelAttribute @Valid FileDeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        uploadService.delete(dto.getTid(), dto.getFileId());
        return new ApiResult();
    }

    @ApiOperation("获取文件地址")
    @GetMapping("/get.json")
    @ResponseBody
    public ApiResult<String> getFile(@ModelAttribute @Valid FileGetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        UploadFile file = uploadService.get(dto.getTid(), dto.getFileId());
        return new ApiResult<>(file.getRemoteUrl());
    }
}
