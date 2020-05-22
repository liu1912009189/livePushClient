package com.example.livepushclient;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.livepushclient.permission.DialogBuilder;
import com.example.livepushclient.permission.PermissionUtils;
import com.example.livepushclient.util.ViewClickUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by liule on 2020/4/30.
 * ----2020/4/30-------liule----xxxxxxxxx--
 */
public class StartActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CAMERA_CODE = 0;
    private static final int REQUEST_MICRO_CODE = 1;
    private Button mBtPermissionCamera;
    private Button mBtPermisssonMicro;
    private Button mBtEnterRoom;
    private boolean isCameraPermitted, isMicroPermitted;
    private EditText mEditText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        mBtEnterRoom = findViewById(R.id.button);
        mBtEnterRoom.setOnClickListener((v) -> {
            if(ViewClickUtils.isFastClick(v)){return;}
            Intent intent = new Intent(StartActivity.this, LiveActivity.class);
            intent.putExtra("url",mEditText.getText().toString().trim());
            startActivity(intent);
//            overridePendingTransition(R.anim.anim_slide_up, 0);
        });
        mEditText = ((EditText) findViewById(R.id.tv_url));
        mEditText.setText("rtmp://112.17.52.56:1935/ios/dianqu");
        mBtPermissionCamera = findViewById(R.id.bt_permission_camera);
        mBtPermisssonMicro = findViewById(R.id.bt_permission_micro);
        mBtPermissionCamera.setOnClickListener(this);
        mBtPermisssonMicro.setOnClickListener(this);

        if (PermissionUtils.permissionsChecking(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        })) {
            mBtEnterRoom.setEnabled(true);
            mBtEnterRoom.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_blue));
            mBtPermisssonMicro.setEnabled(false);
            mBtPermisssonMicro.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_gray_corner_dp25));
            mBtPermissionCamera.setEnabled(false);
            mBtPermissionCamera.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_gray_corner_dp25));
        }

    }

    private void checkPermissions() {
        PermissionUtils.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_CAMERA_CODE);
    }

    private void checkPermissions(String[] permissions, int requestCode) {
        PermissionUtils.requestPermissions(this, permissions, requestCode);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                isCameraPermitted = true;
                mBtPermissionCamera.setBackground(getDrawable(R.drawable.bg_gray_corner_dp25));
                mBtPermissionCamera.setEnabled(false);
            }else {
                boolean result = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA);
                if (result == false) {
                    DialogBuilder.from(this, R.layout.dialog_two_button)
                            .setText(R.id.tv_dialog_title, R.string.tips)
                            .setText(R.id.tv_dialog_message, R.string.camera_permission_tips)
                            .setDismissOnClick(R.id.btn_dialog_ok, true)
                            .setDismissOnClick(R.id.btn_dialog_cancel, true)
                            .setOnClickListener(R.id.btn_dialog_ok, (v) -> {
                                PermissionUtils.launchPermissionSettings(StartActivity.this);
                            })
                            .create()
                            .show();
                }
            }


        }

        if (requestCode == REQUEST_MICRO_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                isMicroPermitted = true;
                mBtPermisssonMicro.setBackground(getDrawable(R.drawable.bg_gray_corner_dp25));
                mBtPermisssonMicro.setEnabled(false);
            }else {
                boolean result = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO);
                if (result == false) {
                    DialogBuilder.from(this, R.layout.dialog_two_button)
                            .setText(R.id.tv_dialog_title, R.string.tips)
                            .setText(R.id.tv_dialog_message, R.string.micro_permission_tips)
                            .setDismissOnClick(R.id.btn_dialog_ok, true)
                            .setDismissOnClick(R.id.btn_dialog_cancel, true)
                            .setOnClickListener(R.id.btn_dialog_ok, (v) -> {
                                PermissionUtils.launchPermissionSettings(StartActivity.this);
                            })
                            .create()
                            .show();
                }
            }
        }

        if (PermissionUtils.permissionsChecking(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        })) {
            mBtEnterRoom.setBackground(getDrawable(R.drawable.bg_blue));
            mBtEnterRoom.setEnabled(true);
        }
    }


    @Override
    public void onClick(View v) {
        if(ViewClickUtils.isFastClick(v)){return;}
        switch (v.getId()) {
            case R.id.bt_permission_camera:
                PermissionUtils.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                }, REQUEST_CAMERA_CODE);
                break;
            case R.id.bt_permission_micro:
                PermissionUtils.requestPermissions(this, new String[]{
                        Manifest.permission.RECORD_AUDIO,
                }, REQUEST_MICRO_CODE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtils.permissionsChecking(this, new String[]{
                Manifest.permission.CAMERA,
        })) {
//
            isCameraPermitted = true;
            mBtPermissionCamera.setEnabled(false);
            mBtPermissionCamera.setBackground(getDrawable(R.drawable.bg_gray_corner_dp25));
        }
        if (PermissionUtils.permissionsChecking(this, new String[]{
                Manifest.permission.RECORD_AUDIO,
        })) {
            isMicroPermitted = true;
            mBtPermisssonMicro.setEnabled(false);
            mBtPermisssonMicro.setBackground(getDrawable(R.drawable.bg_gray_corner_dp25));
        }
        if(isCameraPermitted && isMicroPermitted) {
            mBtEnterRoom.setEnabled(true);
            mBtEnterRoom.setBackground(getDrawable(R.drawable.bg_blue));
        }
    }
}
