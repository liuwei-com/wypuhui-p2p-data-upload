package com.wypuhui.p2p.uploud.data;

import com.wypuhui.p2p.uploud.data.baihang.service.UploadBaihangService;
import com.wypuhui.p2p.uploud.data.baihang.service.UploadBaihangDValueService;
import com.wypuhui.p2p.uploud.data.baihang.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.wypuhui.p2p.uploud.data.baihang.dao")
public class WypuhuiP2pUploadApplicationTests {

    @Resource
    UploadBaihangService uploadBaihangService;

    @Resource
    UploadBaihangDValueService uploadBaihangDValueService;

    @Test
    public void contextLoads() {
    }

    //存量后上报差数据生成文件单元测试
    @Test
    public void uploadHistoryDataService() {
        uploadBaihangDValueService.doTask("2019-09-12", "2019-10-11");
    }

    //存量上报生成文件单元测试
    @Test
    public void setUploadBaihangService() {
        final CountDownLatch latch = new CountDownLatch(3);
        Runnable c1 = new Runnable() {
            @Override
            public void run() {

                try {
                    Date startTime = new Date();
                    log.info("executor create baihangcredit C1 begin , startTime : {} ", DateUtils.getString(startTime, DateUtils.DEF_DATE_TIME_SECOND_FORMAT));

                    String path = uploadBaihangService.doTaskC1("2016-01-01", "2019-09-11");

                    Date endTime = new Date();
                    log.info("executor create baihangcredit C1 data success file path : {} , endTime : {} , runTime : {}s " ,
                            path,DateUtils.getString(endTime, DateUtils.DEF_DATE_TIME_SECOND_FORMAT), ((endTime.getTime()-startTime.getTime())/1000));
                } catch (Exception e) {
                    log.info("执行失败 e:", e);
                }finally {
                    latch.countDown();
                }



            }
        };
        Runnable d2 = new Runnable() {
            @Override
            public void run() {
                try {
                    Date startTime = new Date();
                    log.info("executor create baihangcredit D2 begin , startTime : {} ",DateUtils.getString(startTime, DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                    String path = uploadBaihangService.doTaskD2("2016-01-01", "2019-09-11");
                    Date endTime = new Date();
                    log.info("executor create baihangcredit D2 data success file path : {} , endTime : {} , runTime : {}s " ,
                            path,DateUtils.getString(endTime, DateUtils.DEF_DATE_TIME_SECOND_FORMAT), ((endTime.getTime()-startTime.getTime())/1000));
                } catch (Exception e) {
                    log.info("执行失败 e:", e);
                }finally {
                    latch.countDown();
                }

            }
        };
        Runnable d3 = new Runnable() {
            @Override
            public void run() {
                try {
                    Date startTime = new Date();
                    log.info("executor create baihangcredit D3 begin , startTime : {} ",DateUtils.getString(startTime, DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                    String path = uploadBaihangService.doTaskD3("2016-01-01", "2019-09-11");
                    Date endTime = new Date();
                    log.info("executor create baihangcredit D3 data success file path : {} , endTime : {} , runTime : {}s " ,
                            path,DateUtils.getString(endTime, DateUtils.DEF_DATE_TIME_SECOND_FORMAT), ((endTime.getTime()-startTime.getTime())/1000));
                } catch (Exception e) {
                    log.info("执行失败 e:", e);
                }finally {
                    latch.countDown();
                }

            }
        };

        Thread thread = new Thread(c1);
        Thread thread2 = new Thread(d2);
        Thread thread3 = new Thread(d3);
        thread.start();
        thread2.start();
        thread3.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("await thread exception e : {} ", e);
        }
        log.info("main done");

    }
}
