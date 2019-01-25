package com.fang.bigdata.springcloudbasicpkg.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

/**
 * 获取controller,service,dao参数的切面
 *
 * @param
 * @Author: Yijun.bj
 * @Description:
 * @Date: Created in 15:04 2019/1/18
 */
@Aspect
@Component
public class LogAspect {
    @Autowired
    MetricsRegistry metricsRegistry;

    //controller的切点
    @Pointcut("execution(* com.fang.bigdata.springcloudbasicpkg.controller.*Controller.*(..))")
    public void recordLog() {
    }

    Logger logger = LoggerFactory.getLogger(LogAspect.class);

    //controller的切面
    @Around("recordLog()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        String uuid = UUID.randomUUID().toString().split("-")[0];
        Signature s = pjp.getSignature();
        MethodSignature ms = (MethodSignature) s;
        Method m = ms.getMethod();
        m.getClass().getSuperclass().getName().toString();
        logger.info(pjp.getTarget().getClass().getName());
        Date d1 = new Date();
        logger.info(uuid + "::::" + pjp.getTarget().getClass().getName() + "." + m.getName() + "  开始");
        Object object = pjp.proceed();
        Date d2 = new Date();
        long timeLong = d2.getTime() - d1.getTime();
        if (timeLong < 500)
            logger.info(uuid + "::::" + pjp.getTarget().getClass().getName() + "." + m.getName() + "  结束 耗时：" + (d2.getTime() - d1.getTime()) + "毫秒");
        else
            logger.warn(uuid + "::::" + pjp.getTarget().getClass().getName() + "." + m.getName() + "  结束 耗时：" + (d2.getTime() - d1.getTime()) + "毫秒   耗时查询");
        try {
            metricsRegistry.processCollectResult(pjp.getTarget().getClass().getName(), m.getName(), "", "", "", "", ((int) (timeLong)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    //service的切入点
    @Pointcut("execution(* com.fang.bigdata.springcloudbasicpkg.service.*ServiceImpl.*(..))")
    public void servicePoingcut() {
    }

    //service的切面
    @Around("servicePoingcut()")
    public Object aroundService(ProceedingJoinPoint pjp) throws Throwable {
        Signature s = pjp.getSignature();
        MethodSignature ms = (MethodSignature) s;
        Method m = ms.getMethod();
        m.getClass().getSuperclass().getName().toString();
        Date d1 = new Date();
        Object object = pjp.proceed();
        Date d2 = new Date();
        long timeLong = d2.getTime() - d1.getTime();
        //从堆栈信息中获取联动的controller begin
        String controllerName = "";
        String controllerMethod = "";
        Throwable ex = new Throwable();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace != null) {
            for (int i = 0; i < stackTrace.length; i++) {
                if (stackTrace[i].getClassName().contains("controller") && !stackTrace[i].getClassName().contains("$")) {
                    controllerName = stackTrace[i].getClassName();
                    controllerMethod = stackTrace[i].getMethodName();
                }
            }
        }
        //从堆栈信息中获取联动的controller  end
        try {
            metricsRegistry.processCollectResult(controllerName, controllerMethod, pjp.getTarget().getClass().getName(), m.getName(), "", "", ((int) (timeLong)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    //dao的切入点
    @Pointcut("execution(* com.fang.bigdata.springcloudbasicpkg.dao.*Mapper.*(..))")
    public void daoPointcut() {
    }

    //dao的切面
    @Around("daoPointcut()")
    public Object aroundDao(ProceedingJoinPoint pjp) throws Throwable {
        Signature s = pjp.getSignature();
        MethodSignature ms = (MethodSignature) s;
        Method m = ms.getMethod();
        Date d1 = new Date();
        Object object = pjp.proceed();
        Date d2 = new Date();
        long timeLong = d2.getTime() - d1.getTime();

        //从堆栈信息中获取联动的controller和service
        String controllerName = "";
        String controllerMethod = "";
        String serviceName = "";
        String serviceMethod = "";
        Throwable ex = new Throwable();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace != null) {
            for (int i = 0; i < stackTrace.length; i++) {
                if (stackTrace[i].getClassName().contains("controller") && !stackTrace[i].getClassName().contains("$")) {
                    controllerName = stackTrace[i].getClassName();
                    controllerMethod = stackTrace[i].getMethodName();
                } else if (stackTrace[i].getClassName().contains("service") && !stackTrace[i].getClassName().contains("$")) {
                    serviceName = stackTrace[i].getClassName();
                    serviceMethod = stackTrace[i].getMethodName();

                }

            }
        }
        //从堆栈信息中获取联动的controller和service  end
        try {
            metricsRegistry.processCollectResult(controllerName, controllerMethod, serviceName, serviceMethod, pjp.getTarget().getClass().getName(), m.getName(), ((int) (timeLong)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

}
