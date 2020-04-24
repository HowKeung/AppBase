package com.jungel.base.mvp;

import android.content.Context;

import java.lang.ref.WeakReference;

public class BasePresenterImpl<T extends BaseView> implements BasePresenter {

    protected static final int STATE_SUCCESS = 1;
    protected static final int STATE_FAIL = -1;

    protected WeakReference<Context> mContext;
    protected T mView;

    public BasePresenterImpl(Context context, T view) {
        mContext = new WeakReference<>(context);
        mView = view;
    }

    protected Context getContext() {
        if (mContext != null && mContext.get() != null) {
            return mContext.get();
        } else {
            return null;
        }
    }
}
