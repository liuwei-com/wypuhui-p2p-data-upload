package com.wypuhui.p2p.uploud.data.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liuw
 * @Date: 2019/10/11 17:05
 * @Description:
 */
public class DataSourceContextHolder {

    public static final String DEFAULT_DATASOURCE = "mysql";    //默认数据源

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSource(String dbType) {
        contextHolder.set(dbType);
    }

    public static String getDataSource() {
        return contextHolder.get();
    }

    public static void clean() {
        contextHolder.remove();
    }

}
