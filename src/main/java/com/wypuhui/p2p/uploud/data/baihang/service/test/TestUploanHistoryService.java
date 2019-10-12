package com.wypuhui.p2p.uploud.data.baihang.service.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wypuhui.p2p.uploud.data.baihang.BhCreditApiClient;
import com.wypuhui.p2p.uploud.data.baihang.dto.LoanAccountInfoDto;
import com.wypuhui.p2p.uploud.data.baihang.dto.LoanAfterInfoDto;
import com.wypuhui.p2p.uploud.data.baihang.dto.LoanApplyInfoDto;
import com.wypuhui.p2p.uploud.data.baihang.encrypt.AESUtil;
import com.wypuhui.p2p.uploud.data.baihang.encrypt.RSAUtil;
import com.wypuhui.p2p.uploud.data.baihang.request.FileUploadRequest;
import com.wypuhui.p2p.uploud.data.baihang.response.FileUploadResponse;
import com.wypuhui.p2p.uploud.data.baihang.util.Base64;
import com.wypuhui.p2p.uploud.data.baihang.util.DateUtils;
import com.wypuhui.p2p.uploud.data.baihang.util.EncryptionUtils;
import com.wypuhui.p2p.uploud.data.baihang.zip.ZipUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @Author: liuw
 * @Date: 2019/9/10 15:46
 * @Description:
 */
@Component
public class TestUploanHistoryService {
    private static Logger logger = LoggerFactory.getLogger(TestUploanHistoryService.class);

    private static final String LINEFEED = "\r\n";

    private static final String COMPANY_NAME = "北京威阳普惠信息科技有限公司";

    private static final String AES_KEY = "WYPUHUI_AES_KEY";

    private static String PATH = System.getProperty("java.io.tmpdir") + File.separator;

    public String doTask() {
        String fileName = PATH + COMPANY_NAME + "_" + DateUtils.getString(new Date(), "yyyyMMddHHmmss") + "_" + new Random().nextInt(1000);
        StringBuffer stringBuffer = new StringBuffer();
        try {
            File file = toTxt(fileName);
            BhCreditApiClient creditApiClient = new BhCreditApiClient();
            FileUploadRequest request = new FileUploadRequest();
            creditApiClient.init(RSAUtil.readRSAPublicKey(EncryptionUtils.pubClassPathResource.getInputStream()),AES_KEY);
            logger.info("***********" ,file.getPath());
            request.setDataFile(file.getPath());
            request.setTargetFilePath(PATH);

            FileUploadResponse execute = creditApiClient.execute(request);
            if (execute.isSuccess) {
                String encryptFilePath = execute.getEncryptFilePath();
                logger.info("生成加密文件成功，路径 ：{} ,文件名：{} ",encryptFilePath,execute.getEncryptFileName());
                return encryptFilePath;
            }
            return "任务失败";
        } catch (Exception e) {
            logger.error("执行报送文件失败！",e);
            return "";
        }

    }

    /**
     * 功能描述： 加密zip获取最终.cry文件
     * Author: liuw
     * Date: 2019/8/26 19:46
     * param [zip, fileName]
     * return java.lang.String
     */

    private String encrypZipToCryFile(File zip, String fileName) throws Exception {
        File cryFile = new File(fileName + ".cry");
        if (!cryFile.exists()) {
            cryFile.createNewFile();
        }
        PrintWriter cryPw = new PrintWriter(cryFile);
        //机构设置的AES密钥 根据百行公钥加密并填入cry文件第一行。
        cryPw.println(EncryptionUtils.encrypParams(AES_KEY) + LINEFEED);
        //读取zip
        FileInputStream fileInputStream = new FileInputStream(zip);
        byte[] bytes = new byte[1024];
        int len = -1;
        while ((len = fileInputStream.read(bytes)) != -1) {
            String zipCon = new String(bytes, 0, len);
            //读取zip流分1024进行机构AES加密。
            byte[] enCode = AESUtil.encryptData(AES_KEY, zipCon.getBytes());
            String encodeStr = Base64.byteArrayToBase64(enCode);
            //写入cry文件中
            cryPw.println(encodeStr + LINEFEED);
        }
        cryPw.flush();
        cryPw.close();
        fileInputStream.close();
        return cryFile.getPath();
    }

    /**
     * 功能描述： 生成数据文件并压缩
     * Author: liuw
     * Date: 2019/8/26 19:46
     * param [startTime, endTime, fileName]
     * return java.io.File
     */

    private File createZip(String fileName) throws IOException {
        File file = new File(fileName+".txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        PrintWriter printWriter = new PrintWriter(fileOutputStream);
        printWriter.println(createApplyLoanInfoData());
        printWriter.println(createLoanAccountInfoData());
        printWriter.println(createLoanAfterInfoData());
        printWriter.flush();
        printWriter.close();
        fileOutputStream.close();
        File target = new File(fileName + ".zip");
        if (!target.exists()) {
            target.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(target);
        ZipUtil.toZip(file, outputStream);
        return target;
    }

    public File toTxt(String fileName) throws Exception{
        File file = new File(fileName+".txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        PrintWriter printWriter = new PrintWriter(fileOutputStream);
        printWriter.println(createApplyLoanInfoData());
        printWriter.println(createLoanAccountInfoData());
        printWriter.println(createLoanAfterInfoData());
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

    private String createLoanAccountInfoData() {

        StringBuffer stringBuffer = new StringBuffer("#singleLoanAccountInfo"+LINEFEED);
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
                    String json = JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue);
                    stringBuffer.append(json).append(LINEFEED);
                    logger.info("贷款账户模拟数据dto：【 {} 】", JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue));
                }
            }
            logger.info("测试贷款账户接口完成");
        } catch (Exception e) {
            logger.error("测试百行贷款账户接口失败", e);
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

    private String createLoanAfterInfoData() {
        StringBuffer stringBuffer = new StringBuffer("#singleLoanRepayInfo"+LINEFEED);
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
                    String json = JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue);
                    stringBuffer.append(json).append(LINEFEED);
                    logger.info("贷后模拟数据dto：【 {} 】", JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue));
                }
            }
            logger.info("测试贷后接口完成");
        } catch (Exception e) {
            logger.error("测试百行贷后接口失败", e);
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

    private String createApplyLoanInfoData() {
        StringBuffer stringBuffer = new StringBuffer("#loanApplyInfo"+LINEFEED);
        try {
//            File file = new File("E:/ideaWorkSpace/p2pData/p2pData-web/src/main/resources/d2.txt");
            ClassPathResource resource = new ClassPathResource("c1.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String string;
            while ((string = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(string)) {
                    String[] split = string.split("\t");
                    LoanApplyInfoDto dto = new LoanApplyInfoDto();
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
                    dto.setUploadTs(DateUtils.getISO8601Timestamp(new Date()));
                    dto.setReqID(UUID.randomUUID().toString().replace("-", "").toUpperCase());
                    logger.info("贷款申请模拟数据dto：【 {} 】", JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue));
                    String json = JSON.toJSONString(dto, SerializerFeature.WriteMapNullValue);
                    stringBuffer.append(json).append(LINEFEED);
                }
            }
        } catch (Exception e) {
            logger.error("测试百行贷款申请接口失败", e);
        }
        return stringBuffer.toString();
    }


//    public void encodeCry() throws Exception {
//
//        File file = new File("C:/Users/HePeng/AppData/Local/Temp/北京威阳普惠信息科技有限公司_20190910175707_891.cry");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//        String aes = reader.readLine();
//        byte[] bytes = Base64.base64ToByteArray(aes);
//        String s = EncryptionUtils.DecryptParams(aes);
//        logger.info("解密AES：{} ",s);
//        String string = null;
//        File target = new File("dum.zip");
//        FileOutputStream outputStream = new FileOutputStream(target);
//        while (StringUtils.isNotBlank(string = reader.readLine())) {
//            if (StringUtils.isNotBlank(string)) {
//                byte[] sdf = AESUtil.decryptData(s, Base64.base64ToByteArray(string));
//                outputStream.write(sdf);
//            }
//        }
//        ZipUtil.toZip(file, outputStream);
//
//    }

}
