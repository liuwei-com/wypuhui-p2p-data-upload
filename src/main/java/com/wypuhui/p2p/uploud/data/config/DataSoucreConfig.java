package com.wypuhui.p2p.uploud.data.config;

import com.wypuhui.p2p.uploud.data.core.DynamicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liuw
 * @Date: 2019/10/11 16:43
 * @Description:
 */
@Configuration
public class DataSoucreConfig {

    @Bean(name = "mysql")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "oracle")
    @ConfigurationProperties(prefix = "spring.datasource.oracle")
    public DataSource oracleDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 功能描述： 动态数据源:通过Aop在不同的数据源中动态切换
     */
    @Primary
    @Bean
    public DataSource dataSource() {
        DynamicDataSource source = new DynamicDataSource();
        //默认数据源
        source.setDefaultTargetDataSource(this.mysqlDataSource());
        //配置多数据源
        Map<Object, Object> dsMap = new HashMap<>();
        dsMap.put("mysql", this.mysqlDataSource());
        dsMap.put("oracle", this.oracleDataSource());
        source.setTargetDataSources(dsMap);
        return source;
    }

    /*
     *  配置@Transaction注解事务
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(this.dataSource());
    }
}
