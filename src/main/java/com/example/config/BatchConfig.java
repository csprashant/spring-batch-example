package com.example.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.example.config.faulttolerance.EmployeeStepSkipListener;
import com.example.config.faulttolerance.ExceptionSkipPolicy;
import com.example.listener.JobMonitoringListener;
import com.example.model.Employee;
import com.example.processor.EmployeeInfoItemProcessor;
import com.example.repository.EmployeeRepo;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired private EmployeeRepo employeeRepo;
	@Autowired private JobBuilderFactory jobFactory;
	@Autowired private StepBuilderFactory stepFactory;

	@Bean
	public JobExecutionListener createListener() {
		return new JobMonitoringListener();
	}

	@Bean
	public ItemProcessor<Employee, Employee> createProcessor() {
		return new EmployeeInfoItemProcessor();
	}

	@Bean
	public FlatFileItemReader<Employee> createReader() {
		FlatFileItemReader<Employee> itemReader = new FlatFileItemReader<>();
		itemReader.setResource(new FileSystemResource("src/main/resources/emp.csv"));
		itemReader.setName("emp-csv-reader");
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper());
		return itemReader;
	}
	
	private LineMapper<Employee> lineMapper() {
		DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("empno", "ename", "salary", "email");
		BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Employee.class);
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}

	@Bean
	public RepositoryItemWriter<Employee> createWriter() {
		RepositoryItemWriter<Employee> writer = new RepositoryItemWriter<>();
		writer.setRepository(employeeRepo);
		writer.setMethodName("save");
		return writer;
	}

	@Bean(name = "step1")
	public Step createStep1() {
		return stepFactory.get("step1")
				.<Employee, Employee>chunk(10)
				.reader(createReader())
				.writer(createWriter())
				.processor(createProcessor())
				.faultTolerant()
				.skipPolicy(skipPolicy()) 
				.listener(skipEmployeeListener())
				.build();
	}

	@Bean(name = "job1")
	public Job createJob1() {
		return jobFactory.get("job1")
				.incrementer(new RunIdIncrementer())
				.listener(createListener())
				.start(createStep1())
				.build();
	}
	
	@Bean
	public  SkipListener<?, ?> skipEmployeeListener() {
		return new EmployeeStepSkipListener();
	}
	
	@Bean
	public SkipPolicy skipPolicy(){
		return new ExceptionSkipPolicy();
	}

}
