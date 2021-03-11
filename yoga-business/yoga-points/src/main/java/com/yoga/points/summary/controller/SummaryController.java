package com.yoga.points.summary.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResults;
import com.yoga.core.data.CommonPage;
import com.yoga.core.data.tuple.TwoTuple;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.excelkit.ExcelKit;
import com.yoga.operator.branch.service.BranchService;
import com.yoga.operator.duty.service.DutyService;
import com.yoga.operator.user.model.User;
import com.yoga.points.summary.dto.*;
import com.yoga.points.summary.model.PointsSummary;
import com.yoga.points.summary.model.PointsYear;
import com.yoga.points.summary.model.SummarySetting;
import com.yoga.points.summary.service.PointsStatisticService;
import com.yoga.points.summary.service.PointsSummaryService;
import com.yoga.points.summary.service.PointsYearService;
import com.yoga.points.summary.vo.MyPointsSummaryVo;
import com.yoga.points.summary.vo.PointsSummaryVo;
import com.yoga.points.summary.vo.PointsYearVo;
import com.yoga.setting.annotation.Settable;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Settable
@Api(tags = "积分统计")
@Controller("pointsSummaryController")
@RequestMapping("/admin/points/summary")
public class SummaryController extends BaseController {

	@Autowired
	private PointsSummaryService summaryService;
	@Autowired
	private PointsStatisticService statisticService;
	@Autowired
	private PointsYearService yearService;
	@Autowired
	private BranchService branchService;
	@Autowired
	private DutyService dutyService;

	@ApiIgnore
	@RequiresAuthentication
	@RequestMapping("")
	public String list(ModelMap model, @Valid CustomPage page, @Valid ListDto dto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		SummarySetting setting = summaryService.getSetting(dto.getTid());
		List<Integer> years = yearService.allYears(dto.getTid());
		Collections.sort(years);
		if (dto.getYear() == null) dto.setYear(setting.getAnnualNum());
		PointsYear scoreYear = yearService.getYear(dto.getTid(), dto.getYear());
		if (scoreYear == null) return "redirect:/admin/points/summary/years";
		years.sort((Integer o1, Integer o2) -> o2 - o1);
		PageInfo<PointsSummary> result = summaryService.list(dto.getTid(), dto.getYear(), null, dto.getBranchId(), dto.getDutyId(), dto.getKeyword(), dto.isPenaltyOnly(), page.getPageIndex(), page.getPageSize(), null);
		model.put("param", dto.wrapAsMap());
		model.put("pointses", result.getList());
		model.put("page", new CommonPage(result));
		model.put("branches", branchService.tree(dto.getTid()));
		model.put("duties", dutyService.list(dto.getTid()));
		model.put("years", years);
		model.put("year", scoreYear);
		model.put("progress", statisticService.getProgress(dto.getTid()));
		model.put("setting", setting);
		return "/admin/points/summary";
	}
	@ApiIgnore
	@Settable(module = PointsSummaryService.ModuleName, key = PointsYearService.PointsYears, name = "积分管理-年度管理", showPage = true)
	@RequiresAuthentication
	@RequestMapping("/years")
	public String years(ModelMap model, @Valid CustomPage page, @Valid BaseDto dto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		model.put("param", dto.wrapAsMap());
		PageInfo<PointsYear> years = yearService.list(dto.getTid(), page.getPageIndex(), page.getPageSize());
		model.put("years", years.getList());
		model.put("page", new CommonPage(years));
		return "/admin/points/years";
	}
	@ApiIgnore
	@Settable(module = PointsSummaryService.ModuleName, key = PointsSummaryService.PointsSummary, name = "积分管理-汇总设置", showWith = "gcf_points")
	@RequestMapping("/setting")
	public String scoreSetting(ModelMap model, @Valid BaseDto dto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		SummarySetting setting = summaryService.getSetting(dto.getTid());
		List<Integer> years = yearService.allYears(dto.getTid());
		model.put("years", years);
		model.put("setting", setting);
		return "/admin/points/setting";
	}
	@ApiIgnore
	@ResponseBody
	@RequiresAuthentication
	@RequestMapping("/export")
	public void exportExcel(HttpServletResponse response, @Valid @ModelAttribute ListDto dto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		if (dto.getYear() == null) {
			SummarySetting setting = summaryService.getSetting(dto.getTid());
			dto.setYear(setting.getAnnualNum());
		}
		List<PointsSummary> result = summaryService.list(dto.getTid(), dto.getYear(), null, dto.getBranchId(), dto.getDutyId(), dto.getKeyword(), dto.isPenaltyOnly(), null);
		ExcelKit.$Export(PointsSummary.class, response).downXlsx(result, false);
	}

	@ResponseBody
	@RequiresAuthentication
	@ApiOperation("发起积分统计")
	@RequiresPermissions("points_summary.update")
	@PostMapping("/statistic.json")
	public ApiResult statistic(@Valid @ModelAttribute StatisticDto dto, BindingResult bindingResult) {
		statisticService.statistic(dto.getTid(), dto.getYear());
		return new ApiResult();
	}

	@ResponseBody
	@ApiOperation("查询积分统计进度")
	@RequiresAuthentication
	@GetMapping("/progress.json")
	public ApiResult<String> repeal(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		String progress = statisticService.getProgress(dto.getTid());
		return new ApiResult<>(progress);
	}

	@ResponseBody
	@RequiresAuthentication
	@ApiOperation("当前年度积分TOP10")
	@RequiresPermissions("points_summary.query")
	@GetMapping("/top10.json")
	public ApiResults<PointsSummaryVo> top10(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
		SummarySetting setting = summaryService.getSetting(dto.getTid());
		if (setting == null) throw new BusinessException("尚未配置积分年度");
		int year = setting.getAnnualNum();
		PageInfo<PointsSummary> result = summaryService.list(dto.getTid(), year, null, null, null, null, false, 0, 10, null);
		return new ApiResults<>(result.getList(), PointsSummaryVo.class, (po, vo)-> {
			vo.setPoints(po.getPoints() / 100F);
			vo.setPenalty(po.getPenalty() / 100F);
			vo.setDetail(null);
		});
	}

	@ResponseBody
	@RequiresAuthentication
	@ApiOperation("当前年度积分BOTTOM10")
	@RequiresPermissions("points_summary.query")
	@GetMapping("/bottom10.json")
	public ApiResults<PointsSummaryVo> bottom10(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
		SummarySetting setting = summaryService.getSetting(dto.getTid());
		if (setting == null) throw new BusinessException("尚未配置积分年度");
		int year = setting.getAnnualNum();
		PageInfo<PointsSummary> result = summaryService.list(dto.getTid(), year, null, null, null, null, false, 0, 10, "s.year_rank desc, s.points");
		return new ApiResults<>(result.getList(), PointsSummaryVo.class, (po, vo)-> {
			vo.setPoints(po.getPoints() / 100F);
			vo.setPenalty(po.getPenalty() / 100F);
			vo.setDetail(null);
		});
	}

	@ResponseBody
	@RequiresAuthentication
	@ApiOperation("查询年度积分")
	@RequiresPermissions("points_summary.query")
	@GetMapping("/list.json")
	public ApiResults<PointsSummaryVo> list(@Valid @ModelAttribute CustomPage page, @Valid @ModelAttribute ListDto dto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		if (dto.getYear() == null) {
			SummarySetting setting = summaryService.getSetting(dto.getTid());
			if (setting == null) throw new BusinessException("尚未配置积分年度");
			dto.setYear(setting.getAnnualNum());
		}
		PageInfo<PointsSummary> result = summaryService.list(dto.getTid(), dto.getYear(), null, dto.getBranchId(), dto.getDutyId(), dto.getKeyword(), dto.isPenaltyOnly(), page.getPageIndex(), page.getPageSize(), null);
		return new ApiResults<>(result.getList(), PointsSummaryVo.class, (po, vo)-> {
			vo.setPoints(po.getPoints() / 100F);
			vo.setPenalty(po.getPenalty() / 100F);
			vo.setDetail(null);
		});
	}

	@ResponseBody
	@RequiresAuthentication
	@ApiOperation("查询我是否为积分过低预警")
	@GetMapping("/my/lowest.json")
	public ApiResult<Boolean> isLowest(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		User user = User.getLoginUser();
		boolean is = summaryService.isScoreLowest(dto.getTid(), user.getId());
		return new ApiResult<>(is);
	}

	@ResponseBody
	@RequiresAuthentication
	@ApiOperation("我的当前年度积分情况")
	@GetMapping("/my/score.json")
	public ApiResult<MyPointsSummaryVo> myScore(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		User user = User.getLoginUser();
		TwoTuple<LocalDateTime, PointsSummary> result = summaryService.myScore(dto.getTid(), user.getId());
		return new ApiResult<>(result.second, MyPointsSummaryVo.class, (po, vo)-> {
			vo.setUpdateTime(result.first);
			vo.setPoints(po.getPoints() / 100F);
			vo.setPenalty(po.getPenalty() / 100F);
		});
	}

	@ResponseBody
	@RequiresAuthentication
	@ApiOperation("我的所有年度积分情况")
	@GetMapping("/my/scores.json")
	public ApiResults<PointsSummaryVo> myScores(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		User user = User.getLoginUser();
		List<PointsSummary> summaries = summaryService.myScores(dto.getTid(), user.getId());
		return new ApiResults<>(summaries, PointsSummaryVo.class,  (po, vo)-> {
			vo.setPoints(po.getPoints() / 100F);
			vo.setPenalty(po.getPenalty() / 100F);
			vo.setDetail(null);
		});
	}

	@ResponseBody
	@RequiresAuthentication
	@ApiOperation("获取积分年度")
	@GetMapping("/year/get.json")
	public ApiResult<PointsYearVo> getYear(@Valid @ModelAttribute YearGetDto dto, BindingResult bindingResult) {
		PointsYear year = yearService.getYear(dto.getTid(), dto.getId());
		return new ApiResult<>(year, PointsYearVo.class);
	}
	@ResponseBody
	@RequiresAuthentication
	@ApiOperation("添加积分年度")
	@RequiresPermissions("points_summary.year")
	@PostMapping("/year/add.json")
	public ApiResult addYear(@Valid @ModelAttribute YearAddDto dto, BindingResult bindingResult) {
		yearService.add(dto.getTid(), dto.getYear(), dto.getBeginDate(), dto.getEndDate());
		return new ApiResult();
	}
	@ResponseBody
	@RequiresAuthentication
	@ApiOperation("修改积分年度")
	@RequiresPermissions("points_summary.year")
	@PostMapping("/year/update.json")
	public ApiResult updateYear(@Valid @ModelAttribute YearUpdateDto dto, BindingResult bindingResult) {
		yearService.update(dto.getTid(), dto.getId(), dto.getYear(), dto.getBeginDate(), dto.getEndDate());
		return new ApiResult();
	}

	@ApiIgnore
	@ResponseBody
	@RequiresAuthentication
	@RequestMapping("/setting/save.json")
	public ApiResult saveSetting(@Valid @ModelAttribute SettingDto dto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
		SummarySetting setting = new SummarySetting(dto.getAnnualNum(), dto.getWeekAt());
		summaryService.setSetting(dto.getTid(), setting, dto.getAnnualNum() + " / W" + dto.getWeekAt());
		return new ApiResult();
	}
}
