package com.wypuhui.p2p.uploud.data.baihang.service.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wypuhui.p2p.uploud.data.baihang.common.BHInterfaceUri;
import com.wypuhui.p2p.uploud.data.baihang.dto.LoanApplyInfoDto;
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
public class TestLoanApplyTimer {

    private Logger logger = LoggerFactory.getLogger(TestLoanApplyTimer.class);

    @Autowired
    BHInterfaceUri bhInterfaceUri;

    public void doTask() {
        logger.info("测试贷款申请信息上传接口开始");
        try {
//            File file = new File("E:/ideaWorkSpace/p2pData/p2pData-web/src/main/resources/d2.txt");
            ClassPathResource resource = new ClassPathResource("c1.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String string;
            while ((string = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(string)) {
                    String[] split = string.split("\t");
                    LoanApplyInfoDto dto = new LoanApplyInfoDto();
                    if (split.length == 9) {
                        dto.setName(split[0].trim());
                        dto.setPid(split[1].trim());
                        dto.setMobile(split[2].trim());
                        dto.setQueryReason(StringUtils.isBlank(split[3].trim()) ? null: Integer.valueOf(split[3].trim()));
                        dto.setGuaranteeType(StringUtils.isBlank(split[4].trim()) ? null: Integer.valueOf(split[4].trim()));
                        dto.setLoanPurpose(StringUtils.isBlank(split[5].trim()) ? null: Integer.valueOf(split[5].trim()));
                        dto.setCustomType(StringUtils.isBlank(split[6].trim()) ? null: Integer.valueOf(split[6].trim()));
                        dto.setApplyAmount(new BigDecimal(StringUtils.isBlank(split[7].trim()) ? "0.00" : split[7].trim()));
                        dto.setLoanId(split[8].trim());
                    } else if (split.length == 4) {
                        dto.setName(split[0].trim());
                        dto.setPid(split[1].trim());
                        dto.setMobile(split[2].trim());
                        dto.setQueryReason(StringUtils.isBlank(split[3].trim()) ? null: Integer.valueOf(split[3].trim()));
                    } else {
                        dto.setName(split[0].trim());
                        dto.setPid(split[1].trim());
                        dto.setMobile(split[2].trim());
                        dto.setQueryReason(StringUtils.isBlank(split[3].trim()) ? null: Integer.valueOf(split[3].trim()));
                        dto.setGuaranteeType(StringUtils.isBlank(split[4].trim()) ? null: Integer.valueOf(split[4].trim()));
                        dto.setLoanPurpose(StringUtils.isBlank(split[5].trim()) ? null: Integer.valueOf(split[5].trim()));
                        dto.setCustomType(StringUtils.isBlank(split[6].trim()) ? null: Integer.valueOf(split[6].trim()));
                        dto.setApplyAmount(new BigDecimal(StringUtils.isBlank(split[7].trim()) ? "0.00" : split[7].trim()));
                        dto.setLoanId(split[8].trim());
                        dto.setHomeAddress(split[9].trim());
                        dto.setHomePhone(split[10].trim());
                        dto.setWorkName(split[11].trim());
                        dto.setWorkAddress(split[12].trim());
                        dto.setWorkPhone(split[13].trim());
                    }
                    logger.info("贷款申请模拟数据dto：【 {} 】", JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue));
                    try {
                        Object o = sendApplyLoanToBaiHang(dto);
                        if (o != null) {
                            logger.info("接口测试百行响应JSON ：【 {} 】",JSON.toJSONString(o, SerializerFeature.WriteMapNullValue));
                        }
                    } catch (Exception e) {
                        logger.error("发送百行征信数据失败 reqId:{} , Exception {} ",JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue),e.getMessage());
                    }
                }
            }
            logger.info("测试贷款申请接口完成");
        } catch (Exception e) {
            logger.error("测试百行贷款申请接口失败", e);
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


}
