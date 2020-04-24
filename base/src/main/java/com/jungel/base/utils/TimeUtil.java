package com.jungel.base.utils;

import android.text.TextUtils;

import com.jungel.base.R;
import com.jungel.base.activity.BaseApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static final String FORMATE_NORMAL_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMATE_NATION_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static String getTimeByMillis(long millis) {
        return getTimeByMillis(millis, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getTimeBySecond(long second) {
        return getTimeByMillis(second * 1000, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getTimeBySecond(long second, String format) {
        return getTimeByMillis(second * 1000, format);
    }

    public static String getTimeByMillis(long millis, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);//24小时制
        Date date = new Date();
        if (millis > 0) {
            date.setTime(millis);
        }
        return simpleDateFormat.format(date);
    }

    public static long getMillisByTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return System.currentTimeMillis();
        }
        return getMillisByTime(time, "yyyy-MM-dd HH-mm-ss");
    }

    public static long getMillisByTime(String time, String format) {
        if (TextUtils.isEmpty(time)) {
            return 0;
        }
        long millis = 0;
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat(format).parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        millis = calendar.getTimeInMillis();
        System.out.println("TEST--  millis : " + millis);
        return millis;
    }

    public static long getDayMillis(long second) {
        String date = getTimeBySecond(second, "yyyy-MM-dd");
        return getMillisByTime(date);
    }

    /**
     * 转换成时分秒
     *
     * @param time
     * @return
     */
    public static String format(long time) {
        long hour = time / (1000 * 60 * 60);
        long min = (time - hour * 1000 * 60 * 60) / (1000 * 60);
        long second = (time - hour * 1000 * 60 * 60 - min * 1000 * 60) / 1000;

        String hourStr = String.valueOf(hour);
        String minStr = String.valueOf(min);
        String secStr = String.valueOf(second);

        if (hourStr.length() == 1) {
            hourStr = "0" + hourStr;
        }
        if (minStr.length() == 1) {
            minStr = "0" + minStr;
        }
        if (secStr.length() == 1) {
            secStr = "0" + secStr;
        }
        return hourStr + ":" + minStr + ":" + secStr;
    }

    public static String[] formatArray(String time) {
        long second = 0;
        try {
            second = Long.parseLong(time);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return formatArray(second);
    }

    /**
     * 转换成时分秒
     *
     * @param time
     * @return
     */
    public static String[] formatArray(long time) {
        String[] times = new String[3];
        long hour = time / (1000 * 60 * 60);
        long min = (time - hour * 1000 * 60 * 60) / (1000 * 60);
        long second = (time - hour * 1000 * 60 * 60 - min * 1000 * 60) / 1000;

        String hourStr = String.valueOf(hour);
        String minStr = String.valueOf(min);
        String secStr = String.valueOf(second);

        if (hourStr.length() == 1) {
            hourStr = "0" + hourStr;
        }
        if (minStr.length() == 1) {
            minStr = "0" + minStr;
        }
        if (secStr.length() == 1) {
            secStr = "0" + secStr;
        }
        times[0] = hourStr;
        times[1] = minStr;
        times[2] = secStr;
        return times;
    }

    /**
     * 转换成时分秒
     *
     * @param time
     * @return
     */
    public static long[] getDayAndHour(long time) {
        long day = time / (24 * 60 * 60);
        long hour = (time - day * 24 * 60 * 60) / (60 * 60);
        long min = (time - day * 24 * 60 * 60 - hour * 60 * 60) / 60;
        long second = (time - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        long[] times = new long[4];
        times[0] = day;
        times[1] = hour;
        times[2] = min;
        times[3] = second;
        return times;
    }

    public static String getTimeAfter(long time, String dayAfter) {
        Calendar cld = Calendar.getInstance();
        cld.setTimeInMillis(time * 1000);
        int day = 0;
        try {
            day = Integer.parseInt(dayAfter);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        cld.add(Calendar.DATE, day);
        String hour = String.valueOf(cld.get(Calendar.HOUR));
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        String min = String.valueOf(cld.get(Calendar.MINUTE));
        if (min.length() == 1) {
            min = "0" + min;
        }
        String montn = String.valueOf(cld.get(Calendar.MONTH) + 1);
        if (montn.length() == 1) {
            montn = "0" + montn;
        }
        String dayNew = String.valueOf(cld.get(Calendar.DAY_OF_MONTH));
        if (dayNew.length() == 1) {
            dayNew = "0" + dayNew;
        }
        return cld.get(Calendar.YEAR) + "-" + montn + "-" + dayNew +
                " " + hour + ":" + min;
    }

    public static String getTimeBeforeMinute(long second) {
        long cur = System.currentTimeMillis() / 1000;
        // 超过1小时
        if ((cur - second) > 3600) {
            return getTimeBySecond(second);
        }
        if (cur - second <= 0) {
            return BaseApplication.getContext().getString(R.string.just_now);
        }
        return ((cur - second) / 60) + BaseApplication.getContext().getString(R.string.minute_before);
    }

    public static Date getNationDate(String time) {
        Date date = new Date();
        long millis = getMillisByTime(time, FORMATE_NATION_TIME);
        if (millis > 0) {
            date.setTime(millis);
        }
        return date;
    }

    public static String getTimeByDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMATE_NORMAL_TIME);//24小时制
        return simpleDateFormat.format(date);
    }

    public static String getTimeByDate(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);//24小时制
        return simpleDateFormat.format(date);
    }

    /**
     * 倒计时
     *
     * @param time
     * @return
     */
    public static String getSecondCountDown(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        String day = time.substring(0,
                time.indexOf(BaseApplication.getContext().getString(R.string.day)));
        String hour =
                time.substring(time.indexOf(BaseApplication.getContext().getString(R.string.day)) + 1
                        , time.indexOf(BaseApplication.getContext().getString(R.string.hour)));
        String min =
                time.substring(time.indexOf(BaseApplication.getContext().getString(R.string.hour)) + 1
                        , time.indexOf(BaseApplication.getContext().getString(R.string.min)));
        String sec =
                time.substring(time.indexOf(BaseApplication.getContext().getString(R.string.min)) + 1
                        , time.indexOf(BaseApplication.getContext().getString(R.string.second)));
        int d = 0, h = 0, m = 0, s = 0;
        try {
            d = Integer.parseInt(day);
            h = Integer.parseInt(hour);
            m = Integer.parseInt(min);
            s = Integer.parseInt(sec);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (s > 0) {
            s = s - 1;
        } else if (m > 0) {
            s = 59;
            m = m - 1;
        } else if (h > 0) {
            s = 59;
            m = 59;
            h = h - 1;
        } else if (d > 0) {
            s = 59;
            m = 59;
            h = 23;
            d = d - 1;
        } else {
            return time;
        }
        return d + BaseApplication.getContext().getString(R.string.day)
                + h + BaseApplication.getContext().getString(R.string.hour)
                + m + BaseApplication.getContext().getString(R.string.min)
                + s + BaseApplication.getContext().getString(R.string.second);
    }

    public static void main(String[] args) {

        //        String time = "2018-10-23T11:10:36.500";
        //
        //        System.out.println("time : " + time);
        //
        //        long second = getMillisByTime(time, "yyyy-MM-dd'T'HH:mm:ss.SSS")
        //                + 60 * 1000;
        //        System.out.println("second1 : " + second);
        //
        //        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss
        // .SSS");
        //        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        //
        //
        //        System.out.println("time2 : " + dateFormatter.format(new Date(second)));
        //
        //        System.out.println("time3 : " + getTimeByMillis(second,
        //                "yyyy-MM-dd'T'HH:mm:ss.SSS"));
        //
        //        System.out.println("getNationDate : " + getNationDate(time).getTime());

        //Calendar calendar = Calendar.getInstance();

        //System.out.println("DAY_OF_WEEK : " + calendar.get(Calendar.DAY_OF_WEEK));
    }
}
