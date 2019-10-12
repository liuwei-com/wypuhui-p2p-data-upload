package com.wypuhui.p2p.uploud.data.config;

import com.wypuhui.p2p.uploud.data.core.DynamicDataSourceAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: liuw
 * @Date: 2019/10/12 10:52
 * @Description:
 */
@Configuration
public class DataSourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceAspect dynamicDataSourceAspect() {
        return new DynamicDataSourceAspect();
    }

}
