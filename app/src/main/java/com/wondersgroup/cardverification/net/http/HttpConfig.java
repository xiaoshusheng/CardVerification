package com.wondersgroup.cardverification.net.http;

import android.util.Log;


import com.wondersgroup.cardverification.net.interceptor.HeadRequestInterceptor;
import com.wondersgroup.cardverification.net.interceptor.VLogInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Great by YangZL
 * created on 2019/5/21
 * description: http配置
 */
public class HttpConfig {
    public static String TGA = "HttpConfig";
    public static long CONNECT_TIMEOUT = 60L;
    public static long READ_TIMEOUT = 30L;
    public static long WRITE_TIMEOUT = 30L;
    /**
     * 获取OkHttpClient实例
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new HeadRequestInterceptor())
                .addInterceptor(new VLogInterceptor())
                .build();
        Log.e("request: ", "请求数据");
        return okHttpClient;
    }
}
