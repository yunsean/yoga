package com.yoga.ewedding.messager.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.core.utils.StrUtil;
import com.yoga.ewedding.messager.dto.InfoDto;
import com.yoga.user.user.model.User;
import com.yoga.user.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Explain(value = "Ew消息管理", module = "ew_customer")
@RestController("eweddingMessagerApiController")
@EnableAutoConfiguration
@RequestMapping("/api/ewedding/messager")
public class MessagerApiController extends BaseApiController {

    @Autowired
    private UserService userService;

    @Explain("用户信息")
    @RequestMapping("/info")
    public CommonResult logon(@Valid InfoDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = userService.getUser(dto.getTid(), dto.getId());
        return new CommonResult(new MapConverter<User>((item, map) -> {
            String name = item.getFullname();
            if (StrUtil.isBlank(name)) name = item.getPhone();
            map.set("id", item.getId())
                    .set("name", name)
                    .set("avatar", item.getAvatar())
                    .set("gender", item.getGender());
        }).build(user));
    }
}
