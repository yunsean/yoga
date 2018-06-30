package com.yoga.pay.alipay.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.pay.alipay.model.AlipayParam;
import com.yoga.pay.alipay.service.AlipayService;
import com.yoga.user.basic.TenantDto;
import com.yoga.tenant.setting.Settable;
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
@RequestMapping("/pay/alipay")
public class AlipayWebController extends BaseWebController {

    @Autowired
    private AlipayService alipayService;

    @RequiresAuthentication
    @Settable(module = AlipayService.Module_Name, key = AlipayService.Key_Alipay, name = "支付宝参数设置", systemOnly = false)
    @RequestMapping("/setting")
    public String defaultRegDept(ModelMap model, TenantDto dto) {
        AlipayParam param = alipayService.getSetting(dto.getTid());
        model.put("param", new HashMap<>());
        model.put("setting", param);
        return "/pay/alipay/setting";
    }
}