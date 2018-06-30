package com.yoga.debug.methods.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.debug.methods.model.Group;
import com.yoga.debug.methods.service.MethodService;
import com.yoga.tenant.tenant.service.TenantService;
import com.yoga.user.basic.TenantDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Explain(exclude = true)
@RequestMapping("/api/debug/i")
@Controller(value = "debugMethodApiController")
public class MethodApiController extends BaseApiController {

    @Autowired
    private MethodService methodService;
    @Autowired
    private TenantService tenantService;

    @Explain("刷新API接口")
    @ResponseBody
    @RequestMapping("/refresh")
    public CommonResult onRefresh(TenantDto dto) throws IOException, ClassNotFoundException {
        methodService.refresh();
        List<Group> allMethods = methodService.getAllMethods();
        final Set<String> modules = Arrays.stream(tenantService.modules(dto.getTid())).collect(Collectors.toSet());
        List<Group> groups = allMethods == null ? null : allMethods.stream().filter(group -> {
            if (group.getModule() == null) return true;
            return modules.contains(group.getModule());
        }).collect(Collectors.toList());
        return new CommonResult(groups);
    }

    @Explain("API接口列表")
    @ResponseBody
    @RequestMapping("/list")
    public CommonResult onList(TenantDto dto) throws IOException, ClassNotFoundException {
        List<Group> allMethods = methodService.getAllMethods();
        if (allMethods == null) {
            methodService.refresh();
            allMethods = methodService.getAllMethods();
        }
        final Set<String> modules = Arrays.stream(tenantService.modules(dto.getTid())).collect(Collectors.toSet());
        List<Group> groups = allMethods == null ? null : allMethods.stream().filter(group -> {
            if (group.getModule() == null) return true;
            return modules.contains(group.getModule());
        }).collect(Collectors.toList());
        return new CommonResult(groups);
    }
}
