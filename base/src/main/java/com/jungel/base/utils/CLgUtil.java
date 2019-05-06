package com.jungel.base.utils;

import android.util.Log;

import com.jungel.base.config.CommonConfig;

/**
 * 通用打印日志工具类
 * Created by Administrator on 2016/5/12.
 */
public class CLgUtil {
    static String className;
    static String methodName;
    static int lineNumber;

    private static String createLog(String log) {
        return "【" + methodName + ":" + lineNumber + "】" + log;
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        try {
            className = sElements[1].getFileName();
            methodName = sElements[1].getMethodName();
            lineNumber = sElements[1].getLineNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void v(String msg) {
        if (CommonConfig.DEBUG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.v(className, createLog(msg));
        }
    }

    public static void d(String msg) {
        if (CommonConfig.DEBUG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.d(className, createLog(msg));
        }
    }

    public static void i(String msg) {
        if (CommonConfig.DEBUG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.i(className, createLog(msg));
        }
    }

    public static void w(String msg) {
        if (CommonConfig.DEBUG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.w(className, createLog(msg));
        }
    }

    public static void e(String msg) {
        if (CommonConfig.DEBUG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.e(className, createLog(msg));
        }
    }
}
