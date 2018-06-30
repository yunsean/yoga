package com.yoga.tenant.setting.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.user.basic.TenantDto;
import com.yoga.tenant.setting.dto.GetDto;
import com.yoga.tenant.setting.model.SaveItem;
import com.yoga.tenant.setting.model.Setting;
import com.yoga.tenant.setting.service.SettingService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Explain("系统设置")
@RestController
@RequestMapping("/api/setting")
public class SettingApiController extends BaseApiController {
	
	@Autowired
	private SettingService settingService;

	@Explain(exclude = true)
	@RequiresPermissions("gbl_settable.update")
	@RequestMapping("/save")
	public CommonResult save(@Valid TenantDto dto, @RequestBody @Valid List<SaveItem> bean, BindingResult bindingResult){
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		settingService.save(dto.getTid(), bean);
		return new CommonResult();
	}

	@Explain("读取配置参数")
	@RequiresAuthentication
	@RequestMapping("/get")
	public CommonResult get(@Valid GetDto dto, BindingResult bindingResult){
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		Setting setting = settingService.get(dto.getTid(), dto.getModule(), dto.getKey());
		return new CommonResult(setting.getValue());
	}
}
