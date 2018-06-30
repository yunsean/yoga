package com.yoga.tenant.recharge.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.tenant.recharge.dto.AddDto;
import com.yoga.tenant.recharge.dto.InvoicedDto;
import com.yoga.tenant.recharge.service.RechargeService;
import com.yoga.user.model.LoginUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Explain(value = "GLB租户充值续费", module = RechargeService.ModuleName)
@RestController("tenantRechargeApiController")
@RequestMapping("/api/tenant/recharge")
public class RechargeApiController extends BaseApiController {

    @Autowired
    private RechargeService rechargeService;

    @Explain("添加续费记录")
    @ResponseBody
    @RequestMapping("/add")
    @RequiresPermissions("gbl_tenant_recharge.add")
    public CommonResult add(LoginUser user, @Valid AddDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        rechargeService.add(dto.getTenantId(), user.getId(), dto.getRefereeA(), dto.getRefereeB(), dto.getAmount(), dto.getExpireDate(), dto.isInvoiced(), dto.getInvoiceNo(), dto.getOrderNo(), dto.getTradeNo(), dto.getRemark());
        return new CommonResult();
    }

    @Explain("续费出票")
    @ResponseBody
    @RequestMapping("/invoiced")
    @RequiresPermissions("gbl_tenant_recharge.add")
    public CommonResult invoiced(LoginUser user, @Valid InvoicedDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        rechargeService.invoiced(dto.getId(), dto.getInvoiceNo(), dto.getOrderNo(), dto.getTradeNo(), dto.getRemark());
        return new CommonResult();
    }
}
