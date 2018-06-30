package com.yoga.debug.methods.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.debug.methods.dto.CallDto;
import com.yoga.debug.methods.dto.ListDto;
import com.yoga.debug.methods.model.Group;
import com.yoga.debug.methods.model.Method;
import com.yoga.debug.methods.service.MethodService;
import com.yoga.tenant.tenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/debug/methods")
@Controller(value = "debugMethodWebController")
public class MethodWebController extends BaseWebController {

    @Autowired
    private MethodService methodService;
    @Autowired
    private TenantService tenantService;

    @RequestMapping("")
    public String listInterface(HttpServletRequest request, ModelMap model, ListDto dto) throws ClassNotFoundException {
        List<Group> methods = methodService.getAllMethods(dto.getFilter());
        if (methods == null) {
            methodService.refresh();
            methods = methodService.getAllMethods(dto.getFilter());
        }
        final Set<String> modules = Arrays.stream(tenantService.modules(dto.getTid())).collect(Collectors.toSet());
        List<Group> groups = methods == null ? null : methods.stream().filter(group -> {
            if (group.getModule() == null) return true;
            return modules.contains(group.getModule());
        }).collect(Collectors.toList());
        model.put("filter", dto.getFilter());
        model.put("groups", groups);
        return "/debug/methods";
    }
    @RequestMapping("/refresh")
    public String refreshInterface(HttpServletRequest request, ModelMap model, ListDto dto) throws ClassNotFoundException {
        methodService.refresh();
        List<Group> methods = methodService.getAllMethods(dto.getFilter());
        final Set<String> modules = Arrays.stream(tenantService.modules(dto.getTid())).collect(Collectors.toSet());
        List<Group> groups = methods == null ? null : methods.stream().filter(group -> {
            if (group.getModule() == null) return true;
            return modules.contains(group.getModule());
        }).collect(Collectors.toList());
        model.put("filter", dto.getFilter());
        model.put("groups", groups);
        return "/debug/methods";
    }
    @RequestMapping("/call")
    public String callMethod(HttpServletRequest request, ModelMap model, @Valid CallDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Method method = methodService.getMethod(dto.getUrl());
        model.put("method", method);
        model.put("group", dto.getGroup());
        return "/debug/method";
    }
}
