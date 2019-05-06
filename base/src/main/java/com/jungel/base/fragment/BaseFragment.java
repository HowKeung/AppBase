package com.jungel.base.fragment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jungel.base.R;
import com.jungel.base.utils.AppUtil;
import com.jungel.base.utils.ExEventBus;
import com.jungel.base.utils.LogUtils;
import com.jungel.base.widget.CToast;
import com.jungel.base.window.LoadingWindow;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.Subscribe;

public abstract class BaseFragment<VDB extends ViewDataBinding> extends Fragment
        implements View.OnClickListener {

    protected VDB mDataBinding;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private LoadingWindow mLoading;
    protected boolean isAlive = true;
    private static final int INVALID_VAL = -1;//顶部颜色默认值
    private SystemBarTintManager tintManager;//状态栏管理器
    private boolean mIsLoadingShowing = false;
    protected Activity _mActivity;

    @LayoutRes
    protected abstract int getLayoutRes();

    protected abstract void onBind();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        registerListener();
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        _mActivity = getActivity();
        onBind();
        return mDataBinding.getRoot();
    }

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

    @Override
    public void onDestroy() {
        if (ExEventBus.getDefault().getDefaultEventBus().isRegistered(this)) {
            ExEventBus.getDefault().getDefaultEventBus().unregister(this);
        }
        super.onDestroy();
    }

    public void onMessage(String message) {
        toast(message);
    }

    public void onMessage(int resId) {
        onMessage(getActivity().getString(resId));
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
        if (!TextUtils.isEmpty(content)) {
            //            Toast.makeText(getContext(), content, duration).show();
            Toast toast = CToast.makeText(getActivity(), content, duration);
            if (toast != null) {
                toast.show();
            }
        }
    }

    @Subscribe
    public void onEvent(ExEventBus.MessageEvent event) {

    }

    protected void showLoading(final View rootView) {
        updateUI(new Runnable() {
            @Override
            public void run() {
                if (mLoading == null) {
                    mLoading = new LoadingWindow(getActivity());
                }
                if (getActivity() != null && !mIsLoadingShowing) {
                    mLoading.show(rootView);
                    mIsLoadingShowing = true;
                }
            }
        });
    }

    public void dismissLoading() {
        dismissLoading(null);
    }

    public void dismissLoading(final OnLoadingDismissListener listener) {
        updateUI(new Runnable() {
            @Override
            public void run() {
                if (mLoading != null) {
                    mLoading.dismiss();
                    if (listener != null) {
                        listener.onDismiss();
                    }
                    mIsLoadingShowing = false;
                }
            }
        });
    }

    public interface OnLoadingDismissListener {

        void onDismiss();

    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isAlive = false;
    }

    /**
     * 设置沉浸式状态栏
     *
     * @param res 背景资源
     */
    public void setStatusBar(int res) {
        //        if (tintManager == null)
        tintManager = new SystemBarTintManager(getActivity());
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

    protected View findViewById(int viewId) {
        return mDataBinding.getRoot().findViewById(viewId);
    }

    protected void registerListener() {
        if (!ExEventBus.getDefault().getDefaultEventBus().isRegistered(this)) {
            ExEventBus.getDefault().getDefaultEventBus().register(this);
        }
    }

    protected void initLightStatusBar() {
        if (getActivity() != null) {
            if (AppUtil.isXiaomi()) {
                AppUtil.MIUISetStatusBarLightMode(getActivity(), true);
            } else if (AppUtil.isMeizu()) {
                AppUtil.FlymeSetStatusBarLightMode(getActivity().getWindow(), true);
            } else {
                //                LogUtils.d("setUserVisibleHint -> HDAssetFragment");
                //            setStatusBar(R.color.color_primary_background);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getActivity().getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                                    .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
        }
    }

    private void initBlackStatusBar() {
        if (getActivity() != null) {
            if (AppUtil.isXiaomi()) {
                AppUtil.MIUISetStatusBarLightMode(getActivity(), false);
            } else if (AppUtil.isMeizu()) {
                AppUtil.FlymeSetStatusBarLightMode(getActivity().getWindow(), false);
            } else {
                //            LogUtils.d("setUserVisibleHint -> BaseVBDFragment");
                //            setStatusBar(R.drawable.bg_me_gradient_toolbar);
                getActivity().getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
    }

    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getActivity().getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getActivity().getResources().getDimensionPixelSize(resourceId);
            LogUtils.d("getStatusBarHeight : " + result);
        }
        return result;
    }

    protected int getTitleHeight() {
        int result = 0;
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true);
        if (typedValue.resourceId > 0) {
            result = getActivity().getResources().getDimensionPixelSize(typedValue.resourceId);
            LogUtils.d("getTitleHeight : " + result);
        }
        return result;
    }

    protected int getColor(int resId) {
        return getActivity().getResources().getColor(resId);
    }

    protected void initStatusBarSpace(ViewGroup layoutBase, int color) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.height = getStatusBarHeight();
        View view = new View(getActivity());
        view.setLayoutParams(params);
        view.setBackgroundColor(color);
        layoutBase.addView(view, 0);
    }

    protected void initStatusBarSpace(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = getStatusBarHeight();
        view.setLayoutParams(params);
    }

    protected void setSystemUIVisible(boolean show) {
        if (show) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags |= 0x00001000;
            getActivity().getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            //隐藏虚拟按键，并且全屏
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
                View v = getActivity().getWindow().getDecorView();
                v.setSystemUiVisibility(View.GONE);
            } else if (Build.VERSION.SDK_INT >= 19) {
                //for new api versions.
                View decorView = getActivity().getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }
}
