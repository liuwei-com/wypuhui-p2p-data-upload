package com.wypuhui.p2p.uploud.data.baihang.util;

import com.wypuhui.p2p.uploud.data.baihang.BhCreditApiClient;
import com.wypuhui.p2p.uploud.data.baihang.encrypt.RSAUtil;
import com.wypuhui.p2p.uploud.data.baihang.response.InterfaceUploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @Author: liuw
 * @Date: 2019/8/21 15:52
 * @Description:
 */
public class EncryptionUtils {

    private static Logger logger = LoggerFactory.getLogger(EncryptionUtils.class);
    //初始化设置RSA公钥
    public static ClassPathResource pubClassPathResource = new ClassPathResource("rsa_public_key.pem");

    public static ClassPathResource priClassPathResource = new ClassPathResource("rsa_private_key.pem");

    private static BhCreditApiClient pubKeyclient = new BhCreditApiClient();

    private static  BhCreditApiClient priKeyclient = new BhCreditApiClient();

    static {
        try {
            pubKeyclient.init(RSAUtil.readRSAPublicKey(pubClassPathResource.getInputStream()));
            priKeyclient.init(RSAUtil.readRSAPrivateKey(priClassPathResource.getInputStream()));
        } catch (Exception e) {
            logger.error("初始化百行征信秘钥失败", e);
            pubKeyclient = null;
            priKeyclient = null;
        }
    }

    public static String encrypParams(String param) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        if (pubKeyclient == null) throw new RuntimeException("read baiHangCredit rsa_public_key error");
        //执行加密操作
        InterfaceUploadResponse response = pubKeyclient.execute(param);
        if(response.isSuccess){
            return response.getParams();
        }else{
            throw new RuntimeException("param encryption exception");
        }
    }

}
