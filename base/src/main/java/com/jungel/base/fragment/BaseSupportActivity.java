package com.jungel.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jungel.base.R;
import com.jungel.base.boardcast.NetBroadcastReceiver;
import com.jungel.base.utils.AppUtil;
import com.jungel.base.utils.ExEventBus;
import com.jungel.base.utils.LogUtils;
import com.jungel.base.widget.CToast;
import com.jungel.base.window.LoadingWindow;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by lion on 2017/4/10.
 */

public abstract class BaseSupportActivity<VDB extends ViewDataBinding> extends SupportActivity
        implements View.OnClickListener {

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private LoadingWindow mLoading;
    protected VDB mDataBinding;
    private static final int INVALID_VAL = -1;//顶部颜色默认值
    private SystemBarTintManager tintManager;//状态栏管理器
    private boolean isResume = false;//页面是否恢复
    private long mLastBackTime = 0;
    private int mLastBackTimes = 0;
    private NetBroadcastReceiver mNetBroadcastReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        hideStatusBarShadow();
        /**
         * Only fullscreen activities can request orientation终极解决方法
         */
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
        }
        //initLightStatusBar();
        super.onCreate(savedInstanceState);
        initAnimation();
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (!ExEventBus.getDefault().getDefaultEventBus().isRegistered(this)) {
            ExEventBus.getDefault().getDefaultEventBus().register(this);
        }
        registerBroadcast();
        //这一行注意！看本文最后的说明！！！！
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        mDataBinding = DataBindingUtil.setContentView(this, getLayoutRes());

        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(false);
        }
        if (savedInstanceState == null) {
            onBind(mDataBinding);
        }
    }

    private void registerBroadcast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            mNetBroadcastReceiver = new NetBroadcastReceiver();
            //注册广播接收
            registerReceiver(mNetBroadcastReceiver, filter);
        }
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    protected abstract void onBind(VDB dataBinding);


    public void updateUI(Runnable runnable) {
        if (runnable != null) {
            mHandler.post(runnable);
        }
    }

    public void updateUI(Runnable runnable, long delay) {
        if (runnable != null) {
            mHandler.postDelayed(runnable, delay);
        }
    }

    public void setTextContent(TextView textView, Object data) {
        if (data instanceof String
                || data instanceof CharSequence) {
            textView.setText((CharSequence) data);
        } else if (data instanceof Number) {
            textView.setText(data.toString());
        }
    }

    protected void showLoading(final View rootView) {
        updateUI(new Runnable() {
            @Override
            public void run() {
                if (mLoading == null) {
                    mLoading = new LoadingWindow(BaseSupportActivity.this);
                }
                mLoading.show(rootView);
            }
        });
    }

    protected void dismissLoading() {
        updateUI(new Runnable() {
            @Override
            public void run() {
                if (mLoading != null) {
                    mLoading.dismiss();
                }
            }
        });
    }

    public void toast(final String content) {
        updateUI(new Runnable() {
            @Override
            public void run() {
                toast(content, Toast.LENGTH_SHORT);
            }
        });
    }

    public void toast(String content, int duration) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        //        Toast.makeText(BaseSupportActivity.this, content, duration).show();
        Toast toast = CToast.makeText(this, content, duration);
        if (toast != null) {
            toast.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onMessage(String message) {
        toast(message);
    }

    public void onMessage(int resId) {
        onMessage(getString(resId));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = true;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode != KeyEvent.KEYCODE_MENU && super.onKeyDown(keyCode, event);
    }

    @Subscribe
    public void onEvent(ExEventBus.MessageEvent event) {
        switch (event.getType()) {
            case ExEventBus.MessageEvent.EVENT_TYPE_CHANGE_LANGUAGE:
                recreate();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (ExEventBus.getDefault().getDefaultEventBus().isRegistered(this)) {
            ExEventBus.getDefault().getDefaultEventBus().unregister(this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            unregisterReceiver(mNetBroadcastReceiver);
        }
        super.onDestroy();
    }

    /**
     * 设置沉浸式状态栏
     *
     * @param res 背景资源
     */
    public void setStatusBar(int res) {
        if (tintManager == null)
            tintManager = new SystemBarTintManager(this);
        // 激活状态栏
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint 激活导航栏
        //        tintManager.setNavigationBarTintEnabled(true);
        //设置系统栏设置颜色
        //tintManager.setTintColor(R.color.red);
        //给状态栏设置颜色
        if (res != INVALID_VAL) {
            tintManager.setStatusBarTintResource(res);
            //Apply the specified drawable or color resource to the system navigation bar.
            //给导航栏设置资源
            //            tintManager.setNavigationBarTintResource(res);
        }
    }

    public boolean isResume() {
        return isResume;
    }

    protected void registerListener() {
        if (!ExEventBus.getDefault().getDefaultEventBus().isRegistered(this)) {
            ExEventBus.getDefault().getDefaultEventBus().register(this);
        }
    }

    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
            LogUtils.d("getStatusBarHeight : " + result);
        }
        return result;
    }

    protected void initLightStatusBar() {
        if (AppUtil.isXiaomi()) {
            AppUtil.MIUISetStatusBarLightMode(this, true);
        } else if (AppUtil.isMeizu()) {
            AppUtil.FlymeSetStatusBarLightMode(getWindow(), true);
        } else {
            //                LogUtils.d("setUserVisibleHint -> HDAssetFragment");
            //            setStatusBar(R.color.color_primary_background);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                                .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    protected void exitApp() {
        mLastBackTimes++;
        long current = new Date().getTime();

        if (current - mLastBackTime > 1000) {
            mLastBackTimes = 1;
        }

        if (mLastBackTimes == 1) {
            onMessage(getString(R.string.exit_tips));
        } else {
            if (current - mLastBackTime <= 1000) {
                finish();
            } else {
                mLastBackTimes = 0;
            }
        }
        mLastBackTime = current;
    }

    protected void initAnimation() {
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void initStatusBarSpace(ViewGroup layoutBase, int color) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.height = getStatusBarHeight();
        View view = new View(this);
        view.setLayoutParams(params);
        view.setBackgroundColor(color);
        layoutBase.addView(view, 0);
    }

    protected void initStatusBarSpace(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = getStatusBarHeight();
        view.setLayoutParams(params);
    }

    protected void setStatusBarColor(View viewSpace, int color) {
        viewSpace.setBackgroundColor(color);
    }

    protected void initBlackStatusBar() {
        if (AppUtil.isXiaomi()) {
            AppUtil.MIUISetStatusBarLightMode(this, false);
        } else if (AppUtil.isMeizu()) {
            AppUtil.FlymeSetStatusBarLightMode(this.getWindow(), false);
        } else {
            //            LogUtils.d("setUserVisibleHint -> BaseVBDFragment");
            //            setStatusBar(R.drawable.bg_me_gradient_toolbar);
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    protected int getActionBarHeight() {
        int result = 0;
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true);
        if (typedValue.resourceId > 0) {
            result = getResources().getDimensionPixelSize(typedValue.resourceId);
            //LogUtils.d("getTitleHeight : " + result);
        }
        return result;
    }

    protected int getTitleHeight() {
        int result = getResources().getDimensionPixelSize(R.dimen
                .size_primary_toolbar_height);
        return result;
    }

    protected int getMainTabHeight() {
        int result = getResources().getDimensionPixelSize(R.dimen
                .size_primary_toolbar_height);
        return result;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getTopFragment() != null && getTopFragment() instanceof Fragment) {
            ((Fragment) getTopFragment()).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (getTopFragment() != null && getTopFragment() instanceof Fragment) {
            ((Fragment) getTopFragment()).onRequestPermissionsResult(requestCode, permissions,
                    grantResults);
        }
    }

    /**
     * Only fullscreen activities can request orientation终极解决方法
     *
     * @return
     */
    // Only fullscreen opaque activities can request orientation 修改
    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable")
                    .getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    /**
     * Only fullscreen activities can request orientation终极解决方法
     *
     * @return
     */
    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Only fullscreen activities can request orientation终极解决方法
     *
     * @param requestedOrientation
     */
    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            return;
        }
        super.setRequestedOrientation(requestedOrientation);
    }

    protected void setSystemUIVisible(boolean show) {
        if (show) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            //隐藏虚拟按键，并且全屏
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
                View v = getWindow().getDecorView();
                v.setSystemUiVisibility(View.GONE);
            } else if (Build.VERSION.SDK_INT >= 19) {
                //for new api versions.
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View
                .OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                setSystemUIVisible(false);
            }
        });
    }

    private void hideStatusBarShadow() {
        if (AppUtil.isXiaomi()) {
            return;
        }
        if (AppUtil.isMeizu()) {
            return;
        }
        //去除灰色遮罩
        //Android5.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//Android4.4以上,5.0以下
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
