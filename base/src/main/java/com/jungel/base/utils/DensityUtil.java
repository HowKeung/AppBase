package com.jungel.base.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.jungel.base.R;

/**
 * Created by Aspsine on 2015/11/5.
 */
public class DensityUtil {
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * dp转换成px
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转换成dp
     */
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转换成px
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * sp转换成px
     */
    public static int sp2dp(Context context, float spValue) {
        int pxValue = sp2px(context, spValue);
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转换成sp
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
            //LogUtils.d("getStatusBarHeight : " + result);
        }
        return result;
    }

    public static int getTitleHeight(Context context) {
        int result = context.getResources().getDimensionPixelSize(R.dimen
                .size_primary_toolbar_height);
        return result;
    }

    public static int getMainTabHeight(Context context) {
        int result = context.getResources().getDimensionPixelSize(R.dimen
                .size_primary_toolbar_height);
        return result;
    }
}
