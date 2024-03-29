package com.laifeng.sopcastsdk.video;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Looper;

import com.laifeng.sopcastsdk.camera.CameraHolder;
import com.laifeng.sopcastsdk.camera.CameraListener;
import com.laifeng.sopcastsdk.camera.CameraUtils;
import com.laifeng.sopcastsdk.camera.exception.CameraDisabledException;
import com.laifeng.sopcastsdk.camera.exception.CameraHardwareException;
import com.laifeng.sopcastsdk.camera.exception.CameraNotSupportException;
import com.laifeng.sopcastsdk.camera.exception.NoCameraException;
import com.laifeng.sopcastsdk.configuration.VideoConfiguration;
import com.laifeng.sopcastsdk.entity.Watermark;
import com.laifeng.sopcastsdk.mediacodec.VideoMediaCodec;
import com.laifeng.sopcastsdk.utils.WeakHandler;
import com.laifeng.sopcastsdk.video.effect.AFilter;
import com.laifeng.sopcastsdk.video.effect.Effect;
import com.laifeng.sopcastsdk.video.effect.GrayEffect;
import com.laifeng.sopcastsdk.video.effect.GroupFilter;
import com.laifeng.sopcastsdk.video.effect.NullEffect;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Title: MyRenderer
 * @Package com.laifeng.sopcastsdk.video
 * @Description:
 * @Author Jim
 * @Date 16/9/14
 * @Time 下午2:06
 * @Version
 */
@TargetApi(18)
public class MyRenderer implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener{
    private int mSurfaceTextureId = -1;
    private SurfaceTexture mSurfaceTexture;
    private Watermark mWatermark;
    private RenderScreen mRenderScreen;
    private RenderSrfTex mRenderSrfTex;

    private CameraListener mCameraOpenListener;
    private WeakHandler mHandler = new WeakHandler(Looper.getMainLooper());
    private GLSurfaceView mView;
    private boolean isCameraOpen;
    /**
     * 相机渲染显示效果
     */
    private AFilter mOesEffect;
    /**
     * 中间滤镜特效
     */
    private GroupFilter mGroupFilter;
    private int mEffectTextureId;
    private VideoConfiguration mVideoConfiguration;

    private boolean updateSurface = false;
    private final float[] mTexMtx = GlUtil.createIdentityMtx();

    private int mVideoWidth;
    private int mVideoHeight;
    private int mOutputGroupTextureId;

    public MyRenderer(GLSurfaceView view) {
        mView = view;
        mOesEffect = new NullEffect(mView.getContext());
        mGroupFilter = new GroupFilter(mView.getResources());
    }

    public void setCameraOpenListener(CameraListener cameraOpenListener) {
        this.mCameraOpenListener = cameraOpenListener;
    }

    public void setVideoConfiguration(VideoConfiguration videoConfiguration) {
        mVideoConfiguration = videoConfiguration;
        mVideoWidth = VideoMediaCodec.getVideoSize(mVideoConfiguration.width);
        mVideoHeight = VideoMediaCodec.getVideoSize(mVideoConfiguration.height);
        if(mRenderScreen != null) {
            mRenderScreen.setVideoSize(mVideoWidth, mVideoHeight);
        }
    }

    public void setRecorder(MyRecorder recorder) {
        synchronized(this) {
            if (recorder != null) {
                mRenderSrfTex = new RenderSrfTex(mOutputGroupTextureId, recorder);
                mRenderSrfTex.setVideoSize(mVideoWidth, mVideoHeight);
                if(mWatermark != null) {
                    mRenderSrfTex.setWatermark(mWatermark);
                }
            } else {
                mRenderSrfTex = null;
            }
        }
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized(this) {
            updateSurface = true;
        }
        mView.requestRender();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        initSurfaceTexture();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        startCameraPreview();
        if(isCameraOpen) {

            if (mRenderScreen == null) {
                initScreenTexture();
            }
            mRenderScreen.setSreenSize(width, height);
            if (mVideoConfiguration != null) {
                mRenderScreen.setVideoSize(mVideoWidth, mVideoHeight);
            }
            if (mWatermark != null) {
                mRenderScreen.setWatermark(mWatermark);
            }
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized(this) {
            if (updateSurface) {
                mSurfaceTexture.updateTexImage();
                mSurfaceTexture.getTransformMatrix(mTexMtx);
                updateSurface = false;
            }
        }
        mOesEffect.setMatrix(mTexMtx);
        mOesEffect.draw();
        mGroupFilter.draw();
        if(mRenderScreen != null) {
            mRenderScreen.draw();
        }
        if (mRenderSrfTex != null) {
            mRenderSrfTex.draw();
        }
    }

    private void initSurfaceTexture() {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        mSurfaceTextureId = textures[0];
        mSurfaceTexture = new SurfaceTexture(mSurfaceTextureId);
        mSurfaceTexture.setOnFrameAvailableListener(this);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mSurfaceTextureId);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
    }

    private void initScreenTexture() {
        mOesEffect.setTextureId(mSurfaceTextureId);
        mOesEffect.prepare();
        mEffectTextureId = mOesEffect.getOutputTextureId();
        mGroupFilter.setTextureId(mEffectTextureId);
        mGroupFilter.prepare();
        mOutputGroupTextureId = mGroupFilter.getOutputTextureId();
        mRenderScreen = new RenderScreen(mOutputGroupTextureId);
    }

    private void startCameraPreview() {
        try {
            CameraUtils.checkCameraService(mView.getContext());
        } catch (CameraDisabledException e) {
            postOpenCameraError(CameraListener.CAMERA_DISABLED);
            e.printStackTrace();
            return;
        } catch (NoCameraException e) {
            postOpenCameraError(CameraListener.NO_CAMERA);
            e.printStackTrace();
            return;
        }
        CameraHolder.State state = CameraHolder.instance().getState();
        CameraHolder.instance().setSurfaceTexture(mSurfaceTexture);
        if (state != CameraHolder.State.PREVIEW) {
            try {
                CameraHolder.instance().openCamera();
                CameraHolder.instance().startPreview();
                if(mCameraOpenListener != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCameraOpenListener.onOpenSuccess();
                        }
                    });
                }
                isCameraOpen = true;
            } catch (CameraHardwareException e) {
                e.printStackTrace();
                postOpenCameraError(CameraListener.CAMERA_OPEN_FAILED);
            } catch (CameraNotSupportException e) {
                e.printStackTrace();
                postOpenCameraError(CameraListener.CAMERA_NOT_SUPPORT);
            }
        }
    }

    private void postOpenCameraError(final int error) {
        if(mCameraOpenListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mCameraOpenListener != null) {
                        mCameraOpenListener.onOpenFail(error);
                    }
                }
            });
        }
    }

    public boolean isCameraOpen() {
        return isCameraOpen;
    }

    public void setWatermark(Watermark watermark) {
        mWatermark = watermark;
        if(mRenderScreen != null) {
            mRenderScreen.setWatermark(watermark);
        }
        if(mRenderSrfTex != null) {
            mRenderSrfTex.setWatermark(watermark);
        }
    }


    /**
     * 此方法只是对相机做渲染处理，保证相机数据能渲染在屏幕上，具体参见{@link GrayEffect}和{@link NullEffect}实现
     * 如果想实现更多特效处理，推荐使用{@link GroupFilter}实现
     * @param effect
     */
    @Deprecated
    public void setEffect(Effect effect) {
        mOesEffect.release();
        mOesEffect = effect;
        effect.setTextureId(mSurfaceTextureId);
        effect.prepare();
        mEffectTextureId = effect.getOutputTextureId();
        mGroupFilter.setTextureId(mEffectTextureId);
        mGroupFilter.prepare();
        mOutputGroupTextureId = mGroupFilter.getOutputTextureId();
        if(mRenderScreen != null) {
            mRenderScreen.setTextureId(mOutputGroupTextureId);
        }
        if(mRenderSrfTex != null) {
            mRenderSrfTex.setTextureId(mOutputGroupTextureId);
        }
    }

    /**
     * 增加滤镜配合{@link #complete()}使用
     * @param filter 滤镜
     */
    public void addFilter(AFilter filter){
        mGroupFilter.addFilter(filter);
    }

    /**
     * 移除滤镜配合{@link #complete()}使用
     * @param filter
     */
    public void removeFilter(AFilter filter){
        mGroupFilter.removeFilter(filter);
    }

    /**
     * 移除所有滤镜配合{@link #complete()}使用
     */
    public void removeAllFilter(){
        mGroupFilter.clearAll();
    }

    /**
     * 添加或移除滤镜后请务必调用此函数，否则操作不生效
     */
    public void complete(){
        mGroupFilter.setTextureId(mEffectTextureId);
        mGroupFilter.prepare();
        mOutputGroupTextureId = mGroupFilter.getOutputTextureId();
        if(mRenderScreen != null) {
            mRenderScreen.setTextureId(mOutputGroupTextureId);
        }
        if(mRenderSrfTex != null) {
            mRenderSrfTex.setTextureId(mOutputGroupTextureId);
        }
    }

}
