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
public class ServicesLogger {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Before("execution(* com.zest.zestexperimentorbackend.services.*(..))")
    public void log(JoinPoint point) {
        logger.info(point.getSignature().getName() + " called...");
    }


}