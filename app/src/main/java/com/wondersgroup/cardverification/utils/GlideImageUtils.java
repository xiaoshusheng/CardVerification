package com.wondersgroup.cardverification.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.wondersgroup.cardverification.widget.GlideApp;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * glide加载图片的工具类
 */

public class GlideImageUtils {

    /**
     * 正常加载图片
     *
     * @param context
     * @param iv          ImageView
     * @param url         图片地址
     * @param placeholder 占位符 错误视图 url为空时展示
     */
    public static void displayImage(Context context, ImageView iv, String url, int placeholder) {
        GlideApp.with(context)
                .load(url)
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder)
                .fallback(placeholder)
                .into(iv);
    }

    /**
     * 正常加载图片
     *
     * @param context
     * @param iv          ImageView
     * @param url         图片地址
     * @param placeholder 占位符 错误视图 url为空时展示
     */
    public static void displayImageFitCenter(Context context, ImageView iv, String url, int placeholder) {

        GlideApp.with(context)
                .load(url)
                .override(iv.getMeasuredWidth(), iv.getMaxHeight())
                .fitCenter()
                .error(placeholder)
                .fallback(placeholder)
                .into(iv);
    }

    /**
     * 正常加载图片
     *
     * @param context
     * @param iv          ImageView
     * @param url         图片地址
     * @param placeholder 占位符 错误视图 url为空时展示
     */
    public static void display(Context context, ImageView iv, String url, int placeholder) {

        GlideApp.with(context)
                .load(url)
                .placeholder(placeholder)
                .error(placeholder)
                .fallback(placeholder)
                .into(iv);
    }

    /**
     * 圆角实现
     *
     * @param context
     * @param iv          ImageView
     * @param url         图片地址
     * @param rounder     圆角值（px）
     * @param placeholder 占位符 错误视图 url为空时展示
     */
    public static void roundedCornersImage(Context context, ImageView iv, String url, int rounder, int placeholder) {

        GlideApp.with(context)
                .asBitmap()
                .error(placeholder)
                .fallback(placeholder)
                .load(url)
                .transform(new GlideRoundTransform(context,rounder)) //此处为圆角px值
                .into(iv);
    }

    /**
     * 圆形实现
     *
     * @param context
     * @param iv          ImageView
     * @param url         图片地址
     * @param placeholder 占位符 错误视图 url为空时展示
     */
    public static void circleCropImage(Context context, ImageView iv, String url, int placeholder) {//, int error, int fallback

        GlideApp.with(context)
                .asBitmap()
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder)
                .fallback(placeholder)
                .load(url)
                .into(new BitmapImageViewTarget(iv) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable mRoundedBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(UIUtils.getResources(), resource);
                        mRoundedBitmapDrawable.setCircular(true);
                        iv.setImageDrawable(mRoundedBitmapDrawable);
                    }
                });
    }

    public static void circleCropImage(Context context, ImageView iv, Bitmap bitmap, int placeholder) {//, int error, int fallback

        GlideApp.with(context)
                .asBitmap()
                .centerCrop()
                .placeholder(placeholder)
                .error(placeholder)
                .fallback(placeholder)
                .load(bitmap)
                .into(new BitmapImageViewTarget(iv) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable mRoundedBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(UIUtils.getResources(), resource);
                        mRoundedBitmapDrawable.setCircular(true);
                        iv.setImageDrawable(mRoundedBitmapDrawable);
                    }
                });
    }

    /**
     * 按屏幕宽度比例放大图片
     *
     * @param context
     * @param iv          ImageView
     * @param url         图片地址
     * @param placeholder 占位符 错误视图 url为空时展示
     */
    public static void enlargeImageWithWidth(Context context, ImageView iv, String url, int placeholder) {//, int error, int fallback

        GlideApp.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (iv == null) {
                            return false;
                        }
                        if (iv.getScaleType() != ImageView.ScaleType.CENTER_CROP) {//修改前使用的FIT_XY
                            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
                        int vw = iv.getWidth() - iv.getPaddingLeft() - iv.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + iv.getPaddingTop() + iv.getPaddingBottom();
                        iv.setLayoutParams(params);

                        return false;
                    }
                })
//                .placeholder(placeholder)
                .error(placeholder)
                .fallback(placeholder)
                .into(iv);
    }

}
