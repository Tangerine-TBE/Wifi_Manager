package com.example.module_ad.utils;


import com.example.module_base.base.BaseApplication;
import com.example.module_base.utils.PackageUtil;


public class Contents {

    //是否后台
    public static final String NO_BACK = "no_back";

    //包名
    public static final String APP_PACKAGE = BaseApplication.mPackName;

    //渠道号
    public static final String PLATFORM_KEY = "CHANNEL";

    //广告
    public static final String AD_URL = "http://114.215.47.46:8080/ytkapplicaton/";

    //广告接口缓存
    public static final String AD_INFO = "ad";

    public static final String AD_TYPE = "ad_type";

    public static final String AD_TIMES = "ad_times";


    //穿山甲
    public static final String  CSJ_APPID= "5143358";


    //广告key
    //TT
    public static final String KT_OUTIAO_APPKEY = "kTouTiaoAppKey";
    public static final String KT_OUTIAO_KAIPING = "kTouTiaoKaiPing";
    public static final String KT_OUTIAO_BANNERKEY = "kTouTiaoBannerKey";
    public static final String KT_OUTIAO_CHAPINGKEY = "kTouTiaoChaPingKey";
    public static final String KT_OUTIAO_SENIORKEY = "kTouTiaoSeniorKey";
    public static final String KT_OUTIAO_JILIKEY = "kTouTiaoJiLiKey";

    //TX
    public static final String KGDT_MOBSDK_APPKEY = "kGDTMobSDKAppKey";
    public static final String KGDT_MOBSDK_CHAPINGKEY = "kGDTMobSDKChaPingKey";
    public static final String KGDT_MOBSDK_KAIPINGKEY = "kGDTMobSDKKaiPingKey";
    public static final String KGDT_MOBSDK_BANNERKEY = "kGDTMobSDKBannerKey";
    public static final String KGDT_MOBSDK_NATIVEKEY = "kGDTMobSDKNativeKey";
    public static final String KGDT_MOBSDK_JILIKEY = "kGDTMobSDKJiLiKey";

    //广告接口
    public static final String AD_NAME = "name";
    public static final String AD_VERSION = "version";
    public static final String AD_VERSION_VALUES = PackageUtil.packageCode2(BaseApplication.Companion.getApplication());
    public static final String AD_CHANNEL = "channel";

}
