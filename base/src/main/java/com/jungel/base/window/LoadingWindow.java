package com.jungel.base.window;

import android.content.Context;
import android.view.View;

import com.jungel.base.R;
import com.jungel.base.databinding.WindowCommonLoadingBinding;

/**
 * Created by lion on 2017/4/18.
 */

public class LoadingWindow extends BaseWindow<WindowCommonLoadingBinding> {

    public LoadingWindow(Context context) {
        super(context);
    }

    public void show(View parent) {
        showFullWindow(parent);
    }

    @Override
    protected void onBind(WindowCommonLoadingBinding dataBinding) {
        setOutTouchable(true);
        setCancelable(true);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.window_common_loading;
    }
}
