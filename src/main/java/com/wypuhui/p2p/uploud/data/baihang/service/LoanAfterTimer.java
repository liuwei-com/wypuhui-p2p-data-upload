package com.wypuhui.p2p.uploud.data.baihang.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wypuhui.p2p.uploud.data.baihang.Interface.BaiHangConsts;
import com.wypuhui.p2p.uploud.data.baihang.common.BHInterfaceUri;
import com.wypuhui.p2p.uploud.data.baihang.dao.LoanInfoMapper;
import com.wypuhui.p2p.uploud.data.baihang.dao.PrdClaimsLoanInfoMapper;
import com.wypuhui.p2p.uploud.data.baihang.dto.LoanAfterInfoDto;
import com.wypuhui.p2p.uploud.data.baihang.result.LoanAfterInfo;
import com.wypuhui.p2p.uploud.data.baihang.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
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
public class LoanAfterTimer{

    @Resource
    LoanInfoMapper loanInfoMapper;

    @Autowired
    BHInterfaceUri bhInterfaceUri;

    @Autowired
    PrdClaimsLoanInfoMapper prdClaimsLoanInfoMapper;

    public void doTask(String startTime, String endTime) {
        log.info("贷后信息上报开始 ======  执行时间 ： {} ，执行时间条件 ：[ startTime : {},endTime : {} ]", new Date(), startTime, endTime);
        sendYesterdayRepayRecode(startTime);
        log.info("贷后信息上报完成 ======  完成时间 ： {}", new Date());
    }

    /**
     * 功能描述： 查询昨日
     * Author: liuw
     * Date: 2019/8/23 16:03
     * param []
     * return java.util.List<java.lang.Integer>
     */

    public void sendYesterdayRepayRecode(String startTime) {
//        String startTime = DateUtils.addTime(DateUtils.getString(new Date(), DateUtils.DEF_DATE_NO_TIME_FORMAT), "D", -4, DateUtils.DEF_DATE_NO_TIME_FORMAT);
        List<LoanAfterInfo> loanAfterInfos = prdClaimsLoanInfoMapper.selectAfterLoanInfo(startTime);
        for (LoanAfterInfo afterInfo : loanAfterInfos) {
            try {
                afterInfo.setOpCode(BaiHangConsts.OpCode.valueOf("A").getValue());
                afterInfo.setSendType("INTERFACE");
                LoanAfterInfoDto dto = new LoanAfterInfoDto();
                BeanUtils.copyProperties(afterInfo, dto);
                if (dto.getRealRepayment().compareTo(new BigDecimal(0)) > 0) {
                    dto.setRealRepaymentDate(DateUtils.getISO8601Timestamp(dto.getRealRepaymentDate(), DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                } else {
                    dto.setRealRepaymentDate(null);
                }
                dto.setRemainingAmount(dto.getRemainingAmount().setScale(2, RoundingMode.HALF_EVEN));
                dto.setOverdueAmount(dto.getOverdueAmount().setScale(2, RoundingMode.HALF_EVEN));
                dto.setRealRepayment(dto.getRealRepayment().setScale(2, RoundingMode.HALF_EVEN));
                dto.setTargetRepayment(dto.getTargetRepayment().setScale(2, RoundingMode.HALF_EVEN));
                dto.setPlannedPayment(dto.getPlannedPayment().setScale(2, RoundingMode.HALF_EVEN));
                dto.setStatusConfirmAt(DateUtils.getISO8601Timestamp(dto.getStatusConfirmAt(), DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                dto.setStatusConfirmAt(DateUtils.getISO8601Timestamp(dto.getStatusConfirmAt(), DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                ToBaiHangType.checkedAfterInfoBeanMustParamNull(dto);
                try {
                    Object o = sendToBaiHang(dto);
                    if (o != null) {
                        afterInfo.setResponseJson(JSON.toJSONString(o, SerializerFeature.WriteMapNullValue));
                    }
                } catch (Exception e) {
                    log.error("发送百行征信数据失败", e);
                    afterInfo.setIsSendSuccess(BaiHangConsts.ERROR);
                    afterInfo.setErrorExplain(e.getMessage());
                }
                
                loanInfoMapper.insertLoanAfterInfo(afterInfo);
            } catch (Exception e) {
                afterInfo.setIsSendSuccess(BaiHangConsts.ERROR);
                afterInfo.setErrorExplain(e.getMessage());
                
                loanInfoMapper.insertLoanAfterInfo(afterInfo);
                log.error("初始化参数异常", e);
            }
        }
    }

    private Object sendToBaiHang(LoanAfterInfoDto dto) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        String admin = bhInterfaceUri.getAdmin() + ":" + bhInterfaceUri.getPassword();
        String authorization = "Basic " + Base64.byteArrayToBase64(admin.getBytes());
        dto.setName(EncryptionUtils.encrypParams(dto.getName()));
        dto.setMobile(EncryptionUtils.encrypParams(dto.getMobile()));
        dto.setPid(EncryptionUtils.encrypParams(dto.getPid()));
        Map<String, Object> doPost = HttpClientUtil.doPost(bhInterfaceUri.getD3Url(), JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue), authorization);
        log.info("贷款账户信息提交至百行 响应数据 response ：[ {} ]", doPost);
        return doPost;
    }
}
