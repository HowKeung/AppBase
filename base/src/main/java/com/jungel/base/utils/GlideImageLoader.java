package com.jungel.base.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jungel.base.R;
import com.jungel.base.activity.BaseApplication;

/**
 * Created by mc on 2018/4/25.
 */

public class GlideImageLoader {

    public static void displayImage(Object path, ImageView imageView) {
        if (path instanceof String) {
            path = ((String) path).replace("fs.lesmart.one", "192.168.1.124");
        }
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        Glide.with(BaseApplication.getContext())
                .load(path)
                .error(R.mipmap.ic_placeholder)
                .placeholder(R.drawable.img_place_holder)
                .into(imageView);
    }

    public static void displayImage(Object path, ImageView imageView, final OnLoadStateListener
            listener) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        Glide.with(BaseApplication.getContext())
                .load(path)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        listener.onLoadFailed(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource
                                                           dataSource, boolean isFirstResource) {
                        listener.onResourceReady();
                        return false;
                    }
                })
                .error(R.mipmap.ic_placeholder)
                .placeholder(R.drawable.img_place_holder)
                .into(imageView);
    }

    public static void onStop() {
        Glide.with(BaseApplication.getContext()).onStop();

    }

    public static void clearMemory() {
        Glide.get(BaseApplication.getContext()).clearMemory();
    }

    public interface OnLoadStateListener {
        /**
         * Callback when an image has been successfully loaded.
         * <p>
         * <strong>Note:</strong> You must not recycle the bitmap.
         */
        void onResourceReady();

        /**
         * Callback indicating the image could not be successfully loaded.
         *
         * @param errorRes 内容
         */
        void onLoadFailed(@Nullable Drawable errorRes);
    }

    public void clearMemoryCaches() {
        Glide.get(BaseApplication.getContext()).clearMemory();
    }
}