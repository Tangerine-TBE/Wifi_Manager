package com.example.module_ad.request;

import java.util.Map;

/**
 * @author wujinming QQ:1245074510
 * @name td_horoscope
 * @class name：com.example.module_ad.request
 * @class describe
 * @time 2020/11/10 11:04
 * @class describe
 */
public interface IAdPresent  {
    void getAdMsg();

    void registerCallback(IAdCallback iAdCallback);

    void unregisterCallback(IAdCallback iAdCallback);
}
