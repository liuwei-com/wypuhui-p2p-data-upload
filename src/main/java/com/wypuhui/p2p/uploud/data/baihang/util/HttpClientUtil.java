package com.wypuhui.p2p.uploud.data.baihang.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: liuw
 * @Date: 2019/7/26 17:27
 * @Description:
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static Map<String,Object> doGet(Map<String,String> parameters,String httpPath) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 参数
        URI uri = null;
        try {
            // 将参数放入键值对类NameValuePair中,再放入集合中
            List<NameValuePair> params = new ArrayList<>();
            for (String key : parameters.keySet()) {
                params.add(new BasicNameValuePair(key, parameters.get(key)));
            }
            // 设置uri信息,并将参数集合放入uri;
            // 注:这里也支持一个键值对一个键值对地往里面放setParameter(String key, String value)
            uri = new URIBuilder().setPath(httpPath)
                    .setParameters(params).build();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        // 创建Get请求
        HttpGet httpGet = new HttpGet(uri);
        logger.info("请求Http地址 [ URI:{} ]",uri);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 配置信息
            RequestConfig requestConfig = RequestConfig.custom()
                    // 设置连接超时时间(单位毫秒)
                    .setConnectTimeout(5000)
                    // 设置请求超时时间(单位毫秒)
                    .setConnectionRequestTimeout(5000)
                    // socket读写超时时间(单位毫秒)
                    .setSocketTimeout(5000)
                    // 设置是否允许重定向(默认为true)
                    .setRedirectsEnabled(true).build();
            // 将上面的配置信息 运用到这个Get请求里
            httpGet.setConfig(requestConfig);
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            logger.info("响应状态为:" + response.getStatusLine() + " 响应内容长度为:" + responseEntity.getContentLength());
            if (responseEntity != null) {
                Map<String,Object> object = (Map<String, Object>) JSON.parse(EntityUtils.toString(responseEntity));
                return object;
            }
            return null;
        } catch (Exception e) {
           logger.error("httpGet请求异常~", e);
            return null;
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("释放Http请求资源异常~", e);
            }
        }

    }

    public static Map<String,Object> doPost(String url, String paramJson, String authorization) {
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        try {
            // 声明httpPost请求
            HttpPost httpPost = new HttpPost(url);
            // 判断map不为空
            if (StringUtils.isNotBlank(paramJson)) {
                StringEntity se = new StringEntity(paramJson, "utf-8");
                httpPost.setEntity(se); // post方法中，加入json数据
                httpPost.setHeader("Content-Type", "application/json");
                if (StringUtils.isNotBlank(authorization)) {
                    httpPost.addHeader("Authorization", authorization);
                }
            }
            // 使用HttpClient发起请求，返回response
            logger.info("请求Http地址 [ URI:{}, PARAMETER:{} ]",httpPost,paramJson);
            response = httpClient.execute(httpPost);
            // 解析response封装返回对象httpResult
            if (response != null) {
                Map<String,Object> object = (Map<String, Object>) JSON.parse(EntityUtils.toString(response.getEntity(),"UTF-8"));
                return object;
            }
            // 返回结果
            return null;
        } catch (Exception e) {
            logger.error("httpPost请求异常~", e);
            throw new RuntimeException("send request to baiHangCredit error");
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("释放Http请求资源异常~", e);
            }
        }

    }
}
