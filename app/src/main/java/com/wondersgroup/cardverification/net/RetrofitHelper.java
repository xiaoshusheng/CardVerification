package com.wondersgroup.cardverification.net;


import com.wondersgroup.cardverification.app.Constant;
import com.wondersgroup.cardverification.net.http.HttpConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Great by YangZL
 * created on 2019/5/21
 * description: 网络请求的辅助类
 */
public class RetrofitHelper {
    private static String TGA = "RetrofitHelper";
    private static RetrofitHelper mInstance = null;
    private Retrofit mRetrofit = null;

    public static RetrofitHelper getInstance(){
        synchronized (RetrofitHelper.class){
            if (mInstance == null){
                mInstance = new RetrofitHelper();
            }
        }
        return mInstance;
    }
    private RetrofitHelper(){
        init();
    }
    private void init() {
        resetApp();
    }

    private void resetApp() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(HttpConfig.getOkHttpClient())
                .build();
    }

    public IService getServer(){
        return mRetrofit.create(IService.class);
    }

}
