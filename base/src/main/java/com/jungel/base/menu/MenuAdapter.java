package com.jungel.base.menu;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.jungel.base.R;
import com.jungel.base.adapter.BaseAdapter;
import com.jungel.base.databinding.ItemMenuBinding;


/**
 * Created by lion on 2017/4/11.
 */

public class MenuAdapter<T extends MenuData> extends BaseAdapter<ItemMenuBinding, T> {


    public MenuAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBind(int position, ItemMenuBinding dataBinding, MenuData data) {
        dataBinding.textTitle.setText(data.getContent());
        dataBinding.imgIcon.setImageResource(data.getIconRes());
        if (position == mDataList.size() - 1) {
            dataBinding.viewDivider.setVisibility(View.GONE);
        } else {
            dataBinding.viewDivider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_menu;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
