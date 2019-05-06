package com.jungel.base.menu;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.jungel.base.R;
import com.jungel.base.databinding.WindowMenuDropdownBinding;
import com.jungel.base.window.BaseWindow;

import java.util.List;

/**
 * Created by lion on 2015/11/2 0002.
 */
public class MenuWindow<T extends MenuData> extends BaseWindow<WindowMenuDropdownBinding> {

    private Context mContext;
    private MenuAdapter<T> spinnerAdapter;

    private OnWindowMenuItemClickListener<T> onWindowMenuItemClickListener;

    public void setOnWindowMenuItemClickListener(OnWindowMenuItemClickListener<T> onWindowMenuItemClickListener) {
        this.onWindowMenuItemClickListener = onWindowMenuItemClickListener;
    }

    public MenuWindow(Activity context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.mContext = context;
    }

    @Override
    protected void onBind(WindowMenuDropdownBinding dataBinding) {
        setOutTouchable(true);
        setCancelable(true);
        spinnerAdapter = new MenuAdapter<T>(mContext);
        mDataBinding.listSelector.setAdapter(spinnerAdapter);
        mDataBinding.listSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                if (onWindowMenuItemClickListener != null) {
                    onWindowMenuItemClickListener.onWindowMenuItemClick(position,
                            spinnerAdapter.getData().get(position));
                }
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.window_menu_dropdown;
    }

    public void setData(List<T> data) {
        if (data != null) {
            spinnerAdapter.setData(data);
            spinnerAdapter.notifyDataSetChanged();
        }
    }

    public List<T> getData() {
        if (spinnerAdapter != null) {
            return spinnerAdapter.getData();
        }

        return null;
    }

    public void show(View parent) {
        int[] position = new int[2];
        parent.getLocationOnScreen(position);
        this.show(parent, Gravity.RIGHT | Gravity.TOP,
                0, position[1] + parent.getHeight());
    }

    public interface OnWindowMenuItemClickListener<T> {
        void onWindowMenuItemClick(int position, T content);
    }
}
