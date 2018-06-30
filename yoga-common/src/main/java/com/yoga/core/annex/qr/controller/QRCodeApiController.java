package com.yoga.core.annex.qr.controller;

import com.yoga.core.annex.qr.dto.ChartDto;
import com.yoga.core.annex.qr.dto.QueryDto;
import com.yoga.core.annex.qr.dto.ShowDto;
import com.yoga.core.annex.qr.dto.TransferDto;
import com.yoga.core.annex.qr.model.QRBindInfo;
import com.yoga.core.annex.qr.service.QRCodeService;
import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.exception.IllegalArgumentException;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Explain(value = "QR二维码")
@Controller
@EnableAutoConfiguration
@RequestMapping("/qr")
public class QRCodeApiController extends BaseApiController {

    @Autowired
    private QRCodeService qrCodeService;

    @Explain("生成二维码图片")
    @ResponseBody
    @RequestMapping("/chart")
    public void code(HttpServletRequest request, HttpServletResponse response, @Valid ChartDto dto, BindingResult bindingResult) {
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

    @Explain(exclude = true)
    @RequestMapping("")
    public String chart(HttpServletRequest request, ModelMap model) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String first = uuid.substring(0, 16);
        String last = uuid.substring(16);
        String md5 = DigestUtils.md5Hex(uuid).replace("-", "");
        String qr = first + md5.substring(md5.length() - 8) + last;
        model.put("code", qr);
        return "/qr/qr";
    }

    @Explain(exclude = true)
    @ResponseBody
    @RequestMapping("/query")
    public CommonResult query(HttpServletRequest request, @Valid QueryDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String url = qrCodeService.actionQuery(dto.getCode());
        if (url == null) return new CommonResult(ResultConstants.WARNING_NO_DATA, "没有找到关联数据");
        else return new CommonResult(url);
    }

    @Explain("绑定浏览文档")
    @RequestMapping("/show")
    @ResponseBody
    public CommonResult qrShow(HttpServletRequest request, @Valid ShowDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String code = dto.getCode();
        String first = code.substring(0, 16);
        String check = code.substring(16, 24);
        String last = code.substring(24);
        String uuid = first + last;
        String md5 = DigestUtils.md5Hex(uuid);
        if (!check.equalsIgnoreCase(md5.substring(md5.length() - 8))) throw new IllegalArgumentException("无效的二维码！");
        Enumeration<String> names = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getHeader(name);
            if (value != null) {
                headers.put(name, value);
            }
        }
        qrCodeService.bindQrToShow(code, dto.getUrl(), headers);
        return new CommonResult();
    }

    @Explain(exclude = true)
    @RequestMapping("/transfer")
    public void qrTransfer(HttpServletRequest request, HttpServletResponse response, @Valid TransferDto dto, BindingResult bindingResult) throws Exception {
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
