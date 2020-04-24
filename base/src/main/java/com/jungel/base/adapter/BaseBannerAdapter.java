package com.jungel.base.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseBannerAdapter<VDB extends ViewDataBinding, T> extends LoopPagerAdapter {

    protected OnItemClickListener mOnItemClickListener;
    protected List<T> mDataList;
    protected ViewPager mViewPager;
    protected RollPagerView mPagerView;
    protected WeakReference<Context> mContext;

    @LayoutRes
    public abstract int getLayoutRes();

    public abstract void onBind(VDB dataBinding, T data, int position);

    public BaseBannerAdapter(Context context, RollPagerView viewPager) {
        super(viewPager);
        mContext = new WeakReference<>(context);
        mPagerView = viewPager;
        mViewPager = viewPager.getViewPager();
        mDataList = new ArrayList<>();
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        VDB dataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext.get()),
                getLayoutRes(), container, false);
        final T tempData = mDataList.get(position);
        onBind(dataBinding, tempData, position);
        dataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, mDataList.get(position));
                }
            }
        });
        return dataBinding.getRoot();
    }

    @Override
    public int getRealCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T dataBean);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setData(List<T> list) {
        mDataList = list;
        notifyDataSetChanged();
    }

    protected Context getContext(){
        return mContext.get();
    }

    public int getColor(int resId) {
        return mContext.get().getResources().getColor(resId);
    }
}
