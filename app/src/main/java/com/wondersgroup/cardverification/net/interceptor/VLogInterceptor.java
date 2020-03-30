package com.wondersgroup.cardverification.net.interceptor;

import android.util.Log;


import com.wondersgroup.cardverification.utils.date.DateUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Great by YangZL
 * created on 2019/5/21
 * description: 日志拦截器
 */
public class VLogInterceptor implements Interceptor {

    public static String TGA = "VLogInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request  =  chain.request();
        String url = request.url().toString();
        String params = requestBodyToString(request.body());
//        Response response = chain.proceed(request);
//        String responseString = JsonHandleUtils.jsonHandle(response.body().string());
        String time = DateUtils.getNowDateFormat(DateUtils.DATE_FORMAT_2);
        String log = "\n\n*****请求时间*****:\n" + time+"\n*******路径*******:\n" + url +
                "\n*******参数*******:\n" + params +  "\n*******报文*******:\n"+"\n \n";//  + responseString
        Log.d(TGA,log);
        return chain.proceed(request);
    }
    public  static  String requestBodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null){
                copy.writeTo(buffer);
            }
            else{
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
