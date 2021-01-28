package com.example.module_ad.utils;

import android.os.Debug;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;

/**
 * @author wujinming QQ:1245074510
 * @name td_horoscope
 * @class name：com.example.module_ad.utils
 * @class describe
 * @time 2020/11/26 10:16
 * @class describe
 */
public class InterceptUtil {

    public static OkHttpClient interceptClient(){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);
        LoggingInterceptor httpLoggingInterceptor = new LoggingInterceptor.Builder()
                .loggable(false)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("请求")
                .response("响应")
                .build();
     return    httpClientBuilder.addInterceptor(httpLoggingInterceptor).build();
    }
}
