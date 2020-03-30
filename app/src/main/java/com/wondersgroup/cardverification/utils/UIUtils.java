package com.wondersgroup.cardverification.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.wondersgroup.cardverification.app.MyApplication;

import java.util.Arrays;
import java.util.List;


/**
 * author: YangZL
 * des: 封装和ui相关的操作
 */
public class UIUtils {
    /**
     * 得到上下文
     */
    public static Context getContext() {
        return MyApplication.getContext();
    }

    /**
     * 得到Resource对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到String.xml中的字符串信息
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 得到String.xml中的字符串数组信息
     */
    public static String[] getStrings(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 得到Color.xml中的颜色信息
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 得到应用程序包名
     *
     * @return
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * dip-->px
     */
    public static int dip2Px(int dip) {
        /*
        1.  px/(ppi/160) = dp
        2.  px/dp = density
         */
        //取得当前手机px和dp的倍数关系
        float density = getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + .5f);
        return px;
    }

    public static int px2Dip(int px) {
        // 2.  px/dp = density
        //取得当前手机px和dp的倍数关系
        float density = getResources().getDisplayMetrics().density;
        int dip = (int) (px / density + .5f);
        return dip;
    }

    /**
     * 测量View的宽高
     *
     * @param view View
     */
    public static void measureWidthAndHeight(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideKeyBoard(Context context, View view){
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     *
     * 根据分割符<h4>('.',':','/')</h4>,分割指定字符串
     * @param str 被分割的字符串
     * @param div  分割符
     * @return  list 被分割后的字符集合
     */
    public static List<String> diveString(String str,String div) {
        return Arrays.asList(str.split(div));
    }
    /**
     * 调整时间格式
     */
    public static String getTime(String time) {
        String[] str = time.split("-");
        time = str[0] + str[1];
        return time;
    }

    /**
     * 调整数据显示格式保留小数点后2位
     * @param value
     * @return
     */
    public static String getValue(String value) {
        return BigDecimalUtils.thousandBitFormatDisplay(TextUtils.isEmpty(value) ? "0" : value);
    }

    /**
     * 数据值判空
     * @param value
     * @return
     */
    public static String getStringValue(String value) {
        return TextUtils.isEmpty(value) ? "0" : value;
    }
}
