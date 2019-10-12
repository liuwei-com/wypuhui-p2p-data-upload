package com.wypuhui.p2p.uploud.data.baihang.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: liuw
 * @Date: 2019/8/21 15:13
 * @Description:
 */
@Component
public class BHInterfaceUri {
    @Value("${p2pData.baiHangCredit.https.c1Url}")
    private String c1Url;
    @Value("${p2pData.baiHangCredit.https.d2Url}")
    private String d2Url;
    @Value("${p2pData.baiHangCredit.https.d3Url}")
    private String d3Url;
    @Value("${p2pData.baiHangCredit.admin}")
    private String admin;
    @Value("${p2pData.baiHangCredit.password}")
    private String password;

    public String getC1Url() {
        return c1Url;
    }

    public void setC1Url(String c1Url) {
        this.c1Url = c1Url;
    }

    public String getD2Url() {
        return d2Url;
    }

    public void setD2Url(String d2Url) {
        this.d2Url = d2Url;
    }

    public String getD3Url() {
        return d3Url;
    }

    public void setD3Url(String d3Url) {
        this.d3Url = d3Url;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
