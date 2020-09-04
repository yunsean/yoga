package com.yoga.admin.user.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.admin.user.dto.*;
import com.yoga.admin.user.vo.FilterUserVo;
import com.yoga.admin.user.vo.UserVo;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.*;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.operator.branch.model.Branch;
import com.yoga.operator.branch.service.BranchService;
import com.yoga.operator.duty.service.DutyService;
import com.yoga.operator.role.service.RoleService;
import com.yoga.operator.user.model.User;
import com.yoga.operator.user.service.UserService;
import com.yoga.setting.customize.CustomPage;
import com.yoga.utility.uploader.model.UploadFile;
import com.yoga.utility.uploader.service.UploadService;
import com.yoga.utility.uploader.vo.UploadFileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Api(tags = "用户管理")
@RequestMapping("/admin/operator/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private DutyService dutyService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UploadService uploadService;

    @ApiIgnore
    @RequiresPermissions("admin_user")
    @RequestMapping("/list")
    public String users(ModelMap model, CustomPage page, @Valid UserListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<User> users = userService.list(dto.getTid(), dto.getFilter(), dto.getBranchId(), dto.getDutyId(), null,  null, null, page.getPageIndex(), page.getPageSize());
        model.put("branches", branchService.tree(dto.getTid()));
        model.put("duties", dutyService.list(dto.getTid()));
        model.put("roles", roleService.list(dto.getTid()));
        model.put("list", roleService.list(dto.getTid()));
        model.put("users", users.getList());
        model.put("page", new CommonPage(users));
        model.put("param", dto.wrapAsMap());
        return "/admin/user/users";
    }
    @ApiIgnore
    @RequiresPermissions("admin_user")
    @RequestMapping("/info")
    public String userInfo(ModelMap model, @Valid UserGetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = userService.get(dto.getTid(), dto.getId());
        List<Branch> branches = branchService.tree(dto.getTid());
        if (branches != null && branches.size() > 0) branches.add(0, new Branch(dto.getTid(), "未指定", "", 0L));
        model.put("branches", branches);
        model.put("roles", userService.getRoles(dto.getTid(), dto.getId()));
        model.put("allRoles", roleService.list(dto.getTid()));
        model.put("user", user);
        return "/admin/user/info";
    }

    @ResponseBody
    @RequiresAuthentication
    @PostMapping(value = "/passwd.json")
    @ApiOperation(value = "修改密码")
    public ApiResult password(@Valid @ModelAttribute UserPasswdDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        if (user == null) throw  new UnauthenticatedException("请登录！");
        userService.passwd(dto.getTid(), user.getId(), dto.getOldPwd(), dto.getNewPwd());
        return new ApiResult();
    }
    @ResponseBody
    @RequiresAuthentication
    @GetMapping(value = "/privileges.json")
    @ApiOperation(value = "获取登录用户拥有的权限")
    public ApiResults<String> permissions() {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        return new ApiResults<>(userService.getPrivileges(user.getTenantId(), user.getId()));
    }
    @ResponseBody
    @RequiresAuthentication
    @GetMapping(value = "/get.json")
    @ApiOperation(value = "获取用户的详细信息")
    public ApiResult<UserVo> get(@ModelAttribute UserGetDto dto) {
        if (dto.getId() == null) {
            User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
            dto.setId(user.getId());
        }
        Subject subject = SecurityUtils.getSubject();
        User user = userService.get(dto.getTid(), dto.getId());
        List<Long> roleIds = userService.getRoles(dto.getTid(), dto.getId());
        if (!subject.isPermitted("admin_user")) return new ApiResult<>(user, UserVo.class, (po, vo)-> vo.setRoleIds(roleIds));
        return new ApiResult<>(user, UserVo.class, (po, vo)-> vo.setRoleIds(roleIds));
    }
    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/list.json")
    @ApiOperation(value = "查询用户列表")
    public ApiResults<UserVo> users(CustomPage page, @Valid @ModelAttribute UserListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<User> users = userService.list(dto.getTid(), dto.getFilter(), dto.getBranchId(), dto.getDutyId(), dto.getDutyCode(), dto.getLevelAbove(), null, page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(users, UserVo.class);
    }
    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/highest.json")
    @ApiOperation(value = "查询部门中职级最高的用户")
    public ApiResult<UserVo> highest(@Valid @ModelAttribute UserHighestDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = userService.getHightestDutyLevelUserByChildBranch(dto.getTid(), dto.getBranchId());
        return new ApiResult<>(user, UserVo.class);
    }
    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/ofPrivilege/list.json")
    @ApiOperation(value = "查询具有权限的用户列表")
    public ApiResults<UserVo> ofPrivilege(CustomPage page, @ModelAttribute @Valid UserOfPrivilegeDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<User> users = userService.listOfPrivilege(dto.getTid(), dto.getPrivilege(), dto.getFilter(), dto.getBranchId(), dto.getDutyId(),dto.getDutyCode(), dto.getLevelAbove(), dto.getBranchId(), page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(users, UserVo.class);
    }
    @ResponseBody
    @ApiOperation("用户快速筛选")
    @RequiresAuthentication
    @GetMapping("/filter.json")
    public ApiResults<FilterUserVo> users(CustomPage page, @Valid @ModelAttribute UserFilterDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<User> users = userService.list(dto.getTid(), dto.getFilter(), dto.getBranchId(), dto.getDutyId(), dto.getDutyCode(), dto.getLevelAbove(), null, page.getPageIndex(), page.getPageSize());
        return new ApiResults<>(users, FilterUserVo.class);
    }
    @ResponseBody
    @RequiresPermissions("admin_user.add")
    @PostMapping("/add.json")
    @ApiOperation(value = "新建用户（通过权限新建）")
    public ApiResult addUser(@Valid @ModelAttribute UserAddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        userService.add(dto.getTid(), dto.getUsername(), dto.getPassword(), dto.getBranchId(), dto.getDutyId(), dto.getNickname(),
                dto.getGender(), dto.getTitle(), dto.getAvatar(), dto.getMobile(), dto.getEmail(), dto.getAddress(), dto.getPostcode(),
                dto.getCompany(), dto.getBirthday(), dto.getRoleIds());
        return new ApiResult();
    }
    @ResponseBody
    @RequiresPermissions("admin_user.del")
    @DeleteMapping("/delete.json")
    @ApiOperation(value = "删除现有用户")
    public ApiResult deleteUser(@Valid @ModelAttribute UserDeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        userService.delete(dto.getTid(), dto.getId());
        return new ApiResult();
    }
    @ResponseBody
    @RequiresPermissions("admin_user.update")
    @PostMapping("/enable.json")
    @ApiOperation(value = "启用/禁用用户")
    public ApiResult enableUser(@Valid @ModelAttribute UserEnableDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        userService.disable(dto.getTid(), dto.getId(), !dto.isEnable());
        return new ApiResult();
    }
    @ResponseBody
    @RequiresAuthentication
    @PostMapping(value = "/update.json")
    @ApiOperation(value = "修改用户信息")
    public ApiResult updateInfo(@Valid @ModelAttribute UserUpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getSession().getAttribute("user");
        if (dto.getId() != null && !dto.getId().equals(user.getId()) && !subject.isPermitted("admin_user.update")) throw new UnauthorizedException("您没有修改用户信息的权限");
        if (dto.getId() == null || !subject.isPermitted("admin_user.update")) {  //通过APP端无权修改权限相关信息
            dto.setId(user.getId());
            dto.setBranchId(null);
            dto.setDutyId(null);
            dto.setRoleIds(null);
            dto.setPassword(null);
        }
        userService.update(dto.getTid(), dto.getId(), dto.getUsername(), dto.getPassword(), dto.getBranchId(), dto.getDutyId(),
                dto.getNickname(), dto.getGender(), dto.getTitle(), dto.getAvatar(), dto.getMobile(), dto.getEmail(), dto.getAddress(),
                dto.getPostcode(), dto.getCompany(), dto.getBirthday(), dto.getRoleIds());
        return new ApiResult();
    }

    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/signature/get.json")
    @ApiOperation(value = "获取用户签名信息")
    public ApiResult<UploadFileVo> getSignature(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = (User)SecurityUtils.getSubject().getSession().getAttribute("user");
        long fileId = userService.getSignatureFileId(dto.getTid(), user.getId());
        if (fileId == 0L) throw new BusinessException("尚未设置签名！");
        UploadFile file = uploadService.get(dto.getTid(), fileId);
        if (file == null) throw new BusinessException("签名文件无效，请重新设置签名！");
        return new ApiResult<>(new UploadFileVo(file.getId(), file.getFileSize(), file.getRemoteUrl(), file.getFilename(), file.getContentType()));
    }

    @ResponseBody
    @RequiresAuthentication
    @PostMapping("/signature/get.json")
    @ApiOperation(value = "设置用户签名信息")
    public ApiResult setSignature(@Valid @ModelAttribute UserSignatureSetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = (User)SecurityUtils.getSubject().getSession().getAttribute("user");
        userService.setSignatureFileId(dto.getTid(), user.getId(), dto.getFileId());
        return new ApiResult();
    }
}
