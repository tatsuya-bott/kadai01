package com.japan.compass.annotation;

import com.japan.compass.annotation.service.batch.ApplicationInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableRetry
@SpringBootApplication
public class AnnotationSystemApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(AnnotationSystemApplication.class, args);
		ApplicationInitializer applicationInitializer = ctx.getBean(ApplicationInitializer.class);
		applicationInitializer.execStartup(args);
	}
}
