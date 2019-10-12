package com.wypuhui.p2p.uploud.data;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.wypuhui.p2p.uploud.data.baihang.dao")
public class WypuhuiP2pUploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(WypuhuiP2pUploadApplication.class, args);
    }

}
