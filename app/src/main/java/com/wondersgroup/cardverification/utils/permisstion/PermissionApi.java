package com.wondersgroup.cardverification.utils.permisstion;

import android.app.Activity;

import com.tbruyelle.rxpermissions2.RxPermissions;


/**
 * Created by on 2019/5/21
 * description:
 * 权限工具类
 */
public class PermissionApi {
    private static final String TAG = PermissionApi.class.getSimpleName();
    private static PermissionApi mInstance;

    private PermissionApi() {
    }

    /**
     * 单例化
     *
     * @return
     */
    public static PermissionApi getInstance() {
        if (mInstance == null) {
            synchronized (PermissionApi.class) {
                if (mInstance == null) {
                    mInstance = new PermissionApi();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取RxPermissions实例
     *
     * @return
     */
    public RxPermissions getRxPermissions(Activity activity) {
        return new RxPermissions(activity);
    }

}
