package com.yoga.pay.alipay.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.pay.alipay.dto.SaveParamDto;
import com.yoga.pay.alipay.model.AlipayParam;
import com.yoga.pay.alipay.service.AlipayService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Explain(exclude = true)
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/pay/alipay")
public class AlipayApiController extends BaseApiController {

    @Autowired
    private AlipayService alipayService;

    @RequiresAuthentication
    @RequestMapping("/setting/save")
    public CommonResult findGroup(@Valid SaveParamDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        AlipayParam param = new AlipayParam(dto.getAppId(), dto.getAppPrivateKey(), dto.getAlipayPublicKey());
        alipayService.saveSetting(dto.getTid(), param);
        return new CommonResult();
    }
}
