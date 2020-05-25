package com.example.livepushclient.fragment;

import android.text.TextUtils;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.example.livepushclient.R;
import com.example.livepushclient.StartActivity;
import com.example.livepushclient.bean.LiveData;
import com.example.livepushclient.view.XEditText;

/**
 * Created by liule on 2020/5/22.
 * ----2020/5/22-------liule----xxxxxxxxx--
 */
public class SettingSheetFrg extends BaseBottomSheetFrag {

    private XEditText mEtFps;
    private XEditText mEtMinBps;
    private XEditText mEtMaxBps;
    private XEditText mEtInitBps;
    private RadioGroup mRgPix;
    private RadioGroup mRgNetwokr;
    private LiveData mLiveData = new LiveData();
    private SeekBar mSeekBar;

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
        mSeekBar = rootView.findViewById(R.id.seekBar);

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

        mEtFps.setMaxmumFilter(15, 60, 0);
        mEtInitBps.setMaxmumFilter(600,2000,0);
        mEtMinBps.setMaxmumFilter(0, 800, 0);
        mEtMaxBps.setMaxmumFilter(800, 2000, 0);

        mSeekBar.setProgress(mLiveData.beautyFlag);
        mEtFps.setText(mLiveData.Fps);
        mEtMinBps.setText(mLiveData.minBps);
        mEtMaxBps.setText(mLiveData.maxBps);
        mEtInitBps.setText(mLiveData.initBps);
        initResolution();
        initNetWork();
    }

    private void initNetWork() {
        if ("0".equals(mLiveData.netFlag)) {
            mRgNetwokr.check(R.id.rb_dev);
        } else if ("1".equals(mLiveData.netFlag)) {
            mRgNetwokr.check(R.id.rb_test);
        }
    }

    private void initResolution() {
        switch (mLiveData.resolution) {
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
        switch (checkedId) {
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
        switch (checkedId) {
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
        String maxBps = mEtMaxBps.getText().toString().trim();
        int tempMaxBps = 0;
        if (!TextUtils.isEmpty(maxBps)) {
             tempMaxBps = Integer.parseInt(maxBps);
            if (tempMaxBps >= 800 && tempMaxBps <= 200) {
                mLiveData.maxBps = maxBps;
            }
        }
        String minBps = mEtMinBps.getText().toString().trim();
        int tempMinBps = 0;
        if (!TextUtils.isEmpty(minBps)) {
             tempMinBps = Integer.parseInt(minBps);
            if (tempMinBps >= 0 && tempMinBps <= 800) {
                mLiveData.minBps = minBps;
            }
        }
        String initBps = mEtInitBps.getText().toString().trim();
        if (!TextUtils.isEmpty(initBps)) {
            int tempInitBps = Integer.parseInt(initBps);
            if (tempInitBps >= tempMinBps && tempInitBps <= tempMaxBps) {
                mLiveData.initBps = initBps;
            }
        }
        String fps = mEtFps.getText().toString().trim();
        if (!TextUtils.isEmpty(fps)) {
            int temFps = Integer.parseInt(fps);
            if (temFps >= 15 && temFps <= 60) {
                mLiveData.Fps = fps;
            }
        }
        mLiveData.beautyFlag = mSeekBar.getProgress();
        ((StartActivity) getActivity()).setLiveData(mLiveData);
    }

    public void setLiveData(LiveData liveData) {
        mLiveData.netFlag = liveData.netFlag;
        mLiveData.resolution = liveData.resolution;
        mLiveData.Fps = liveData.Fps;
        mLiveData.initBps = liveData.initBps;
        mLiveData.minBps = liveData.minBps;
        mLiveData.maxBps = liveData.maxBps;
        mLiveData.beautyFlag = liveData.beautyFlag;


    }
}
