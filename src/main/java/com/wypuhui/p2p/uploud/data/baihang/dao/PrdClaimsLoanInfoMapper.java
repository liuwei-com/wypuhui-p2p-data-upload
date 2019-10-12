package com.wypuhui.p2p.uploud.data.baihang.dao;

import com.wypuhui.p2p.uploud.data.annotation.MyDataSource;
import com.wypuhui.p2p.uploud.data.baihang.result.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: liuw
 * @Date: 2019/10/11 19:35
 * @Description:
 */
@Mapper
@MyDataSource(value = "oracle")
public interface PrdClaimsLoanInfoMapper {

    List<LoanAccountInfo> selectLoanAccountInfo(@Param("startTime")String startTime, @Param("endTime")String endTime);

    List<LoanAfterInfo> selectAfterLoanInfo(@Param("startTime")String startTime);

    List<LoanApplyInfo> selectApplyLonInfo(@Param("startTime")String startTime, @Param("endTime")String endTime);

    List<RpPlanCash> queryRpPlanCaseByPrdClaimsId(Integer id);

    LoanApplyInfo selectApplyLoanInfoNow(Integer id);
}
