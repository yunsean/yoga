package com.yoga.utility.feie.controller;

import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.ChainMap;
import com.yoga.core.data.ApiResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.setting.annotation.Settable;
import com.yoga.utility.feie.dto.PrinterQueueResult;
import com.yoga.utility.feie.dto.SaveFeiePrinterDto;
import com.yoga.utility.feie.model.FeiePrinterConfig;
import com.yoga.utility.feie.service.FeiePrintService;
import com.yoga.utility.feie.vo.PrintInfo;
import com.yoga.utility.feie.vo.PrintQueue;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Settable
@Controller
@Api(tags = "飞鹅打印机")
@RequestMapping("/admin/utility/feie")
public class FeiePrintController extends BaseController {

    @Autowired
    private FeiePrintService printService;

    @ApiIgnore
    @Settable(key = FeiePrintService.SinglePrinterConfig, name = "系统设置-飞鹅云打印机设置", module = FeiePrintService.ModuleName)
    @RequestMapping("/setting")
    public String defaultRegDept(ModelMap model, @Valid BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        model.put("config", printService.readConfig(dto.getTid()));
        return "/admin/utility/feie/setting";
    }

    @ApiIgnore
    @ResponseBody
    @RequiresAuthentication
    @RequestMapping("/setting/save.json")
    public ApiResult upsertDefaultRegDept(@Valid SaveFeiePrinterDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        printService.addPrinter(dto.getSn(), dto.getKey(), dto.getName());
        FeiePrinterConfig config = new FeiePrinterConfig();
        config.setSn(dto.getSn());
        config.setName(dto.getName());
        printService.saveConfig(dto.getTid(), config);
        return new ApiResult();
    }

    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/info")
    @ApiOperation(value = "打印机信息")
    public ApiResult<PrintInfo> printerInfo(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        FeiePrinterConfig config = printService.readConfig(dto.getTid());
        return new ApiResult<>(new PrintInfo(config.getSn(), config.getName()));
    }

    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/status")
    @ApiOperation(value = "打印机在线状态")
    public ApiResult<String> printerStatus(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String status = printService.printerStatus(dto.getTid());
        return new ApiResult<>(status);
    }

    @ResponseBody
    @RequiresAuthentication
    @GetMapping("/queue")
    @ApiOperation(value = "打印机排队状态")
    public ApiResult<PrintQueue> printerQueue(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PrinterQueueResult result = printService.printerQueue(dto.getTid());
        return new ApiResult<>(new PrintQueue(result.getPrint(), result.getWaiting()));
    }

    @ResponseBody
    @RequiresPermissions("api_feie_printer.clean")
    @GetMapping("/clean")
    @ApiOperation(value = "清空打印机队列")
    public ApiResult cleanPrinter(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        printService.cleanQueue(dto.getTid());
        return new ApiResult();
    }
}
