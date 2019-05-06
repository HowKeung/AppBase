package com.jungel.base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jungel.base.R;
import com.jungel.base.activity.BaseApplication;
import com.jungel.base.widget.CToast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;

public class AppUtil {
    /**
     * 获取本地软件版本号
     */
    public static int getVersionCode() {
        Context ctx = BaseApplication.getContext();
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName()
                    , 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getVersionName() {
        String localVersion = "";
        Context ctx = BaseApplication.getContext();
        try {
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName()
                    , 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public static String getBigDecimal(double cash) {
        //        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00000000");
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.########");
        return decimalFormat.format(cash);
    }

    private static long lastClickTime;
    private final static long INTERVAL = 500;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < INTERVAL) {
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

    /**
     * 获取本地手机的IMEI信息
     *
     * @return IMEI号码
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI() {
        try {
            // 获取IMEI
            TelephonyManager phoneMgr = (TelephonyManager) BaseApplication.getContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return phoneMgr.getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }

    // 获取电话的MAC
    public static String getLoaclPhoneMac(Context context) {

        if (context == null) {
            return null;
        }
        String macAddress = null;
        try {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            if (null != info) {
                macAddress = info.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    public static String getSerialNumber() {
        return android.os.Build.SERIAL;
    }

    public static void openBrowser(Activity activity, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : NameMap of the component implementing an activity that can display the intent
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(Intent.createChooser(intent,
                    activity.getResources().getString(R.string.find_select_browser)));
        }
    }

    // 是否是小米手机
    public static boolean isXiaomi() {
        return "Xiaomi".equalsIgnoreCase(Build.MANUFACTURER);
    }

    // 设置小米状态栏
    public static void setXiaomiStatusBar(Window window, boolean isLightStatusBar) {
        Class<? extends Window> clazz = window.getClass();
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, isLightStatusBar ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMannufacturer() {
        return Build.MANUFACTURER + " " + Build.BOARD;
    }

    public static String getSystemVersion() {
        return "Android " + Build.VERSION.RELEASE;
    }

    public static void test() {
        String phoneInfo = "Product: " + android.os.Build.PRODUCT + "\n";
        phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI + "\n";
        phoneInfo += ", TAGS: " + android.os.Build.TAGS + "\n";
        phoneInfo += ", VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE + "\n";
        phoneInfo += ", MODEL: " + android.os.Build.MODEL + "\n";
        phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK + "\n";
        phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE + "\n";
        phoneInfo += ", DEVICE: " + android.os.Build.DEVICE + "\n";
        phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY + "\n";
        phoneInfo += ", BRAND: " + android.os.Build.BRAND + "\n";
        phoneInfo += ", BOARD: " + android.os.Build.BOARD + "\n";
        phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT + "\n";
        phoneInfo += ", ID: " + android.os.Build.ID + "\n";
        phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER + "\n";
        phoneInfo += ", USER: " + android.os.Build.USER + "\n";
        phoneInfo += ", BOOTLOADER: " + android.os.Build.BOOTLOADER + "\n";
        phoneInfo += ", HARDWARE: " + android.os.Build.HARDWARE + "\n";
        phoneInfo += ", INCREMENTAL: " + android.os.Build.VERSION.INCREMENTAL + "\n";
        phoneInfo += ", CODENAME: " + android.os.Build.VERSION.CODENAME + "\n";
        phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK_INT + "\n";

        CLgUtil.d(phoneInfo);
    }

    /* 
     * 判断服务是否启动,context上下文对象 ，className服务的name 
     */
    public static boolean isServiceRunning(String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) BaseApplication.getContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static void openBrowser(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(BaseApplication.getContext().getPackageManager()) != null) {
            BaseApplication.getContext().startActivity(Intent.createChooser(intent,
                    BaseApplication.getContext().getString(R.string
                            .find_select_browser)));
        } else {
            CToast.toast(R.string.please_no_application);
        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 需要MIUIV6以上
     *
     * @param activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View
                                .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                                .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View
                                .SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // 是否是小米手机
    public static boolean isMeizu() {
        return "meizu".equalsIgnoreCase(Build.MANUFACTURER);
    }
}
