package com.example.module_ad.advertisement;

import com.example.module_ad.base.IAdWatcher;
import com.example.module_ad.base.IShowAdCallback;
import com.example.module_ad.utils.Contents;
import com.example.module_ad.utils.AdMsgUtil;


import java.util.Map;

public  class AdWatcher implements IAdWatcher<IShowAdCallback> {


    public   String mKTouTiaoAppKey;
    public   String mKTouTiaoKaiPing;
    public   String mKTouTiaoBannerKey;
    public   String mKTouTiaoChaPingKey;
    public   String mKTouTiaoJiLiKey;
    public   String mKTouTiaoSeniorKey;
    public   String mKgdtMobSDKAppKey;
    public   String mKgdtMobSDKChaPingKey;
    public   String mKgdtMobSDKKaiPingKey;
    public   String mKgdtMobSDKBannerKey;
    public   String mKgdtMobSDKNativeKey;
    public   String mKgdtMobSDKJiLiKey;

    public AdWatcher() {
        Map<String, String> adKey = AdMsgUtil.getADKey();
        if (adKey!=null) {
            mKTouTiaoAppKey = adKey.get(Contents.KT_OUTIAO_APPKEY);
            mKTouTiaoKaiPing = adKey.get(Contents.KT_OUTIAO_KAIPING);
            mKTouTiaoBannerKey = adKey.get(Contents.KT_OUTIAO_BANNERKEY);
            mKTouTiaoChaPingKey = adKey.get(Contents.KT_OUTIAO_CHAPINGKEY);
            mKTouTiaoJiLiKey = adKey.get(Contents.KT_OUTIAO_JILIKEY);
            mKTouTiaoSeniorKey = adKey.get(Contents.KT_OUTIAO_SENIORKEY);

            mKgdtMobSDKAppKey = adKey.get(Contents.KGDT_MOBSDK_APPKEY);
            mKgdtMobSDKChaPingKey = adKey.get(Contents.KGDT_MOBSDK_CHAPINGKEY);
            mKgdtMobSDKKaiPingKey = adKey.get(Contents.KGDT_MOBSDK_KAIPINGKEY);
            mKgdtMobSDKBannerKey = adKey.get(Contents.KGDT_MOBSDK_BANNERKEY);
            mKgdtMobSDKNativeKey = adKey.get(Contents.KGDT_MOBSDK_NATIVEKEY);
            mKgdtMobSDKJiLiKey = adKey.get(Contents.KGDT_MOBSDK_JILIKEY);
        }
    }

    public IShowAdCallback mIShowAdCallback=null;
    @Override
    public void showAd() {

    }

    @Override
    public void releaseAd() {

    }

    @Override
    public void setOnShowError(IShowAdCallback iShowAdCallback) {
        this.mIShowAdCallback=iShowAdCallback;
    }


}
