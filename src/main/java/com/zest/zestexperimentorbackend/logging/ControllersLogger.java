package com.zest.zestexperimentorbackend.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class ControllersLogger {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static long beforetime;

    @Pointcut("execution(* com.zest.zestexperimentorbackend.controllers.*(..))")
    public void webPoint(){

    }

    @Before("webPoint()")
    public void log(JoinPoint point1) { logger.info(point1.getSignature().getName() + " called..."); }


    @Before("webPoint()")
    public void doBefore(JoinPoint joinPoint){
        //在controller层以外获取当前正在处理的请求，固定格式
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        String ip = request.getRemoteAddr();
        String method = request.getMethod();
        String URL = request.getRequestURI().toString();

        String params = null;

        if ("POST".equals(method) || "PUT".equals(method)) {
            //joinPoint 就是targer
            //获取target方法中的所有参数
            //如果target = sendCode 那么args = params
            //如果target = register 那么args = userParam
            Object[] args = joinPoint.getArgs();
            if (args.length > 0){
                params = args[0].toString();
            }
        }
        beforetime = System.currentTimeMillis();
        //打印日志
        logger.info("\n\n\n{}\n\n\n{}\n\n\n{}\n\n\n{}\n\n\n",ip,method,URL,params);

    }

    @After("webLog()")
    public void afterTime(){
        long betweenTime = System.currentTimeMillis() - beforetime;

        logger.info("耗时：{}",betweenTime);

    }

}