package com.wypuhui.p2p.uploud.data.baihang.dto;

/**
 * @Author: liuw
 * @Date: 2019/8/20 14:38
 * @Description:
 */
public class BaseLoanDto {
    //记录本条数据的唯一标识，数字加字母构成 （0,40）
    private String reqID;
    //A新增，M修改， D删除 暂时不支持
    private String opCode;
    //记录生成的当前时间 ISO 8601格式
    private String uploadTs;
    //姓名
    private String name;
    //身份证
    private String pid;
    //手机号
    private String mobile;
    //贷款编号
    private String loanId;

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String getOpCode() {
        return opCode;
    }

    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }

    public String getUploadTs() {
        return uploadTs;
    }

    public void setUploadTs(String uploadTs) {
        this.uploadTs = uploadTs;
    }

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

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }
}
