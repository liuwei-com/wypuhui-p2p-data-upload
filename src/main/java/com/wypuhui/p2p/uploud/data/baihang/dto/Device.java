package com.wypuhui.p2p.uploud.data.baihang.dto;

/**
 * @Author: liuw
 * @Date: 2019/8/19 17:36
 * @Description: 设备信息
 */
public class Device {

    //设备类型
    private Integer deviceType;
    //设备 IMEI/MEID
    private String imei;
    // MAC地址
    private String mac;
    // IP地址
    private String ipAddress;
    //设备操作系统标签
    private Integer osName;

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getOsName() {
        return osName;
    }

    public void setOsName(Integer osName) {
        this.osName = osName;
    }
}
