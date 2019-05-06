package com.jungel.base.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Toast;

import com.jungel.base.R;
import com.jungel.base.activity.BaseApplication;
import com.jungel.base.databinding.LayoutCommonToastBinding;
import com.jungel.base.databinding.LayoutCommonToastWithIconBinding;
import com.jungel.base.utils.CLgUtil;

/**
 * Created by lion on 2017/4/20.
 */

public class CToast {

    private static Handler handler = new Handler(Looper.getMainLooper());

    public static Toast makeIconAnText(Context context, String content, int duration) {
        return makeIconAnText(context, R.mipmap.ic_launcher, content, duration);
    }

    public static Toast makeIconAnText(Context context, @DrawableRes int iconRes,
                                       String content, int duration) {
        return makeIconAnText(context, iconRes, R.drawable.bg_common_toast, content, duration);
    }

    public static Toast makeIconAnText(Context context, @DrawableRes int iconRes,
                                       @DrawableRes int bgRes, String content, int duration) {
        LayoutCommonToastWithIconBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_common_toast_with_icon, null, false);
        dataBinding.textContent.setText(content);
        dataBinding.layoutBg.setBackgroundResource(bgRes);
        dataBinding.imgIcon.setImageResource(iconRes);
        //Toast的初始化
        Toast toast = new Toast(context);
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toast.setGravity(Gravity.BOTTOM, 0, height / 5);
        toast.setDuration(duration);
        toast.setView(dataBinding.getRoot());
        return toast;
    }

    public static Toast makeText(Context context, String content, int duration) {
        return makeText(context, content, R.drawable.bg_common_toast, duration);
    }

    public static Toast makeText(Context context, String content,
                                 @DrawableRes int bgRes, int duration) {
        try {
            LayoutCommonToastBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                    R.layout.layout_common_toast, null, false);
            dataBinding.textContent.setText(content);
            dataBinding.layoutBg.setBackgroundResource(bgRes);
            //Toast的初始化
            Toast toast = new Toast(context);
            //获取屏幕高度
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();
            //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
            toast.setGravity(Gravity.BOTTOM, 0, height / 5
            );
            toast.setDuration(duration);
            toast.setView(dataBinding.getRoot());
            return toast;
        } catch (Exception e) {
            CLgUtil.e(e.toString());
        }
        return null;
    }

    public static void toast(String content, int duration) {
        Toast toast = makeText(BaseApplication.getContext(), content, duration);
        if (toast != null) {
            toast.show();
        }
    }

    public static void toast(final String content) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                toast(content, Toast.LENGTH_SHORT);
            }
        });
    }

    public static void toast(final int content) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                toast(BaseApplication.getContext().getResources()
                        .getString(content), Toast.LENGTH_SHORT);
            }
        });
    }
}
