package com.example.livepushclient.presenter;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 预览画面的presenter
 * @author CainHuang
 * @date 2019/7/3
 */
public abstract class BasePresenter<T extends AppCompatActivity> extends IPresenter<T> {

    BasePresenter(T target) {
        super(target);
    }

}
