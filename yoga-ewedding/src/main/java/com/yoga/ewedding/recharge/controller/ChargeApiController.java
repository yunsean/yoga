package com.yoga.ewedding.recharge.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.ewedding.counselor.service.CounselorService;
import com.yoga.ewedding.recharge.dto.GetDto;
import com.yoga.ewedding.recharge.dto.RechargeDto;
import com.yoga.ewedding.recharge.dto.SetDto;
import com.yoga.ewedding.recharge.enums.PayType;
import com.yoga.ewedding.recharge.enums.RechargeStatus;
import com.yoga.ewedding.recharge.model.Charge;
import com.yoga.ewedding.recharge.model.Order;
import com.yoga.ewedding.recharge.service.ChargeService;
import com.yoga.ewedding.recharge.service.RechargeService;
import com.yoga.user.basic.TenantDto;
import com.yoga.user.basic.TenantPage;
import com.yoga.user.model.LoginUser;
import com.yoga.user.user.model.User;
import com.yoga.user.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

@Explain(value = "Ew充值收费", module = "ew_charge")
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/ewedding/charge")
public class ChargeApiController extends BaseApiController {

    @Autowired
    private ChargeService chargeService;
    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private CounselorService counselorService;
    @Autowired
    private UserService userService;

    @RequiresAuthentication
    @Explain("顾问充值")
    @RequestMapping("/recharge")
    public CommonResult rechare(@Valid RechargeDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        Date[] dates = counselorService.prepareRecharge(dto.getTid(), user.getId(), dto.getType(), dto.getExpireTo());
        if (dto.getPayType() == PayType.Alipay) {
            String sign = rechargeService.alipay(dto.getTid(), user.getDeptId(), user.getId(), dto.getType(), dates[0], dates[1], dto.getAmount());
            return new CommonResult(sign);
        } else {
            throw new BusinessException("目前暂只支持支付宝支付");
        }
    }

    @RequiresAuthentication
    @Explain("充值费率")
    @RequestMapping("/get")
    public CommonResult get(@Valid GetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Subject subject = SecurityUtils.getSubject();
        User user = userService.getLoginInfo();
        if (dto.getTypeId() == null) dto.setTypeId(user.getDeptId());
        if (dto.getTypeId() == null) throw new BusinessException("请输入顾问类型");
        if (dto.getTypeId().longValue() != user.getDeptId() && !subject.isPermitted("ew_charge.query") && !subject.isPermitted("ew_charge.update")) {
            throw new UnauthorizedException();
        }
        Charge charge = chargeService.get(dto.getTid(), dto.getTypeId());
        return new CommonResult(charge);
    }

    @RequiresPermissions("ew_charge.update")
    @Explain(exclude = true)
    @RequestMapping("/set")
    public CommonResult set(@Valid SetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        chargeService.set(dto.getTid(), dto.getTypeId(), dto.getOriginalFee(), dto.getMonthlyFee(), dto.getQuarterlyFee(), dto.getHalfyearFee(), dto.getYearlyFee());
        return new CommonResult();
    }

    @RequiresAuthentication
    @Explain("充值记录")
    @RequestMapping("/orders")
    public CommonResult orders(LoginUser user, TenantPage page, @Valid TenantDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageList<Order> orders = rechargeService.find(dto.getTid(), user.getId(), null, RechargeStatus.paied, null, null, null, page.getPageIndex(), page.getPageSize());
        return new CommonResult(new MapConverter<Order>(new MapConverter.Converter<Order>() {
            @Override
            public void convert(Order item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("orderTime", item.getCreateTime())
                        .set("amount", item.getAmount())
                        .set("type", item.getRechargeType())
                        .set("status", item.getStatus())
                        .set("orderNo", item.getOrderNo());
            }
        }).build(orders), orders.getPage());
    }

    @Explain(exclude = true)
    @RequestMapping("/alipay/closed/{tid}")
    public CommonResult closed(@PathVariable(value = "tid") Long tenantId, Map<String, String> payload) {
        return new CommonResult();
    }
    @Explain(exclude = true)
    @RequestMapping("/alipay/paied/{tid}")
    public CommonResult paied(@PathVariable(value = "tid") Long tenantId, @RequestParam Map<String, String> payload) {
        rechargeService.paied(tenantId, payload);
        return new CommonResult();
    }
}
