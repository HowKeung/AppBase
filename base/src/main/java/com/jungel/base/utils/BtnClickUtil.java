package com.jungel.base.utils;

import com.jungel.base.R;
import com.jungel.base.widget.CToast;

/**
 * All rights Reserved, Designed By JiXiangMei Company
 *
 * @version V1.0
 * @Package: com.zhongwei.jxm.util.button
 * @Description: ${TODO}(判断是否点击过快)
 * @author: YangJW
 * @date: 2017/8/19 14:23
 */
public class BtnClickUtil {

    private static long lastClickTime;
    private final static long INTERVAL = 1500;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < INTERVAL) {
            CToast.toast(R.string.click_too_fast);
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * @param timeInterval 时间间隔
     * @return
     */
    public static boolean isFastDoubleClick(Long timeInterval) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < timeInterval) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
