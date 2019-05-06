package com.jungel.base.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lion on 2017/4/10.
 */

public abstract class BaseAdapter<VDB extends ViewDataBinding, T> extends android.widget.BaseAdapter {

    protected Context mContext;

    public BaseAdapter(Context activity) {
        mContext = activity;
    }

    protected List<T> mDataList;

    public void setData(List<T> data) {
        this.mDataList = data;
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mDataList;
    }

    public void clear() {
        if (this.mDataList != null) {
            this.clear();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VDB dataBinding = null;
        if (convertView == null) {
            dataBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), getLayoutRes(), parent, false);
            convertView = dataBinding.getRoot();
            convertView.setTag(dataBinding);
        } else {
            dataBinding = (VDB) convertView.getTag();
        }
        final T tempData = mDataList.get(position);
        onBind(position, dataBinding, tempData);

        return convertView;
    }

    public abstract void onBind(int position, VDB dataBinding, T tempData);

    @LayoutRes
    public abstract int getLayoutRes();

    @Override
    public int getCount() {
        if (this.mDataList != null) {
            return this.mDataList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (this.mDataList != null) {
            return this.mDataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
