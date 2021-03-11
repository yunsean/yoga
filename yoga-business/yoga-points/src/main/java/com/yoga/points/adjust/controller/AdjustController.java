package com.yoga.points.adjust.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseController;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.CommonPage;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.operator.branch.service.BranchService;
import com.yoga.operator.duty.service.DutyService;
import com.yoga.operator.user.model.User;
import com.yoga.points.adjust.dto.AddDto;
import com.yoga.points.adjust.dto.DeleteDto;
import com.yoga.points.adjust.dto.ListDto;
import com.yoga.points.adjust.model.PointsAdjust;
import com.yoga.points.adjust.service.PointsAdjustService;
import com.yoga.points.summary.model.PointsYear;
import com.yoga.points.summary.model.SummarySetting;
import com.yoga.points.summary.service.PointsSummaryService;
import com.yoga.points.summary.service.PointsYearService;
import com.yoga.setting.customize.CustomPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "积分调整")
@RequiresAuthentication
@Controller("pointsAdjustController")
@RequestMapping("/admin/points/adjust")
public class AdjustController extends BaseController {

	@Autowired
	private PointsAdjustService adjustService;
	@Autowired
    private PointsYearService yearService;
	@Autowired
    private BranchService branchService;
	@Autowired
    private DutyService dutyService;
	@Autowired
    private PointsSummaryService summaryService;

	@ApiIgnore
    @RequiresAuthentication
    @RequestMapping("")
    public String statistics2(ModelMap model, @Valid CustomPage page, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        SummarySetting setting = summaryService.getSetting(dto.getTid());
        PointsYear pointsYear = yearService.getYear(dto.getTid(), setting.getAnnualNum());
        if (dto.getBeginDate() == null && pointsYear != null) dto.setBeginDate(pointsYear.getBeginDate());
        if (dto.getEndDate() == null && pointsYear != null) dto.setEndDate(pointsYear.getEndDate());
        Map<String, Object> param = new HashMap<>();
        PageInfo<PointsAdjust> adjusts = adjustService.list(dto.getTid(), null, dto.getBranchId(), dto.getDutyId(), dto.getName(), dto.getBeginDate(), dto.getEndDate(), page.getPageIndex(), page.getPageSize());
        model.put("param", dto.wrapAsMap());
        model.put("adjusts", adjusts.getList());
        model.put("page", new CommonPage(adjusts));
        model.put("setting", setting);
        model.put("branches", branchService.tree(dto.getTid()));
        model.put("duties", dutyService.list(dto.getTid()));
        return "/admin/points/adjust";
    }

    @ResponseBody
    @RequiresAuthentication
	@ApiOperation("添加积分调整")
	@RequiresPermissions("points_adjust.update")
	@PostMapping("/add.json")
	public ApiResult add(@Valid @ModelAttribute AddDto dto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
        int points = (int)(dto.getPoints() * 100);
		if (points == 0) throw new BusinessException("调整分值不能为0");
		adjustService.add(dto.getTid(), dto.getUserId(), dto.getDate().atStartOfDay(), points, dto.getReason(), user.getId());
		return new ApiResult();
	}

    @ResponseBody
    @RequiresAuthentication
	@ApiOperation("撤销积分调整，必须是调整积分的本人")
	@RequiresPermissions("points_adjust.update")
	@DeleteMapping("/repeal.json")
	public ApiResult repeal(@Valid @ModelAttribute DeleteDto dto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = User.getLoginUser();
		adjustService.repeal(dto.getTid(), dto.getId(), user.getId());
		return new ApiResult();
	}
}
