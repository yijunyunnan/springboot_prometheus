package com.fang.bigdata.springcloudbasicpkg.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * prometheus 收集并暴露自定义的指标
 * @param
 * @Author: Yijun.bj
 * @Description:
 * @Date: Created in 11:33 2019/1/18
 */
@Service
public class MetricsRegistry {
    @Autowired
    MeterRegistry meterRegistry;

    public void processCollectResult(String controllerName, String controllerMethod,String serviceName, String serviceMethod, String daoName, String daoMethod, Integer second) throws Exception {
        //收集并暴露指标
        Timer timer = meterRegistry.timer("bigdata.timer", "controllermethod", controllerMethod, "controllername", controllerName, "servicename",serviceName,"servicemethod",serviceMethod,"daoname",daoName,"daomethod",daoMethod);
        timer.record(second, TimeUnit.MILLISECONDS);
    }
}
