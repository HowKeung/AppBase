package com.jungel.base.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.jungel.base.fragment.BaseSupportFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

public class BaseSupportFragmentAdapter<T extends ViewPager> extends FragmentPagerAdapter {

    protected WeakReference<Activity> mContext;
    protected SupportFragment[] mFragments;

    protected String[] mTitles;

    protected T mViewPager;

    public BaseSupportFragmentAdapter(Activity context, FragmentManager fm, T viewPager) {
        super(fm);
        mContext = new WeakReference<>(context);
        mViewPager = viewPager;
    }

    @Override
    public SupportFragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    public List<String> getTitles() {
        List<String> data = new ArrayList<>();
        data.addAll(Arrays.asList(mTitles));
        return data;
    }

    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        for (SupportFragment fragment : mFragments) {
            fragment.onFragmentResult(requestCode, resultCode, data);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (SupportFragment fragment : mFragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        for (SupportFragment fragment : mFragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        mFragments[mViewPager.getCurrentItem()].setUserVisibleHint(isVisibleToUser);
        for (int i = 0; i < mFragments.length; i++) {
            if (i != mViewPager.getCurrentItem()) {
                SupportFragment fragment = mFragments[i];
                if (fragment instanceof BaseSupportFragment) {
                    if (!((BaseSupportFragment) fragment).mIsOnBind) {
                        fragment.setUserVisibleHint(isVisibleToUser);
                    }
                }
            }
        }
    }

    public void onHiddenChanged(boolean hidden) {
        mFragments[mViewPager.getCurrentItem()].onHiddenChanged(hidden);
    }


    public void onResume() {
        mFragments[mViewPager.getCurrentItem()].onResume();
    }


    public void onPause() {
        mFragments[mViewPager.getCurrentItem()].onPause();
    }


    public void onStop() {
        mFragments[mViewPager.getCurrentItem()].onStop();
    }

    public void onSupportVisible() {
        mFragments[mViewPager.getCurrentItem()].onSupportVisible();
    }

    public T getViewPager() {
        return mViewPager;
    }

    protected Activity getContext() {
        return mContext.get();
    }

    protected int getColor(int resId) {
        return mContext.get().getResources().getColor(resId);
    }
}
