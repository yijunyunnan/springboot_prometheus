package com.fang.bigdata.springcloudbasicpkg.config;

/**
 * Created by Administrator on 2017/3/9.
 */
public class DatabaseContextHolder {
    public static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setDatabaseType(String type) {
        contextHolder.set(type);
    }

    public static String getDatabaseType() {
        System.out.println("HOLDER:" + (String) contextHolder.get());
        return (String) contextHolder.get();
    }

    static {
        contextHolder.set(DatabaseType.MYSQL);
    }
}