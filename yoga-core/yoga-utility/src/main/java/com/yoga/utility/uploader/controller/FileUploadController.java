package com.yoga.utility.uploader.controller;

import com.yoga.core.base.BaseController;
import com.yoga.core.data.ApiResult;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.utils.StringUtil;
import com.yoga.utility.uploader.dto.FileDeleteDto;
import com.yoga.utility.uploader.dto.FileGetDto;
import com.yoga.utility.uploader.dto.FileUploadDto;
import com.yoga.utility.uploader.dto.ZuiUploadDto;
import com.yoga.utility.uploader.model.UploadFile;
import com.yoga.utility.uploader.service.UploadService;
import com.yoga.utility.uploader.vo.UploadFileVo;
import com.yoga.utility.uploader.vo.UploadResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;

@Api(tags = "文件上传")
@Controller("globalFileUploadController")
@RequestMapping("/admin/system/uploader")
public class FileUploadController extends BaseController {

    @Autowired
    private UploadService uploadService;

    @ApiOperation("文件上传")
    @PostMapping("/upload.json")
    @ResponseBody
    public ApiResult<UploadFileVo> upload(@Valid @ModelAttribute FileUploadDto dto, @ModelAttribute("file") @RequestParam(value = "file", required = true) MultipartFile file, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        try {
            File destFile = uploadService.generateFilePath(dto.getTid(), file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
            String path = destFile.getPath();
            if (dto.isResize()) path = uploadService.resize(path, dto.getWidth(), dto.getHeight());
            UploadFile uploadFile = uploadService.add(dto.getTid(), destFile.getName(), path, dto.getPurpose(), null, null);
            return new ApiResult<>(new UploadFileVo(uploadFile.getId(), uploadFile.getFileSize(), uploadFile.getRemoteUrl(), uploadFile.getFilename(), uploadFile.getContentType()));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException(ex.getMessage());
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
    public ApiResult<UploadFileVo> get(@ModelAttribute @Valid FileGetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        UploadFile uploadFile = uploadService.get(dto.getTid(), dto.getFileId());
        return new ApiResult<>(new UploadFileVo(uploadFile.getId(), uploadFile.getFileSize(), uploadFile.getRemoteUrl(), uploadFile.getFilename(), uploadFile.getContentType()));
    }
}
