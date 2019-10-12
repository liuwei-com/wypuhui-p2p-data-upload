package com.wypuhui.p2p.uploud.data.baihang.timer;

import com.wypuhui.p2p.uploud.data.baihang.service.LoanAccountTimer;
import com.wypuhui.p2p.uploud.data.baihang.service.LoanAfterTimer;
import com.wypuhui.p2p.uploud.data.baihang.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: liuw
 * @Date: 2019/8/26 13:54
 * @Description:
 */
//@Component
//@EnableScheduling
//@Lazy(false)
public class SendBaiHangCreditTimer {

    private Logger logger = LoggerFactory.getLogger(SendBaiHangCreditTimer.class);

//    @Autowired
    private LoanAfterTimer loanAfterTimer;

//    @Autowired
    private LoanAccountTimer loanAccountTimer;

//    @Scheduled(cron = "0 0 2 * * ?")
    private void doTask() {
        String startTime = DateUtils.addTime(DateUtils.getString(new Date(), DateUtils.DEF_DATE_NO_TIME_FORMAT), "D", -1, DateUtils.DEF_DATE_NO_TIME_FORMAT);

        try {
            logger.info("开始执行百行征信贷款账户数据推送");
            loanAccountTimer.doTask(startTime, startTime);
            logger.info("执行百行征信贷款账户数据推送完成");
        } catch (Exception e) {
            logger.error("百行征信推送贷款账户数据失败！", e);
        }
        String afterStartTime = DateUtils.addTime(DateUtils.getString(new Date(), DateUtils.DEF_DATE_NO_TIME_FORMAT), "D", -1, DateUtils.DEF_DATE_NO_TIME_FORMAT);
        try {
            logger.info("开始执行百行征信贷后数据推送");
            loanAfterTimer.doTask(afterStartTime, afterStartTime);
            logger.info("执行百行征信贷后数据推送完成");
        } catch (Exception e) {
            logger.error("百行征信推送贷后数据失败！", e);
        }

    }
}
