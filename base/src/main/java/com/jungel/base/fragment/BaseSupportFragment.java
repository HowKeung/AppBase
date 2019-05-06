package com.jungel.base.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jungel.base.utils.AppUtil;
import com.jungel.base.utils.CLgUtil;
import com.jungel.base.utils.ExEventBus;
import com.jungel.base.widget.CToast;
import com.jungel.base.window.LoadingWindow;

import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

public abstract class BaseSupportFragment<VDB extends ViewDataBinding> extends SwipeBackFragment
        implements View.OnClickListener {

    protected VDB mDataBinding;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private LoadingWindow mLoading;
    protected boolean isAlive = true;
    private static final int INVALID_VAL = -1;//顶部颜色默认值
    private boolean mIsLoadingShowing = false;

    protected abstract void onBind();

    @LayoutRes
    protected abstract int getLayoutRes();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        registerListener();
        mDataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        onBind();
        return attachToSwipeBack(mDataBinding.getRoot());
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
        onMessage(_mActivity.getString(resId));
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
            Toast toast = CToast.makeText(_mActivity, content, duration);
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
                    mLoading = new LoadingWindow(_mActivity);
                }
                if (_mActivity != null && !mIsLoadingShowing) {
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

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
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

    protected View findViewById(int viewId) {
        return mDataBinding.getRoot().findViewById(viewId);
    }

    protected void registerListener() {
        if (!ExEventBus.getDefault().getDefaultEventBus().isRegistered(this)) {
            ExEventBus.getDefault().getDefaultEventBus().register(this);
        }
        if (getParentFragment() == null && getPreFragment() != null) {
            setSwipeBackEnable(true);
            getSwipeBackLayout().setEnableGesture(true);
        } else {
            setSwipeBackEnable(false);
            getSwipeBackLayout().setEnableGesture(false);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        initBlackStatusBar();
    }

    protected void initLightStatusBar() {
        if (_mActivity != null) {
            if (AppUtil.isXiaomi()) {
                AppUtil.MIUISetStatusBarLightMode(_mActivity, true);
            } else if (AppUtil.isMeizu()) {
                AppUtil.FlymeSetStatusBarLightMode(_mActivity.getWindow(), true);
            } else {
                //                LogUtils.d("setUserVisibleHint -> HDAssetFragment");
                //            setStatusBar(R.color.color_primary_background);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    _mActivity.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                                    .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
        }
    }

    private void initBlackStatusBar() {
        if (_mActivity != null) {
            if (AppUtil.isXiaomi()) {
                AppUtil.MIUISetStatusBarLightMode(_mActivity, false);
            } else if (AppUtil.isMeizu()) {
                AppUtil.FlymeSetStatusBarLightMode(_mActivity.getWindow(), false);
            } else {
                //            LogUtils.d("setUserVisibleHint -> BaseVBDFragment");
                //            setStatusBar(R.drawable.bg_me_gradient_toolbar);
                _mActivity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
    }

    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = _mActivity.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = _mActivity.getResources().getDimensionPixelSize(resourceId);
            CLgUtil.d("getStatusBarHeight : " + result);
        }
        return result;
    }

    protected int getColor(int resId) {
        return _mActivity.getResources().getColor(resId);
    }

    protected void initStatusBarSpace(ViewGroup layoutBase, int color) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.height = getStatusBarHeight();
        View view = new View(_mActivity);
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
            _mActivity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            //隐藏虚拟按键，并且全屏
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
                View v = _mActivity.getWindow().getDecorView();
                v.setSystemUiVisibility(View.GONE);
            } else if (Build.VERSION.SDK_INT >= 19) {
                //for new api versions.
                View decorView = _mActivity.getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }
}
