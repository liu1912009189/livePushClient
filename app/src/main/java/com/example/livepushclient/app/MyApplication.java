package com.example.livepushclient.app;

import android.app.Application;

import com.example.livepushclient.bean.DeviceInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by liule on 2020/5/3.
 * ----2020/5/3-------liule----xxxxxxxxx--
 */
public class MyApplication extends Application {
    private static MyApplication globalContext = null;
    private static String DEFAULT_CACHE_DIR = AppConfig.PRE_FLAVOR + "_cache";
    private static String CACHE_USER_INFO = AppConfig.PRE_FLAVOR + "_deviceInfo";
    private DeviceInfo mDeviceInfo;


    @Override
    public void onCreate() {
        super.onCreate();

        if (globalContext == null) {
            synchronized (MyApplication.class) {
                if (globalContext == null) {
                    globalContext = this;
                }
            }
        }

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("TAG"))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

    }

    public static MyApplication getInstance() {
        return globalContext;
    }

    public ACache getCache() {
        return ACache.get(globalContext, DEFAULT_CACHE_DIR);
    }

    public DeviceInfo getDeviceInfo() {
        if (mDeviceInfo == null) {
            Object deviceInfo = globalContext.getCache().getAsObject(CACHE_USER_INFO);
            if (deviceInfo != null && deviceInfo instanceof DeviceInfo) {
                mDeviceInfo = (DeviceInfo) deviceInfo;
            }
        }
        return mDeviceInfo;
    }
    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.mDeviceInfo = deviceInfo;
        try {
            if (deviceInfo != null) {
                globalContext.getCache().put(CACHE_USER_INFO, deviceInfo);
            } else {
                globalContext.getCache().remove(CACHE_USER_INFO);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
