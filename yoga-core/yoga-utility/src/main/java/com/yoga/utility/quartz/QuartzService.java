package com.yoga.utility.quartz;

import com.yoga.core.exception.BusinessException;
import com.yoga.core.utils.DateUtil;
import org.hibernate.service.spi.ServiceException;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuartzService {

    @Autowired
    private Scheduler scheduler;

    public List<QuartzTask> list() {
        final List<QuartzTask> list = new ArrayList<>();
        try {
            for (final String groupJob : scheduler.getJobGroupNames()) {
                for (final JobKey jobKey : scheduler.getJobKeys(GroupMatcher.groupEquals(groupJob))) {
                    final List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    for (final Trigger trigger : triggers) {
                        QuartzTask info = updateJob(trigger, jobKey);
                        list.add(info);
                    }
                }
            }
        } catch (SchedulerException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public QuartzTask get(final String jobName, final String jobGroup) {
        try {
            final TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            if (!scheduler.checkExists(triggerKey)) throw new BusinessException("定时任务不存在！");
            final Trigger trigger = scheduler.getTrigger(triggerKey);
            final JobKey jobKey = trigger.getJobKey();
            return updateJob(trigger, jobKey);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    private QuartzTask updateJob(Trigger trigger, JobKey jobKey) throws SchedulerException {
        final Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
        final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        String cronExpression = "";
        String createTime = "";
        if (trigger instanceof CronTrigger) {
            final CronTrigger cronTrigger = (CronTrigger) trigger;
            cronExpression = cronTrigger.getCronExpression();
            createTime = cronTrigger.getDescription();
        }
        final QuartzTask info = new QuartzTask();
        info.setName(jobKey.getName());
        info.setGroup(jobKey.getGroup());
        info.setDescription(jobDetail.getDescription());
        info.setStatus(triggerState.name());
        info.setExpression(cronExpression);
        info.setCreateTime(createTime);
        info.setDataMap(jobDetail.getJobDataMap());
        return info;
    }

    public void add(final QuartzTask info) {
        final String jobName = info.jobClass();
        final String jobGroup = info.getGroup();
        final String cronExpression = info.getExpression();
        final String jobDescription = info.getDescription();
        final String createTime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            if (!scheduler.checkExists(triggerKey)) {
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
                        .withMisfireHandlingInstructionDoNothing();
                CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                        .withDescription(createTime).withSchedule(cronScheduleBuilder).build();
                Class<? extends Job> clazz = Class.forName(jobName).asSubclass(Job.class);
                JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
                JobDataMap dataMap = info.getDataMap() == null ? new JobDataMap() : info.getDataMap();
                JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey)
                        .withDescription(jobDescription)
                        .usingJobData(dataMap)
                        .build();
                scheduler.scheduleJob(jobDetail, cronTrigger);
            } /*else {
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
                        .withMisfireHandlingInstructionDoNothing();
                CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                        .withDescription(createTime).withSchedule(cronScheduleBuilder).build();
                JobKey jobKey = new JobKey(jobName, jobGroup);
                JobBuilder jobBuilder = scheduler.getJobDetail(jobKey).getJobBuilder();
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                JobDataMap dataMap = info.getDataMap() == null ? jobDetail.getJobDataMap() : info.getDataMap();
                jobDetail = jobBuilder.usingJobData(dataMap).withDescription(jobDescription).build();
                Set<Trigger> triggerSet = new HashSet<>();
                triggerSet.add(cronTrigger);
                scheduler.scheduleJob(jobDetail, triggerSet, true);
            } //*/
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("类名不存在或执行表达式错误");
        }
    }

    public void edit(final QuartzTask info) {
        final String jobName = info.jobClass();
        final String jobGroup = info.getGroup();
        final String cronExpression = info.getExpression();
        final String jobDescription = info.getDescription();
        final String createTime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            if (!scheduler.checkExists(triggerKey)) throw new ServiceException(String.format("Job不存在, jobName:{%s},jobGroup:{%s}", jobName, jobGroup));
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                    .withDescription(createTime).withSchedule(cronScheduleBuilder).build();
            JobKey jobKey = new JobKey(jobName, jobGroup);
            JobBuilder jobBuilder = scheduler.getJobDetail(jobKey).getJobBuilder();
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (info.getDataMap() == null) info.setDataMap(jobDetail.getJobDataMap());
            jobDetail = jobBuilder.usingJobData(info.getDataMap()).withDescription(jobDescription).build();
            Set<Trigger> triggerSet = new HashSet<>();
            triggerSet.add(cronTrigger);
            scheduler.scheduleJob(jobDetail, triggerSet, true);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new ServiceException("类名不存在或执行表达式错误");
        }
    }

    public void delete(final String jobName, final String jobGroup) {
        try {
            final TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            if (scheduler.checkExists(triggerKey)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }
    public void delete(final Class<?> clazz, final String jobGroup) {
        try {
            final TriggerKey triggerKey = TriggerKey.triggerKey(clazz.getName(), jobGroup);
            if (scheduler.checkExists(triggerKey)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    public void pause(final String jobName, final String jobGroup) {
        try {
            final TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            if (scheduler.checkExists(triggerKey)) {
                scheduler.pauseTrigger(triggerKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }

    public void resume(final String jobName, final String jobGroup) {
        try {
            final TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            if (scheduler.checkExists(triggerKey)) {
                scheduler.resumeTrigger(triggerKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
    }
}
