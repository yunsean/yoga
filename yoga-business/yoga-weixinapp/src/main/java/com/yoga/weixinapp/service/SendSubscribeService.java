package com.yoga.weixinapp.service;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@DisallowConcurrentExecution
public class SendSubscribeService implements Job {
    @Autowired
    private WxmpService wxmpService;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            wxmpService.trySendSubscribes();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
