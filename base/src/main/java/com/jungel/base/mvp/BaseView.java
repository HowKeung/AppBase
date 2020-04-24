package com.jungel.base.mvp;

/**
 * Created by lion on 2017/3/14.
 * MVP base view
 */

public interface BaseView {

    void onMessage(String message);

    void dismissLoading();

    void onMessage(int resId);
}
