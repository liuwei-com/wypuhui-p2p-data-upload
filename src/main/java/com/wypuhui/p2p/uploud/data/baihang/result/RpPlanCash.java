package com.wypuhui.p2p.uploud.data.baihang.result;

import lombok.Data;

import java.util.Date;

/**
 * @Author: liuw
 * @Date: 2019/10/11 20:13
 * @Description:
 */
@Data
public class RpPlanCash {
    // 分期还款日期
    private Date repayPerDate;
    // 是否提前还款
    private Integer isEarlyPrepayment;
}
