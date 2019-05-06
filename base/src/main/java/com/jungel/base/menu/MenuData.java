package com.jungel.base.menu;

import android.support.annotation.DrawableRes;

/**
 * Created by lion on 2017/4/11.
 */

public class MenuData {

    public static final int MENU_ID_LOGOUT = 1;
    public static final int MENU_ID_LOGIN = 2;
    public static final int MENU_ID_TRANSFER_IN = 3;// 资产转入
    public static final int MENU_ID_TRANSFER_OUT = 4;// 资产转出
    public static final int MENU_ID_MY_FUND = 5;// 我的理财

    private int id;
    private String content;

    @DrawableRes
    private int iconRes;

    public MenuData() {
    }

    public MenuData(int id, String content, @DrawableRes int iconRes) {
        this.id = id;
        this.content = content;
        this.iconRes = iconRes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(@DrawableRes int iconRes) {
        this.iconRes = iconRes;
    }
}
