package com.yoga.utility.qr.controller;

import com.yoga.core.data.ApiResults;
import com.yoga.utility.qr.dto.*;
import com.yoga.utility.qr.model.QRBindInfo;
import com.yoga.utility.qr.service.QRCodeService;
import com.yoga.core.base.BaseController;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.utility.uploader.model.UploadFile;
import com.yoga.utility.uploader.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Api(tags = "二维码工具")
@Controller
@EnableAutoConfiguration
@RequestMapping("/admin/qr")
public class QRCodeController extends BaseController {

    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private UploadService uploadService;

    @ResponseBody
    @GetMapping("/chart")
    @ApiOperation(value = "生成二维码图片")
    public void code(HttpServletResponse response, @Valid @ModelAttribute ChartDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        try {
            OutputStream os = response.getOutputStream();
            response.setContentType("image/png");
            QRCode qrCode = QRCode.from(dto.getCode()).withSize(dto.getWidth(), dto.getHeight()).to(ImageType.PNG);
            ByteArrayOutputStream baos = qrCode.stream();
            os.write(baos.toByteArray());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @ApiIgnore
    @RequestMapping("")
    public String chart(ModelMap model) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String first = uuid.substring(0, 16);
        String last = uuid.substring(16);
        String md5 = DigestUtils.md5Hex(uuid).replace("-", "");
        String qr = first + md5.substring(md5.length() - 8) + last;
        model.put("code", qr);
        return "/admin/utility/qr/qr";
    }

    @ApiIgnore
    @ResponseBody
    @GetMapping("/query.json")
    public ApiResult<String> query(@Valid @ModelAttribute QrQueryDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String url = qrCodeService.actionQuery(dto.getCode());
        if (url == null) return new ApiResult<>(ResultConstants.WARNING_NO_DATA, "没有找到关联数据");
        else return new ApiResult<>(url);
    }

    @ResponseBody
    @PostMapping("/show.json")
    @ApiOperation(value = "绑定浏览文档")
    public ApiResult qrShow(HttpServletRequest request, @Valid @ModelAttribute QrShowDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String code = dto.getCode();
        String first = code.substring(0, 16);
        String check = code.substring(16, 24);
        String last = code.substring(24);
        String uuid = first + last;
        String md5 = DigestUtils.md5Hex(uuid);
        if (!check.equalsIgnoreCase(md5.substring(md5.length() - 8))) throw new IllegalArgumentException("无效的二维码！");
        qrCodeService.bindToQr(code, request, dto.getUrl(), dto.wrapAsMap());
        return new ApiResult();
    }

    @ApiIgnore
    @RequestMapping("/upload")
    public String upload(ModelMap model, QrUploadDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        try {
            QRBindInfo bindInfo = qrCodeService.pickFromQr(dto.getCode());
            model.put("token", bindInfo.getToken());
            model.put("code", dto.getCode());
            model.put("tid", bindInfo.getHeaders().get("tid"));
            model.put("param", bindInfo.getParams());
        } catch (Exception ex) {
            return "redirect:/admin/qr";
        }
        return "/admin/utility/qr/upload";
    }
    @ResponseBody
    @ApiOperation("绑定上传二维码信息")
    @RequiresAuthentication
    @PostMapping("/upload/qr.json")
    public ApiResult qrUpload(HttpServletRequest request, @Valid @ModelAttribute QrUploadDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String code = dto.getCode();
        String first = code.substring(0, 16);
        String check = code.substring(16, 24);
        String last = code.substring(24);
        String uuid = first + last;
        String md5 = DigestUtils.md5Hex(uuid);
        if (!check.equalsIgnoreCase(md5.substring(md5.length() - 8))) throw new IllegalArgumentException("无效的二维码！");
        qrCodeService.bindToQr(code, request, "/admin/qr/upload", dto.wrapAsMap());
        return new ApiResult();
    }

    @ApiIgnore
    @RequestMapping("/download")
    public String download(ModelMap model, QrDownloadDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        try {
            QRBindInfo bindInfo = qrCodeService.pickFromQr(dto.getCode());
            long tenantId = Long.valueOf(bindInfo.getHeaders().get("tid"));
            long fileId = Long.valueOf(bindInfo.getHeaders().get("fileId"));
            UploadFile file = uploadService.get(tenantId, fileId);
            model.put("file", file);
        } catch (Exception ex) {
            return "redirect:/admin/qr";
        }
        return "/admin/utility/qr/download";
    }
    @ResponseBody
    @ApiOperation("绑定下载二维码信息")
    @RequiresAuthentication
    @PostMapping("/download/qr.json")
    public ApiResult qrDownload(HttpServletRequest request, @Valid @ModelAttribute QrDownloadDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String code = dto.getCode();
        String first = code.substring(0, 16);
        String check = code.substring(16, 24);
        String last = code.substring(24);
        String uuid = first + last;
        String md5 = DigestUtils.md5Hex(uuid);
        if (!check.equalsIgnoreCase(md5.substring(md5.length() - 8))) throw new IllegalArgumentException("无效的二维码！");
        qrCodeService.bindToQr(code, request, "/admin/qr/download", dto.wrapAsMap());
        return new ApiResult();
    }
    @ApiIgnore
    @ResponseBody
    @RequestMapping("/uploaded.json")
    public ApiResult setQrFiles(@Valid QrUploadDto dto, @Valid @RequestBody List<UploadedFileBean> bean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        qrCodeService.bindFilesToQr(dto.getCode(), bean);
        qrCodeService.unbindFromQr(dto.getCode());
        return new ApiResult();
    }
    @ResponseBody
    @RequiresAuthentication
    @ApiOperation("获取二维码上传的文件信息")
    @GetMapping("/upload/files.json")
    public ApiResults<UploadedFileBean> qrFiles(@Valid @ModelAttribute QrUploadFilesDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String code = dto.getCode();
        String first = code.substring(0, 16);
        String check = code.substring(16, 24);
        String last = code.substring(24);
        String uuid = first + last;
        String md5 = DigestUtils.md5Hex(uuid);
        if (!check.equalsIgnoreCase(md5.substring(md5.length() - 8))) throw new IllegalArgumentException("无效的二维码！");
        List<UploadedFileBean> files = qrCodeService.unbindFilesFromQr(code);
        if (files == null) return new ApiResults<>(1, "未找到对应二维码信息！");
        return new ApiResults<>(files);
    }

    @ApiIgnore
    @RequestMapping("/transfer")
    public void qrTransfer(HttpServletResponse response, @Valid QrTransferDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String code = dto.getCode();
        String first = code.substring(0, 16);
        String check = code.substring(16, 24);
        String last = code.substring(24);
        String uuid = first + last;
        String md5 = DigestUtils.md5Hex(uuid);
        if (!check.equalsIgnoreCase(md5.substring(md5.length() - 8))) throw new IllegalArgumentException("无效的二维码！");
        QRBindInfo bindInfo = qrCodeService.unbindFromQr(dto.getCode());
        OutputStream os = response.getOutputStream();
        URL uri = new URL(bindInfo.getUrl());
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        if (bindInfo.getHeaders() != null) {
            for (String key : bindInfo.getHeaders().keySet()) {
                connection.addRequestProperty(key, bindInfo.getHeaders().get(key));
            }
        }
        connection.connect();
        response.setStatus(connection.getResponseCode());
        response.setContentType(connection.getContentType());
        response.setContentLength(connection.getContentLength());
        if (connection.getContentLength() > 0) {
            InputStream is = connection.getInputStream();
            int bufferSize = 1024 * 100;
            byte by[] = new byte[bufferSize];
            int index = is.read(by, 0, bufferSize);
            while (index != -1) {
                os.write(by, 0, index);
                index = is.read(by, 0, bufferSize);
            }
        }
        os.flush();
        os.close();
    }
}
