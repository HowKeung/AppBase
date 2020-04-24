package com.jungel.base.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.jungel.base.R;
import com.jungel.base.activity.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lion on 2017/6/2.
 */

public class SDTools {

    public static final String ROOT_DIR_NAME = "ixiaoguo";
    public static final String DOWNLOAD_PATH = "download";

    private static final int UPLOAD_WIDTH = 300;
    private static final int UPLOAD_WIDTH_BIG = 480;

    public static String getSDPath() {
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            return Environment.getExternalStorageDirectory().toString();//获取跟目录
        }
        return null;
    }

    public static String getRootPath(Context context) {
        if (getSDPath() != null) {
            String path = getSDPath() + File.separator + ROOT_DIR_NAME;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            return path;
        }
        Toast.makeText(context, R.string.no_sdcard_can_not_save, Toast.LENGTH_SHORT).show();
        return null;
    }

    public static String getDownloadPath(Context context) {
        String root = getRootPath(context);
        if (!TextUtils.isEmpty(root)) {
            String path = root + File.separator + DOWNLOAD_PATH;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            return path;
        }

        return null;
    }

    /**
     * 根据URL获取文件名
     *
     * @param url URL
     * @return 文件名
     */
    public static String getFileNameFromUrl(String url) {
        if (url.indexOf("/") != -1)
            return url.substring(url.lastIndexOf("/")).replace("/", "");
        else
            return url;
    }

    /**
     * TODO<根据路径删除指定的目录或文件，无论存在与否>
     *
     * @return boolean
     */
    public static boolean deleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
                // 为目录时调用删除目录方法
                return deleteDir(filePath);
            }
        }
    }

    /**
     * TODO<创建文件夹>
     *
     * @return File
     */
    public static File createDir(String path) {
        File dir = new File(path);
        if (!isExist(dir)) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * TODO<删除文件夹及文件夹下的文件>
     *
     * @return boolean
     */
    public static boolean deleteDir(String dirPath) {
        boolean flag = false;
        // 如果dirPath不以文件分隔符结尾，自动添加文件分隔符
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
        File dirFile = new File(dirPath);

        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }

        flag = true;
        File[] files = dirFile.listFiles();
        // 遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                // 删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                // 删除子目录
                flag = deleteDir(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前空目录
        return dirFile.delete();
    }

    /**
     * TODO<获取指定目录下文件的个数>
     *
     * @return int
     */
    public static int getFileCount(String dirPath) {
        int count = 0;

        // 如果dirPath不以文件分隔符结尾，自动添加文件分隔符
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
        File dirFile = new File(dirPath);

        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return count;
        }

        // 获取该目录下所有的子项文件(文件、子目录)
        File[] files = dirFile.listFiles();
        // 遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                // 删除子文件
                count += 1;
            }
        }

        return count;
    }

    /**
     * TODO<创建文件>
     *
     * @return File
     */
    public static File createFile(String path, String fileName) {
        File file = new File(createDir(path), fileName);
        if (!isExist(file)) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LogUtils.e("创建文件出错：" + e.toString());
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * TODO<删除指定地址的文件夹>
     *
     * @return void
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && isExist(file))
            return file.delete();
        return false;
    }

    /**
     * 复制单个文件
     *
     * @param srcPath String 原文件路径
     * @param desPath String 目标路径
     */
    public static void copyFile(String srcPath, String desPath) {
        int bytesum = 0;
        int byteread = 0;
        File oldfile = new File(srcPath);

        if (isExist(oldfile)) {// 源文件存在
            try {
                InputStream inStream = new FileInputStream(srcPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(desPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                fs.close();
                inStream.close();
                LogUtils.d("拷贝文件成功,文件总大小为：" + bytesum + "字节");
            } catch (IOException e) {
                LogUtils.e("拷贝文件出错：" + e.toString());
                e.printStackTrace();
            }
        } else {// 源文件不存在
            LogUtils.e("拷贝文件出错：源文件不存在！");
        }
    }

    /**
     * 复制整个文件夹内容
     *
     * @param srcPath String 原文件路径
     * @param desPath String 复制后路径
     */
    public static void copyFolder(String srcPath, String desPath) {

        try {
            (new File(desPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File a = new File(srcPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (srcPath.endsWith(File.separator)) {
                    temp = new File(srcPath + file[i]);
                } else {
                    temp = new File(srcPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(desPath
                            + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {// 如果是子文件夹
                    copyFolder(srcPath + "/" + file[i], desPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    /**
     * TODO<判断File对象所指的目录或文件是否存在>
     *
     * @return boolean
     */
    public static boolean isExist(File file) {
        return file.exists();
    }

    /**
     * @param context
     * @return
     */
    public static String getProjectSDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            PackageManager pm = context.getPackageManager();
            String appName = context.getApplicationInfo().loadLabel(pm).toString();
            String pjPath = getSDPath() + "/" + appName + "/";
            if (!isExist(new File(pjPath))) {
                createDir(pjPath);
            }
            return pjPath;
        } else {
            PackageManager pm = context.getPackageManager();
            String appName = context.getApplicationInfo().loadLabel(pm).toString();
            String pjPath = BaseApplication.getContext().getCacheDir() + "/" + appName + "/";
            if (!isExist(new File(pjPath))) {
                createDir(pjPath);
            }
            return pjPath;
        }
    }

    /**
     * 创建文件
     *
     * @param file
     * @return
     */
    private static File createNewFile(File file) {

        try {

            if (file.exists()) {
                return file;
            }

            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            LogUtils.e(e.getMessage().toString());
            return null;
        }
        return file;
    }

    /**
     * 创建文件
     *
     * @param path
     */
    public static File createNewFile(String path) {
        File file = new File(path);
        return createNewFile(file);
    }

    /**
     * 压缩一张图片
     *
     * @param file
     * @return
     */
    public static File imageFileZip(File file) {
        int mScalType = UPLOAD_WIDTH_BIG;
        final String path = getProjectSDirPath(BaseApplication.getContext());
        if (path == null || TextUtils.isEmpty(path)) {
            LogUtils.d("创建压缩路径失败");
            return null;
        }
        String filePath = path + "/photo/" + "upload_file_zip" + "_" + System.currentTimeMillis()
                + ".jpg";
        if (file != null) {
            try {
                BitmapFactory.Options newOpts = new BitmapFactory.Options();
                newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
                newOpts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), newOpts);//此时返回bm为空
                newOpts.inJustDecodeBounds = false;

                int width = newOpts.outWidth;
                int heigth = newOpts.outHeight;

                //                Bitmap bitTemp = BitmapFactory.decodeStream(inputStream);
                //                if (bitTemp != null) {
                //                    int width = bitTemp.getWidth();
                //                    int heigth = bitTemp.getHeight();
                LogUtils.d("width : " + width + ", height : " + heigth);
                if (width > mScalType) {
                    float scale = (float) width / mScalType;
                    //                        int targetWidth = mScalType;
                    //                        int targetHeight = (int) (scale * heigth + +0.5f);
                    //                        LogUtils.d("targetWidth : " + targetWidth + ",
                    // targetHeight : " + targetHeight);
                    //                        Bitmap targetScaleBitmap = Bitmap
                    // .createScaledBitmap(bitTemp, targetWidth, targetHeight, false);
                    newOpts.inSampleSize = (int) scale;//设置缩放比例
                    Bitmap targetScaleBitmap = BitmapFactory.decodeFile(file.getPath(), newOpts);
                    if (targetScaleBitmap != null) {
                        File saveFile = new File(filePath);
                        if (saveFile.exists()) {
                            LogUtils.d("save file is exists.");
                            if (!saveFile.delete()) {
                                LogUtils.d("删除文件失败。");
                                return null;
                            }
                            LogUtils.d("delete file is sucess.");
                        } else {
                            LogUtils.d("save file is not exists.");
                        }

                        FileOutputStream scaleOupputStream = null;
                        try {
                            if (!saveFile.createNewFile()) {
                                LogUtils.d("创建文件失败。");
                                return null;
                            }
                            LogUtils.d("create file is sucess。");
                            scaleOupputStream = new FileOutputStream(saveFile);
                            targetScaleBitmap.compress(Bitmap.CompressFormat.JPEG, 80,
                                    scaleOupputStream);
                            scaleOupputStream.flush();
                            LogUtils.d("write target bitmap file into target file sucess。");
                            return saveFile;
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (scaleOupputStream != null) {
                                try {
                                    scaleOupputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                    } else {
                        LogUtils.d("缩放文件失败。");
                    }

                    if (targetScaleBitmap != null) {
                        targetScaleBitmap.recycle();
                    }
                } else {
                    LogUtils.d("文件大小很小不用压缩。");
                }
                //                } else {
                //                    LogUtils.d("从文件中读取图片失败。");
                //                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            LogUtils.d("文件为空。");
        }


        return null;
    }

    /**
     * 检查文件是否存在
     */
    public static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    /**
     * 将图片内容解析成字节数组
     *
     * @param path
     * @return byte[]
     * @throws Exception
     */
    public static byte[] getByteFromFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        try {
            InputStream inStream = new FileInputStream(path);
            byte[] buffer = new byte[1024];
            int len = -1;
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] data = outStream.toByteArray();
            outStream.close();
            inStream.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore
                    .Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
