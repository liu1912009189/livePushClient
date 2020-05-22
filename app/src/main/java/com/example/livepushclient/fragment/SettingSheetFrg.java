package com.example.livepushclient.fragment;

import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.livepushclient.R;
import com.example.livepushclient.StartActivity;
import com.example.livepushclient.bean.LiveData;

/**
 * Created by liule on 2020/5/22.
 * ----2020/5/22-------liule----xxxxxxxxx--
 */
public class SettingSheetFrg extends BaseBottomSheetFrag {

    private EditText mEtFps;
    private EditText mEtMinBps;
    private EditText mEtMaxBps;
    private EditText mEtInitBps;
    private RadioGroup mRgPix;
    private RadioGroup mRgNetwokr;
    private LiveData mLiveData = new LiveData();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void initView() {
        mRgPix = rootView.findViewById(R.id.resolution_group);
        mEtFps = rootView.findViewById(R.id.et_frame_rate);
        mEtMinBps = rootView.findViewById(R.id.et_min_bitrate);
        mEtMaxBps = rootView.findViewById(R.id.et_max_bitrate);
        mEtInitBps = rootView.findViewById(R.id.et_init_bitrate);
        mRgNetwokr = rootView.findViewById(R.id.rg_network);

        mRgPix.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setResolution(checkedId);
            }
        });
        mRgNetwokr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setNetwork(checkedId);
            }
        });
        mEtFps.setText(mLiveData.Fps);
        mEtMinBps.setText(mLiveData.minBps);
        mEtMaxBps.setText(mLiveData.maxBps);
        mEtInitBps.setText(mLiveData.initBps);
        initResolution();
        initNetWork();
    }

    private void initNetWork() {
        if("0".equals(mLiveData.netFlag)){
            mRgNetwokr.check(R.id.rb_dev);
        }else if("1".equals(mLiveData.netFlag)){
            mRgNetwokr.check(R.id.rb_test);
        }
    }

    private void initResolution() {
        switch (mLiveData.resolution){
            case "360,640":
                mRgPix.check(R.id.rb_360);
                break;
            case "480,720":
                mRgPix.check(R.id.rb_480);
                break;
            case "544,960":
                mRgPix.check(R.id.rb_540);
                break;
            case "720,1280":
                mRgPix.check(R.id.rb_720);
                break;
            default:
                break;
        }
    }

    private void setNetwork(int checkedId) {
        switch (checkedId){
            case R.id.rb_dev:
                mLiveData.netFlag = "0";
                break;
            case R.id.rb_test:
                mLiveData.netFlag = "1";
                break;
            default:
                mLiveData.netFlag = "0";
                break;
        }
    }

    private void setResolution(int checkedId) {
        switch (checkedId){
            case R.id.rb_360:
                mLiveData.resolution = "360,640";
                break;
            case R.id.rb_480:
                mLiveData.resolution = "480,720";
                break;
            case R.id.rb_540:
                mLiveData.resolution = "544,960";
                break;
            case R.id.rb_720:
                mLiveData.resolution = "720,1280";
                break;
            default:
                mLiveData.resolution = "360,640";
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mLiveData.maxBps = mEtMaxBps.getText().toString().trim();
        mLiveData.minBps= mEtMinBps.getText().toString().trim();
        mLiveData.initBps = mEtInitBps.getText().toString().trim();
        mLiveData.Fps = mEtFps.getText().toString().trim();
        ((StartActivity) getActivity()).setLiveData(mLiveData);
    }

    public void setLiveData(LiveData liveData) {
        mLiveData.netFlag = liveData.netFlag;
        mLiveData.resolution = liveData.resolution;
        mLiveData.Fps = liveData.Fps;
        mLiveData.initBps = liveData.initBps;
        mLiveData.minBps = liveData.minBps;
        mLiveData.maxBps = liveData.maxBps;


    }
}
