package com.jungel.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Base64;

import com.jungel.base.activity.BaseApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * 数据缓存（sp持久化）大量数据请勿使用
 * Created by Administrator on 2017/3/18.
 */
public class CacheUtil {

    private static Context context = BaseApplication.getContext();

    /**
     * 数据缓存
     *
     * @param key
     * @param object
     */
    public static void saveObject(String key, Object object) {
        if (object == null) {
            removeObject(key);
            return;
        }
        PackageManager pm = context.getPackageManager();
        String appName = context.getApplicationInfo().loadLabel(pm).toString();
        saveObject(appName, key, object);
    }

    /**
     * 提取数据缓存
     *
     * @param key
     * @return
     */
    public static Object getObject(String key, Object defaultObject) {
        PackageManager pm = context.getPackageManager();
        String appName = context.getApplicationInfo().loadLabel(pm).toString();
        return getObject(appName, key, defaultObject);
    }

    /**
     * 数据缓存
     *
     * @param fileName
     * @param key
     * @param object
     */
    public static void saveObject(String fileName, String key, Object object) {
        if (null == object) {
            removeObject(fileName, key);
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String type = object.getClass().getSimpleName();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
            editor.commit();
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
            editor.commit();
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
            editor.commit();
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
            editor.commit();
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
            editor.commit();
        } else {
            CacheBody body = new CacheBody();
            body.obj = object;
            editor.remove(key).commit();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(body);
                String strList = new String(Base64.encode(baos.toByteArray(),
                        Base64.DEFAULT));
                editor.putString(key, strList);
                editor.commit();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 移除数据缓存
     *
     * @param fileName
     * @param key
     */
    public static void removeObject(String fileName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key).commit();
    }

    /**
     * 移除数据缓存
     *
     * @param key
     */
    public static void removeObject(String key) {
        PackageManager pm = context.getPackageManager();
        String appName = context.getApplicationInfo().loadLabel(pm).toString();
        removeObject(appName, key);
    }


    /**
     * 提取数据缓存
     *
     * @param fileName
     * @param key
     * @return
     */
    public static Object getObject(String fileName, String key, Object defaultObject) {

        String type = defaultObject.getClass().getSimpleName();

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Activity.MODE_PRIVATE);
        if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defaultObject);
        } else {
            CacheBody body = null;

            String message = sharedPreferences.getString(key, "");
            if (message.equals("")) {
                return defaultObject;
            }
            byte[] buffer = Base64.decode(message.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            try {
                ObjectInputStream ois = new ObjectInputStream(bais);
                body = (CacheBody) ois.readObject();
                if (null == body) {
                    return defaultObject;
                } else {
                    return body.obj;
                }
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return defaultObject;
    }

    /**
     * 清除数据缓存
     *
     * @param fileName
     */
    public static void clear(String fileName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                fileName, Activity.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}

class CacheBody implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // public Date d;
    public Object obj;
}