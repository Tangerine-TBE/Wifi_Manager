package com.example.module_ad.request;

import com.example.module_ad.bean.AdBean;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * @author wujinming QQ:1245074510
 * @name td_horoscope
 * @class name：com.example.module_ad.request
 * @class describe
 * @time 2020/11/10 11:00
 * @class describe
 */
public interface AdService {

    @GET("twxWifiMaster")
    Call<AdBean> getAdMessage(@QueryMap Map<String,String> params);
}
