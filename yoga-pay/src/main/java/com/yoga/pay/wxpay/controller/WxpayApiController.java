package com.yoga.pay.wxpay.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.property.PropertiesService;
import com.yoga.pay.wxpay.dto.SaveParamDto;
import com.yoga.pay.wxpay.model.WxpayParam;
import com.yoga.pay.wxpay.service.WxpayService;
import com.yoga.user.basic.TenantDto;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@EnableAutoConfiguration
@RequestMapping("/api/pay/wxpay")
public class WxpayApiController extends BaseApiController{
    @Autowired
    private WxpayService wxpayService;
    @Autowired
    private PropertiesService propertiesService;

    @Explain(exclude = true)
    @RequiresAuthentication
    @RequestMapping("/setting/save")
    public CommonResult findGroup(@Valid SaveParamDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        WxpayParam param = new WxpayParam(dto.getAppId(),dto.getMchId(),dto.getApiKey(),dto.getCert());
        wxpayService.saveSetting(dto.getTid(), param);
        return new CommonResult();
    }

    @RequestMapping("/callback")
    public CommonResult callback(HttpServletRequest request) {
        return new CommonResult();
    }

    @Explain("支付测试")
    @RequestMapping("/test")
    public CommonResult test(HttpServletRequest request, TenantDto dto) {
        String clientIp = getIRealIPAddr(request);
        String notifyUrl = "http://wx.yunsean.com/api/pay/wxpay/callback";
        Map<String, String> result = wxpayService.signApp(dto.getTid(), "111111", 0.01, notifyUrl, "Wexin pay Demo", clientIp);
        return new CommonResult(result);
    }

    private String getIRealIPAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "null".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0) {
            ip = "";
        }
        String[] parts = ip.split(",");
        for (String part : parts) {
            if ("unknown".equalsIgnoreCase(part)) {
                ip = part;
                break;
            }
        }
        return ip;
    }
}
