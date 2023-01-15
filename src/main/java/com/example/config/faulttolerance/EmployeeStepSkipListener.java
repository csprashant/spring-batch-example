package com.example.config.faulttolerance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;

import com.example.model.Employee;

public class EmployeeStepSkipListener implements SkipListener<Employee, Number>{
	
	Logger logger = LoggerFactory.getLogger(EmployeeStepSkipListener.class);

	@Override
	public void onSkipInRead(Throwable t) {
		logger.error("Exception while reading record {}",t.getMessage());
	}

	@Override
	public void onSkipInProcess(Employee item, Throwable t) {
		logger.error("Recrod : {} having issue while processing error:{}",item.toString(),t.getMessage());
	}
	
	@Override
	public void onSkipInWrite(Number item, Throwable t) {
		logger.error("Record {} having issue while wirting exception {}",item.toString(),t.getMessage());
	}

	

}
