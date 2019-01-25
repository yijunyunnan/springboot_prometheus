package com.fang.bigdata.springcloudbasicpkg.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/3/9.
 */
@Aspect
@Component
public class DataSourceAspect {
    @Autowired
    HttpServletRequest request;

    @Pointcut("execution(* com.fang.bigdata.springcloudbasicpkg.dao.*Mapper.*(..))")
    public void pointCutAt_Mysql() {
    }

    @Before("pointCutAt_Mysql()")
    public void setDataSource_contractWrite(JoinPoint jointPoint) {
        DatabaseContextHolder.setDatabaseType(DatabaseType.MYSQL);
        System.out.println("CW:" + DatabaseContextHolder.getDatabaseType());
    }


    @Pointcut("execution(* com.fang.bigdata.springcloudbasicpkg.daokylin.*Mapper.*(..))")
    public void pointCutAt_Kylin() {
    }

    @Before("pointCutAt_Kylin()")
    public void setDataSource_contractRead(JoinPoint joinPoint) {
        DatabaseContextHolder.setDatabaseType(DatabaseType.KYLIN);
        System.out.println("CR:" + DatabaseContextHolder.getDatabaseType());
    }

}