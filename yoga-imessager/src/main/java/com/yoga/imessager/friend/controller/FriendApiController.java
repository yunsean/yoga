package com.yoga.imessager.friend.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.imessager.friend.dto.*;
import com.yoga.imessager.friend.service.FriendService;
import com.yoga.user.basic.TenantDto;
import com.yoga.user.basic.TenantPage;
import com.yoga.user.model.LoginUser;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Explain(value = "IM朋友管理", module = "gcf_imessager")
@RestController
@RequestMapping("/api/im/friend")
public class FriendApiController extends BaseApiController {

    @Autowired
    private FriendService friendService;

    @RequiresAuthentication
    @RequestMapping("/apply")
    public CommonResult apply(LoginUser user, @Valid ApplyDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        friendService.apply(dto.getTid(), user.getId(), dto.getFriendId(), dto.isAllowMoment(), dto.getAlias(), dto.getHello());
        return new CommonResult();
    }

    @RequiresAuthentication
    @RequestMapping("/accept")
    public CommonResult accept(LoginUser user, @Valid AcceptDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        friendService.accept(dto.getTid(), user.getId(), dto.getFriendId(), dto.isAllowMoment(), dto.getAlias());
        return new CommonResult();
    }

    @RequiresAuthentication
    @RequestMapping("/reject")
    public CommonResult reject(LoginUser user, @Valid RejectDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        friendService.reject(dto.getTid(), user.getId(), dto.getFriendId(), dto.getReason());
        return new CommonResult();
    }

    @RequiresAuthentication
    @RequestMapping("/delete")
    public CommonResult delete(LoginUser user, @Valid DeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        friendService.delete(dto.getTid(), user.getId(), dto.getFriendId());
        return new CommonResult();
    }

    @RequiresAuthentication
    @RequestMapping("/modify")
    public CommonResult modify(LoginUser user, @Valid ModifyDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        friendService.modify(dto.getTid(), user.getId(), dto.getFriendId(), dto.getAllowMoment(), dto.getAlias());
        return new CommonResult();
    }

    @RequiresAuthentication
    @RequestMapping("/block")
    public CommonResult block(LoginUser user, @Valid BlockDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        friendService.block(dto.getTid(), user.getId(), dto.getFriendId(), dto.isBlock());
        return new CommonResult();
    }

    @RequiresAuthentication
    @RequestMapping("/list")
    public CommonResult list(LoginUser user, TenantPage page, @Valid TenantDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        return new CommonResult();
    }
}
