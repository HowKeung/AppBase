package com.jungel.base.window;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jungel.base.utils.CLgUtil;
import com.jungel.base.widget.CToast;

/**
 * Created by lion on 2017/4/26.
 */

public abstract class BaseWindow<VDB extends ViewDataBinding> {

    protected VDB mDataBinding;
    protected PopupWindow mWindow;
    protected Context mContext;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    protected boolean mCancelable = true;

    public BaseWindow(Context context) {
        this(context, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public BaseWindow(Context context, int width, int height) {
        this.mContext = context;
        mDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                getLayoutRes(), null, false);
        View contentView = mDataBinding.getRoot();
        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        mWindow = new PopupWindow(contentView, width, height, true);
        mWindow.setFocusable(true);
        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (mCancelable) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        CLgUtil.d("dismiss");
                        dismiss();
                        return true;
                    }
                    return false;
                } else {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            }
        });
        onBind(mDataBinding);
    }

    protected abstract void onBind(VDB dataBinding);

    public void setOutTouchable(boolean touchable) {
        if (mWindow != null) {
            mWindow.setOutsideTouchable(touchable);
        }
    }

    public void setCancelable(final boolean cancelable) {
        mCancelable = cancelable;
        //        if (mWindow != null) {
        //            mWindow.getContentView().setFocusable(true);
        //            mWindow.getContentView().setFocusableInTouchMode(true);
        //            mWindow.setBackgroundDrawable(new BitmapDrawable());
        //            mWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
        //                @Override
        //                public boolean onKey(View v, int keyCode, KeyEvent event) {
        //                    if (cancelable) {
        //                        if (keyCode == KeyEvent.KEYCODE_BACK) {
        //                            LogUtils.d("dismiss");
        //                            dismiss();
        //                            return true;
        //                        }
        //                        return false;
        //                    } else {
        //                        return keyCode == KeyEvent.KEYCODE_BACK;
        //                    }
        //                }
        //            });
        //        }
    }

    public void show(View parent, int gravity, int x, int y) {
        if (mWindow != null) {
            //            mWindow.showAtLocation(parent, gravity, x, y);
            mWindow.showAsDropDown(parent);
        }
    }

    public void showFullWindow(View parent) {
        if (mWindow != null) {
            try {
                mWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
            }
        }
    }

    public void dismiss() {
        if (mWindow != null) {
            mWindow.dismiss();
        }
    }

    public boolean isShowing() {
        return mWindow != null && mWindow.isShowing();
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    public void updateUI(Runnable runnable) {
        mHandler.post(runnable);
    }

    public void updateUI(Runnable runnable, long delay) {
        mHandler.postDelayed(runnable, delay);
    }

    public void onMessage(String message) {
        toast(message);
    }

    public void onMessage(int message) {
        toast(mContext.getResources().getString(message));
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
            Toast toast = CToast.makeText(mContext, content, duration);
            if (toast != null) {
                toast.show();
            }
        }
    }
}
