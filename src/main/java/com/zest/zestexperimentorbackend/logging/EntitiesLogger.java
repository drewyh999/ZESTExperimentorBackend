package com.zest.zestexperimentorbackend.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class EntitiesLogger {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.zest.zestexperimentorbackend.persists.entities.*(..))")
    public void log(JoinPoint point1) { logger.info(point1.getSignature().getName() + " called..."); }

}