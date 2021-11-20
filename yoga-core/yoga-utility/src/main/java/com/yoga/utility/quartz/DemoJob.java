package com.yoga.utility.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class DemoJob implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.debug("JobName: {}", context.getJobDetail().getKey().getName());
    }
}
