package com.jungel.base.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.jungel.base.bean.CommonTabData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class BaseFragmentAdapter extends FragmentPagerAdapter {

    protected WeakReference<Activity> mContext;
    protected Fragment[] mFragments = {};

    protected String[] mTitles;

    public BaseFragmentAdapter(Activity context, FragmentManager fm) {
        super(fm);
        this.mContext = new WeakReference<>(context);
    }

    @Override
    public Fragment getItem(int position) {
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

    public ArrayList<CustomTabEntity> getTitles() {
        ArrayList<CustomTabEntity> data = new ArrayList<>();
        for (String title : mTitles) {
            data.add(new CommonTabData(title));
        }
        return data;
    }

    protected Activity getContext(){
        return mContext.get();
    }

    protected int getColor(int resId) {
        return mContext.get().getResources().getColor(resId);
    }
}
