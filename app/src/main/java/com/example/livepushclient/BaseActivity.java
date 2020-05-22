package com.example.livepushclient;

import android.os.Bundle;
import android.widget.Toast;

import com.example.livepushclient.app.AppManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by liule on 2020/5/14.
 * ----2020/5/14-------liule----xxxxxxxxx--
 */
public class BaseActivity extends AppCompatActivity {

    private boolean enableDoubleClickExitApplication;
    private int doubleClickSpacingInterval = 2 * 1000; //双击退出程序的间隔时间
    private static long lastClickBackButtonTime; //记录上次点击返回按钮的时间，用来配合实现双击返回按钮退出应用程序的功能

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    public void onBackPressed() {
        if (enableDoubleClickExitApplication) {
            long currentMillisTime = System.currentTimeMillis();
            //两次点击的间隔时间尚未超过规定的间隔时间将执行退出程序
            if (lastClickBackButtonTime != 0
                    && (currentMillisTime - lastClickBackButtonTime) < doubleClickSpacingInterval) {
                AppManager.getAppManager().finishAllActivity();
            } else {
                onPromptExitApplication();
                lastClickBackButtonTime = currentMillisTime;
            }
        } else {
            finish();
        }
    }

    protected void onPromptExitApplication() {
        Toast.makeText(getApplicationContext(),"再按一次退出程序！",Toast.LENGTH_SHORT).show();
    }

    public void setEnableDoubleClickExitApplication(){
        enableDoubleClickExitApplication = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
