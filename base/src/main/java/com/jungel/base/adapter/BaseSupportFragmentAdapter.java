package com.jungel.base.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

public class BaseSupportFragmentAdapter<T extends SupportFragment> extends FragmentPagerAdapter {

    protected Activity mContext;
    protected T[] mFragments;

    protected String[] mTitles;

    public BaseSupportFragmentAdapter(Activity context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public T getItem(int position) {
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

    public void setUserVisibleHint(boolean isVisibleToUser) {
        for (SupportFragment fragment : mFragments) {
            fragment.setUserVisibleHint(isVisibleToUser);
        }
    }

    public void onHiddenChanged(boolean hidden) {
        for (SupportFragment fragment : mFragments) {
            fragment.onHiddenChanged(hidden);
        }
    }


    public void onResume() {
        for (SupportFragment fragment : mFragments) {
            fragment.onResume();
        }
    }


    public void onPause() {
        for (SupportFragment fragment : mFragments) {
            fragment.onPause();
        }
    }


    public void onStop() {
        for (SupportFragment fragment : mFragments) {
            fragment.onStop();
        }
    }

    public void onSupportVisible() {
        for (SupportFragment fragment : mFragments) {
            fragment.onSupportVisible();
        }
    }
}
