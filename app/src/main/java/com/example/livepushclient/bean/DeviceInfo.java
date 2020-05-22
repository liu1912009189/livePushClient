package com.example.livepushclient.bean;

import java.io.Serializable;

/**
 * Created by liule on 2020/5/8.
 * ----2020/5/8-------liule----xxxxxxxxx--
 */
public class DeviceInfo implements Serializable {
    private String brand;//品牌
    private String deviceName;//设备名称
    private String osName;//操作系统名字
    private String imei;//设备唯一标识符
    private String appVersion;//app版本
    private String osVersion;//系统版本

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }
}
