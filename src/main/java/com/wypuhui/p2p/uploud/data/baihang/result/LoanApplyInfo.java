package com.wypuhui.p2p.uploud.data.baihang.result;


import java.math.BigDecimal;

/**
 * @Author: liuw
 * @Date: 2019/8/19 17:26
 * @Description: 百行--贷款申请信息
 */
public class LoanApplyInfo extends BaseLoan{
    //借款人姓名
    private String name;
    //身份证号
    private String pid;
    //手机号
    private String mobile;
    //查询原因
    private Integer queryReason;
    //贷款担保类型
    private Integer guaranteeType;
    //贷款用途
    private Integer loanPurpose;
    //客户类型 1在校学生 2 在职人员 3自雇人员 4 其他人士 99 人群未知
    private Integer customType;
    //贷款金额
    private BigDecimal applyAmount;
    //贷款/授信账号编号
    private String loanId;
    //家庭地址
    private String homeAddress;
    //家庭电话
    private String homePhone;
    //工作单位
    private String workName;
    //工作地址
    private String workAddress;
    //工作单位电话
    private String workPhone;
    //描述 json格式
    private Device device;
    //
    private String tradeMark;

    private String purpose;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getQueryReason() {
        return queryReason;
    }

    public void setQueryReason(Integer queryReason) {
        this.queryReason = queryReason;
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

    public Integer getCustomType() {
        return customType;
    }

    public void setCustomType(Integer customType) {
        this.customType = customType;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
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
