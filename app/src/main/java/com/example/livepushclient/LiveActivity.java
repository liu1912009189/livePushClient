package com.example.livepushclient;

import android.os.Bundle;
import android.widget.Toast;

import com.example.livepushclient.presenter.LivePresenter;
import com.example.livepushclient.presenter.ViewUpateInterface;
import com.example.livepushclient.util.HttpUtils;
import com.laifeng.sopcastsdk.camera.CameraListener;
import com.laifeng.sopcastsdk.configuration.AudioConfiguration;
import com.laifeng.sopcastsdk.configuration.CameraConfiguration;
import com.laifeng.sopcastsdk.configuration.VideoConfiguration;
import com.laifeng.sopcastsdk.stream.packer.rtmp.RtmpPacker;
import com.laifeng.sopcastsdk.stream.sender.rtmp.RtmpSender;
import com.laifeng.sopcastsdk.ui.CameraLivingView;
import com.laifeng.sopcastsdk.utils.SopCastLog;
import com.laifeng.sopcastsdk.video.effect.Beauty;
import com.laifeng.sopcastsdk.video.effect.LookupFilter;


public class LiveActivity extends BaseActivity implements ViewUpateInterface {
    private static final String TAG = LiveActivity.class.getSimpleName();
    private CameraLivingView mLiveCameraView;
    private String rtmpUrl = "";
    private LiveUI mLiveUI;
    private LivePresenter mLivePresenter;
    private VideoConfiguration mVideoConfiguration;
    private RtmpSender mRtmpSender;
    private int mCurrentBps;
    /**
     * 当前直播间状态是否为正在播,即是否手动点击开播且成功
     */
    private boolean isRecording;
    /**
     * 直播状态是否开启，仅当第一次点击开启直播且成功时，直播开启成功
     * 只有点击下播时状态才为下播状态
     */
    private boolean liveStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        StatusBarUtils.setTranslucentStatus(this);
        rtmpUrl = getIntent().getStringExtra("url");
//        rtmpUrl = "rtmp://rtmp.jiangshi99.com/jiang/1?auth_key=1588844080-0-0-5192cc6cb11c580a8d47c0643fca8048";
//        rtmpUrl = "rtmp://112.17.52.56:1935/ios/dianqu1234";
//        rtmpUrl = "rtmp://96357.livepush.myqcloud.com/live/test?txSecret=4fbe6168eb46c8b47e08101692608790&txTime=5EC6A57F";
        initLiveView();
        mLiveUI = new LiveUI(this, mLiveCameraView, rtmpUrl);
        mLivePresenter = new LivePresenter(this, rtmpUrl);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLiveCameraView.pause();
        mLivePresenter.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLiveCameraView.resume();
        mLivePresenter.onStart();
    }

    public boolean isRecording() {
        return isRecording;
    }


    public boolean isLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(boolean liveStatus) {
        this.liveStatus = liveStatus;
    }

    private void initLiveView() {
        mLiveCameraView = findViewById(R.id.liveView);
        SopCastLog.isOpen(true);
        mLiveCameraView.init();
        CameraConfiguration.Builder cameraBuilder = new CameraConfiguration.Builder();
        cameraBuilder.setOrientation(CameraConfiguration.Orientation.PORTRAIT)
                .setFacing(CameraConfiguration.Facing.BACK);
        CameraConfiguration cameraConfiguration = cameraBuilder.build();
        mLiveCameraView.setCameraConfiguration(cameraConfiguration);

        VideoConfiguration.Builder videoBuilder = new VideoConfiguration.Builder();
        videoBuilder.setSize(640, 360);
        mVideoConfiguration = videoBuilder.build();
        mLiveCameraView.setVideoConfiguration(mVideoConfiguration);

        Beauty beauty = new Beauty(getApplicationContext().getResources());
        LookupFilter lookupFilter = new LookupFilter(getApplicationContext().getResources());
        lookupFilter.setMaskImage("lookup/purity.png");
        beauty.setFlag(6);
        lookupFilter.setIntensity(1f);
        mLiveCameraView.addFilter(lookupFilter);
        mLiveCameraView.addFilter(beauty);
        mLiveCameraView.complete();

        //设置水印
//        Bitmap watermarkImg = BitmapFactory.decodeResource(getResources(), R.mipmap.watermark);
//        Watermark watermark = new Watermark(watermarkImg, 50, 25, WatermarkPosition.WATERMARK_ORIENTATION_BOTTOM_RIGHT, 8, 8);
//        mLiveCameraView.setWatermark(watermark);

        //设置预览监听
        mLiveCameraView.setCameraOpenListener(new CameraListener() {
            @Override
            public void onOpenSuccess() {
                Toast.makeText(LiveActivity.this, "camera open success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onOpenFail(int error) {
                Toast.makeText(LiveActivity.this, "camera open fail", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCameraChange() {
                Toast.makeText(LiveActivity.this, "camera switch", Toast.LENGTH_LONG).show();
            }
        });

        //初始化flv打包器
        RtmpPacker packer = new RtmpPacker();
        packer.initAudioParams(AudioConfiguration.DEFAULT_FREQUENCY, 16, false);
        mLiveCameraView.setPacker(packer);
        //设置发送器
        mRtmpSender = new RtmpSender();
        mRtmpSender.setVideoParams(640, 360);
        mRtmpSender.setAudioParams(AudioConfiguration.DEFAULT_FREQUENCY, 16, false);
        mRtmpSender.setSenderListener(mSenderListener);
        mLiveCameraView.setSender(mRtmpSender);
        mLiveCameraView.setLivingStartListener(new CameraLivingView.LivingStartListener() {
            @Override
            public void startError(int error) {
                //直播失败
                Toast.makeText(LiveActivity.this, "start living fail", Toast.LENGTH_SHORT).show();
                mLiveCameraView.stop();
            }

            @Override
            public void startSuccess() {
                //直播成功
            }
        });
    }

    private RtmpSender.OnSenderListener mSenderListener = new RtmpSender.OnSenderListener() {
        @Override
        public void onConnecting() {

        }

        @Override
        public void onConnected() {
//            mLiveCameraView.start();
            mCurrentBps = mVideoConfiguration.maxBps;
            liveStatus = true;
            mLiveUI.setRoomStatus(true);
            HttpUtils.changeLiveroomState(rtmpUrl, "1", null);
            mLivePresenter.setStartStatus(true);
            mLiveCameraView.start();
        }

        @Override
        public void onDisConnected() {
            mLiveCameraView.stop();
            isRecording = false;
            mLiveUI.setRoomStatus(false);
            Toast.makeText(getApplicationContext(), R.string.network_error_retry, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onPublishFail() {
            isRecording = false;
            Toast.makeText(LiveActivity.this, "fail to publish stream", Toast.LENGTH_SHORT).show();
            mLiveUI.setRoomStatus(false);
        }

        @Override
        public void onNetGood() {
            if (mCurrentBps + 50 <= mVideoConfiguration.maxBps) {
                SopCastLog.d(TAG, "BPS_CHANGE good up 50");
                int bps = mCurrentBps + 50;
                if (mLiveCameraView != null) {
                    boolean result = mLiveCameraView.setVideoBps(bps);
                    if (result) {
                        mCurrentBps = bps;
                    }
                }
            } else {
                SopCastLog.d(TAG, "BPS_CHANGE good good good");
            }
            SopCastLog.d(TAG, "Current Bps: " + mCurrentBps);
        }

        @Override
        public void onNetBad() {
            if (mCurrentBps - 100 >= mVideoConfiguration.minBps) {
                SopCastLog.d(TAG, "BPS_CHANGE bad down 100");
                int bps = mCurrentBps - 100;
                if (mLiveCameraView != null) {
                    boolean result = mLiveCameraView.setVideoBps(bps);
                    if (result) {
                        mCurrentBps = bps;
                    }
                }
            } else {
                SopCastLog.d(TAG, "BPS_CHANGE bad down 100");
            }
            SopCastLog.d(TAG, "Current Bps: " + mCurrentBps);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLiveCameraView.stop();
        mLiveCameraView.release();
        mLivePresenter.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mLiveUI.setExit(true);
        if (liveStatus) {
            liveStatus = false;
            stopStreaming(true);
        } else {
            finish();
        }
    }

    @Override
    public void setRoomState(boolean open) {
        mLiveUI.setRoomStatus(open);
    }


    @Override
    public void startStream() {
        if (!isRecording) {
            mRtmpSender.setAddress(rtmpUrl);
            mRtmpSender.connect();
            isRecording = true;
        }
    }

    @Override
    public boolean isExitClicked() {
        return mLiveUI.isExit();
    }

    public void startLive() {
        mLivePresenter.startLive();
    }

    public void stopStreaming(boolean isExit) {
        mLiveCameraView.pause();
        closeRoom(isExit);
        isRecording = false;
    }

    public void closeRoom(boolean isExit){
        mLiveUI.setRoomStatus(false);
        mLivePresenter.closeLiveRoom(isExit);
    }

    public void onUserComeBack() {
        mLiveCameraView.resume();
        isRecording = true;
        openRoom();
    }

    public void openRoom() {
        mLiveUI.setRoomStatus(true);
        HttpUtils.changeLiveroomState(rtmpUrl, "1", null);
    }
}
