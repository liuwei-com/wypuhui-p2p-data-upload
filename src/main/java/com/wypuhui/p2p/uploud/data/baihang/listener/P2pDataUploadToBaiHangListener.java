//package com.wypuhui.p2p.uploud.data.baihang.listener;
//
//import com.wypuhui.p2p.uploud.data.baihang.service.P2pDataUploadToBaiHangService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageListener;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.io.UnsupportedEncodingException;
//
///**
// * Created by zyd on 2019/4/24.
// */
////@Component
//@Slf4j
//public class P2pDataUploadToBaiHangListener implements MessageListener {
//
//
//    @Resource
//    private P2pDataUploadToBaiHangService p2pDataUploadToBaiHangService;
//
//    public String byteArrayToString(byte[] body) {
//        String s = null;
//        try {
//            s = new String(body, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        log.info(s);
//        return s;
//    }
//
//    @Override
//    public void onMessage(Message message) {
////        log.info("[数据报送服务] 检测到消息！");
////        byte[] body = message.getBody();
////        String data = byteArrayToString(body);
////        try {
////            p2pDataUploadToBaiHangService.upload(data);
////        } catch (Exception e) {
////            log.info("[数据报送服务] 消息异常："+data);
////            log.info(e);
////        }
//    }
//
//
//
//
//}
