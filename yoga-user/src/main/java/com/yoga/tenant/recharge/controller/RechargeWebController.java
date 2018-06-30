package com.yoga.tenant.recharge.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.tenant.recharge.dto.ListDto;
import com.yoga.tenant.recharge.model.Recharge;
import com.yoga.tenant.recharge.service.RechargeService;
import com.yoga.tenant.setting.Settable;
import com.yoga.tenant.tenant.service.TenantService;
import com.yoga.user.basic.TenantPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Settable
@Controller("tenantRechargeWebController")
@RequestMapping("/tenant")
public class RechargeWebController extends BaseWebController {

    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private TenantService tenantService;

    @RequiresPermissions("gbl_tenant_recharge.query")
    @RequestMapping("/recharge")
    public String list(TenantPage page, ListDto dto, ModelMap model) {
        if (dto.getTid() != 0) throw new BusinessException("非法操作");
        PageList<Recharge> recharges = rechargeService.list(dto.getTenantId(), dto.getTenantName(), dto.getReferee(), dto.getBeginDate(), dto.getEndDate(), dto.getInvoiced(), dto.getNumber(), page.getPageIndex(), page.getPageSize());
        model.put("recharges", recharges);
        model.put("tenants", tenantService.getAll());
        model.put("param", dto.wrapAsMap());
        model.put("page", recharges.getPage());
        return "/tenant/recharge/recharges";
    }
}
