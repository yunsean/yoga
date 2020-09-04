package com.yoga.admin.duty.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.admin.duty.dto.*;
import com.yoga.admin.duty.vo.Duty2Vo;
import com.yoga.admin.duty.vo.DutyVo;
import com.yoga.admin.role.dto.RoleListDto;
import com.yoga.core.base.BaseController;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.data.CommonPage;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.operator.duty.model.Duty;
import com.yoga.operator.duty.service.DutyService;
import com.yoga.operator.role.service.RoleService;
import com.yoga.setting.customize.CustomPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Controller
@Api(tags = "职级管理")
@RequestMapping("/admin/operator/duty")
public class DutyController extends BaseController {
	
	@Autowired
	private DutyService dutyService;
	@Autowired
	private RoleService roleService;

	@ApiIgnore
	@RequiresPermissions("admin_duty")
	@RequestMapping("/list")
	public String duties(ModelMap model, CustomPage page, @Valid RoleListDto dto, BindingResult bindingResult){
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		PageInfo<Duty> duties = dutyService.list(dto.getTid(), dto.getFilter(), page.getPageIndex(), page.getPageSize());
		model.put("duties", duties.getList());
		model.put("page", new CommonPage(duties));
		model.put("roles", roleService.list(dto.getTid()));
		model.put("param", dto.wrapAsMap());
		return "/admin/duty/duties";
	}

	@RequiresAuthentication
	@ResponseBody
	@GetMapping("/roles.json")
	@ApiOperation(value = "职级列表")
	public ApiResults<Duty2Vo> roles(CustomPage page, @Valid @ModelAttribute DutyListDto dto, BindingResult bindingResult){
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		PageInfo<Duty> duties =  dutyService.list(dto.getTid(), dto.getFilter(), page.getPageIndex(), page.getPageSize());
		return new ApiResults<>(duties, Duty2Vo.class);
	}
	@ResponseBody
	@PostMapping("/add.json")
	@RequiresPermissions("admin_duty.add")
	@ApiOperation(value = "增加新职级")
	public ApiResult add(@Valid @ModelAttribute DutyAddDto dto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		dutyService.add(dto.getTid(), dto.getBelowId(), dto.getName(), dto.getCode(), dto.getRemark(), dto.getRoleIds());
		return new ApiResult();
	}
	@ResponseBody
	@DeleteMapping("/delete.json")
	@RequiresPermissions("admin_duty.del")
	@ApiOperation(value = "删除现有职级")
	public ApiResult delete(@Valid @ModelAttribute DutyDeleteDto dto, BindingResult bindingResult){
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		dutyService.delete(dto.getTid(), dto.getId());
		return new ApiResult();
	}
	@ResponseBody
	@PostMapping("/update.json")
	@RequiresPermissions("admin_duty.update")
	@ApiOperation(value = "修改职级信息")
	public ApiResult updateDuty(@Valid @ModelAttribute DutyUpdateDto dto, BindingResult bindingResult){
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		dutyService.update(dto.getTid(), dto.getId(), dto.getName(), dto.getCode(), dto.getRemark(), dto.getRoleIds());
		return new ApiResult();
	}
	@ResponseBody
	@GetMapping("/get.json")
	@RequiresAuthentication
	@ApiOperation(value = "获取职级信息")
	public ApiResult<DutyVo> get(@Valid @ModelAttribute DutyGetDto dto, BindingResult bindingResult){
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		Duty duty = dutyService.get(dto.getTid(), dto.getId());
		return new ApiResult<>(duty, DutyVo.class, (po, vo)-> vo.setRoleIds(dutyService.listRoles(dto.getTid(), dto.getId())));
	}
}
