package com.example.livepushclient;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.livepushclient.util.ViewClickUtils;
import com.laifeng.sopcastsdk.ui.CameraLivingView;
import com.laifeng.sopcastsdk.video.effect.Beauty;
import com.laifeng.sopcastsdk.video.effect.LookupFilter;


/**
 * Created by WangShuo on 2018/2/26.
 */

public class LiveUI implements View.OnClickListener {

    private LiveActivity activity;
    private CameraLivingView liveCameraView;
    private String rtmpUrl = "";
    boolean isBeatify = true;

    private Button btnStartStreaming;
    private ImageView mBeautyOneStep;
    private ImageView mSwapCamera;
    private View mExit;
    private TextView mTvStatus;
    private boolean isExit = false;

    public LiveUI(LiveActivity liveActivity, CameraLivingView liveCameraView, String rtmpUrl) {
        this.activity = liveActivity;
        this.liveCameraView = liveCameraView;
        this.rtmpUrl = rtmpUrl;
        init();
    }


    private void init() {
        btnStartStreaming = activity.findViewById(R.id.btn_camera_shutter);
        btnStartStreaming.setOnClickListener(this);

        mSwapCamera = activity.findViewById(R.id.bt_camera_exchange);
        mSwapCamera.setOnClickListener(this);

        mBeautyOneStep = activity.findViewById(R.id.btn_camera_beauty);
        mBeautyOneStep.setOnClickListener(this);
        mBeautyOneStep.setImageResource(R.drawable.camra_beauty_close);

        mExit = activity.findViewById(R.id.btn_camera_exit);
        mExit.setOnClickListener(this);
        mTvStatus = activity.findViewById(R.id.tv_status);


    }

    public void setRoomStatus(boolean open) {
        mTvStatus.setText(open == true ? R.string.broadcast_status_on : R.string.broadcast_status_off);
        btnStartStreaming.setText(open ? R.string.end_live : R.string.start_live);
    }

    @Override
    public void onClick(View view) {
        if (ViewClickUtils.isFastClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.btn_camera_shutter://开始推流
                if(!activity.isLiveStatus()){
                    mTvStatus.setText(R.string.broadcast_connecting);
                    activity.startLive();
                }else if (!activity.isRecording()) {
                    setRoomStatus(true);
                    activity.onUserComeBack();
                } else {
                    activity.stopStreaming(false);
                }
                break;
//
            case R.id.btn_camera_exit:
                isExit = true;
                if(activity.isLiveStatus()){
                    activity.setLiveStatus(false);
                    activity.stopStreaming(true);
                }else {
                    activity.finish();
                }


                break;
            case R.id.bt_camera_exchange://切换摄像头
                liveCameraView.switchCamera();
                break;
            case R.id.btn_camera_beauty:
                if (isBeatify) {
                    liveCameraView.removeAllFilter();
                    liveCameraView.complete();
                } else {
                    Beauty beauty = new Beauty(activity.getResources());
                    LookupFilter lookupFilter = new LookupFilter(activity.getResources());
                    lookupFilter.setMaskImage("lookup/purity.png");
                    beauty.setFlag(6);
                    lookupFilter.setIntensity(1f);
                    liveCameraView.addFilter(lookupFilter);
                    liveCameraView.addFilter(beauty);
                    liveCameraView.complete();
                }
                mBeautyOneStep.setImageResource(isBeatify?R.drawable.camra_beauty:R.drawable.camra_beauty_close);
                isBeatify = !isBeatify;

                break;
            default:
                break;
        }
    }

    public boolean isExit() {
        return isExit;
    }

    public void setExit(boolean exit) {
        isExit = exit;
    }

}
