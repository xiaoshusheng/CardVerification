package com.wondersgroup.cardverification.widget.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * image tools
 * Created by zhouzhuo810 on 2017/6/16.
 */

public class BitmapUtils {

    public static Bitmap setBitmapSize(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = (newWidth * 1.0f) / width;
        float scaleHeight = (newHeight * 1.0f) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static File saveBitMap(Context context, String filePath, final byte[] data, int rectLeft, int rectTop, int rectWidth, int rectHeight, boolean isNeedCut) throws IOException {
        File file = new File(filePath);
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int degrees = getExifRotateDegree(filePath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap photo = BitmapFactory.decodeByteArray(data, 0, data.length,options);
        photo = rotateBitmap(photo,degrees);
        int scW = ScreenUtils.getScreenWidth(context);
        int scH = ScreenUtils.getScreenHeight(context);
        float ratio = scW  * 1.0f / photo.getWidth();
        photo = Bitmap.createScaledBitmap(photo, (int)(photo.getWidth() * ratio), (int)(photo.getHeight() *ratio), true);
        if (isNeedCut) {
            photo = Bitmap.createBitmap(photo, rectLeft*photo.getWidth()/scW, rectTop* photo.getHeight() / scH , rectWidth*photo.getWidth()/scW, rectHeight* photo.getHeight() / scH);
        }
        if (photo != null) {
            imageZoom(photo, file);
            return file;
        } else {
            return null;
        }

    }

    private static int getExifRotateDegree(String path){
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degrees = getExifRotateDegrees(orientation);
            return degrees;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int getExifRotateDegrees(int exifOrientation) {
        int degrees = 0;
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                degrees = 0;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                degrees = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degrees = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degrees = 270;
                break;
        }
        return degrees;
    }

    private static Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    //压缩
    private static void imageZoom(Bitmap bitMap, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.e("compressImageToFile: ", String.valueOf(bitMap.getAllocationByteCount()));
        }
        //图片允许最大空间   单位：KB
        double maxSize = 200.00;
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        Log.e("compressImageToFile: ", String.valueOf(b.length));
        //将字节换成KB
        double mid = b.length / 1024;
        Log.e("compressImageToFile: ", String.valueOf(mid));
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            Log.e("compressImageToFile: ", String.valueOf(i));
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
                    bitMap.getHeight() / Math.sqrt(i));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.e("compressImageToFile: ", String.valueOf(bitMap.getAllocationByteCount()));
        }

        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos1);
        Log.e("compressImageToFile: ", String.valueOf(baos1.toByteArray().length));
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos1.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }
}
