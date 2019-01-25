package com.fang.bigdata.springcloudbasicpkg.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configConsumere")
@RefreshScope
public class ConfigClientController {
    @Value("${datasource.mysql.url}")
    private String config;

    @RequestMapping("/getConfigInfo")
    public String getConfigInfo() {
        return config;
    }
}
