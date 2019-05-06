package com.jungel.base.bean;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Created by lion on 2017/7/28.
 */

public class CommonTabData implements CustomTabEntity {

    private String mTitle;

    public CommonTabData(String title) {
        this.mTitle = title;
    }

    @Override
    public String getTabTitle() {
        return mTitle;
    }

    @Override
    public int getTabSelectedIcon() {
        return 0;
    }

    @Override
    public int getTabUnselectedIcon() {
        return 0;
    }
}
