package com.example.livepushclient;

import android.os.Bundle;

import com.gyf.immersionbar.ImmersionBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by liule on 2020/5/28.
 * ----2020/5/28-------liule----xxxxxxxxx--
 */
public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImmersionBar.with(this).init();
    }


}
