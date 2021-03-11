package com.yoga.points.summary.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.data.tuple.TwoTuple;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.logging.annotation.LoggingPrimary;
import com.yoga.points.adjust.service.PointsAdjustService;
import com.yoga.points.summary.mapper.PointsSummaryMapper;
import com.yoga.points.summary.mapper.PointsYearMapper;
import com.yoga.points.summary.model.PointsSummary;
import com.yoga.points.summary.model.PointsYear;
import com.yoga.points.summary.model.SummarySetting;
import com.yoga.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Service
@LoggingPrimary(module = PointsAdjustService.ModuleName, name = "积分统计")
public class PointsSummaryService extends BaseService {

    @Autowired
    private PointsYearMapper yearMapper;
    @Autowired
    private PointsSummaryMapper summaryMapper;
    @Autowired
    private SettingService settingService;
    @Autowired
    private PointsYearService yearService;

    public final static String ModuleName = "points_summary";
    public final static String PointsLowestCount = "points.lowest.count";
    public final static String PointsSummary = "points.summary";

    public PageInfo<PointsSummary> list(long tenantId, int year, Long userId, Long branchId, Long dutyId, String keyword, boolean penaltyOnly, int pageIndex, int pageSize, String orderBy) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<PointsSummary> summaries = summaryMapper.list(tenantId, year, userId, branchId, dutyId, keyword, penaltyOnly, penaltyOnly ? "penalty" : orderBy);
        return new PageInfo<>(summaries);
    }
    public List<PointsSummary> list(long tenantId, int year, Long userId, Long branchId, Long dutyId, String keyword, boolean penaltyOnly, String orderBy) {
        return summaryMapper.list(tenantId, year, userId, branchId, dutyId, keyword, penaltyOnly, penaltyOnly ? "penalty" : orderBy);
    }
    public void setLowestWarning(long tenantId, int year) {
        int percent = getScoreLowestCount(tenantId);
        if (percent < 1) return;
        PointsYear pointsYear = yearService.getYear(tenantId, year);
        if (pointsYear == null) return;
        SummarySetting setting = getSetting(tenantId);
        if (setting == null) return;
        if (setting.getAnnualNum() != year) return;;
        long count = new MapperQuery<>(PointsSummary.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("year", year)
                .count(summaryMapper) * percent / 100;
        List<Long> ids = summaryMapper.lowestPoints(tenantId, year, count);
        String allIds = ids.stream().map(id -> id.toString()).reduce(",", (l, r) -> l + r + ",");
        PointsYear updated = new PointsYear(pointsYear.getId(), allIds);
        yearMapper.updateByPrimaryKeySelective(updated);
    }
    public boolean isScoreLowest(long tenantId, long userId) {
        SummarySetting setting = getSetting(tenantId);
        if (setting == null) return false;
        int year = setting.getAnnualNum();
        PointsYear pointsYear = yearService.getYear(tenantId, year);
        if (pointsYear == null) return false;
        if (pointsYear.getWarningUserIds() == null) return false;
        return pointsYear.getWarningUserIds().contains("," + userId + ",");
    }

    public TwoTuple<LocalDateTime, PointsSummary> myScore(long tenantId, long userId) {
        SummarySetting setting = getSetting(tenantId);
        if (setting == null) throw new BusinessException("尚未配置积分年度");
        int year = setting.getAnnualNum();
        PointsYear pointsYear = yearService.getYear(tenantId, year);
        if (pointsYear == null) throw new BusinessException("尚未配置积分年度");
        PointsSummary summaryItem = new MapperQuery<>(PointsSummary.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("userId", userId)
                .andEqualTo("year", year)
                .queryFirst(summaryMapper);
        if (summaryItem == null) throw new BusinessException("尚未汇总积分");
        return new TwoTuple<>(pointsYear.getUpdateTime(), summaryItem);
    }
    public List<PointsSummary> myScores(long tenantId, long userId) {
        List<PointsSummary> summaryItems = new MapperQuery<>(PointsSummary.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("userId", userId)
                .query(summaryMapper);
        return summaryItems;
    }

    public int getScoreLowestCount(long tenantId) {
        return settingService.get(tenantId, ModuleName, PointsLowestCount, 0);
    }
    public void setSetting(long tenantId, SummarySetting value, String showValue) {
        settingService.save(tenantId, ModuleName, PointsSummary, value.toString(), showValue);
    }
    public SummarySetting getSetting(long tenantId) {
        SummarySetting setting = settingService.get(tenantId, ModuleName, PointsSummary, SummarySetting.class);
        if (setting == null) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            PointsYear pointsYear = yearService.getYear(tenantId, year);
            if (pointsYear == null) {
                pointsYear = yearMapper.maxYear(tenantId);
            }
            if (pointsYear == null) {
                pointsYear = new PointsYear();
                pointsYear.setTenantId(tenantId);
                pointsYear.setYear(calendar.get(Calendar.YEAR));
                calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);
                pointsYear.setBeginDate(LocalDate.of(calendar.get(Calendar.YEAR), 1, 1));
                pointsYear.setEndDate(LocalDate.of(calendar.get(Calendar.YEAR), 1, 1).plusYears(1).minusDays(1));
                yearMapper.insert(pointsYear);
            }
            setting = new SummarySetting();
            setting.setAnnualNum(pointsYear.getYear());
            setting.setWeekAt(0);
        }
        return setting;
    }
}
