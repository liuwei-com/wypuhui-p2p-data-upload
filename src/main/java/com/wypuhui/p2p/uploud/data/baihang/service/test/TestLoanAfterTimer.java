package com.wypuhui.p2p.uploud.data.baihang.service.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wypuhui.p2p.uploud.data.baihang.common.BHInterfaceUri;
import com.wypuhui.p2p.uploud.data.baihang.dto.LoanAfterInfoDto;
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
public class TestLoanAfterTimer {

    private Logger logger = LoggerFactory.getLogger(TestLoanAfterTimer.class);

    @Autowired
    BHInterfaceUri bhInterfaceUri;

    public void doTask() {
        logger.info("测试贷后信息上传接口开始");
        try {
//            File file = new File("E:/ideaWorkSpace/p2pData/p2pData-web/src/main/resources/d2.txt");
            ClassPathResource resource = new ClassPathResource("d3.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String string;
            while ((string = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(string)) {
                    String[] split = string.split("\t");
                    LoanAfterInfoDto dto = new LoanAfterInfoDto();
                    dto.setReqID(split[0].trim());
                    dto.setOpCode(split[1].trim());
                    dto.setUploadTs(split[2].trim());
                    dto.setLoanId(split[3].trim());
                    dto.setName(split[4].trim());
                    dto.setPid(split[5].trim());
                    dto.setMobile(split[6].trim());
                    dto.setTermNo(Integer.valueOf(split[7].trim()));
                    dto.setTermStatus(split[8].trim());
                    dto.setTargetRepaymentDate(split[9].trim());
                    dto.setRealRepaymentDate(split[10].trim());
                    dto.setPlannedPayment(new BigDecimal(split[11].trim()));
                    dto.setTargetRepayment(new BigDecimal(split[12].trim()));
                    dto.setRealRepayment(new BigDecimal(split[13].trim()));
                    dto.setOverdueStatus(split[14].trim());
                    dto.setStatusConfirmAt(split[15].trim());
                    dto.setOverdueAmount(new BigDecimal(split[16].trim()));
                    dto.setRemainingAmount(new BigDecimal(split[17].trim()));
                    dto.setLoanStatus(Integer.parseInt(split[18].trim()));

                    logger.info("贷后模拟数据dto：【 {} 】", JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue));
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
            logger.info("测试贷后接口完成");
        } catch (Exception e) {
            logger.error("测试百行贷后接口失败", e);
        }
    }


    private Object sendToBaiHang(LoanAfterInfoDto dto) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        String admin = bhInterfaceUri.getAdmin() + ":" + bhInterfaceUri.getPassword();
        String authorization = "Basic " + Base64.byteArrayToBase64(admin.getBytes());
        dto.setName(EncryptionUtils.encrypParams(dto.getName()));
        dto.setMobile(EncryptionUtils.encrypParams(dto.getMobile()));
        dto.setPid(EncryptionUtils.encrypParams(dto.getPid()));
        Map<String, Object> doPost = HttpClientUtil.doPost(bhInterfaceUri.getD3Url(), JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue), authorization);
        logger.info("贷后信息提交至百行 响应数据 response ：[ {} ]", doPost);
        return doPost;
    }


}
