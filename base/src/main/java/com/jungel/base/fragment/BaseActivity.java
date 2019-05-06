package com.jungel.base.fragment;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jungel.base.utils.ExEventBus;
import com.jungel.base.widget.CToast;
import com.jungel.base.window.LoadingWindow;

import org.greenrobot.eventbus.Subscribe;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by lion on 2017/4/10.
 */

public abstract class BaseActivity<VDB extends ViewDataBinding> extends SupportActivity {

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private LoadingWindow mLoading;
    protected VDB mDataBinding;
    private static final int INVALID_VAL = -1;//顶部颜色默认值

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (!ExEventBus.getDefault().getDefaultEventBus().isRegistered(this)) {
            ExEventBus.getDefault().getDefaultEventBus().register(this);
        }
        //这一行注意！看本文最后的说明！！！！
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        mDataBinding = DataBindingUtil.setContentView(this, getLayoutRes());

        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(false);
        }

        onBind(mDataBinding);
    }

    @LayoutRes
    public abstract int getLayoutRes();

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
                    mLoading = new LoadingWindow(BaseActivity.this);
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
        //        Toast.makeText(BaseActivity.this, content, duration).show();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

    }

    @Override
    protected void onDestroy() {
        if (ExEventBus.getDefault().getDefaultEventBus().isRegistered(this)) {
            ExEventBus.getDefault().getDefaultEventBus().unregister(this);
        }
        super.onDestroy();
    }
}
