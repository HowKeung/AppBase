package com.jungel.base.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseViewAdapter<VDB extends ViewDataBinding, T> extends PagerAdapter {

    protected WeakReference<Context> mContext;
    protected List<T> mDataList;
    protected OnItemClickListener mOnItemClickListener;

    protected Context getContext() {
        return mContext.get();
    }

    public BaseViewAdapter(Context context) {
        mContext = new WeakReference<>(context);
        mDataList = new ArrayList<>();
    }

    @LayoutRes
    public abstract int getLayoutRes();

    public abstract void onBind(VDB dataBinding, T data, int position);

    @Override
    public int getCount() {
        if (mDataList != null) {
            return mDataList.size();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        VDB dataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext.get()),
                getLayoutRes(), null, false);
        container.addView(dataBinding.getRoot());
        final T tempData = mDataList.get(position);
        onBind(dataBinding, tempData, position);
        dataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, tempData);
                }
            }
        });
        return dataBinding.getRoot();
    }

    public void setData(List<T> wallets) {
        if (mDataList != null && mDataList.size() > 0) {
            mDataList.clear();
        }
        this.mDataList = wallets;
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mDataList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T data);
    }

    protected int getColor(int resId) {
        return mContext.get().getResources().getColor(resId);
    }
}
