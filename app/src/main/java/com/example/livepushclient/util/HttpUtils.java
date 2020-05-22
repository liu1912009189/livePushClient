package com.example.livepushclient.util;

import android.util.Log;

import com.example.livepushclient.bean.DeviceInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by liule on 2020/5/3.
 * ----2020/5/3-------liule----xxxxxxxxx--
 */
public class HttpUtils {
    public static String TAG = "rtmpHttp";
    private static String BASE_URL = "http://20.26.20.81:8185";

    public interface NetWorkCall {
        void onError(Call call, Exception e, int id);

        void onReponse(String response, int id);
    }

    public static void setBaseUrl(String netFlag){
        if("0".equals(netFlag)){
            BASE_URL = "https://wap.zj.10086.cn";
        }else if("1".equals(netFlag)){
            BASE_URL = "http://20.26.20.81:8185";
        }else {
            BASE_URL = "https://wap.zj.10086.cn";
        }
    }

    /**
     * 直播状态0 下播 1在播 2预览未连接 3链接中   4未连接  5离开  99错误
     */
    public static void changeLiveroomState(String trmpUrl, String state,final NetWorkCall netWorkCall) {
        DeviceInfo deviceInfo = SystemUtils.getDeviceInfo();
        String url = BASE_URL +"/zjweb/live/roomState.do";
        OkHttpUtils
                .get()
                .tag(TAG)
                .url(url)
                .addParams("token", "72b8566f33c87af29484c21b3e4e1cbc")
                .addParams("rtmpUrl", trmpUrl)
                .addParams("liveState", state)
                .addParams("brand", deviceInfo.getBrand())
                .addParams("device_name", deviceInfo.getDeviceName())
                .addParams("os_name", deviceInfo.getOsName())
                .addParams("imei", deviceInfo.getImei())
                .addParams("app_version", deviceInfo.getAppVersion())
                .addParams("os_version", deviceInfo.getOsVersion())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (netWorkCall != null) {
                            netWorkCall.onError(call, e, id);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG,response);
                        if (netWorkCall != null) {
                            netWorkCall.onReponse(response, id);
                        }
                    }
                });
    }

    public static void isRoomLiveUsed(String rtmpUrl,final NetWorkCall netWorkCall ) {
         DeviceInfo deviceInfo = SystemUtils.getDeviceInfo();

        String url = BASE_URL + "/zjweb/live/addrState.do";
        OkHttpUtils
                .get()
                .tag(TAG)
                .url(url)
                .addParams("token", "72b8566f33c87af29484c21b3e4e1cbc")
                .addParams("rtmpUrl", rtmpUrl)
                .addParams("imei", deviceInfo.getImei())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (netWorkCall != null) {
                            netWorkCall.onError(call, e, id);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG,response);
                        if (netWorkCall != null) {
                            netWorkCall.onReponse(response, id);
                        }
                    }
                });

    }

    public static void cancleHttp(String tag) {
        OkHttpUtils.getInstance().cancelTag(tag);
    }
}
