package com.wypuhui.p2p.uploud.data.baihang.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wypuhui.p2p.uploud.data.baihang.Interface.BaiHangConsts;
import com.wypuhui.p2p.uploud.data.baihang.common.BHInterfaceUri;
import com.wypuhui.p2p.uploud.data.baihang.dao.LoanInfoMapper;
import com.wypuhui.p2p.uploud.data.baihang.dao.PrdClaimsLoanInfoMapper;
import com.wypuhui.p2p.uploud.data.baihang.dto.Device;
import com.wypuhui.p2p.uploud.data.baihang.dto.LoanAccountInfoDto;
import com.wypuhui.p2p.uploud.data.baihang.result.LoanAccountInfo;
import com.wypuhui.p2p.uploud.data.baihang.result.RpPlanCash;
import com.wypuhui.p2p.uploud.data.baihang.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: liuw
 * @Date: 2019/8/21 20:36
 * @Description:
 */
@Component
@Slf4j
public class LoanAccountTimer {

    @Resource
    LoanInfoMapper loanInfoMapper;
    
    @Autowired
    BHInterfaceUri bhInterfaceUri;

    @Autowired
    PrdClaimsLoanInfoMapper prdClaimsLoanInfoMapper;

    public void doTask(String startTime, String endTime) {
        log.info("贷款账户信息上报开始 ======  执行时间 ： {} ，执行时间条件 ：[ startTime : {},endTime : {} ]", new Date(), startTime, endTime);
//        String startTime = DateUtils.addTime(DateUtils.getString(new Date(), DateUtils.DEF_DATE_NO_TIME_FORMAT), "D", -1, DateUtils.DEF_DATE_NO_TIME_FORMAT);
        List<LoanAccountInfo> loanAccountInfoList = prdClaimsLoanInfoMapper.selectLoanAccountInfo(startTime, endTime);
        for (LoanAccountInfo accountInfo : loanAccountInfoList) {
            try {
                accountInfo.setOpCode(BaiHangConsts.OpCode.valueOf("A").getValue());
                accountInfo.setSendType("INTERFACE");
                LoanAccountInfoDto dto = new LoanAccountInfoDto();
                BeanUtils.copyProperties(accountInfo, dto);
                
                List<RpPlanCash> rpPlanCashes = prdClaimsLoanInfoMapper.queryRpPlanCaseByPrdClaimsId(accountInfo.getPrdId());
                StringBuffer stringBuffer = new StringBuffer();
                for (RpPlanCash rpPlanCash : rpPlanCashes) {
                    if (rpPlanCash.getIsEarlyPrepayment() != null && rpPlanCash.getIsEarlyPrepayment() == 1) {
                        continue;
                    }
                    stringBuffer.append(DateUtils.getString(rpPlanCash.getRepayPerDate(), DateUtils.DEF_DATE_NO_TIME_FORMAT)).append(",");
                }
                dto.setLoanAmount(dto.getLoanAmount().setScale(2, RoundingMode.HALF_EVEN));
                dto.setGuaranteeType(BaiHangConsts.LoanGuaranteeType.valueOf(ToBaiHangType.conversionToBHLoanGuaranteeType(accountInfo.getTradeMark())).val());
                dto.setLoanPurpose(BaiHangConsts.LoanPurpose.valueOf(ToBaiHangType.conversionToBHLoanPurposeType(accountInfo.getPurpose())).val());
                dto.setTargetRepayDateList(stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1));
                dto.setApplyDate(DateUtils.getISO8601Timestamp(accountInfo.getApplyDate(), DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                dto.setAccountOpenDate(DateUtils.getISO8601Timestamp(accountInfo.getAccountOpenDate(), DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                dto.setIssueDate(DateUtils.getISO8601Timestamp(accountInfo.getIssueDate(), DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                dto.setUploadTs(DateUtils.getISO8601Timestamp(accountInfo.getUploadTs(), DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                dto.setTargetRepayDateType(BaiHangConsts.TargerRepayDateType.valueOf("FIXED_DATE").val());
                dto.setTermPeriod(-1);
                dto.setGracePeriod(0);
                Device device = new Device();
                device.setDeviceType(BaiHangConsts.DeviceType.valueOf("COMPUTER").val());
                dto.setDevice(device);
                ToBaiHangType.checkedAccountInfoBeanMustParamNull(dto);
                try {
                    Object o = sendToBaiHang(dto);
                    if (o != null) {
                        accountInfo.setResponseJson(JSON.toJSONString(o, SerializerFeature.WriteMapNullValue));
                    }
                } catch (Exception e) {
                    log.error("发送百行征信数据失败", e);
                    accountInfo.setIsSendSuccess(BaiHangConsts.ERROR);
                    accountInfo.setErrorExplain(e.getMessage());
                }
                
                loanInfoMapper.insertLoanAccountInfo(accountInfo);
            } catch (Exception e) {
                accountInfo.setIsSendSuccess(BaiHangConsts.ERROR);
                accountInfo.setErrorExplain(e.getMessage());
                
                loanInfoMapper.insertLoanAccountInfo(accountInfo);
                log.error("初始化贷款账户数据异常", e);
            }

        }
        log.info("贷款账户信息上报完成 ======  结束时间 ： {}", new Date());
    }

    private Object sendToBaiHang(LoanAccountInfoDto dto) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        String admin = bhInterfaceUri.getAdmin() + ":" + bhInterfaceUri.getPassword();
        String authorization = "Basic " + Base64.byteArrayToBase64(admin.getBytes());
        dto.setName(EncryptionUtils.encrypParams(dto.getName()));
        dto.setMobile(EncryptionUtils.encrypParams(dto.getMobile()));
        dto.setPid(EncryptionUtils.encrypParams(dto.getPid()));
        Map<String, Object> doPost = HttpClientUtil.doPost(bhInterfaceUri.getD2Url(), JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue), authorization);
        log.info("贷款账户信息提交至百行 响应数据 response ：[ {} ]", doPost);
        return doPost;
    }
}
