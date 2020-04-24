package com.jungel.base.menu;

import android.support.annotation.DrawableRes;

/**
 * Created by lion on 2017/4/11.
 */

public class MenuData {

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
