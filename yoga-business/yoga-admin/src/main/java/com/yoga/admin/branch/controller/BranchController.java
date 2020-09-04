package com.yoga.admin.branch.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.admin.branch.vo.Branch2Vo;
import com.yoga.admin.branch.vo.BranchFilterVo;
import com.yoga.admin.branch.vo.BranchNodeVo;
import com.yoga.admin.branch.vo.BranchVo;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.base.BaseVo;
import com.yoga.core.data.*;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.admin.branch.dto.*;
import com.yoga.operator.branch.model.Branch;
import com.yoga.operator.branch.service.BranchService;
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
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/admin/operator/branch")
@Api(tags = "部门管理")
public class BranchController extends BaseController {

    @Autowired
    private BranchService branchService;
    @Autowired
    private RoleService roleService;

    @ApiIgnore
    @RequiresPermissions("admin_branch")
    @RequestMapping("/list")
    public String depts(ModelMap model, @Valid BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        model.put("param", dto.wrapAsMap());
        List<Branch> branches = branchService.tree(dto.getTid());
        model.put("branches", branches);
        model.put("roles", roleService.list(dto.getTid()));
        return "/admin/branch/branches";
    }

    @RequiresAuthentication
    @ResponseBody
    @GetMapping("/list.json")
    @ApiOperation(value = "获取部门列表")
    public ApiResults<Branch2Vo> list(CustomPage page, @Valid @ModelAttribute BranchListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Branch> branches = branchService.list(dto.getTid(), dto.getName(), page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(branches, Branch2Vo.class);
    }
    @RequiresAuthentication
    @ResponseBody
    @GetMapping("/filter.json")
    @ApiOperation(value = "快速过滤部门")
    public ApiResults<BranchFilterVo> filter(CustomPage page, @Valid @ModelAttribute BranchFilterDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Branch> branches = branchService.list(dto.getTid(), dto.getFilter(), page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(branches, BranchFilterVo.class);
    }
    @RequiresAuthentication
    @ResponseBody
    @GetMapping("/children.json")
    @ApiOperation(value = "获取部门树")
    public ApiResults<BranchNodeVo> children(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<Branch> branches = branchService.tree(dto.getTid());
        return new ApiResults<>(convert(branches));
    }
    @RequiresAuthentication
    @ResponseBody
    @GetMapping("/get.json")
    @ApiOperation(value = "获取部门详情")
    public ApiResult<BranchVo> get(@Valid @ModelAttribute BranchGetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Branch branch = branchService.get(dto.getTid(), dto.getId());
        return new ApiResult<>(branch, BranchVo.class, (po, vo)-> vo.setRoleIds(branchService.listRoles(dto.getTid(), dto.getId())));
    }
    @ResponseBody
    @PostMapping("/add.json")
    @RequiresPermissions("admin_branch.add")
    @ApiOperation(value = "增加新部门")
    public ApiResult addDept(@Valid @ModelAttribute BranchAddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        branchService.add(dto.getTid(), dto.getParentId(), dto.getName(), dto.getRemark(), dto.getRoleIds());
        return new ApiResult();
    }
    @ResponseBody
    @DeleteMapping("/delete.json")
    @RequiresPermissions("admin_branch.del")
    @ApiOperation(value = "删除已有部门")
    public ApiResult delDept(@Valid @ModelAttribute BranchDeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        branchService.delete(dto.getTid(), dto.getId());
        return new ApiResult();
    }
    @ResponseBody
    @PostMapping("/update.json")
    @RequiresPermissions("admin_branch.update")
    @ApiOperation(value = "修改部门信息")
    public ApiResult updateDept(@Valid @ModelAttribute BranchUpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        branchService.update(dto.getTid(), dto.getId(), dto.getName(), dto.getRemark(), dto.getParentId(), dto.getRoleIds());
        return new ApiResult();
    }

    private static List<BranchNodeVo> convert(Collection<Branch> branches) {
        if (branches == null || branches.size() < 1) return null;
        return BaseVo.copys(branches, BranchNodeVo.class, (po, vo)-> vo.setChildren(convert(po.getChildren())));
    }
}

