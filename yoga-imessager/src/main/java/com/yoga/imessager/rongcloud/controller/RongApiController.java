package com.yoga.imessager.rongcloud.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.imessager.rongcloud.dto.SaveSettingDto;
import com.yoga.imessager.rongcloud.models.RongCloudSetting;
import com.yoga.imessager.rongcloud.service.RongService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Explain(exclude = true)
@RestController
@RequestMapping("/api/im/rong")
public class RongApiController extends BaseApiController {

    @Autowired
    private RongService rongService;

    @RequiresAuthentication
    @RequestMapping("/setting/save")
    public CommonResult findGroup(@Valid SaveSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        RongCloudSetting setting = new RongCloudSetting(dto.getAppCode(), dto.getAppSecret());
        rongService.saveSetting(dto.getTenantId(), setting);
        return new CommonResult();
    }
}
