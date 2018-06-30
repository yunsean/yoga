package com.yoga.user.duty.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.user.duty.dto.AddDutyDto;
import com.yoga.user.duty.dto.DelDutyDto;
import com.yoga.user.duty.dto.GetDutyRolesDto;
import com.yoga.user.duty.dto.UpdateDutyDto;
import com.yoga.user.duty.service.DutyService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Explain(value = "职级管理", module = "pri_duty")
@Controller
@RequestMapping("/api/duty")
public class DutyApiController extends BaseApiController {
	
	@Autowired
	private DutyService dutyService;

	@Explain("职级列表")
	@ResponseBody
	@RequestMapping("/roles")
	@RequiresAuthentication
	public CommonResult getDutyRoles(HttpServletRequest request, @Valid GetDutyRolesDto dto, BindingResult bindingResult){
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		List dutyRoles =  dutyService.getDutyRoles(dto.getTid(), dto.getId());
		return new CommonResult(dutyRoles);
	}

	@Explain("增加新职级")
	@ResponseBody
	@RequestMapping("/add")
	@RequiresPermissions("pri_duty.add")
	public CommonResult addRole(HttpServletRequest request, @Valid AddDutyDto dutyDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		dutyService.addDuty(dutyDto);
		return new CommonResult();
	}

	@Explain("删除现有职级")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("pri_duty.del")
	public CommonResult delDuty(HttpServletRequest request, @Valid DelDutyDto dto, BindingResult bindingResult){
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		try {
			dutyService.delDuty(dto.getTid(), dto.getId());
		} catch (Exception ex) {
			return new CommonResult(ResultConstants.ERROR_BUSINESSERROR, ex.getMessage());
		}
		return new CommonResult();
	}

	@Explain("修改职级信息")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("pri_duty.update")
	public CommonResult updateDuty(HttpServletRequest request, @Valid UpdateDutyDto updateDutyDto, BindingResult bindingResult){
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		dutyService.updateDuty(updateDutyDto);
		return new CommonResult();
	}
}
