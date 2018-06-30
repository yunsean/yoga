package com.yoga.ewedding.recharge.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.core.data.PageList;
import com.yoga.ewedding.counselor.service.CounselorService;
import com.yoga.ewedding.recharge.dto.RechargeListDto;
import com.yoga.ewedding.recharge.enums.RechargeStatus;
import com.yoga.ewedding.recharge.enums.RechargeType;
import com.yoga.ewedding.recharge.model.Charge;
import com.yoga.ewedding.recharge.model.Order;
import com.yoga.ewedding.recharge.service.ChargeService;
import com.yoga.ewedding.recharge.service.RechargeService;
import com.yoga.user.basic.TenantDto;
import com.yoga.user.basic.TenantPage;
import com.yoga.user.dept.service.DepartmentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@EnableAutoConfiguration
@RequestMapping("/ewedding/charge")
public class ChargeWebController extends BaseWebController {

    @Autowired
    private CounselorService counselorService;
    @Autowired
    private ChargeService chargeService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private RechargeService rechargeService;

    @RequiresPermissions(value = {"ew_charge.query", "ew_charge.update"})
    @RequestMapping("")
    public String list(ModelMap model, TenantDto dto) {
        Long deptId = counselorService.getDepartmentId(dto.getTid());
        if (deptId == null) deptId = 0L;
        List<Charge> charges = chargeService.list(dto.getTid(), deptId);
        model.put("charges", charges);
        model.put("deptMap", departmentService.getDeptMap(dto.getTid()));
        return "/charge/charge";
    }

    @RequiresPermissions(value = {"ew_recharge.query"})
    @RequestMapping("/recharge")
    public String orders(ModelMap model, TenantPage page, RechargeListDto dto) {
        PageList<Order> orders = rechargeService.find(dto.getTid(), null, dto.getType(), dto.getStatus(), dto.getUser(), dto.getBegin(), dto.getEnd(), page.getPageIndex(), page.getPageSize());
        model.put("orders", orders);
        model.put("page", orders.getPage());
        model.put("param", dto.wrapAsMap());
        model.put("types", RechargeType.values());
        model.put("status", RechargeStatus.values());
        return "/charge/orders";
    }
}