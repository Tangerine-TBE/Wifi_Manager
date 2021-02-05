package com.example.module_ad.utils;

import android.text.TextUtils;

import com.example.module_ad.advertisement.AdType;
import com.example.module_ad.base.AdTypeBean;

import com.example.module_ad.bean.AdBean;
import com.example.module_base.utils.SPUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


/**
 * @author: Administrator
 * @date: 2020/7/11 0011
 */
public class AdMsgUtil {
    public static AdBean.DataBean getAdState() {
        String ad = SPUtil.getInstance().getString(Contents.AD_INFO, "");
        if (!TextUtils.isEmpty(ad)) {
            AdBean.DataBean dataBean = new Gson().fromJson(ad, AdBean.DataBean.class);
            return dataBean;
        }
        return null;
    }

    public static Map<String, String> getADKey() {
        Map<String, String> map = new HashMap<>();
        String ad = SPUtil.getInstance().getString(Contents.AD_INFO, "");
        if (!TextUtils.isEmpty(ad)) {
            AdBean.DataBean dataBean = new Gson().fromJson(ad, AdBean.DataBean.class);
            //广告信息
            AdBean.DataBean.AdvertisementBean advertisement = dataBean.getAdvertisement();
            if (advertisement != null) {
                //穿山甲广告
                String kTouTiaoAppKey = advertisement.getKTouTiaoAppKey();
                String kTouTiaoKaiPing = advertisement.getKTouTiaoKaiPing();
                String kTouTiaoBannerKey = advertisement.getKTouTiaoBannerKey();
                String kTouTiaoChaPingKey = advertisement.getKTouTiaoChaPingKey();
                String kTouTiaoJiLiKey = advertisement.getKTouTiaoJiLiKey();
                String kTouTiaoSeniorKey = advertisement.getKTouTiaoSeniorKey();
                map.put(Contents.KT_OUTIAO_APPKEY, kTouTiaoAppKey);
                map.put(Contents.KT_OUTIAO_KAIPING, kTouTiaoKaiPing);
                map.put(Contents.KT_OUTIAO_BANNERKEY, kTouTiaoBannerKey);
                map.put(Contents.KT_OUTIAO_CHAPINGKEY, kTouTiaoChaPingKey);
                map.put(Contents.KT_OUTIAO_JILIKEY, kTouTiaoJiLiKey);
                map.put(Contents.KT_OUTIAO_SENIORKEY, kTouTiaoSeniorKey);

                //广点通广告
                String kgdtMobSDKAppKey = advertisement.getKGDTMobSDKAppKey();
                String kgdtMobSDKChaPingKey = advertisement.getKGDTMobSDKChaPingKey();
                String kgdtMobSDKKaiPingKey = advertisement.getKGDTMobSDKKaiPingKey();
                String kgdtMobSDKBannerKey = advertisement.getKGDTMobSDKBannerKey();
                String kgdtMobSDKNativeKey = advertisement.getKGDTMobSDKNativeKey();
                String kgdtMobSDKJiLiKey = advertisement.getKGDTMobSDKJiLiKey();

                map.put(Contents.KGDT_MOBSDK_APPKEY, kgdtMobSDKAppKey);
                map.put(Contents.KGDT_MOBSDK_CHAPINGKEY, kgdtMobSDKChaPingKey);
                map.put(Contents.KGDT_MOBSDK_KAIPINGKEY, kgdtMobSDKKaiPingKey);
                map.put(Contents.KGDT_MOBSDK_BANNERKEY, kgdtMobSDKBannerKey);
                map.put(Contents.KGDT_MOBSDK_NATIVEKEY, kgdtMobSDKNativeKey);
                map.put(Contents.KGDT_MOBSDK_JILIKEY, kgdtMobSDKJiLiKey);

                return map;
            }
        }
        return null;
    }


    public static boolean isHaveAdData() {
        AdBean.DataBean dataBean = getAdState();
        Map<String, String> adKey = getADKey();
        if (dataBean != null & adKey != null) {
            return true;
        }
        return false;
    }


    public static AdTypeBean switchAdType(AdType type, AdBean.DataBean dataBean) {
        if (type == AdType.SEE_DETAIL) {
            return dataBean.getSee_detail();
        } else if (type == AdType.CLEAN_FINISHED) {
            return dataBean.getClean_finished();
        } else if (type == AdType.COOLING_PAGE) {
            return dataBean.getCooling_page();
        } else if (type == AdType.COOLING_FINISHED) {
            return dataBean.getCooling_finished();
        } else if (type == AdType.BATTERY_PAGE) {
            return dataBean.getBattery_page();
        } else if (type == AdType.POWERSAVING_PAGE) {
            return dataBean.getPowersaving_page();
        } else if (type == AdType.CHARGE_PAGE) {
            return dataBean.getCharge_page();
        } else if (type == AdType.COMMONLY_USED_PAGE_SECOND_LEVEL) {
            return dataBean.getCommonly_used_page_second_level();
        } else if (type == AdType.VIDEO_MANAGEMENT) {
            return dataBean.getVideo_management();
        } else if (type == AdType.WX_QQ_SHORTVIDEO_PACKAGE_PICTURE_PAGE) {
            return dataBean.getWx_qq_shortvideo_package_picture_page();
        } else if (type == AdType.ALBUM_VIDEO_MUSIC_FILE_PACKAGE_PAGE) {
            return dataBean.getAlbum_video_music_file_package_page();
        } else if (type == AdType.EXIT_PAGE) {
            return dataBean.getExit_page();
        } else if (type == AdType.SETTING_PAGE) {
            return dataBean.getSetting_page();
        }
        else if (type == AdType.SOFTWARE_MANAGEMENT_PAGE) {
            return dataBean.getSoftware_management_page();
        }
        return dataBean.getSetting_page();
    }


}
