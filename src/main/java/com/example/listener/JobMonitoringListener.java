package com.example.listener;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobMonitoringListener implements JobExecutionListener {
	
	Logger logger =  LoggerFactory.getLogger(JobMonitoringListener.class);
	private long start;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		start = System.currentTimeMillis();
		logger.info("Job is about to Start @{}",new Date());

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		long end = System.currentTimeMillis();
		logger.info("Job completed at::{}",new Date());
		logger.info("Job Exection time::{} ms",(end - start));
		logger.info("Job completion status ::{}",jobExecution.getStatus());
	}
}
