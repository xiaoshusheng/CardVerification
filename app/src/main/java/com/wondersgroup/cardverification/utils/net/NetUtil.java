package com.wondersgroup.cardverification.utils.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by XML on 2018/3/21
 * Description
 * 用于判断当前网络状态的工具类
 */
public class NetUtil {
    /**
     * 无网络链接
     */
    public static final int NET_NO_CONNECTION = 0;

    /**
     * WIFI
     */
    public static final int NET_TYPE_WIFI = 1;
    /**
     * 2G
     */
    public static final int NET_TYPE_2G = 2;
    /**
     * 3G
     */
    public static final int NET_TYPE_3G = 3;
    /**
     * 4G
     */
    public static final int NET_TYPE_4G = 4;

    /**
     * 获取网络类型
     *
     * @param context
     */
    public static int checkNetworkType(Context context) {

        int netType = NET_NO_CONNECTION;
        //连接管理对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取NetworkInfo对象
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        switch (networkInfo.getType()) {
            case ConnectivityManager.TYPE_WIFI://wifi
            case ConnectivityManager.TYPE_WIMAX:
            case ConnectivityManager.TYPE_ETHERNET:
                return NET_TYPE_WIFI;
            case ConnectivityManager.TYPE_MOBILE:
                switch (networkInfo.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_LTE:  // 4G
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        return NET_TYPE_4G;
                    case TelephonyManager.NETWORK_TYPE_UMTS: // 3G
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        return NET_TYPE_3G;
                    case TelephonyManager.NETWORK_TYPE_GPRS: // 2G
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        return NET_TYPE_2G;
                    default:
                        return netType;
                }
            default:
                return netType;
        }

    }
}
