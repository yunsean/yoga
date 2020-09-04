package com.yoga.admin.logging.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.admin.logging.dto.LoggingDetailDto;
import com.yoga.admin.logging.dto.LoggingQueryDto;
import com.yoga.core.base.BaseController;
import com.yoga.core.data.ChainMap;
import com.yoga.core.data.CommonPage;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.MapConverter;
import com.yoga.logging.model.Logging;
import com.yoga.logging.service.LoggingService;
import com.yoga.setting.customize.CustomPage;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "日志查询")
@Controller
@RequestMapping("/admin/system/logging")
public class LoggingController extends BaseController {

    @Autowired
    private LoggingService loggingService;

    @ApiIgnore
    @RequestMapping("/list")
    @RequiresPermissions("sys_logging")
    public String review(ModelMap model, CustomPage page, LoggingQueryDto dto) {
        PageInfo<Logging> loggings = loggingService.list(dto.getTid(), dto.getUserId(), dto.getModule(), dto.getFilter(), dto.getPrimaryId(), dto.getBeginDate(), dto.getEndDate(), page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("loggings", loggings.getList());
        model.put("page", new CommonPage(loggings));
        model.put("modules", loggingService.allModules());
        return "/admin/system/loggings";
    }

    @ApiIgnore
    @RequestMapping("/get.json")
    @ResponseBody
    public CommonResult detail(ModelMap model, LoggingDetailDto dto) {
        Logging logging = loggingService.get(dto.getId());
        return new CommonResult(new MapConverter<>((Logging item, ChainMap<String, Object> map)-> {
            map.set("id", item.getLogId())
                    .set("createTime", item.getCreateTime())
                    .set("userId", item.getUserId())
                    .set("username", item.getUsername())
                    .set("module", item.getModule())
                    .set("moduleName", loggingService.getModuleName(item.getModule()))
                    .set("method", item.getMethod())
                    .set("result", item.getResult())
                    .set("primaryId", item.getPrimaryId())
                    .set("primaryInfo", item.getPrimaryInfo())
                    .set("description", item.getDescription())
                    .set("detail", item.getDetail());
        }).build(logging));
    }
}
