package com.wypuhui.p2p.uploud.data.baihang.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wypuhui.p2p.uploud.data.baihang.BhCreditApiClient;
import com.wypuhui.p2p.uploud.data.baihang.Interface.BaiHangConsts;
import com.wypuhui.p2p.uploud.data.baihang.dao.LoanInfoMapper;
import com.wypuhui.p2p.uploud.data.baihang.dao.PrdClaimsLoanInfoMapper;
import com.wypuhui.p2p.uploud.data.baihang.dto.Device;
import com.wypuhui.p2p.uploud.data.baihang.dto.LoanAccountInfoDto;
import com.wypuhui.p2p.uploud.data.baihang.dto.LoanAfterInfoDto;
import com.wypuhui.p2p.uploud.data.baihang.dto.LoanApplyInfoDto;
import com.wypuhui.p2p.uploud.data.baihang.encrypt.RSAUtil;
import com.wypuhui.p2p.uploud.data.baihang.request.FileUploadRequest;
import com.wypuhui.p2p.uploud.data.baihang.response.FileUploadResponse;
import com.wypuhui.p2p.uploud.data.baihang.result.LoanAccountInfo;
import com.wypuhui.p2p.uploud.data.baihang.result.LoanAfterInfo;
import com.wypuhui.p2p.uploud.data.baihang.result.LoanApplyInfo;
import com.wypuhui.p2p.uploud.data.baihang.result.RpPlanCash;
import com.wypuhui.p2p.uploud.data.baihang.util.DateUtils;
import com.wypuhui.p2p.uploud.data.baihang.util.EncryptionUtils;
import com.wypuhui.p2p.uploud.data.baihang.util.ToBaiHangType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: liuw
 * @Date: 2019/9/12 15:10
 * @Description:    存量数据上报文件
 */
@Component("uploadBaihangService")
public class UploadBaihangService {

    private static Logger logger = LoggerFactory.getLogger(UploadBaihangService.class);

    private static final String LINEFEED = "\r\n";

    private static final String COMPANY_NAME = "北京威阳普惠信息科技有限公司";

    private static final String AES_KEY = "WYPUHUI_AES_KEY";

    private static String PATH = System.getProperty("java.io.tmpdir") + File.separator;

    @Autowired
    private LoanAfterTimer loanAfterTimer;

    @Autowired
    private LoanAccountTimer loanAccountTimer;

    @Autowired
    PrdClaimsLoanInfoMapper prdClaimsLoanInfoMapper;

    @Autowired
    LoanInfoMapper loanInfoMapper;

    public String doTaskC1(String startTime,String endTime) {
        String fileName = PATH + COMPANY_NAME + "_C1_" + endTime.replace("-", "") + "_" + "0001";
        try {
            File file = createTxt(startTime,endTime,fileName,createApplyLoanInfoData(startTime, endTime));
            BhCreditApiClient creditApiClient = new BhCreditApiClient();
            FileUploadRequest request = new FileUploadRequest();
            creditApiClient.init(RSAUtil.readRSAPublicKey(EncryptionUtils.pubClassPathResource.getInputStream()),AES_KEY);
            request.setDataFile(file.getPath());
            request.setTargetFilePath(PATH);
            FileUploadResponse execute = creditApiClient.execute(request);
            if (execute.isSuccess) {
                String encryptFilePath = execute.getEncryptFilePath();
                String encryptFileName = execute.getEncryptFileName();
                logger.info("生成加密文件成功，路径 ：{} ,文件名：{} ", encryptFilePath, encryptFileName);
                return encryptFilePath + encryptFileName;
            }
            return "任务失败";
        } catch (Exception e) {
            logger.error("执行报送文件失败！",e);
            return "";
        }

    }


    public String doTaskD2(String startTime,String endTime) {
        String fileName = PATH + COMPANY_NAME + "_D2_" + endTime.replace("-", "") + "_" +"0001";
        try {
            File file = createTxt(startTime,endTime,fileName,createLoanAccountInfoData(startTime, endTime));
            BhCreditApiClient creditApiClient = new BhCreditApiClient();
            FileUploadRequest request = new FileUploadRequest();
            creditApiClient.init(RSAUtil.readRSAPublicKey(EncryptionUtils.pubClassPathResource.getInputStream()),AES_KEY);
            request.setDataFile(file.getPath());
            request.setTargetFilePath(PATH);
            FileUploadResponse execute = creditApiClient.execute(request);
            if (execute.isSuccess) {
                String encryptFilePath = execute.getEncryptFilePath();
                String encryptFileName = execute.getEncryptFileName();
                logger.info("生成加密文件成功，路径 ：{} ,文件名：{} ", encryptFilePath, encryptFileName);
                return encryptFilePath + encryptFileName;
            }
            return "任务失败";
        } catch (Exception e) {
            logger.error("执行报送文件失败！",e);
            return "";
        }

    }


    public String doTaskD3(String startTime,String endTime) {
        String fileName = PATH + COMPANY_NAME + "_D3_" + endTime.replace("-", "") + "_0001";
        try {
            File file = createTxt(startTime,endTime,fileName,createLoanAfterInfoData(startTime, endTime));
            BhCreditApiClient creditApiClient = new BhCreditApiClient();
            FileUploadRequest request = new FileUploadRequest();
            creditApiClient.init(RSAUtil.readRSAPublicKey(EncryptionUtils.pubClassPathResource.getInputStream()),AES_KEY);
            request.setDataFile(file.getPath());
            request.setTargetFilePath(PATH);
            FileUploadResponse execute = creditApiClient.execute(request);
            if (execute.isSuccess) {
                String encryptFilePath = execute.getEncryptFilePath();
                String encryptFileName = execute.getEncryptFileName();
                logger.info("生成加密文件成功，路径 ：{} ,文件名：{} ", encryptFilePath, encryptFileName);
                return encryptFilePath + encryptFileName;
            }
            return "任务失败";
        } catch (Exception e) {
            logger.error("执行报送文件失败！",e);
            return "";
        }

    }


    private File createTxt(String startTime,String endTime,String fileName,String context) throws IOException {
        File file = new File(fileName+".txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        PrintWriter printWriter = new PrintWriter(fileOutputStream);
        printWriter.println(context);
        printWriter.flush();
        printWriter.close();
        fileOutputStream.close();
        return file;
    }


    /**
     * 功能描述： 构建贷款账户数据块
     * Author: liuw
     * Date: 2019/8/26 19:45
     * param [startTime, endTime]
     * return java.lang.String
     */

    private String createLoanAccountInfoData(String startTime,String endTime) {
        
        List<LoanAccountInfo> loanAccountInfos = prdClaimsLoanInfoMapper.selectLoanAccountInfo(startTime, endTime);
        StringBuffer stringBuffer = new StringBuffer("#singleLoanAccountInfo"+LINEFEED);
        for (LoanAccountInfo accountInfo : loanAccountInfos) {
            try {
                accountInfo.setSendType("FILE");
                accountInfo.setOpCode(BaiHangConsts.OpCode.valueOf("A").getValue());
                LoanAccountInfoDto dto = new LoanAccountInfoDto();
                BeanUtils.copyProperties(accountInfo, dto);
                List<RpPlanCash> rpPlanCashes = prdClaimsLoanInfoMapper.queryRpPlanCaseByPrdClaimsId(accountInfo.getPrdId());
                StringBuffer sb = new StringBuffer();
                for (RpPlanCash rpPlanCash : rpPlanCashes) {
                    if (rpPlanCash.getIsEarlyPrepayment() != null && rpPlanCash.getIsEarlyPrepayment() == 1) {
                        continue;
                    }
                    sb.append(DateUtils.getString(rpPlanCash.getRepayPerDate(),DateUtils.DEF_DATE_NO_TIME_FORMAT)).append(",");
                }
                dto.setLoanAmount(dto.getLoanAmount().setScale(2, RoundingMode.HALF_EVEN));
                dto.setGuaranteeType(BaiHangConsts.LoanGuaranteeType.valueOf(ToBaiHangType.conversionToBHLoanGuaranteeType(accountInfo.getTradeMark())).val());
                dto.setLoanPurpose(BaiHangConsts.LoanPurpose.valueOf(ToBaiHangType.conversionToBHLoanPurposeType(accountInfo.getPurpose())).val());
                dto.setTargetRepayDateList(sb.toString().substring(0, sb.toString().length() - 1));
                dto.setApplyDate(DateUtils.getISO8601Timestamp(accountInfo.getApplyDate(), DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                dto.setAccountOpenDate(DateUtils.getISO8601Timestamp(accountInfo.getAccountOpenDate(), DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                dto.setIssueDate(DateUtils.getISO8601Timestamp(accountInfo.getIssueDate(), DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                dto.setTargetRepayDateType(BaiHangConsts.TargerRepayDateType.valueOf("FIXED_DATE").val());
                dto.setUploadTs(DateUtils.getISO8601Timestamp(accountInfo.getUploadTs(), DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                dto.setTermPeriod(-1);
                dto.setGracePeriod(0);
                Device device = new Device();
                device.setDeviceType(BaiHangConsts.DeviceType.valueOf("COMPUTER").val());
                dto.setDevice(device);
                ToBaiHangType.checkedAccountInfoBeanMustParamNull(dto);
//                dto.setName(StringReplace.replaceSubString(dto.getName(),"帅",1,0, 0));
//                dto.setPid(StringReplace.replaceSubString(dto.getPid(),"0",15,0, 0));
//                dto.setMobile(StringReplace.replaceSubString(dto.getMobile(),"0",3,4, 0));
                String json = JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue);
                stringBuffer.append(json).append(LINEFEED);
            } catch (Exception e) {
                logger.error("验证实体bean属性不合格",e);
                accountInfo.setIsSendSuccess(BaiHangConsts.ERROR);
                accountInfo.setErrorExplain(e.getMessage());
            }
        }
        try {
            
            loanInfoMapper.batchInsertAccountInfo(loanAccountInfos);
        } catch (Exception e) {
            logger.error("批量插入贷款账户数据失败", e);
        }
        return stringBuffer.toString();
    }

    /**
     * 功能描述： 构建贷后数据块
     * Author: liuw
     * Date: 2019/8/26 19:45
     * param [startTime, endTime]
     * return java.lang.String
     */

    private String createLoanAfterInfoData(String startTime,String endTime) {
        StringBuffer stringBuffer = new StringBuffer("#singleLoanRepayInfo"+LINEFEED);
        int discrepancyNum = DateUtils.getDiscrepancyNum(new Timestamp(DateUtils.parseDate(startTime, DateUtils.DEF_DATE_NO_TIME_FORMAT).getTime()),
                new Timestamp(DateUtils.parseDate(endTime, DateUtils.DEF_DATE_NO_TIME_FORMAT).getTime()));
        logger.info("贷后数据历史任务开始日期：{} 结束时间：{} 间隔天数：{}",startTime,endTime,discrepancyNum);
        discrepancyNum++;
        for (int i = 0; i < discrepancyNum; i++) {
            String afterStartTime = DateUtils.addTime(startTime, "D", i, DateUtils.DEF_DATE_NO_TIME_FORMAT);
            if (i == discrepancyNum-1) {
                logger.info("贷后数据历史任务最后日期：{}",afterStartTime);
            }
            
            List<LoanAfterInfo> loanAfterInfos = prdClaimsLoanInfoMapper.selectAfterLoanInfo(afterStartTime);
            for (LoanAfterInfo afterInfo : loanAfterInfos) {
                try {
                    afterInfo.setOpCode(BaiHangConsts.OpCode.valueOf("A").getValue());
                    afterInfo.setSendType("FILE");
                    LoanAfterInfoDto dto = new LoanAfterInfoDto();
                    BeanUtils.copyProperties(afterInfo, dto);
                    dto.setRemainingAmount(dto.getRemainingAmount().setScale(2, RoundingMode.HALF_EVEN));
                    dto.setOverdueAmount(dto.getOverdueAmount().setScale(2, RoundingMode.HALF_EVEN));
                    dto.setRealRepayment(dto.getRealRepayment().setScale(2, RoundingMode.HALF_EVEN));
                    dto.setTargetRepayment(dto.getTargetRepayment().setScale(2, RoundingMode.HALF_EVEN));
                    dto.setPlannedPayment(dto.getPlannedPayment().setScale(2, RoundingMode.HALF_EVEN));
                    if (dto.getRealRepayment().compareTo(new BigDecimal(0)) > 0) {
                        dto.setRealRepaymentDate(DateUtils.getISO8601Timestamp(dto.getRealRepaymentDate(), DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                    } else {
                        dto.setRealRepaymentDate(null);
                    }
                    dto.setStatusConfirmAt(DateUtils.getISO8601Timestamp(dto.getStatusConfirmAt(), DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                    dto.setUploadTs(DateUtils.getISO8601Timestamp(dto.getUploadTs(), DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                    ToBaiHangType.checkedAfterInfoBeanMustParamNull(dto);
//                    dto.setName(StringReplace.replaceSubString(dto.getName(),"帅",1,0, 0));
//                    dto.setPid(StringReplace.replaceSubString(dto.getPid(),"0",15,0, 0));
//                    dto.setMobile(StringReplace.replaceSubString(dto.getMobile(),"0",3,4, 0));
                    String json = JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue);
                    stringBuffer.append(json).append(LINEFEED);
                } catch (Exception e) {
                    logger.error("验证实体bean属性不合格",e);
                    afterInfo.setIsSendSuccess(BaiHangConsts.ERROR);
                    afterInfo.setErrorExplain(e.getMessage());
                }
            }
            if (loanAfterInfos != null && loanAfterInfos.size() > 0) {
                try {
                    
                    loanInfoMapper.batchInsertAfterInfo(loanAfterInfos);
                } catch (Exception e) {
                    logger.error("批量插入贷后数据失败", e);
                }
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 功能描述： 构建贷款申请数据块
     * Author: liuw
     * Date: 2019/8/26 20:46
     * param [startTime, endTime]
     * return java.lang.String
     */

    private String createApplyLoanInfoData(String startTime,String endTime) {
        StringBuffer stringBuffer = new StringBuffer("#loanApplyInfo"+LINEFEED);
        List<LoanApplyInfo> loanApplyInfos = prdClaimsLoanInfoMapper.selectApplyLonInfo(startTime, endTime);
        for (LoanApplyInfo loanApplyInfo : loanApplyInfos) {
            try {
                loanApplyInfo.setQueryReason(BaiHangConsts.QueryReason.valueOf("CREDIT_APPROVAL").val());
                loanApplyInfo.setUploadTs(DateUtils.getISO8601Timestamp(loanApplyInfo.getUploadTs(),DateUtils.DEF_DATE_TIME_SECOND_FORMAT));
                loanApplyInfo.setSendType("FILE");
                loanApplyInfo.setCustomType(BaiHangConsts.CustomType.valueOf("OTHER_PEOPLE").val());
                String bhLoanGuaranteeType = ToBaiHangType.conversionToBHLoanGuaranteeType(loanApplyInfo.getTradeMark());
                loanApplyInfo.setGuaranteeType(BaiHangConsts.LoanGuaranteeType.valueOf(bhLoanGuaranteeType).val());
                //转换百行贷款用途
                String bhLoanPurposeType = ToBaiHangType.conversionToBHLoanPurposeType(loanApplyInfo.getPurpose());
                loanApplyInfo.setLoanPurpose(BaiHangConsts.LoanPurpose.valueOf(bhLoanPurposeType).val());
                LoanApplyInfoDto dto = new LoanApplyInfoDto();
                Device device = new Device();
                device.setDeviceType(BaiHangConsts.DeviceType.valueOf("COMPUTER").val());
                dto.setDevice(device);
                BeanUtils.copyProperties(loanApplyInfo, dto);
                ToBaiHangType.checkedApplyInfoBeanMustParamNull(dto);
                dto.setApplyAmount(dto.getApplyAmount().setScale(2, RoundingMode.HALF_EVEN));
                String json = JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue);
                stringBuffer.append(json).append(LINEFEED);
            } catch (Exception e) {
                logger.error("验证实体bean属性不合格",e);
                loanApplyInfo.setIsSendSuccess(BaiHangConsts.ERROR);
                loanApplyInfo.setErrorExplain(e.getMessage());
            }

        }
        try {
            loanInfoMapper.batchInsertApplyInfo(loanApplyInfos);
        } catch (Exception e) {
            logger.error("批量插入贷后数据失败", e);
        }
        return stringBuffer.toString();

    }
}
