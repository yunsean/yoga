package com.yoga.ewedding.counselor.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.ewedding.counselor.dto.DetailDto;
import com.yoga.ewedding.counselor.dto.ListDto;
import com.yoga.ewedding.counselor.enums.CounselorStatus;
import com.yoga.ewedding.counselor.model.CounselorUser;
import com.yoga.ewedding.counselor.service.CounselorService;
import com.yoga.ewedding.counselor.service.SettingService;
import com.yoga.user.basic.TenantPage;
import com.yoga.user.dept.model.Department;
import com.yoga.user.dept.service.DepartmentService;
import com.yoga.tenant.setting.Settable;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@EnableAutoConfiguration
@RequestMapping("/ewedding/counselor")
@Settable(module = CounselorService.Module_Name, key = CounselorService.DepartmentId_Key, type = SettingService.class, name = "顾问所属部门")
@Settable(module = CounselorService.Module_Name, key = CounselorService.ExperienceDays_Key, type = Integer.class, name = "新注册顾问免费体验天数", defaultValue = "5")
public class CounselorWebController extends BaseWebController {

    @Autowired
    private CounselorService counselorService;
    @Autowired
    private DepartmentService departmentService;

    @RequiresAuthentication
    @RequestMapping("/list")
    public String list(ModelMap model, TenantPage page, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageList<CounselorUser> counselors = counselorService.list(dto.getTid(), dto.getTypeId(), dto.getStatus(), dto.getName(), dto.getCompany(), page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("page", counselors.getPage());
        model.put("counselors", counselors);
        model.put("checking", false);
        model.put("deptMap", departmentService.getDeptMap(dto.getTid()));

        return "/counselor/counselors";
    }
    @RequiresAuthentication
    @RequestMapping("/checking")
    public String examines(ModelMap model, TenantPage page, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getStatus() == null) dto.setStatus(CounselorStatus.checking);
        PageList<CounselorUser> counselors = counselorService.list(dto.getTid(), dto.getTypeId(), dto.getStatus(), dto.getName(), dto.getCompany(), page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("page", counselors.getPage());
        model.put("counselors", counselors);
        model.put("checking", true);
        model.put("deptMap", departmentService.getDeptMap(dto.getTid()));
        return "/counselor/counselors";
    }
    @RequiresAuthentication
    @RequestMapping("/detail")
    public String detail(ModelMap model, TenantPage page, @Valid DetailDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        CounselorUser user = counselorService.get(dto.getTid(), dto.getId());
        model.put("param", dto.wrapAsMap());
        model.put("counselor", user);
        Department department = departmentService.get(dto.getTid(), user.getDeptId());
        if (department != null) {
            model.put("department", department);
        }
        return "/counselor/counselor_detail";
    }
}