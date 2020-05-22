package com.example.livepushclient.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.livepushclient.LiveActivity;
import com.example.livepushclient.R;
import com.example.livepushclient.listener.NetWokrListener;
import com.example.livepushclient.util.HttpUtils;
import com.example.livepushclient.util.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import okhttp3.Call;

/**
 * Created by liule on 2020/5/3.
 * ----2020/5/3-------liule----xxxxxxxxx--
 */
public class LivePresenter extends BasePresenter<LiveActivity> implements NetWokrListener.NetWorkStateListener {
    private String TAG = getClass().getSimpleName();
    /**
     * 推流是否已经开始
     */
    private String mRtmpUrl;
    private NetWokrListener mNetWokrListener;
    private boolean shouldReStart;

    public LivePresenter(LiveActivity target, String rtmpUrl) {
        super(target);
        mRtmpUrl = rtmpUrl;
        init();
    }

    private void init() {
        mNetWokrListener = new NetWokrListener(getTarget());
        mNetWokrListener.begin(this);
    }


    public void startLive() {
        HttpUtils.isRoomLiveUsed(mRtmpUrl, new HttpUtils.NetWorkCall() {
            @Override
            public void onError(Call call, Exception e, int id) {
                getTarget().setRoomState(false);
                if (!NetUtil.isNetworkAvailable(getTarget().getApplicationContext())) {
                    Toast.makeText(getTarget().getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getTarget().getApplicationContext(), R.string.servererror, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onReponse(String response, int id) {
                Log.e(TAG, response);
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String result = jsonObject.optString("result");
                        if (!"0".equals(result)) {
                            getTarget().setRoomState(false);
                            String msg = jsonObject.optString("msg");
                            Toast.makeText(getTarget().getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String use = jsonObject.optString("use");
                       boolean isUse = "0".equals(use) ? false : true;
                        if (isUse) {
                            String brand = jsonObject.optString("brand");
                            String deviceName = jsonObject.optString("device_name");
                            Toast.makeText(getTarget().getApplicationContext(), "房间已经被" + brand + "(" + deviceName + ")" + "占用", Toast.LENGTH_SHORT).show();
                            getTarget().setRoomState(false);
                            return;
                        }
                        getTarget().startStream();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void closeLiveRoom(boolean isExit){
        String status = isExit? "0":"5";
        HttpUtils.changeLiveroomState(mRtmpUrl, status, new HttpUtils.NetWorkCall() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (!NetUtil.isNetworkAvailable(getTarget().getApplicationContext())) {
                    Toast.makeText(getTarget().getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getTarget().getApplicationContext(), R.string.exit_room, Toast.LENGTH_SHORT).show();
                }
                boolean exit = getTarget().isExitClicked();
                if (exit == true) {
                    getTarget().finish();
                }
            }

            @Override
            public void onReponse(String response, int id) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String result = jsonObject.optString("result");
                        if (!"0".equals(result)) {
                            Toast.makeText(getTarget().getApplicationContext(), R.string.exit_room, Toast.LENGTH_SHORT).show();
                        }
                        boolean exit = getTarget().isExitClicked();
                        if (exit == true) {
                            getTarget().finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public Context getContext() {
        return getTarget();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getTarget().isRecording()){
            getTarget().openRoom();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if(getTarget().isRecording()){
            getTarget().closeRoom(false);
        }
    }

    @Override
    public void onDestroy() {
        HttpUtils.cancleHttp(HttpUtils.TAG);
        mNetWokrListener.unregisterListener();
        super.onDestroy();
    }

    public void setStartStatus(boolean isStart) {
        this.shouldReStart = isStart;
    }


    @Override
    public void onNetworkChanged() {
//        if (getTarget().shouldReStart() == true) {
//            Log.e(TAG,"onNetworkChanged");
//            getTarget().stopStreaming(true);
//            getTarget().startStream();
//        }
    }

    @Override
    public void onNetWorkDisable() {
        Log.e(TAG,"onNetWorkDisable");
    }
}
