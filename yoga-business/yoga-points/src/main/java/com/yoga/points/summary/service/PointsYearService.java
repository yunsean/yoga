package com.yoga.points.summary.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.logging.annotation.LoggingPrimary;
import com.yoga.logging.service.LoggingPrimaryHandler;
import com.yoga.points.summary.mapper.PointsYearMapper;
import com.yoga.points.summary.model.PointsYear;
import com.yoga.points.summary.model.SummarySetting;
import com.yoga.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@LoggingPrimary(module = PointsYearService.ModuleName, name = "积分年度")
public class PointsYearService extends BaseService implements LoggingPrimaryHandler {

    @Autowired
    private PointsYearMapper yearMapper;
    @Autowired
    private SettingService settingService;

    public final static String ModuleName = "points_year";
    public final static String PointsYears = "score.years";
    public String getPrimaryInfo(Object primaryId) {
        PointsYear year = yearMapper.selectByPrimaryKey(primaryId);
        if (year == null) return null;
        return year.getYear() + "年";
    }


    public long add(long tenantId, int year, LocalDate beginDate, LocalDate endDate) {
        PointsYear pointsYear = new PointsYear(tenantId, year, beginDate, endDate);
        yearMapper.insert(pointsYear);
        return pointsYear.getId();
    }
    public List<Integer> allYears(long tenantId) {
        return new MapperQuery<>(PointsYear.class)
                .andEqualTo("tenantId", tenantId)
                .query(yearMapper)
                .stream()
                .map(PointsYear::getYear)
                .collect(Collectors.toList());
    }
    public PointsYear getYear(long tenantId, int year) {
        return new MapperQuery<>(PointsYear.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("year", year)
                .queryFirst(yearMapper);
    }
    public PointsYear getYear(long tenantId, long id) {
        PointsYear year = yearMapper.selectByPrimaryKey(id);
        if (year == null || year.getTenantId() != tenantId) throw new BusinessException("积分年度不存在！");
        return year;
    }
    public PageInfo<PointsYear> list(long tenantId, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<PointsYear> years = new MapperQuery<>(PointsYear.class)
                .andEqualTo("tenantId", tenantId)
                .query(yearMapper);
        return new PageInfo<>(years);
    }
    public void update(long tenantId, long id, Integer year, LocalDate beginDate, LocalDate endDate) {
        PointsYear pointsYear = yearMapper.selectByPrimaryKey(id);
        if (pointsYear == null || pointsYear.getTenantId() != tenantId) throw new BusinessException("年度不存在");
        if (year != null) pointsYear.setYear(year);
        if (beginDate != null) pointsYear.setBeginDate(beginDate);
        if (endDate != null) pointsYear.setEndDate(endDate);
        yearMapper.updateByPrimaryKey(pointsYear);
    }
    public void updated(long tenantId, int year) {
        PointsYear pointsYear = new MapperQuery<>(PointsYear.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("year", year)
                .queryFirst(yearMapper);
        if (pointsYear == null || pointsYear.getTenantId() != tenantId) throw new BusinessException("年度不存在");
        PointsYear updated = new PointsYear(pointsYear.getId(), LocalDateTime.now());
        yearMapper.updateByPrimaryKeySelective(updated);
    }

}
