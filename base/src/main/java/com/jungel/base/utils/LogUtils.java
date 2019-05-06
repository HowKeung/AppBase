package com.jungel.base.utils;

import android.util.Log;

import com.jungel.base.config.CommonConfig;

/**
 * Created by lion on 2016/6/15 0015.
 */
public class LogUtils {

    //    static {
    //        Logger.init();
    //    }
    //
    //    public static void d(Object... args) {
    //        if (Constants.SHOW_LOG) {
    //            String output = combineParams(args);
    //            if (!TextUtils.isEmpty(output)) {
    //                Logger.d(output);
    //            } else {
    //                Logger.e("params is null");
    //            }
    //        }
    //    }
    //
    //    public static void i(Object... args) {
    //        if (Constants.SHOW_LOG) {
    //            String output = combineParams(args);
    //            if (!TextUtils.isEmpty(output)) {
    //                Logger.i(output);
    //            } else {
    //                Logger.e("params is null");
    //            }
    //        }
    //    }
    //
    //    public static void e(Object... args) {
    //        if (Constants.SHOW_LOG) {
    //            String output = combineParams(args);
    //            if (!TextUtils.isEmpty(output)) {
    //                Logger.e(output);
    //            } else {
    //                Logger.e("params is null");
    //            }
    //        }
    //    }
    //
    //    public static void exception(Throwable throwable) {
    //        if (Constants.SHOW_LOG) {
    //            if (throwable != null) {
    //                Logger.e(throwable.getMessage());
    //            } else {
    //                Logger.e("params is null");
    //            }
    //        }
    //    }
    //
    //    private static String combineParams(Object... args) {
    //        if (args == null) {
    //            return null;
    //        }
    //
    //        String output = "";
    //        for (Object arg : args) {
    //            output += arg.toString();
    //        }
    //
    //        return output;
    //    }

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
        if (CommonConfig.SHOW_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.v(className, createLog(msg));
        }
    }

    public static void d(String msg) {
        if (CommonConfig.SHOW_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.d(className, createLog(msg));
        }
    }

    public static void i(String msg) {
        if (CommonConfig.SHOW_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.i(className, createLog(msg));
        }
    }

    public static void w(String msg) {
        if (CommonConfig.SHOW_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.w(className, createLog(msg));
        }
    }

    public static void e(String msg) {
        if (CommonConfig.SHOW_LOG) {
            getMethodNames(new Throwable().getStackTrace());
            Log.e(className, createLog(msg));
        }
    }

    public static void exception(Throwable throwable) {
        if (CommonConfig.SHOW_LOG) {
            if (throwable != null) {
                e(throwable.getMessage());
            } else {
                e("params is null");
            }
        }
    }
}
