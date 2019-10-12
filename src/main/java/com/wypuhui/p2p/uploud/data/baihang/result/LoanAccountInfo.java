package com.wypuhui.p2p.uploud.data.baihang.result;

import java.math.BigDecimal;

/**
 * @Author: liuw
 * @Date: 2019/8/20 13:46
 * @Description:
 */
public class LoanAccountInfo extends BaseLoan{

    //原贷款编号
    private String originalLoanId;
    //贷款担保类型
    private Integer guaranteeType;
    //贷款用途类型
    private Integer loanPurpose;
    //贷款申请时间
    private String applyDate;
    //账户开户时间
    private String accountOpenDate;
    //贷款放款时间
    private String issueDate;
    //贷款到期日期
    private String dueDate;
    //借款金额
    private BigDecimal loanAmount;
    //还款总期
    private Integer totalTerm;
    //账单日类型
    private Integer targetRepayDateType;
    //每期还款周期
    private Integer termPeriod;
    //账单日列表
    private String targetRepayDateList;
    //首次应还款日期
    private String firstRepaymentDate;
    //宽限期
    private Integer gracePeriod;
    //设备信息
    private Device device;

    private String tradeMark;

    private String purpose;

    public String getOriginalLoanId() {
        return originalLoanId;
    }

    public void setOriginalLoanId(String originalLoanId) {
        this.originalLoanId = originalLoanId;
    }

    public Integer getGuaranteeType() {
        return guaranteeType;
    }

    public void setGuaranteeType(Integer guaranteeType) {
        this.guaranteeType = guaranteeType;
    }

    public Integer getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(Integer loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getAccountOpenDate() {
        return accountOpenDate;
    }

    public void setAccountOpenDate(String accountOpenDate) {
        this.accountOpenDate = accountOpenDate;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getTotalTerm() {
        return totalTerm;
    }

    public void setTotalTerm(Integer totalTerm) {
        this.totalTerm = totalTerm;
    }

    public Integer getTargetRepayDateType() {
        return targetRepayDateType;
    }

    public void setTargetRepayDateType(Integer targetRepayDateType) {
        this.targetRepayDateType = targetRepayDateType;
    }

    public Integer getTermPeriod() {
        return termPeriod;
    }

    public void setTermPeriod(Integer termPeriod) {
        this.termPeriod = termPeriod;
    }

    public String getTargetRepayDateList() {
        return targetRepayDateList;
    }

    public void setTargetRepayDateList(String targetRepayDateList) {
        this.targetRepayDateList = targetRepayDateList;
    }

    public String getFirstRepaymentDate() {
        return firstRepaymentDate;
    }

    public void setFirstRepaymentDate(String firstRepaymentDate) {
        this.firstRepaymentDate = firstRepaymentDate;
    }

    public Integer getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(Integer gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getTradeMark() {
        return tradeMark;
    }

    public void setTradeMark(String tradeMark) {
        this.tradeMark = tradeMark;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
