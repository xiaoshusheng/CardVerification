package com.wondersgroup.cardverification.widget.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static android.content.ContentValues.TAG;

/**
 * Rect to crop
 * Created by zhouzhuo810 on 2017/6/15.
 */
public class RectView extends View {

    private int width;
    private int height;
    private int cornerColor = 0xff00ff00;
    private int bgColor = 0x3f000000;
    private int rectLineColor = 0xffffffff;
    private int textColor = 0xffffffff;
    private int textSize = 30;
    private int topOffset = 0;

    private Paint bgPaint;
    private Paint rectPaint;
    private Paint cornerPaint;
    private Paint linePaint;//边框颜色
    private Paint textPaint;

    private RectF topRect;
    private RectF leftRect;
    private RectF rightRect;
    private RectF bottomRect;

    private float percent;

    private String hintText = CameraConfig.DEFAULT_HINT_TEXT;

    public RectView(Context context) {
        super(context);
        init(context, null);
    }

    public RectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context ctx, AttributeSet attrs) {
        bgPaint = new Paint();
        bgPaint.setColor(bgColor);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAntiAlias(true);

        cornerPaint = new Paint();
        cornerPaint.setColor(cornerColor);
        cornerPaint.setStyle(Paint.Style.STROKE);
        cornerPaint.setStrokeWidth(10f);
        cornerPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(cornerColor);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStrokeWidth(3f);
        linePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);

        rectPaint = new Paint();
        rectPaint.setColor(rectLineColor);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(2f);
        bgPaint.setAntiAlias(true);
    }

    public void setMaskColor(int color) {
        this.bgColor = color;
        bgPaint.setColor(bgColor);
    }


    public void setHintTextAndTextSize(String hint, int textSizeInPixel) {
        this.hintText = hint;
        this.textSize = textSizeInPixel;
        textPaint.setTextSize(textSize);
    }

    public void setRatioAndPercentOfScreen(int w, int h, float percent) {
        this.percent = percent;
        if (w >= h) {
            this.width = (int) (ScreenUtils.getScreenWidth(getContext()) * percent);
            this.height = width * h / w;
        } else {
            this.height = (int) ((ScreenUtils.getScreenHeight(getContext()) - dp2px(100f)) * percent);
            this.width = height * w / h;
        }

//        Log.e("XXX", "w="+w+",h="+h+",percnet="+percent+",width="+width+",height="+height);
        invalidate();
    }

    /**
     * dp转换成px
     */
    private int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public void updateRatio(int w, int h) {
        this.height = width * h / w;
        invalidate();
    }

    public void setTopOffset(int topOffset) {
        this.topOffset = topOffset;
        if (topOffset == 0) {
            this.topOffset = -dp2px(40) / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width > 0) {
            drawBgWithoutRect(canvas);
            drawCorner(canvas);
            drawText(canvas);
        }
    }

    private void drawText(Canvas canvas) {
        if (hintText != null) {
            float textWidth = textPaint.measureText(hintText);
            canvas.drawText(hintText, getWidth() / 2 - textWidth / 2, topRect.bottom - textSize, textPaint);
        }
    }

    private void drawCorner(Canvas canvas) {
        Path leftTopPath = new Path();
        leftTopPath.moveTo(leftRect.right + 50, leftRect.top);
        leftTopPath.lineTo(leftRect.right, leftRect.top);
        leftTopPath.lineTo(leftRect.right, leftRect.top + 50);
        canvas.drawPath(leftTopPath, cornerPaint);

        Path rightTop = new Path();
        rightTop.moveTo(rightRect.left - 50, rightRect.top);
        rightTop.lineTo(rightRect.left, rightRect.top);
        rightTop.lineTo(rightRect.left, rightRect.top + 50);
        canvas.drawPath(rightTop, cornerPaint);

        Path leftBottom = new Path();
        leftBottom.moveTo(leftRect.right + 50, leftRect.bottom);
        leftBottom.lineTo(leftRect.right, leftRect.bottom);
        leftBottom.lineTo(leftRect.right, leftRect.bottom - 50);
        canvas.drawPath(leftBottom, cornerPaint);

        Path rightBottom = new Path();
        rightBottom.moveTo(rightRect.left - 50, rightRect.bottom);
        rightBottom.lineTo(rightRect.left, rightRect.bottom);
        rightBottom.lineTo(rightRect.left, rightRect.bottom - 50);
        canvas.drawPath(rightBottom, cornerPaint);
        //设置边框西线
        Path topPath = new Path();
        topPath.moveTo(leftRect.right, leftRect.top);
        topPath.lineTo(rightRect.left, rightRect.top);
        canvas.drawPath(topPath, linePaint);
        Path rightPath = new Path();
        rightPath.moveTo(rightRect.left, rightRect.top);
        rightPath.lineTo(rightRect.left, rightRect.bottom);
        canvas.drawPath(rightPath, linePaint);
        Path bottomPath = new Path();
        bottomPath.moveTo(rightRect.left, rightRect.bottom);
        bottomPath.lineTo(leftRect.right, leftRect.bottom);
        canvas.drawPath(bottomPath, linePaint);
        Path leftPath = new Path();
        leftPath.moveTo(leftRect.right, leftRect.bottom);
        leftPath.lineTo(leftRect.right, leftRect.top);
        canvas.drawPath(leftPath, linePaint);
    }

    private void drawBgWithoutRect(Canvas canvas) {
        topRect = new RectF(0, 0 + topOffset, getWidth(), (getHeight() - height) / 3 + topOffset);
        bottomRect = new RectF(0, (getHeight() - height) / 3 + topOffset + height, getWidth(), getHeight());

        leftRect = new RectF(0, (getHeight() - height) / 3 + topOffset, (getWidth() - width) / 2, (getHeight() - height) / 3 + topOffset + height);
        rightRect = new RectF((getWidth() + width) / 2, (getHeight() - height) / 3 + topOffset, getWidth(), (getHeight() - height) / 3 + topOffset + height);
        Log.e(TAG, "topRect: " + topRect + "\n" + "leftRect: " + leftRect + "\n"
                + "rightRect: " +  rightRect + "\n" + "bottomRect: " + bottomRect );
        canvas.drawRect(topRect, bgPaint);
        canvas.drawRect(leftRect, bgPaint);
        canvas.drawRect(rightRect, bgPaint);
        canvas.drawRect(bottomRect, bgPaint);
    }

    public int getCropLeft() {
        return (int) leftRect.right;
    }

    public int getCropTop() {
        return (int) topRect.bottom;
    }

    public int getCropWidth() {
        return (int) (rightRect.left - leftRect.right);
    }

    public int getCropHeight() {
        return (int) (bottomRect.top - topRect.bottom);
    }

    public void setCornerColor(int rectCornerColor) {
        this.rectLineColor = rectCornerColor;
        rectPaint.setColor(rectLineColor);
    }
}
