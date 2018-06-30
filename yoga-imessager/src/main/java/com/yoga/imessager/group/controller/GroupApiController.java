package com.yoga.imessager.group.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.imessager.group.dto.*;
import com.yoga.imessager.group.enums.UserType;
import com.yoga.imessager.group.model.Group;
import com.yoga.imessager.group.model.Member;
import com.yoga.imessager.group.service.GroupService;
import com.yoga.imessager.user.model.UserAndType;
import com.yoga.user.basic.TenantDto;
import com.yoga.user.model.LoginUser;
import com.yoga.tenant.setting.Settable;
import com.yoga.user.user.model.User;
import com.yoga.user.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Explain(value = "IM群组管理", module = "gcf_imessager")
@RestController
@RequestMapping("/api/im/group")
@Settable(key = GroupService.Setting_EveryOneGroup, name = "启用全员群组", module = GroupService.Setting_Module, type = boolean.class)
public class GroupApiController extends BaseApiController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;

    @Explain("查找群组")
    @RequiresAuthentication
    @RequestMapping("/find")
    public CommonResult findGroup(HttpServletRequest request, LoginUser login, @Valid FindDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (groupService.everyOneGroup(dto.getTid())) {
            groupService.getEveryOneGroup(dto.getTid());
        }
        List<Group> groups = groupService.find(dto.getTid(), dto.getName());
        return new CommonResult(new MapConverter<>(new MapConverter.Converter<Group>() {
            @Override
            public void convert(Group item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("name", item.getName())
                        .set("avatar", item.getAvatar())
                        .set("memberCount", item.getMemberCount())
                        .set("createTime", item.getCreateTime())
                        .set("creator", item.getCreator())
                        .set("creatorId", item.getCreatorId());
            }
        }).build(groups));
    }

    @Explain("创建群组")
    @RequiresAuthentication
    @RequestMapping("/create")
    public CommonResult create(HttpServletRequest request, LoginUser login, @Valid TenantDto dto, @RequestBody @Valid CreateBean bean, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<Member> members = bean.getMembers();
        if (bean.isAllUser()) {
            members = new ArrayList<>();
            PageList<User> users = userService.findUsers(dto.getTid(), null, null, null, 0, 1000);
            for (User user : users) {
                if (user.getId() == login.getId()) continue;
                members.add(new Member(user.getId(), user.getFullname(), user.getAvatar()));
            }
        }
        groupService.create(dto.getTid(), bean.getName(), bean.getAvatar(), login.getId(), login.getNickname(), login.getAvatar(), members);
        return new CommonResult();
    }

    @Explain("解散群组")
    @RequiresAuthentication
    @RequestMapping("/dismiss")
    public CommonResult dismiss(HttpServletRequest request, @Valid DismissDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        groupService.dismiss(dto.getTid(), dto.getId(), user.getId());
        return new CommonResult();
    }

    @Explain("重命名群组")
    @RequiresAuthentication
    @RequestMapping("/rename")
    public CommonResult rename(HttpServletRequest request, @Valid RenameDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        groupService.rename(dto.getTid(), dto.getId(), dto.getName(), dto.getAvatar());
        return new CommonResult();
    }

    @Explain("拉入群组")
    @RequiresAuthentication
    @RequestMapping("/pull")
    public CommonResult pullinto(HttpServletRequest request, @RequestBody @Valid PullBean bean, @Valid TenantDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        groupService.join(dto.getTid(), bean.getId(), UserType.NORMAL, bean.getMembers());
        return new CommonResult();
    }

    @Explain("踢出用户")
    @RequiresAuthentication
    @RequestMapping("/kick")
    public CommonResult kickout(HttpServletRequest request, @Valid KickDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        groupService.quit(dto.getTid(), dto.getId(), dto.getUserIds());
        return new CommonResult();
    }

    @Explain("群组详情")
    @RequiresAuthentication
    @RequestMapping("/info")
    public CommonResult groupInfo(HttpServletRequest request, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Group group = groupService.get(dto.getTid(), dto.getId());
        List<UserAndType> users = groupService.users(dto.getTid(), dto.getId());
        return new CommonResult(new MapConverter.MapItem<String, Object>()
                .set("group", new MapConverter<>(new MapConverter.Converter<Group>() {
                    @Override
                    public void convert(Group item, MapConverter.MapItem<String, Object> map) {
                        map.set("id", item.getId())
                                .set("name", item.getName())
                                .set("avatar", item.getAvatar())
                                .set("memberCount", item.getMemberCount())
                                .set("createTime", item.getCreateTime())
                                .set("creator", item.getCreator())
                                .set("creatorId", item.getCreatorId());
                    }
                }).build(group))
                .set("members", new MapConverter<>(new MapConverter.Converter<UserAndType>() {
                    @Override
                    public void convert(UserAndType item, MapConverter.MapItem<String, Object> map) {
                        map.set("id", item.getId())
                                .set("nickname", item.getNickname())
                                .set("avatar", item.getAvatar())
                                .set("userType", item.getUserType());
                    }
                }).build(users)));
    }

    @Explain("群组用户列表")
    @RequiresAuthentication
    @RequestMapping("/members")
    public CommonResult getGroupUsers(HttpServletRequest request, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<UserAndType> users = groupService.users(dto.getTid(), dto.getId());
        return new CommonResult(new MapConverter<>(new MapConverter.Converter<UserAndType>() {
            @Override
            public void convert(UserAndType item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("nickname", item.getNickname())
                        .set("avatar", item.getAvatar())
                        .set("userType", item.getUserType());
            }
        }).build(users));
    }

    @Explain("同意入群申请")
    @RequiresAuthentication
    @RequestMapping("/accept")
    public CommonResult acceptJoin(HttpServletRequest request, LoginUser login, @Valid AcceptDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        groupService.accept(dto.getTid(), dto.getId(), login.getId(), dto.getUserId());
        return new CommonResult();
    }

    @Explain("拒绝入群申请")
    @RequiresAuthentication
    @RequestMapping("/reject")
    public CommonResult rejectJoin(HttpServletRequest request, LoginUser login, @Valid AcceptDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        groupService.reject(dto.getTid(), dto.getId(), login.getId(), dto.getUserId());
        return new CommonResult();
    }
}
