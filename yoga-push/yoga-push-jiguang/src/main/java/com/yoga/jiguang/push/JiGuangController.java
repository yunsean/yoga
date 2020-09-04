package com.yoga.jiguang.push;

import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.ApiResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.setting.annotation.Settable;
import com.yoga.setting.service.SettingService;
import com.yoga.utility.push.service.PushService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Settable
@ApiIgnore
@Controller
@RequestMapping("/admin/setting/push/jiguang")
public class JiGuangController extends BaseController {

    @Autowired
    private JiGuangService iGetUiService;

    @RequestMapping("")
    @RequiresAuthentication
    @Settable(module = PushService.ModuleName, key = JiGuangService.Key_Setting, name = "推送设置-极光推送参数")
    public String defaultRegDept(ModelMap model, @Valid BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        JiGuangSetting setting = iGetUiService.getSetting(dto.getTid());
        if (setting == null) setting = new JiGuangSetting();
        model.put("setting", setting);
        return "/admin/utility/push/jiguang";
    }

    @ResponseBody
    @RequiresPermissions(SettingService.Permission_Update)
    @RequestMapping("/save.json")
    public ApiResult save(@Valid JiGuangSettingSaveDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        iGetUiService.setSetting(dto.getTid(), new JiGuangSetting(dto.getAppKey(), dto.getMasterSecret(), dto.isProduct()));
        return new ApiResult();
    }
}
