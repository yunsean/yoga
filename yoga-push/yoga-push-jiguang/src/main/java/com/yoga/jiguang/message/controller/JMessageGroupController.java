package com.yoga.jiguang.message.controller;

import cn.jmessage.api.group.GroupInfoResult;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.data.CommonPage;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.jiguang.message.service.JMessageGroupService;
import com.yoga.operator.branch.service.BranchService;
import com.yoga.operator.duty.service.DutyService;
import com.yoga.setting.customize.CustomPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "聊天群组")
@Controller
@RequestMapping("/admin/jmessage/group")
public class JMessageGroupController extends BaseController {

    @Autowired
    private JMessageGroupService groupService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private DutyService dutyService;

    @ApiIgnore
    @RequiresPermissions("moment_group")
    @RequestMapping("/list")
    public String list(ModelMap model, CustomPage page, @Valid BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<GroupInfoResult> groups = groupService.listGroups(dto.getTid(), page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("groups", groups.getList());
        model.put("page", new CommonPage(groups));
        return "/admin/moment/groups";
    }
}
