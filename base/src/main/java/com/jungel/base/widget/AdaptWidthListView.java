package com.jungel.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.jungel.base.R;

/**
 * Created by lion on 2017/1/6.
 */

public class AdaptWidthListView extends ListView {

    private float mMaxHeight = LayoutParams.WRAP_CONTENT;

    public AdaptWidthListView(Context context) {
        super(context);
    }

    public AdaptWidthListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray arrays = context.obtainStyledAttributes(attrs,
                R.styleable.AdaptWidthListView, defStyleAttr, 0);
        initView(arrays);
        arrays.recycle();
    }

    public AdaptWidthListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arrays = context.obtainStyledAttributes(attrs,
                R.styleable.AdaptWidthListView);
        initView(arrays);
        arrays.recycle();
    }

    private void initView(TypedArray typedArray) {
        if (typedArray != null) {
            mMaxHeight = typedArray.getDimension(
                    R.styleable.AdaptWidthListView_maxHeight, -1);
        } else {
            mMaxHeight = -1;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       // int width = getMaxWidthOfChildren() + getPaddingLeft() + getPaddingRight();//计算listview的宽度
        int maxHeight = getMaxHeight() + getPaddingTop() + getPaddingBottom();
        if (mMaxHeight != -1 && maxHeight > mMaxHeight) {
            int height = (int) mMaxHeight;
//            super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            super.onMeasure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));//设置listview的宽高
        } else {
//            super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            super.onMeasure(widthMeasureSpec,
                    heightMeasureSpec);//设置listview的宽高
        }
    }

    /**
     * 计算item的最大宽度
     *
     * @return
     */
    private int getMaxWidthOfChildren() {
        int maxWidth = 0;
        View view = null;
        int count = getAdapter().getCount();
        for (int i = 0; i < count; i++) {
            view = getAdapter().getView(i, view, this);
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            if (view.getMeasuredWidth() > maxWidth)
                maxWidth = view.getMeasuredWidth();
        }
        return maxWidth;
    }

    private int getMaxHeight() {
        int maxHeight = 0;
        View view = null;
        int count = getAdapter().getCount();
        if(count > 0) {
            view = getAdapter().getView(0, view, this);
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            maxHeight = view.getMeasuredHeight() * count;
        }
        return maxHeight;
    }

}
