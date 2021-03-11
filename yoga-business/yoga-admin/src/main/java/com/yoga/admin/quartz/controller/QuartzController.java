package com.yoga.admin.quartz.controller;

import com.yoga.admin.quartz.dto.QuartzGetDto;
import com.yoga.admin.quartz.dto.QuartzUpdateDto;
import com.yoga.admin.quartz.vo.QuartzTaskVo;
import com.yoga.core.base.BaseController;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.data.ChainMap;
import com.yoga.core.data.MapConverter;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.utility.quartz.QuartzService;
import com.yoga.utility.quartz.QuartzTask;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/admin/setting/quartz")
public class QuartzController extends BaseController {

    @Autowired
    private QuartzService quartzService;

    @RequestMapping("/list")
    @RequiresPermissions("cfg_quartz")
    public String list(ModelMap model) {
        List<QuartzTask> quartzTasks = quartzService.list();
        model.put("tasks", quartzTasks);
        return "/admin/system/quartz";
    }

    @ResponseBody
    @PostMapping("/pause.json")
    @RequiresPermissions("cfg_quartz.update")
    public ApiResult pause(@Valid QuartzGetDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        quartzService.pause(dto.getName(), dto.getGroup());
        return new ApiResult();
    }

    @ResponseBody
    @PostMapping("/resume.json")
    @RequiresPermissions("cfg_quartz.update")
    public ApiResult resume(@Valid QuartzGetDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        quartzService.resume(dto.getName(), dto.getGroup());
        return new ApiResult();
    }

    @ResponseBody
    @PostMapping("/update.json")
    @RequiresPermissions("cfg_quartz.update")
    public ApiResult update(@Valid QuartzUpdateDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        quartzService.edit(new QuartzTask(dto.getName(), dto.getGroup(), dto.getDescription(), dto.getExpression()));
        return new ApiResult();
    }

    @ResponseBody
    @GetMapping("/get.json")
    @RequiresPermissions("cfg_quartz.update")
    public ApiResult<QuartzTaskVo> get(@Valid QuartzGetDto dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        QuartzTask task = quartzService.get(dto.getName(), dto.getGroup());
        return new ApiResult<>(task, QuartzTaskVo.class);
    }
}
