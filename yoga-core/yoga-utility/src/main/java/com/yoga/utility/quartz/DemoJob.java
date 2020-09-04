package com.yoga.utility.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DemoJob implements Job {

    private Logger logger = LoggerFactory.getLogger(DemoJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("JobName: {}", context.getJobDetail().getKey().getName());
    }
}
