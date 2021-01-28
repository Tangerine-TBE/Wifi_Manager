package com.example.module_ad.request;



import com.example.module_ad.utils.Contents;
import com.example.module_ad.utils.InterceptUtil;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wujinming QQ:1245074510
 * @name td_horoscope
 * @class name：com.example.module_ad.request
 * @class describe
 * @time 2020/11/10 11:01
 * @class describe
 */
public class RetrofitUtil {

    private static RetrofitUtil  sInstance;

    public AdService getAdService() {
        return adService;
    }
    private final AdService adService;

    public static RetrofitUtil getInstance() {
        if (sInstance == null) {
            sInstance = new RetrofitUtil();
        }
        return sInstance;
    }
     private RetrofitUtil() {
         adService = new Retrofit.Builder()
                 .baseUrl(Contents.AD_URL)
                 .addConverterFactory(GsonConverterFactory.create())
                 .client(InterceptUtil.interceptClient())
                 .build()
                 .create(AdService.class);
     }




}
