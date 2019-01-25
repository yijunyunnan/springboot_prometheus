package com.fang.bigdata.springcloudbasicpkg.config;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableApolloConfig(value = "application",order =10)
public class AppConfig {

}
