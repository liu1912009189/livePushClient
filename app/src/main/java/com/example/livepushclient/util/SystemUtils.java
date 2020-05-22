package com.example.livepushclient.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.example.livepushclient.app.MyApplication;
import com.example.livepushclient.bean.DeviceInfo;

import java.util.UUID;

/**
 * Created by liule on 2020/5/8.
 * ----2020/5/8-------liule----xxxxxxxxx--
 */
public class SystemUtils {
    public static DeviceInfo getDeviceInfo() {
        DeviceInfo deviceInfo = MyApplication.getInstance().getDeviceInfo();
        if (deviceInfo == null) {
            deviceInfo = new DeviceInfo();
            try {
                PackageInfo info = MyApplication.getInstance().getPackageManager().getPackageInfo
                        (MyApplication.getInstance().getPackageName(), 0);
                deviceInfo.setAppVersion(info.versionName);
                deviceInfo.setBrand(Build.BRAND);
                deviceInfo.setDeviceName(Build.MODEL);
                deviceInfo.setOsName("android" + Build.VERSION.RELEASE);
                deviceInfo.setOsVersion(Build.VERSION.SDK_INT + "");
                deviceInfo.setImei(UUID.randomUUID().toString());
                MyApplication.getInstance().setDeviceInfo(deviceInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return deviceInfo;
    }
}
