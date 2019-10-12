package com.wypuhui.p2p.uploud.data.baihang.dao;

import com.wypuhui.p2p.uploud.data.annotation.MyDataSource;
import com.wypuhui.p2p.uploud.data.baihang.result.LoanAccountInfo;
import com.wypuhui.p2p.uploud.data.baihang.result.LoanAfterInfo;
import com.wypuhui.p2p.uploud.data.baihang.result.LoanApplyInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@MyDataSource
public interface LoanInfoMapper {
    //贷款申请
    int insertLoanApplyInfo(LoanApplyInfo applyInfo);
    //贷后
    int insertLoanAfterInfo(LoanAfterInfo afterInfo);
    //贷款账户
    int insertLoanAccountInfo(LoanAccountInfo accountInfo);

    void batchInsertAfterInfo(List<LoanAfterInfo> list);

    void batchInsertApplyInfo(List<LoanApplyInfo> list);

    void batchInsertAccountInfo(List<LoanAccountInfo> list);

}