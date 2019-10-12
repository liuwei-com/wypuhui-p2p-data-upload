package com.wypuhui.p2p.uploud.data.baihang.dto;

import java.math.BigDecimal;

/**
 * @Author: liuw
 * @Date: 2019/8/20 14:31
 * @Description:
 */
public class LoanAfterInfoDto extends BaseLoanDto {

    //当前还款期数
    private Integer termNo;
    //本期还款状态
    private String termStatus;
    //本期应还款日
    private String targetRepaymentDate;
    //实际还款日期
    private String realRepaymentDate;
    //本期计划应还款金额
    private BigDecimal plannedPayment;
    //本期剩余应还金额
    private BigDecimal targetRepayment;
    //本次应还贷款金额
    private BigDecimal realRepayment;
    //当前逾期天数 如果正常 则为空 否则 D+逾期天数
    private String overdueStatus;
    //本期还款状态确认时间 IOS 8601 本地
    private String statusConfirmAt;
    //当前逾期总额
    private BigDecimal overdueAmount;
    //贷款余额
    private BigDecimal remainingAmount;
    //本笔贷款状态
    private Integer loanStatus;

    public Integer getTermNo() {
        return termNo;
    }

    public void setTermNo(Integer termNo) {
        this.termNo = termNo;
    }

    public String getTermStatus() {
        return termStatus;
    }

    public void setTermStatus(String termStatus) {
        this.termStatus = termStatus;
    }

    public String getTargetRepaymentDate() {
        return targetRepaymentDate;
    }

    public void setTargetRepaymentDate(String targetRepaymentDate) {
        this.targetRepaymentDate = targetRepaymentDate;
    }

    public String getRealRepaymentDate() {
        return realRepaymentDate;
    }

    public void setRealRepaymentDate(String realRepaymentDate) {
        this.realRepaymentDate = realRepaymentDate;
    }

    public BigDecimal getPlannedPayment() {
        return plannedPayment;
    }

    public void setPlannedPayment(BigDecimal plannedPayment) {
        this.plannedPayment = plannedPayment;
    }

    public BigDecimal getTargetRepayment() {
        return targetRepayment;
    }

    public void setTargetRepayment(BigDecimal targetRepayment) {
        this.targetRepayment = targetRepayment;
    }

    public BigDecimal getRealRepayment() {
        return realRepayment;
    }

    public void setRealRepayment(BigDecimal realRepayment) {
        this.realRepayment = realRepayment;
    }

    public String getOverdueStatus() {
        return overdueStatus;
    }

    public void setOverdueStatus(String overdueStatus) {
        this.overdueStatus = overdueStatus;
    }

    public String getStatusConfirmAt() {
        return statusConfirmAt;
    }

    public void setStatusConfirmAt(String statusConfirmAt) {
        this.statusConfirmAt = statusConfirmAt;
    }

    public BigDecimal getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(BigDecimal overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Integer getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(Integer loanStatus) {
        this.loanStatus = loanStatus;
    }
}
