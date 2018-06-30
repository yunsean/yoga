package com.yoga.user.duty.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.user.basic.TenantDto;
import com.yoga.user.duty.model.Duty;
import com.yoga.user.duty.service.DutyService;
import com.yoga.user.role.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/privilege")
public class DutyWebController extends BaseWebController {
	
	@Autowired
	private DutyService dutyService;
	
	@Autowired
	private RoleService roleService;

	@RequiresAuthentication
	@RequestMapping("/duties")
	public String duties(HttpServletRequest request, ModelMap model, @Valid TenantDto tenantDto, BindingResult bindingResult){
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		List<Duty> duties = dutyService.allDuties(tenantDto.getTid());
		model.put("duties", duties);
		model.put("roles", roleService.roles(tenantDto.getTid()));
		Map<String, Object> params = new HashMap<>();
		model.put("param", params);
		return "/duty/duty";
	}
}
