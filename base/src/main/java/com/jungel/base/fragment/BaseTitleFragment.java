package com.jungel.base.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jungel.base.R;
import com.jungel.base.menu.MenuData;
import com.jungel.base.menu.MenuWindow;
import com.jungel.base.utils.ExEventBus;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lion on 2017/4/10.
 */

public abstract class BaseTitleFragment<VDB extends ViewDataBinding> extends BaseSupportFragment
        implements MenuWindow.OnWindowMenuItemClickListener<MenuData> {

    private boolean mHasLoginMenu = true;
    protected String mHelpUrl = null;

    protected View mViewSpace;

    protected LinearLayout mBackLayout;
    protected TextView mBackText;
    protected ImageView mBackImage;

    protected LinearLayout mTitleLayout;
    protected TextView mTitleText;
    protected ImageView mTitleImage;

    protected LinearLayout mForwardLayout;
    protected TextView mForwardText;
    protected ImageView mForwardImage;

    private FrameLayout mToolbarLayout;

    protected MenuWindow<MenuData> mMenuWindow;
    private List<MenuData> mMenuData;
    private ViewGroup mView;
    protected VDB mDataBinding;

    protected abstract void onBind();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        try {
            registerListener();
            if (mView != null) {
                ViewGroup viewGroup = (ViewGroup) mView.getParent();
                if (viewGroup != null) {
                    viewGroup.removeView(mView);
                }
            } else {
                mView = (ViewGroup) inflater.inflate(R.layout.fragment_base, null);
                mDataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), mView, false);
                FrameLayout rootLayout = mView.findViewById(R.id.rootLayout);
                rootLayout.addView(mDataBinding.getRoot());
                initTitle();
            }
            onBind();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attachToSwipeBack(mView);
    }

    private void initTitle() {
        mToolbarLayout = mView.findViewById(R.id.toolbarLayout);

        mViewSpace = mView.findViewById(R.id.viewSpace);

        mBackImage = mView.findViewById(R.id.imageBack);
        mBackText = mView.findViewById(R.id.textBack);
        mBackLayout = mView.findViewById(R.id.layoutBack);
        mBackLayout.setOnClickListener(this);

        mTitleText = mView.findViewById(R.id.textTitle);
        mTitleImage = mView.findViewById(R.id.imageTitle);
        mTitleLayout = mView.findViewById(R.id.layoutTitle);
        mTitleLayout.setOnClickListener(this);

        mForwardText = mView.findViewById(R.id.textForward);
        mForwardImage = mView.findViewById(R.id.imageForward);
        mForwardLayout = mView.findViewById(R.id.layoutForward);
        mForwardLayout.setOnClickListener(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.height = getStatusBarHeight();
        mViewSpace.setLayoutParams(params);
    }

    protected void setTitleVisible(boolean isVisible) {
        if (mToolbarLayout != null) {
            if (isVisible) {
                mToolbarLayout.setVisibility(View.VISIBLE);
            } else {
                mToolbarLayout.setVisibility(View.GONE);
            }
        }

    }

    protected void setTitle(@StringRes int resId) {
        setTitle(getString(resId));
    }

    protected void setTitle(String title) {
        if (mTitleText != null && !TextUtils.isEmpty(title)) {
            mTitleText.setText(title);
        }
    }

    protected void initTitle(@StringRes int resId) {
        initTitle(getString(resId));
    }

    protected void initTitle(String title) {
        initTitle(title, true, false, false);
    }

    protected void initTitle(@StringRes int resId, boolean hasBack) {
        initTitle(getString(resId), hasBack, false, false);
    }

    protected void initTitle(String resId, boolean hasBack) {
        initTitle(resId, hasBack, false, false);
    }


    protected void initTitle(@StringRes int resId, boolean hasBack, boolean hasMenu, boolean
            canClick) {
        initTitle(getString(resId), hasBack, hasMenu, canClick);
    }

    protected void initTitle(String title, boolean hasBack, boolean hasMenu, boolean canClick) {
        if (hasBack) {
            mBackLayout.setVisibility(View.VISIBLE);
        } else {
            mBackLayout.setVisibility(View.GONE);
        }
        if (hasMenu) {
            initMenuWindow();
            mForwardLayout.setVisibility(View.VISIBLE);
        } else {
            mForwardLayout.setVisibility(View.GONE);
        }
        if (canClick) {
            mTitleImage.setVisibility(View.VISIBLE);
        } else {
            mTitleImage.setVisibility(View.GONE);
        }
        _mActivity.setTitle("");
        if (mTitleText != null && !TextUtils.isEmpty(title)) {
            mTitleText.setText(title);
        }
    }


    protected void setTitleColor(int colorId) {
        if (mTitleText != null) {
            if (colorId > 0) {
                mTitleText.setTextColor(getColor(colorId));
            } else {
                mTitleText.setTextColor(colorId);
            }
        }
    }

    protected void setTitleIcon(int imageId) {
        if (mTitleImage != null && imageId > 0) {
            mTitleImage.setImageResource(imageId);
        }
    }

    protected void setTitleIconVisible(boolean visible) {
        if (mTitleImage != null) {
            if (visible) {
                mTitleImage.setVisibility(View.VISIBLE);
            } else {
                mTitleImage.setVisibility(View.GONE);
            }
        }
    }

    protected View getTitleLayout() {
        return mToolbarLayout;
    }

    protected void setMenu(int textId, int imageId) {
        if (mForwardText != null) {
            if (textId > 0) {
                mForwardText.setText(getString(textId));
                mForwardText.setVisibility(View.VISIBLE);
                mForwardLayout.setVisibility(View.VISIBLE);
            } else {
                mForwardText.setVisibility(View.GONE);
            }
        }
        if (mForwardImage != null) {
            if (imageId > 0) {
                mForwardImage.setImageResource(imageId);
                mForwardImage.setVisibility(View.VISIBLE);
                mForwardLayout.setVisibility(View.VISIBLE);
            } else {
                mForwardImage.setVisibility(View.GONE);
            }
        }
    }

    protected void setMenuIcon(int imageId) {
        if (mForwardImage != null) {
            mForwardImage.setImageResource(imageId);
            mForwardImage.setVisibility(View.VISIBLE);
            mForwardLayout.setVisibility(View.VISIBLE);
        }
        if (mForwardText != null) {
            mForwardText.setVisibility(View.GONE);
        }
    }

    protected void setMenuIconVisible(boolean visible) {
        if (mForwardImage != null) {
            if (visible) {
                mForwardImage.setVisibility(View.VISIBLE);
                if (mForwardLayout != null) {
                    mForwardLayout.setVisibility(View.VISIBLE);
                }
            } else {
                mForwardImage.setVisibility(View.GONE);
            }
        }
    }

    protected void setMenuText(int textId) {
        if (mForwardText != null) {
            mForwardText.setText(getString(textId));
            mForwardText.setVisibility(View.VISIBLE);
            mForwardLayout.setVisibility(View.VISIBLE);
        }
        if (mForwardImage != null) {
            mForwardImage.setVisibility(View.GONE);
        }
    }

    protected void setBackIcon(int imageId) {
        if (mBackImage != null) {
            mBackImage.setImageResource(imageId);
            mBackImage.setVisibility(View.VISIBLE);
        }
    }

    protected void setBackText(int textId) {
        if (mBackText != null) {
            mBackText.setText(getString(textId));
            mBackText.setVisibility(View.VISIBLE);
        }
    }

    public boolean isHasLoginOptionMenu() {
        return mHasLoginMenu;
    }

    protected void initMenuWindow() {
        if (mMenuWindow == null) {
            mMenuWindow = new MenuWindow<>(_mActivity);
            mMenuData = new ArrayList<>();
            mMenuWindow.setOnWindowMenuItemClickListener(this);
            onCreateWindowMenu(mMenuData);
        }
    }

    public void notifyWindowMenuChanged() {
        if (mMenuData != null) {
            mMenuData.clear();
        }
        onCreateWindowMenu(mMenuData);
    }

    @Override
    public void onWindowMenuItemClick(int position, MenuData content) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void onCreateWindowMenu(final List<MenuData> mMenuData) {
        if (mMenuData != null) {
            updateUI(new Runnable() {
                @Override
                public void run() {
                    mMenuWindow.setData(mMenuData);
                }
            });
        }
    }

    protected List<MenuData> getMenuData() {
        return mMenuData;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setHasLoginOptionMenu(boolean hasLoginOptionMenu) {
        this.mHasLoginMenu = hasLoginOptionMenu;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.layoutTitle) {
            onTitleClick();
        } else if (id == R.id.layoutBack) {
            onBackClick();
        } else if (id == R.id.layoutForward) {
            onForwardClick();
        }
    }

    protected void onTitleClick() {

    }

    protected void onBackClick() {
        if (getPreFragment() == null) {
            _mActivity.finish();
        } else {
            pop();
        }
    }

    protected void onForwardClick() {
        if (mForwardLayout != null && mMenuWindow != null) {
            mMenuWindow.show(mForwardLayout);
        }
    }

    @Subscribe
    public void onEvent(ExEventBus.MessageEvent event) {
        super.onEvent(event);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //        if (_mActivity != null) {
        //            LogUtils.d("setUserVisibleHint -> BaseTitleFragment");
        //            setStatusBar(R.drawable.bg_common_gradient);
        //            _mActivity.getWindow().getDecorView().setSystemUiVisibility(
        //                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //        }
    }

    protected void setTitleLayoutBackground(int resId) {
        if (resId > 0) {
            mToolbarLayout.setBackgroundResource(resId);
        }
    }

    protected void initEmptyTitle(boolean hasBack) {
        initTitle("", hasBack);
        setTitleLayoutBackground(R.color.color_primary_item_normal);
        if (hasBack) {
            setBackIcon(R.mipmap.ic_hd_back_colorful);
        }
    }

    protected void initLightTitle() {
        mTitleText.setTextColor(_mActivity.getColor(R.color.color_primary_text_highlight));
        mForwardText.setTextColor(_mActivity.getColor(R.color.color_primary_text_highlight));
        setTitleLayoutBackground(R.color.color_primary_item_normal);
        setBackIcon(R.mipmap.ic_hd_back_colorful);
    }

    protected void initBlackTitle() {
        mTitleText.setTextColor(_mActivity.getColor(R.color.color_primary_btn_text));
        mForwardText.setTextColor(_mActivity.getColor(R.color.color_primary_btn_text));
        setTitleLayoutBackground(R.color.color_primary_text_highlight);
        setBackIcon(R.mipmap.ic_hd_back_white);
    }
}
