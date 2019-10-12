package com.wypuhui.p2p.uploud.data.baihang.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wypuhui.p2p.uploud.data.baihang.Interface.BaiHangConsts;
import com.wypuhui.p2p.uploud.data.baihang.common.BHInterfaceUri;
import com.wypuhui.p2p.uploud.data.baihang.dao.LoanInfoMapper;
import com.wypuhui.p2p.uploud.data.baihang.dao.PrdClaimsLoanInfoMapper;
import com.wypuhui.p2p.uploud.data.baihang.dto.LoanApplyInfoDto;
import com.wypuhui.p2p.uploud.data.baihang.result.Device;
import com.wypuhui.p2p.uploud.data.baihang.result.LoanApplyInfo;
import com.wypuhui.p2p.uploud.data.baihang.service.P2pDataUploadToBaiHangService;
import com.wypuhui.p2p.uploud.data.baihang.util.Base64;
import com.wypuhui.p2p.uploud.data.baihang.util.EncryptionUtils;
import com.wypuhui.p2p.uploud.data.baihang.util.HttpClientUtil;
import com.wypuhui.p2p.uploud.data.baihang.util.ToBaiHangType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liuw
 * @Date: 2019/8/20 16:45
 * @Description:
 */
@Service("p2pDataUploadToBaiHangService")
public class P2pDataUploadToBaiHangServiceImpl implements P2pDataUploadToBaiHangService {

    private static Logger logger = LoggerFactory.getLogger(P2pDataUploadToBaiHangServiceImpl.class);

    @Autowired
    BHInterfaceUri bhInterfaceUri;

    @Autowired
    PrdClaimsLoanInfoMapper prdClaimsLoanInfoMapper;

    @Autowired
    LoanInfoMapper loanInfoMapper;


//    @Autowired

    @Override
    public void upload(String msg) {
        Map map = JSON.parseObject(msg, HashMap.class);
          if (map.get("prdClaimsId") == null) {
             return;
        }
        Integer prdClamisId = Integer.valueOf(map.get("prdClaimsId").toString());
        LoanApplyInfo loanApplyInfo = new LoanApplyInfo();
        try {
            loanApplyInfo = initLoanApplyInfo(prdClamisId);
            loanApplyInfo.setSendType("INTERFACE");
            loanApplyInfo.setQueryReason(BaiHangConsts.QueryReason.valueOf("CREDIT_APPROVAL").val());
            Device device = new Device();
            device.setDeviceType(BaiHangConsts.DeviceType.valueOf("COMPUTER").val());
            loanApplyInfo.setDevice(device);
            try {
                LoanApplyInfoDto loanApplyInfoDto = new LoanApplyInfoDto();
                BeanUtils.copyProperties(loanApplyInfo, loanApplyInfoDto);
                ToBaiHangType.checkedApplyInfoBeanMustParamNull(loanApplyInfoDto);
                Object o = sendApplyLoanToBaiHang(loanApplyInfoDto);
                if (o != null) {
                    loanApplyInfo.setResponseJson(JSON.toJSONString(o, SerializerFeature.WriteMapNullValue));
                }
            } catch (Exception e) {
                loanApplyInfo.setIsSendSuccess(BaiHangConsts.ERROR);
                loanApplyInfo.setErrorExplain(e.getMessage());
                logger.error("send applyLoanInfo to baihangCredit error ", e);
            }
            
            loanInfoMapper.insertLoanApplyInfo(loanApplyInfo);
        } catch (Exception e) {
            loanApplyInfo.setIsSendSuccess(BaiHangConsts.ERROR);
            loanApplyInfo.setErrorExplain(e.getMessage());
            
            loanInfoMapper.insertLoanApplyInfo(loanApplyInfo);
            logger.error("初始化贷款信息参数失败", e);
        }
    }


    private Object sendApplyLoanToBaiHang(LoanApplyInfoDto loanApplyInfoDto) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        String admin = bhInterfaceUri.getAdmin() + ":" + bhInterfaceUri.getPassword();
        String authorization = "Basic " + Base64.byteArrayToBase64(admin.getBytes());
        loanApplyInfoDto.setName(EncryptionUtils.encrypParams(loanApplyInfoDto.getName()));
        loanApplyInfoDto.setMobile(EncryptionUtils.encrypParams(loanApplyInfoDto.getMobile()));
        loanApplyInfoDto.setPid(EncryptionUtils.encrypParams(loanApplyInfoDto.getPid()));
        Map<String, Object> doPost = HttpClientUtil.doPost(bhInterfaceUri.getC1Url(),JSON.toJSONString(loanApplyInfoDto, SerializerFeature.WriteMapNullValue), authorization);
        logger.info("贷款申请信息提交至百行 响应数据 response ：[ {} ]", doPost);
        return doPost;
    }

    private LoanApplyInfo initLoanApplyInfo(Integer prdClamisId) {
        LoanApplyInfo info = prdClaimsLoanInfoMapper.selectApplyLoanInfoNow(prdClamisId);
        if (StringUtils.isBlank(info.getName()) || StringUtils.isBlank(info.getPid()) || StringUtils.isBlank(info.getMobile()) || info.getApplyAmount() == null) {
            throw new RuntimeException("apply loan LoanApplyInfo[name, idno, mobile] exist null");
        }
        info.setCustomType(BaiHangConsts.CustomType.valueOf("OTHER_PEOPLE").val());
        String bhLoanGuaranteeType = ToBaiHangType.conversionToBHLoanGuaranteeType(info.getTradeMark());
        info.setGuaranteeType(BaiHangConsts.LoanGuaranteeType.valueOf(bhLoanGuaranteeType).val());
        //转换百行贷款用途
        String bhLoanPurposeType = ToBaiHangType.conversionToBHLoanPurposeType(info.getPurpose());
        info.setLoanPurpose(BaiHangConsts.LoanPurpose.valueOf(bhLoanPurposeType).val());
        info.setApplyAmount(info.getApplyAmount().setScale(2, RoundingMode.HALF_EVEN));
        return info;
    }

}
