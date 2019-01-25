package com.fang.bigdata.springcloudbasicpkg.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.fang.bigdata.springcloudbasicpkg.bean.DbPropertiesMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2017/3/9.
 */
@Configuration
@MapperScan(basePackages = {"com.fang.bigdata.springcloudbasicpkg.dao", "com.fang.bigdata.springcloudbasicpkg.daokylin"})
public class MyBatisConfig implements EnvironmentAware {
    @Autowired
    DbPropertiesMap dbPropertiesMap;
    @Bean
    @Primary
    public DynamicDataSource dataSource() throws Exception {
        DataSource dataSource_Mysql = dataSource_Mysql();
        DataSource dataSource_Kylin = dataSource_Kylin();
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DatabaseType.MYSQL, dataSource_Mysql);
        targetDataSource.put(DatabaseType.KYLIN, dataSource_Kylin);

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSource);
        dataSource.setDefaultTargetDataSource(dataSource_Mysql);
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource ds) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(ds);
        fb.setPlugins(new Interceptor[]{new MybatisLogInterceptor()});
        return fb.getObject();
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

    @RefreshScope
    DataSource dataSource_Mysql() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", env.getProperty("datasource.mysql.driverclass"));
        props.put("url", env.getProperty("datasource.mysql.url"));
        props.put("username", env.getProperty("datasource.mysql.username"));
        props.put("password", dbPropertiesMap.getM().get("datasource.mysql.password"));
//        props.put("password", env.getProperty("datasource.mysql.password"));
        props.put("filters", env.getProperty("datasource.mysql.filters"));
        props.put("maxActive", env.getProperty("datasource.mysql.maxActive"));
        props.put("initialSize", env.getProperty("datasource.mysql.initialSize"));
        props.put("maxWait", env.getProperty("datasource.mysql.maxWait"));
        props.put("minIdle", env.getProperty("datasource.mysql.minIdle"));
        props.put("timeBetweenEvictionRunsMillis", env.getProperty("datasource.mysql.timeBetweenEvictionRunsMillis"));
        props.put("minEvictableIdleTimeMillis", env.getProperty("datasource.mysql.minEvictableIdleTimeMillis"));
        props.put("validationQuery", env.getProperty("datasource.mysql.validationQuery"));
        props.put("testWhileIdle", env.getProperty("datasource.mysql.testWhileIdle"));
        props.put("testOnBorrow", env.getProperty("datasource.mysql.testOnBorrow"));
        props.put("testOnReturn", env.getProperty("datasource.mysql.testOnReturn"));
        props.put("poolPreparedStatements", env.getProperty("datasource.mysql.poolPreparedStatements"));
        props.put("maxOpenPreparedStatements", env.getProperty("datasource.mysql.maxOpenPreparedStatements"));
        return DruidDataSourceFactory.createDataSource(props);
    }

    @RefreshScope
    DataSource dataSource_Kylin() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", env.getProperty("datasource.kylin.driverclass"));
//        props.put("url", env.getProperty("datasource.kylin.url"));
        props.put("username", env.getProperty("datasource.kylin.username"));
//        props.put("password", env.getProperty("datasource.kylin.password"));
        props.put("url", env.getProperty("datasource.kylin.url")+env.getProperty("datasource.kylin_newsfb.urltail"));
        props.put("password", dbPropertiesMap.getM().get("datasource.kylin.password"));
        props.put("filters", env.getProperty("datasource.kylin.filters"));
        props.put("maxActive", env.getProperty("datasource.kylin.maxActive"));
        props.put("initialSize", env.getProperty("datasource.kylin.initialSize"));
        props.put("maxWait", env.getProperty("datasource.kylin.maxWait"));
        props.put("minIdle", env.getProperty("datasource.kylin.minIdle"));

        props.put("timeBetweenEvictionRunsMillis", env.getProperty("datasource.kylin.timeBetweenEvictionRunsMillis"));
        props.put("minEvictableIdleTimeMillis", env.getProperty("datasource.kylin.minEvictableIdleTimeMillis"));
        props.put("validationQuery", env.getProperty("datasource.kylin.validationQuery"));
        props.put("testWhileIdle", env.getProperty("datasource.kylin.testWhileIdle"));
        props.put("testOnBorrow", env.getProperty("datasource.kylin.testOnBorrow"));
        props.put("testOnReturn", env.getProperty("datasource.kylin.testOnReturn"));
//        props.put("poolPreparedStatements", env.getProperty("datasource.kylin.poolPreparedStatements"));
//        props.put("maxOpenPreparedStatements", env.getProperty("datasource.kylin.maxOpenPreparedStatements"));

        return DruidDataSourceFactory.createDataSource(props);
    }


    Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}