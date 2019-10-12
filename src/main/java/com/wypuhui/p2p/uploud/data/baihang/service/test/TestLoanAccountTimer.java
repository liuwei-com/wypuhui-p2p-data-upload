package com.wypuhui.p2p.uploud.data.baihang.service.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wypuhui.p2p.uploud.data.baihang.common.BHInterfaceUri;
import com.wypuhui.p2p.uploud.data.baihang.dto.LoanAccountInfoDto;
import com.wypuhui.p2p.uploud.data.baihang.util.Base64;
import com.wypuhui.p2p.uploud.data.baihang.util.EncryptionUtils;
import com.wypuhui.p2p.uploud.data.baihang.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

/**
 * @Author: liuw
 * @Date: 2019/9/5 17:09
 * @Description:
 */
@Component
public class TestLoanAccountTimer  {

    private Logger logger = LoggerFactory.getLogger(TestLoanAccountTimer.class);

    @Autowired
    BHInterfaceUri bhInterfaceUri;

    public void doTask() {
        logger.info("测试贷款账户信息上传接口开始");
        try {
//            File file = new File("E:/ideaWorkSpace/p2pData/p2pData-web/src/main/resources/d2.txt");
            ClassPathResource resource = new ClassPathResource("d2.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String string;
            while ((string = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(string)) {
                    String[] split = string.split("\t");
                    LoanAccountInfoDto dto = new LoanAccountInfoDto();
                    dto.setReqID(split[0].trim());
                    dto.setOpCode(split[1].trim());
                    dto.setUploadTs(split[2].trim());
                    dto.setName(split[3].trim());
                    dto.setPid(split[4].trim());
                    dto.setMobile(split[5].trim());
                    dto.setLoanId(split[6].trim());
                    dto.setOriginalLoanId(split[7].trim());
                    dto.setGuaranteeType(Integer.valueOf(split[8].trim()));
                    dto.setLoanPurpose(Integer.valueOf(split[9].trim()));
                    dto.setApplyDate(split[10].trim());
                    dto.setAccountOpenDate(split[11].trim());
                    dto.setIssueDate(split[12].trim());
                    dto.setDueDate(split[13].trim());
                    dto.setLoanAmount(new BigDecimal(split[14].trim()));
                    dto.setTotalTerm(Integer.valueOf(split[15].trim()));
                    dto.setTargetRepayDateType(Integer.valueOf(split[16].trim()));
                    dto.setTermPeriod(Integer.valueOf(split[17].trim()));
                    dto.setTargetRepayDateList(split[18].trim());
                    dto.setFirstRepaymentDate(split[19].trim());
                    dto.setGracePeriod(Integer.valueOf(split[20].trim()));

                    logger.info("贷款账户模拟数据dto：【 {} 】", JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue));
                    try {
                        Object o = sendToBaiHang(dto);
                        if (o != null) {
                            logger.info("接口测试百行响应JSON ：【 {} 】",JSON.toJSONString(o, SerializerFeature.WriteMapNullValue));
                        }
                    } catch (Exception e) {
                        logger.error("发送百行征信数据失败 reqId:{} , Exception {} ", dto.getReqID(),e.getMessage());
                    }
                }
            }
            logger.info("测试贷款账户接口完成");
        } catch (Exception e) {
            logger.error("测试百行贷款账户接口失败", e);
        }
    }


    private Object sendToBaiHang(LoanAccountInfoDto dto) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        String admin = bhInterfaceUri.getAdmin() + ":" + bhInterfaceUri.getPassword();
        String authorization = "Basic " + Base64.byteArrayToBase64(admin.getBytes());
        dto.setName(EncryptionUtils.encrypParams(dto.getName()));
        dto.setMobile(EncryptionUtils.encrypParams(dto.getMobile()));
        dto.setPid(EncryptionUtils.encrypParams(dto.getPid()));
        Map<String, Object> doPost = HttpClientUtil.doPost(bhInterfaceUri.getD2Url(), JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue), authorization);
        logger.info("贷款账户信息提交至百行 响应数据 response ：[ {} ]", doPost);
        return doPost;
    }


}
