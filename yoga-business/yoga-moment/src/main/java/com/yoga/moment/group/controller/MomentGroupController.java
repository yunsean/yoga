package com.yoga.moment.group.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.base.BaseVo;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.data.CommonPage;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.StringUtil;
import com.yoga.moment.group.ao.UserForGroup;
import com.yoga.moment.group.dto.*;
import com.yoga.moment.group.model.MomentGroup;
import com.yoga.moment.group.service.MomentGroupService;
import com.yoga.moment.group.vo.MomentGroupVo;
import com.yoga.moment.message.dto.MomentIssueDto;
import com.yoga.moment.message.dto.MomentListDto;
import com.yoga.moment.message.dto.MomentReplyDto;
import com.yoga.moment.message.dto.MomentUpvoteDto;
import com.yoga.moment.message.model.MomentMessage;
import com.yoga.moment.message.service.MomentService;
import com.yoga.moment.message.vo.MomentFollowVo;
import com.yoga.moment.message.vo.MomentMessageVo;
import com.yoga.moment.message.vo.MomentUserVo;
import com.yoga.operator.branch.model.Branch;
import com.yoga.operator.branch.service.BranchService;
import com.yoga.operator.duty.service.DutyService;
import com.yoga.operator.user.model.User;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "信息流群组")
@Controller
@RequestMapping("/admin/moment/group")
public class MomentGroupController extends BaseController {

    @Autowired
    private MomentGroupService groupService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private DutyService dutyService;

    @ApiIgnore
    @RequiresPermissions("moment_group")
    @RequestMapping("/list")
    public String list(ModelMap model, CustomPage page, @Valid GroupListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<MomentGroup> groups = groupService.list(dto.getTid(), dto.getFilter(), page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("groups", groups.getList());
        model.put("page", new CommonPage(groups));
        return "/admin/moment/groups";
    }
    @ApiIgnore
    @RequiresPermissions("moment_group")
    @RequestMapping("/users")
    public String listUsers(ModelMap model, CustomPage page, @Valid GroupUsersDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (!SecurityUtils.getSubject().isPermitted("moment_group.update")) dto.setIncludedOnly(true);
        PageInfo<UserForGroup> users = groupService.allUser(dto.getTid(), dto.getGroupId(), dto.getBranchId(), dto.getDutyId(), dto.getFilter(), dto.isIncludedOnly(), page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("users", users.getList());
        model.put("page", new CommonPage(users));
        model.put("branches", branchService.tree(dto.getTid()));
        model.put("duties", dutyService.list(dto.getTid()));
        return "/admin/moment/users";
    }

    @RequiresAuthentication
    @ResponseBody
    @GetMapping("/list.json")
    @ApiOperation(value = "获取群组列表")
    public ApiResults<MomentGroupVo> list(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        List<MomentGroup> groups = groupService.listForUser(dto.getTid(), user.getId());
        return new ApiResults<>(groups, MomentGroupVo.class);
    }
    @RequiresAuthentication
    @ResponseBody
    @GetMapping("/get.json")
    @ApiOperation(value = "获取群组详情")
    public ApiResult<MomentGroupVo> get(@Valid @ModelAttribute GroupGetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        MomentGroup group = groupService.get(dto.getTid(), dto.getId());
        return new ApiResult<>(group, MomentGroupVo.class);
    }
    @ResponseBody
    @PostMapping("/add.json")
    @RequiresPermissions("moment_group.add")
    @ApiOperation(value = "增加新群组")
    public ApiResult add(@Valid @ModelAttribute GroupAddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        groupService.add(dto.getTid(), dto.getName(), dto.getRemark());
        return new ApiResult();
    }
    @ResponseBody
    @DeleteMapping("/delete.json")
    @RequiresPermissions("moment_group.delete")
    @ApiOperation(value = "删除已有群组")
    public ApiResult delete(@Valid @ModelAttribute GroupDeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        groupService.delete(dto.getTid(), dto.getId());
        return new ApiResult();
    }
    @ResponseBody
    @PostMapping("/update.json")
    @RequiresPermissions("moment_group.update")
    @ApiOperation(value = "修改群组信息")
    public ApiResult update(@Valid @ModelAttribute GroupUpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        groupService.update(dto.getTid(), dto.getId(), dto.getName(), dto.getRemark());
        return new ApiResult();
    }
    @ResponseBody
    @PostMapping("/users/add.json")
    @RequiresPermissions("moment_group.update")
    @ApiOperation(value = "添加群组用户")
    public ApiResult addUser(@Valid @ModelAttribute GroupUserAddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        groupService.addUser(dto.getTid(), dto.getGroupId(), dto.getUserId());
        return new ApiResult();
    }
    @ResponseBody
    @PostMapping("/users/delete.json")
    @RequiresPermissions("moment_group.update")
    @ApiOperation(value = "删除群组用户")
    public ApiResult deleteUser(@Valid @ModelAttribute GroupUserDeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        groupService.deleteUser(dto.getTid(), dto.getGroupId(), dto.getUserId());
        return new ApiResult();
    }
    @ResponseBody
    @PostMapping("/users/update.json")
    @RequiresPermissions("moment_group.update")
    @ApiOperation(value = "更新群组用户")
    public ApiResult updateUsers(BaseDto dto, @Valid @RequestBody GroupUserUpdateDto bean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        groupService.updateUser(dto.getTid(), bean.getGroupId(), bean.getAddIds(), bean.getDeleteIds());
        return new ApiResult();
    }
}
