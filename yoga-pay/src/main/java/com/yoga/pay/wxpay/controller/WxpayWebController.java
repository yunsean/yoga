package com.yoga.pay.wxpay.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.pay.alipay.model.AlipayParam;
import com.yoga.pay.wxpay.model.WxpayParam;
import com.yoga.pay.wxpay.service.WxpayService;
import com.yoga.tenant.setting.Settable;
import com.yoga.user.basic.TenantDto;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Settable
@Controller
@EnableAutoConfiguration
@RequestMapping("/pay/wxpay")
public class WxpayWebController extends BaseWebController{
    @Autowired
    private  WxpayService wxpayService;
    @RequiresAuthentication
    @Settable(module = WxpayService.Module_Name, key = WxpayService.Key_wxpay, name = "微信参数设置", systemOnly = false)
    @RequestMapping("/setting")
    public String defaultRegDept(ModelMap model, TenantDto dto) {
        WxpayParam param = wxpayService.getSetting(dto.getTid());
        model.put("param", new HashMap<>());
        model.put("setting", param);
        return "/pay/wxpay/setting";
    }
}



