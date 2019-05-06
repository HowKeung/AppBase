package com.jungel.base.activity;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDexApplication;

import com.jungel.base.utils.GlideCacheUtil;

import me.yokeyword.fragmentation.Fragmentation;

/**
 * Created by lion on 2017/3/14.
 */

public class BaseApplication extends MultiDexApplication {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        context = base;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fragmentation.builder()
                // show stack view. Mode: BUBBLE, SHAKE, NONE
                .stackViewMode(Fragmentation.NONE)
                .debug(false)
                .install();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        try {
            if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) { // 60
                GlideCacheUtil.getInstance().clearImageMemoryCache(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try {
            GlideCacheUtil.getInstance().clearImageMemoryCache(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
