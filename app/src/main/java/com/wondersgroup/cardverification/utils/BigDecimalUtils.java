package com.wondersgroup.cardverification.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 作者：create by YangZ on 2018/4/20 09:00
 * 邮箱：YangZL8023@163.com
 * 科学计数法的工具类
 */

public class BigDecimalUtils {
    private static String patternDefault_2digit = "#0.00";
    private static String patternDefault_3digit = "###,##0.000";

    /**
     * 正常转换
     */
    public static String normalConversionString(String str) {
        String value;
        BigDecimal bd = new BigDecimal(str);
        value = bd.toPlainString();
        return value;
    }

    /**
     * 去掉小数点后边的0
     */
    public static String abandonConversionString(String str) {
        String value;
        BigDecimal bd = new BigDecimal(str);
        value = bd.toPlainString();
        if (value.indexOf(".") > 0) {
            //正则表达
            value = value.replaceAll("0+?$", "");//去掉后面无用的零
            value = value.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return value;
    }


    /**
     * 千分位显示大额数值 默认小数点后两位
     */
    public static String thousandBitFormatDisplay(BigDecimal number) {
        return thousandBitFormatDisplay(number, patternDefault_2digit);
    }

    /**
     * 千分位显示大额数值
     */
    public static String thousandBitFormatDisplay(BigDecimal number, int digit2or3) {
        return thousandBitFormatDisplay(number, digit2or3 == 2 ? patternDefault_2digit : patternDefault_3digit);
    }

    /**
     * 千分位显示大额数值
     */
    public static String thousandBitFormatDisplay(BigDecimal number, String pattern) {
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);
    }


    /**
     * 千分位显示大额数值 默认小数点后两位
     */
    public static String thousandBitFormatDisplay(String numberStr) {
        return thousandBitFormatDisplay(numberStr, patternDefault_2digit);
    }

    /**
     * 千分位显示大额数值
     */
    public static String thousandBitFormatDisplay(String numberStr, int digit2or3) {
        return thousandBitFormatDisplay(numberStr, digit2or3 == 2 ? patternDefault_2digit : patternDefault_3digit);
    }

    /**
     * 千分位显示大额数值
     */
    public static String thousandBitFormatDisplay(String numberStr, String pattern) {
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(new BigDecimal(numberStr));
    }


}
