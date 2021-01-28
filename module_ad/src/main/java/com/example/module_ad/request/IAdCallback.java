package com.example.module_ad.request;

import com.example.module_ad.bean.AdBean;

/**
 * @author wujinming QQ:1245074510
 * @name td_horoscope
 * @class name：com.example.module_ad.request
 * @class describe
 * @time 2020/11/10 11:06
 * @class describe
 */
public interface IAdCallback {
    void onLoadAdSuccess(AdBean adBean);
    void onLoadError();
}
