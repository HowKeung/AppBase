package com.jungel.base.fragment;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.jungel.base.adapter.BaseSupportFragmentAdapter;
import com.jungel.base.utils.LogUtils;


public abstract class BasePagerVDBFragment<T extends BaseSupportFragmentAdapter, VDB extends
        ViewDataBinding> extends BaseSupportFragment<VDB> {

    protected T mPagerAdapter;

    @Override
    public void onResume() {
        super.onResume();
        if (mPagerAdapter != null) {
            mPagerAdapter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPagerAdapter != null) {
            mPagerAdapter.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPagerAdapter != null) {
            mPagerAdapter.onPause();
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (mPagerAdapter != null) {
            mPagerAdapter.onSupportVisible();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mPagerAdapter != null) {
            mPagerAdapter.onHiddenChanged(hidden);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mPagerAdapter != null) {
            mPagerAdapter.setUserVisibleHint(isVisibleToUser);
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        LogUtils.d(getClass().getSimpleName() + " onFragmentResult");
        if (mPagerAdapter != null) {
            mPagerAdapter.onFragmentResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d(getClass().getSimpleName() + " onActivityResult");
        if (mPagerAdapter != null) {
            mPagerAdapter.onActivityResult(requestCode, resultCode, data);
        }
    }
}
