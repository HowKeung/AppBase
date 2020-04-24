package com.jungel.base.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jungel.base.widget.ListItemAnimator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lion on 2017/4/10.
 */

public abstract class BaseRecyclerAdapter<VDB extends ViewDataBinding, T> extends
        RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder<VDB>> {

    protected WeakReference<Context> mContext;
    protected List<T> mDataList;
    protected OnItemClickListener mOnItemClickListener;

    protected ListItemAnimator mItemAnimator;

    public BaseRecyclerAdapter(Context context) {
        mContext = new WeakReference<>(context);
        mDataList = new ArrayList<>();
        mItemAnimator = new ListItemAnimator();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @LayoutRes
    public abstract int getLayoutRes();

    public abstract void onBind(VDB dataBinding, T data, int position);

    @Override
    public ViewHolder<VDB> onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext.get()),
                getLayoutRes(), parent, false);
        ViewHolder<VDB> holder = new ViewHolder<>(dataBinding.getRoot());
        holder.setDataBinding((VDB) dataBinding);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        VDB dataBinding = (VDB) (holder.getDataBinding());
        final T tempData = mDataList.get(position);
        onBind(dataBinding, tempData, position);

        dataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, tempData);
                }
            }
        });

        mItemAnimator.startAnimation(dataBinding.getRoot(), position);
    }

    @Override
    public int getItemCount() {
        if (mDataList != null) {
            return mDataList.size();
        } else {
            return 0;
        }
    }

    public void clear() {
        if (mDataList != null) {
            mDataList.clear();
        }
    }

    public void resetItemAnimator() {
        if (mItemAnimator != null) {
            mItemAnimator.resetAnimatorPosition();
        }
    }

    public List<T> getData() {
        return mDataList;
    }

    public void setData(List<T> wallets) {
        if (mDataList != null && mDataList.size() > 0) {
            mDataList.clear();
        }
        this.mDataList = wallets;
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(T data) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void removeData(int index) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.remove(index);
        notifyDataSetChanged();
    }

    public void clearData() {
        if (mDataList != null) {
            mDataList.clear();
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T data);
    }

    public static class ViewHolder<VDB> extends RecyclerView.ViewHolder {

        private VDB mDataBinding;

        public VDB getDataBinding() {
            return mDataBinding;
        }

        public void setDataBinding(VDB dataBinding) {
            this.mDataBinding = dataBinding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    protected Context getContext() {
        return mContext.get();
    }

    protected int getColor(int resId) {
        return mContext.get().getResources().getColor(resId);
    }
}
