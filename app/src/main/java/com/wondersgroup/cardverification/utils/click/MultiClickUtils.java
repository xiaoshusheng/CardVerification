package com.wondersgroup.cardverification.utils.click;

/**
 * 作者：create by YangZ on 2018/4/3 09:21
 * 邮箱：YangZL8023@163.com
 * 避免重复点击的工具类
 */

public class MultiClickUtils {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
