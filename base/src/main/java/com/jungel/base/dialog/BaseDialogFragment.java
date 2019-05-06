package com.jungel.base.dialog;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jungel.base.R;
import com.jungel.base.fragment.BaseSupportFragment;
import com.jungel.base.utils.ExEventBus;
import com.jungel.base.utils.LogUtils;
import com.jungel.base.widget.CToast;
import com.jungel.base.window.LoadingWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;

/**
 * All rights Reserved, Designed By JiXiangMei Company
 *
 * @version V1.0
 * @Package: com.zhongwei.jxm.ui.common
 * @Description: (DialogFragment基类)
 * @author: ZC
 * @date: 2017/7/12 16:59
 */
public abstract class BaseDialogFragment<VDB extends ViewDataBinding> extends DialogFragment
        implements View.OnClickListener {

    private static final long SHOW_SPACE = 200L;//延时显示软键盘

    protected boolean isAlive = false;//页面是否出于显示的生命周期
    protected View mView = null;//业务层根界面view

    private InputMethodManager mIMM;
    protected boolean isFullScreen = false;//是否全屏显示

    private boolean isResumeHide = false;//是否在页面恢复后隐藏各种弹框
    private boolean isShowInBottom = true;//是否显示在页面底部

    private boolean mNeedHideSoft = false;//是否在页面恢复后隐藏软键盘

    private boolean isDismiss = false;

    protected VDB mDataBinding;

    protected Activity _mActivity;
    private boolean mIsLoadingShowing = false;
    private LoadingWindow mLoading;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 子类layoutid设置
     *
     * @return
     */
    @LayoutRes
    protected abstract int getLayoutRes();

    /**
     * 子类view初始化
     */
    protected abstract void onBind();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            return;
        }
        isAlive = true;
        setCancelable(true);//默认设置不能取消弹框
        int style = DialogFragment.STYLE_NO_TITLE;
        int theme = getTheme();
        if (getTheme() == 0) {
            theme = R.style.Dialog_NoTitle_Fade_in;
        }
        if (isFullScreen) {
            style = STYLE_NO_FRAME;
        }
        setStyle(style, theme);
        _mActivity = getActivity();
        ExEventBus.getDefault().getDefaultEventBus().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isAlive) {
            try {
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow()
                        .getAttributes().height);

                int gravity = Gravity.BOTTOM;
                int height = WindowManager.LayoutParams.MATCH_PARENT;
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                if (getTheme() == R.style.Dialog_Bottom_In_Out) {
                    //如果有显示在底部的需求，则设置显示在底部
                    if (isShowInBottom) {
                        gravity = Gravity.BOTTOM;
                    } else {
                        gravity = Gravity.CENTER;
                    }
                    height = WindowManager.LayoutParams.WRAP_CONTENT;
                } else if (getTheme() == R.style.Dialog_Left_In_Out) {
                    gravity = Gravity.LEFT;
                    width = WindowManager.LayoutParams.WRAP_CONTENT;
                } else if (getTheme() == R.style.Dialog_Right_In_Out) {
                    gravity = Gravity.RIGHT;
                    width = WindowManager.LayoutParams.WRAP_CONTENT;
                } else if (getTheme() == R.style.Dialog_Top_Right_In_Out) {
                    gravity = Gravity.TOP | Gravity.RIGHT;
                    height = WindowManager.LayoutParams.WRAP_CONTENT;
                    width = WindowManager.LayoutParams.WRAP_CONTENT;
                } else if (getTheme() == R.style.Dialog_NoTitle) {
                    gravity = Gravity.CENTER;
                    height = WindowManager.LayoutParams.WRAP_CONTENT;
                }
                Window window = getDialog().getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.gravity = gravity; //底部
                lp.width = width;
                lp.height = height;
                window.setAttributes(lp);
            } catch (Exception e) {
                LogUtils.e(e.toString());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        try {
            if (mView != null) {
                //防止oncreateview被多次调用
                ViewGroup viewGroup = (ViewGroup) mView.getParent();
                if (viewGroup != null) {
                    viewGroup.removeView(mView);
                }
            } else {
                mDataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
                mView = mDataBinding.getRoot();
            }
            //设置进场出场动画
            if (null != getDialog() && null != getDialog().getWindow()) {
                setAnimation();
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            try {
                super.onActivityCreated(savedInstanceState);
                onBind();
            } catch (Exception e) {
                dismiss();
            }
        } else {
            dismiss();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onLazyInitView(savedInstanceState);
    }

    protected void onLazyInitView(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 设置进场出场动画
     */
    protected void setAnimation() {

    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        isDismiss = true;
        isAlive = false;
        super.onDestroyView();
    }


    @Subscribe
    public void onEvent(ExEventBus.MessageEvent event) {

    }

    protected void setShowInBottom(boolean bottom) {
        this.isShowInBottom = bottom;
    }
    //
    //    public void showBlockLoadingDialog(String content, boolean canCancel) {
    //        try {
    //            if (blockLoadingFragment == null) {
    //                blockLoadingFragment = BlockLoadingFragment.newInstance(canCancel, content);
    //            }
    //            if (!blockLoadingFragment.isAdded()) {
    //                blockLoadingFragment.show(getFragmentManager().beginTransaction(), "");
    //            }
    //        } catch (Exception e) {
    //            LogUtils.e(e + "======showBlockLoadingDialog");
    //        }
    //    }
    //
    //    public void hideBlockLoadingDialog() {
    //        try {
    //            if (blockLoadingFragment != null && isAlive
    //                    && ((BaseVDBActivity) getContext()).isResume()) {
    //                blockLoadingFragment.dismiss();
    //                blockLoadingFragment = null;
    //            }
    //            if (!((BaseVDBActivity) getContext()).isResume()) {
    //                isResumeHide = true;
    //            }
    //        } catch (Exception e) {
    //            LogUtils.e(e + "======showBlockLoadingDialog");
    //        }
    //    }

    private void initImm() {
        if (mIMM == null) {
            mIMM = (InputMethodManager) getActivity().getSystemService(Context
                    .INPUT_METHOD_SERVICE);
        }
    }

    /**
     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
     */
    protected void showSoftInput(final View view) {
        if (view == null) return;
        initImm();
        view.requestFocus();
        mNeedHideSoft = true;
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIMM.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, SHOW_SPACE);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput() {
        if (getView() != null) {
            initImm();
            mIMM.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }


    /**
     * 此方法只是关闭软键盘
     */
    public void hideSoftInputFromWindow(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        if (imm.isActive() && editText != null) {
            if (editText.getWindowToken() != null) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager
                        .HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (isAdded()) {
            manager.beginTransaction().remove(this).commitAllowingStateLoss();
        }
        try {
            Field f = DialogFragment.class.getDeclaredField("mDismissed");
            f.setAccessible(true);
            f.set(this, false);
            f = DialogFragment.class.getDeclaredField("mShownByMe");
            f.setAccessible(true);
            f.set(this, true);
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            super.show(manager, tag);
        }
    }

    public void show(FragmentManager manager) {
        if (isAdded()) {
            manager.beginTransaction().remove(this).commitAllowingStateLoss();
        }
        try {
            Field f = DialogFragment.class.getDeclaredField("mDismissed");
            f.setAccessible(true);
            f.set(this, false);
            f = DialogFragment.class.getDeclaredField("mShownByMe");
            f.setAccessible(true);
            f.set(this, true);
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, this.getClass().getSimpleName());
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            super.show(manager, this.getClass().getSimpleName());
        }
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        if (isAdded()) {
            transaction.remove(this).commitAllowingStateLoss();
        }
        try {
            Field f = DialogFragment.class.getDeclaredField("mDismissed");
            f.setAccessible(true);
            f.set(this, false);
            f = DialogFragment.class.getDeclaredField("mShownByMe");
            f.setAccessible(true);
            f.set(this, true);
            transaction.add(this, tag);
            f = DialogFragment.class.getDeclaredField("mViewDestroyed");
            f.setAccessible(true);
            f.set(this, false);
            int tmp = transaction.commitAllowingStateLoss();
            f = DialogFragment.class.getDeclaredField("mBackStackId");
            f.setAccessible(true);
            f.set(this, tmp);
            return tmp;
        } catch (Exception e) {
            return super.show(transaction, tag);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isResumeHide) {
            //            hideBlockLoadingDialog();
            isResumeHide = false;
        }
        if (mNeedHideSoft) {
            mNeedHideSoft = false;
            //??此处是否要进行隐藏软键盘操作
            //            hideSoftInput();
        }
    }

    @Override
    public void dismiss() {
        isDismiss = true;
        super.dismissAllowingStateLoss();
    }

    public void showToast(String msg) {
        if (isAlive && null != getActivity() && !TextUtils.isEmpty(msg)) {
            CToast.toast(msg);
        }
    }

    public void showDialog() {
        if (getDialog() != null)
            getDialog().show();
    }

    public void hideDialog() {
        if (getDialog() != null)
            getDialog().hide();
    }

    public boolean isDismiss() {
        return isDismiss;
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

    @Override
    public void onClick(View view) {

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

    public void dismissLoading(final BaseSupportFragment.OnLoadingDismissListener listener) {
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
}