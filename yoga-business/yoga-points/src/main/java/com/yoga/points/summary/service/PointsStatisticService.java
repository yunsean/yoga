package com.yoga.points.summary.service;

import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.operator.user.model.User;
import com.yoga.operator.user.service.UserService;
import com.yoga.points.summary.mapper.PointsSummaryMapper;
import com.yoga.points.summary.model.PointsSummary;
import com.yoga.points.summary.model.PointsYear;
import com.yoga.points.summary.model.SummaryItem;
import com.yoga.tenant.tenant.service.TenantService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PointsStatisticService extends BaseService {

    @Autowired
    private TenantService tenantService;
    @Autowired
    private PointsYearService yearService;
    @Autowired
    private PointsSummaryService summaryService;
    @Autowired
    private PointsSummaryMapper summaryMapper;
    @Autowired
    private UserService userService;

    public interface Entry {
        List<SummaryItem> summary(long tenantId, LocalDate beginDate, LocalDate endDate);
    }
    public void registerStatisticEntry(Entry entry, String moduleName) {
        statisticEntries.put(entry, moduleName);
    }
    public void unregisterStatisticEntry(Entry entry) {
        statisticEntries.remove(entry);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    protected void doSync(long tenantId, int year, LocalDate beginDate, LocalDate endDate) {
        Map<Long, SummaryItemEx> allSummaryItems = new HashMap<>();
        Set<String> modules = new HashSet<String>(Arrays.asList(tenantService.getModules(tenantId)));
        int[] index = new int[1];
        statisticEntries
                .entrySet()
                .stream()
                .filter(entryStringEntry -> modules.contains(entryStringEntry.getValue()))
                .forEach(entryStringEntry -> {
                    index[0]++;
                    updateProgress(tenantId, index[0] + "/" + (statisticEntries.size() + 1));
                    List<SummaryItem> summaryItems = entryStringEntry.getKey().summary(tenantId, beginDate, endDate);
                    if (summaryItems != null) {
                        summaryItems
                                .forEach(summaryItem -> {
                                    SummaryItemEx item = allSummaryItems.get(summaryItem.getUserId());
                                    if (item == null) allSummaryItems.put(summaryItem.getUserId(), new SummaryItemEx(summaryItem));
                                    else item.append(summaryItem);
                                });
                    }
                });
        index[0]++;
        updateProgress(tenantId, index[0] + "/" + (statisticEntries.size() + 1));
        saveSummaries(tenantId, year, allSummaryItems);
    }

    @Transactional(propagation = Propagation.NEVER)
    protected void saveSummaries(long tenantId, int year, Map<Long, SummaryItemEx> allSummaryItems) {
        new MapperQuery<>(PointsSummary.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("year", year)
                .delete(summaryMapper);
        if (allSummaryItems == null) return;
        List<User> allUser = userService.list(tenantId, null, null, null, null, null, null);
        List<PointsSummary> pointsSummaries = new ArrayList<>();
        Map<Long, List<PointsSummary>> dutyMap = new HashMap<>();
        Map<Long, List<PointsSummary>> branchMap = new HashMap<>();
        allUser.forEach(user -> {
            SummaryItemEx item = allSummaryItems.get(user.getId());
            PointsSummary summary;
            if (item != null) summary = new PointsSummary(tenantId, item.getId(), year, item.getScore() + item.getPenalty(), item.getPenalty(), item.getDetail());
            else summary = new PointsSummary(tenantId, user.getId(), year, 0, 0, "暂无数据");
            pointsSummaries.add(summary);
            if (user.getDutyId() == null) user.setDutyId(0L);
            List<PointsSummary> dutyData = dutyMap.computeIfAbsent(user.getDutyId(), k -> new ArrayList<>());
            dutyData.add(summary);
            if (user.getBranchId() == null) user.setBranchId(0L);
            List<PointsSummary> branchData = branchMap.computeIfAbsent(user.getBranchId(), k -> new ArrayList<>());
            branchData.add(summary);
        });
        if (!pointsSummaries.isEmpty()) {
            int rank = 1;
            int lastPoints = 0;
            int count = 0;
            pointsSummaries.sort((ls, rs)-> rs.getPoints() - ls.getPoints());
            for (PointsSummary it : pointsSummaries) {
                count++;
                if (lastPoints != it.getPoints()) {
                    rank = count;
                    lastPoints = it.getPoints();
                }
                it.setYearRank(rank);
            }
        }
        dutyMap.values().forEach(summaries-> {
            int rank = 1;
            int lastPoints = 0;
            int count = 0;
            summaries.sort((ls, rs)-> rs.getPoints() - ls.getPoints());
            for (PointsSummary it : summaries) {
                count++;
                if (lastPoints != it.getPoints()) {
                    rank = count;
                    lastPoints = it.getPoints();
                }
                it.setDutyRank(rank);
            }
        });
        branchMap.values().forEach(summaries-> {
            int rank = 1;
            int lastPoints = 0;
            int count = 0;
            summaries.sort((ls, rs)-> rs.getPoints() - ls.getPoints());
            for (PointsSummary it : summaries) {
                count++;
                if (lastPoints != it.getPoints()) {
                    rank = count;
                    lastPoints = it.getPoints();
                }
                it.setBranchRank(rank);
            }
        });
        if (!pointsSummaries.isEmpty()) summaryMapper.insertList(pointsSummaries);
        yearService.updated(tenantId, year);
        summaryService.setLowestWarning(tenantId, year);
    }

    public void statistic(long tenantId, int year) {
        if (!acquireSyncLock(tenantId)) throw new BusinessException("其他用户正在进行积分统计");
        PointsYear pointsYear = yearService.getYear(tenantId, year);
        if (pointsYear == null) throw new BusinessException("未配置该年份");
        new Thread(() -> {
            logger.info("[" + tenantId + "]开始积分统计");
            try {
                doSync(tenantId, pointsYear.getYear(), pointsYear.getBeginDate(), pointsYear.getEndDate());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            releaseSyncLock(tenantId);
            cleanProgress(tenantId);
            logger.info("[" + tenantId + "]结束积分统计");
        }).start();
    }

    public String getProgress(long tenantId) {
        final String key = ProgressKey + tenantId;
        return redisOperator.get(key);
    }

    private static final long LockExpire = 60 * 60 * 1000;           //millis
    private static final String LockKey = "points.statistic.lock.";
    @PostConstruct
    public void cleanSync() {
        redisOperator.removePattern(LockKey + "*");
    }
    private boolean acquireSyncLock(long tenantId) {
        final String key = LockKey + tenantId;
        final long expires = System.currentTimeMillis() + LockExpire + 1;
        final String expiresStr = String.valueOf(expires);
        if (redisOperator.setNX(key, expiresStr)) return true;
        final String currentValueStr = redisOperator.get(key);
        if (currentValueStr != null && java.lang.Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
            final String oldValueStr = redisOperator.getSet(key, expiresStr);
            if (oldValueStr != null && oldValueStr == currentValueStr) return true;
        }
        return false;
    }
    private void releaseSyncLock(long tenantId) {
        final String key = LockKey + tenantId;
        redisOperator.remove(key);
    }

    private static final String ProgressKey = "points.statistic.lock.statistic.progress.";
    private void updateProgress(long tenantId, String percent) {
        final String key = ProgressKey + tenantId;
        redisOperator.set(key, percent);
    }
    private void cleanProgress(long tenantId) {
        final String key = ProgressKey + tenantId;
        redisOperator.remove(key);
    }

    private Map<Entry, String> statisticEntries = new HashMap<>();
    @Getter
    private class SummaryItemEx {
        private Long id;
        private int score = 0;
        private int penalty = 0;
        private String detail = null;

        public SummaryItemEx(SummaryItem item) {
            this.id = item.getUserId();
            append(item);
        }
        public void append(SummaryItem item) {
            if (item.getPoints() < 0) this.penalty += item.getPoints();
            else this.score += item.getPoints();
            if (StringUtil.isNotBlank(this.detail)) this.detail += "\r\n" + item.getDetail();
            else this.detail = item.getDetail();
        }
    }
}
