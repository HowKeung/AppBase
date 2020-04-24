package com.jungel.base.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

    private static final int MAX_WX_IMAGE = 64;// 实际为128，但是压缩之后会比预想的大10%，所以最大值减小10%

    /**
     * 获取bitmap的size
     *
     * @param bitmap
     * @return
     */
    public static long getBitmapSize(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public static Bitmap compress(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        return bitmap;//压缩好比例大小后再进行质量压缩
        //return bitmap;
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        LogUtils.d("scaleWidth : " + scaleWidth);
        LogUtils.d("scaleHeight : " + scaleHeight);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    public static Bitmap imageZoom(Bitmap src_bitmap) {
        // 图片允许最大空间 单位：KB
        double maxSize = MAX_WX_IMAGE;//32
        // 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            // 获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            LogUtils.d("imageZoom : " + i);
            // 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            Bitmap bitmap = zoomImage(src_bitmap, src_bitmap.getWidth() / Math.sqrt(i),
                    src_bitmap.getHeight() / Math.sqrt(i));
            return bitmap;
        }
        return src_bitmap;
    }

    public static Bitmap drawWXMiniBitmap(Bitmap bitmap, int width, int height) {
        Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 用这个Bitmap生成一个Canvas,然后canvas就会把内容绘制到上面这个bitmap中
        Canvas mCanvas = new Canvas(mBitmap);
        // 绘制画笔
        Paint mPicturePaint = new Paint();
        // 绘制背景图片
        mCanvas.drawBitmap(mBitmap, 0.0f, 0.0f, mPicturePaint);
        // 绘制图片的宽、高
        int width_head = bitmap.getWidth();
        int height_head = bitmap.getHeight();
        // 绘制图片－－保证其在水平方向居中
        mCanvas.drawBitmap(bitmap, (width - width_head) / 2, (height - height_head) / 2,
                mPicturePaint);
        // 保存绘图为本地图片
        mCanvas.save();
        mCanvas.restore();
        return mBitmap;
    }

    public static boolean isOverSize(Bitmap bitmap, int maxSize) {
        // 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        LogUtils.d("isOverSize : " + mid);
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        return mid > maxSize;
    }

    public static boolean writeBitmapToFile(Bitmap bitmap, File path) {
        boolean isCompress = false;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            if (path.getName().toLowerCase().endsWith(".jpg")) {
                isCompress = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } else {
                isCompress = bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                // ignore
            }
        }
        return isCompress;
    }

    /**
     * 将图片保存到本地时进行压缩, 即将图片从Bitmap形式变为File形式时进行压缩,
     * 特点是: File形式的图片确实被压缩了, 但是当你重新读取压缩后的file为 Bitmap是,它占用的内存并没有改变
     *
     * @param bmp
     * @param file
     */
    public static void compressBmpToFile(Bitmap bmp, File file, int options) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        //        while (baos.toByteArray().length / 1024 > 100) {
        //            baos.reset();
        //            options -= 10;
        //        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        //        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                //F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }

    public static Bitmap drawWXMiniBitmap(Bitmap bitmap) {
        int width;
        int height;
        boolean isWidthLong = bitmap.getWidth() > bitmap.getHeight();
        if (isWidthLong) {
            width = bitmap.getWidth();
            height = width * 4 / 5;
        } else {
            height = bitmap.getHeight();
            width = height * 5 / 4;
        }
        width = width * 5 / 4;
        height = height * 5 / 4;
        Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 用这个Bitmap生成一个Canvas,然后canvas就会把内容绘制到上面这个bitmap中
        Canvas mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.WHITE);
        // 绘制画笔
        Paint mPicturePaint = new Paint();
        // 绘制背景图片
        mCanvas.drawBitmap(mBitmap, 0.0f, 0.0f, mPicturePaint);
        // 绘制图片的宽、高
        int width_head = bitmap.getWidth();
        int height_head = bitmap.getHeight();
        // 绘制图片－－保证其在水平方向居中
        if (isWidthLong) {
            mCanvas.drawBitmap(bitmap, 0, (height - height_head) / 4, mPicturePaint);
        } else {
            mCanvas.drawBitmap(bitmap, (width - width_head) / 4, 0, mPicturePaint);
        }
        //        mCanvas.drawBitmap(bitmap, 0, 0, mPicturePaint);
        // 保存绘图为本地图片
        mCanvas.save();
        mCanvas.restore();
        while (isOverSize(mBitmap, MAX_WX_IMAGE)) {
            mBitmap = imageZoom(mBitmap);
        }
        return mBitmap;
    }
}
