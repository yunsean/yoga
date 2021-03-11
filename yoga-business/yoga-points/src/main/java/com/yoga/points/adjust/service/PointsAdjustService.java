package com.yoga.points.adjust.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.logging.annotation.LoggingPrimary;
import com.yoga.logging.service.LoggingPrimaryHandler;
import com.yoga.points.adjust.mapper.PointsAdjustMapper;
import com.yoga.points.adjust.model.PointsAdjust;
import com.yoga.points.summary.model.SummaryItem;
import com.yoga.points.summary.service.PointsStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@LoggingPrimary(module = PointsAdjustService.ModuleName, name = "积分调整")
public class PointsAdjustService extends BaseService implements LoggingPrimaryHandler {

    @Autowired
    private PointsAdjustMapper adjustMapper;
    @Autowired
    private PointsStatisticService statisticService;

    public final static String ModuleName = "points_adjust";
    @Override
    public String getPrimaryInfo(Object primaryId) {
        PointsAdjust adjust = adjustMapper.selectByPrimaryKey(primaryId);
        if (adjust == null) return null;
        return adjust.getId().toString();
    }

    public long add(long tenantId, long userId, LocalDateTime date, int score, String reason, long submitterId) {
        PointsAdjust adjust = new PointsAdjust(tenantId, userId, date, score, reason, submitterId);
        adjustMapper.insert(adjust);
        return adjust.getId();
    }
    public PageInfo<PointsAdjust> list(long tenantId, Long userId, Long branchId, Long dutyId, String keyword, LocalDate beginDate, LocalDate endDate, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<PointsAdjust> adjusts = adjustMapper.list(tenantId, userId, branchId, dutyId, keyword, beginDate, endDate);
        return new PageInfo<>(adjusts);
    }
    @Transactional
    public void repeal(long tenantId, long id, long userId) {
        PointsAdjust adjust = adjustMapper.selectByPrimaryKey(id);
        if (null == adjust || adjust.getTenantId() != tenantId) throw new BusinessException("不存在该ID");
        if (adjust.getSubmitterId() != userId) throw new BusinessException("只能撤销自己添加的积分调整");
        adjustMapper.deleteByPrimaryKey(id);
    }


    @PostConstruct
    public void registerEntry() {
        statisticService.registerStatisticEntry((tid, begin, end) -> summaryScore(tid, begin, end), ModuleName);
        statisticService.registerStatisticEntry((tid, begin, end) -> missingScore(tid, begin, end), ModuleName);
    }
    private List<SummaryItem> missingScore(long tenantId, LocalDate beginDate, LocalDate endDate) {
        return adjustMapper.penaltyPoints(tenantId, beginDate, endDate)
                .stream()
                .map(it-> new SummaryItem(it.getUserId(), it.getPoints(), "扣分" + it.getCount() + "次，扣除" + (it.getPoints() / 100F) + "积分"))
                .collect(Collectors.toList());
    }
    private List<SummaryItem> summaryScore(long tenantId, LocalDate beginDate, LocalDate endDate) {
        return adjustMapper.rewardPoints(tenantId, beginDate, endDate)
                .stream()
                .map(it-> new SummaryItem(it.getUserId(), it.getPoints(), "加分" + it.getCount() + "次，得到" + (it.getPoints() / 100F) + "积分"))
                .collect(Collectors.toList());
    }
}
