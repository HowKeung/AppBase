package com.jungel.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.jungel.base.R;

/**
 * Created by lion on 2017/4/17.
 */

public class AverageHeightListView extends ScrollView {

    private int mMaxShowItem = 5;

    private LinearLayout mContainerLinearLayout;
    private BaseAdapter mAdapter;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            onAdapterUpdate();
        }
    };

    public AverageHeightListView(Context context) {
        super(context);
        initView(null);
    }

    public AverageHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.AverageHeightListView);
        initView(typedArray);
    }

    public AverageHeightListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.AverageHeightListView, defStyleAttr, 0);
        initView(typedArray);
    }

    @RequiresApi(21)
    public AverageHeightListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.AverageHeightListView, defStyleAttr, defStyleRes);
        initView(typedArray);
    }

    private void initView(TypedArray typedArray) {
        if (typedArray != null) {
            mMaxShowItem = typedArray.getInt(
                    R.styleable.AverageHeightListView_maxShowItem, 5);
        } else {
            mMaxShowItem = 5;
        }

        addContainer();
    }

    private void addContainer() {
        mContainerLinearLayout = new LinearLayout(getContext());
        mContainerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mContainerLinearLayout.setLayoutParams(params);
        this.addView(mContainerLinearLayout);
    }

    public void setAdapter(BaseAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        this.mAdapter = adapter;

        if (mAdapter != null) {
            this.mAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }

    private void onAdapterUpdate() {
        if (mAdapter != null) {
            int eachItemHeight = getHeight() / mMaxShowItem;
            mContainerLinearLayout.removeAllViews();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                final View view = mAdapter.getView(i, null, mContainerLinearLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, eachItemHeight);
                view.setLayoutParams(params);
                final int finalI = i;
                final int finalI1 = i;
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(null, view, finalI, finalI1);
                        }
                    }
                });
                mContainerLinearLayout.addView(view);
            }
        }
    }

    public void setSelection(final int position) {
        post(new Runnable() {
            @Override
            public void run() {
                int count = mContainerLinearLayout.getChildCount();
                if (position < count) {
                    int scrollY = 0;
                    for (int i = 0; i < position - 1; i++) {
                        scrollY += mContainerLinearLayout.getChildAt(i).getLayoutParams().height;
                    }
                    scrollTo(0, scrollY);
                }
            }
        });
    }

    public void scrollToEnd() {
        post(new Runnable() {
            @Override
            public void run() {
                int height = mContainerLinearLayout.getMeasuredHeight();
                scrollTo(0, height);
            }
        });
    }
}
