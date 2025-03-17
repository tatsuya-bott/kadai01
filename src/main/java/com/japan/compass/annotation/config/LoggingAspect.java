package com.japan.compass.annotation.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @AfterReturning("execution(void com.japan.compass.annotation.service..*Service.*(..))")
    public void serviceUpdateLog(JoinPoint jp) {
        log.info("{} {}", jp.getSignature(), jp.getArgs());
    }
}
