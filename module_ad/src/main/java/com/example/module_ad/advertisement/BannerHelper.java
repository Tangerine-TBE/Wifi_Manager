package com.example.module_ad.advertisement;

import android.app.Activity;
import android.widget.FrameLayout;

import com.example.module_ad.base.AdActionBean;
import com.example.module_ad.base.AdTypeBean;
import com.example.module_ad.base.IShowAdCallback;
import com.example.module_ad.bean.AdBean;
import com.example.module_ad.utils.AdMsgUtil;
import com.example.module_ad.utils.AdProbabilityUtil;

import java.util.Map;

public class BannerHelper {

    private AdTypeBean mManager_page;
    private AdActionBean mBaseBanner_screen;
    private String mBaseAd_percent;
    private TXBannerAd mTxBannerAd;
    private TTBannerAd mTtBannerAd;
    private Activity mActivity;
    private FrameLayout mBannerContainer;
    private boolean mAddToutiaoAdError_ban = false;
    private boolean mAddTengxunAdError_ban = false;


    public BannerHelper(Activity activity, FrameLayout bannerContainer) {
        this.mActivity=activity;
        this.mBannerContainer=bannerContainer;
    }




    public void showAd(AdType type) {
        AdBean.DataBean dataBean = AdMsgUtil.getAdState();
        Map<String, String> adKey = AdMsgUtil.getADKey();
        if (dataBean != null&adKey!=null) {
            mManager_page = AdMsgUtil.switchAdType(type, dataBean);

            mBaseBanner_screen = mManager_page.getBaseBanner_screen();
            mBaseAd_percent = mBaseBanner_screen.getBaseAd_percent();
            double bannerProbability = AdProbabilityUtil.showAdProbability(mBaseAd_percent);

            //按比例展示banner
            double random = Math.random();
            if (mBaseBanner_screen.isBaseStatus()) {
                if (random >= bannerProbability) {
                    showTTBannerAd();

                } else {
                    showTXBannerAd();
                }
            }

        }

    }

    private void showTTBannerAd() {
        //TT
        mTtBannerAd = new TTBannerAd(mActivity, mBannerContainer);
        mTtBannerAd.showAd();
        mTtBannerAd.setOnShowError(new IShowAdCallback() {
            @Override
            public void onShowError() {
                if (!mAddToutiaoAdError_ban) {
                    showTXBannerAd();
                }
                mAddToutiaoAdError_ban = true;
            }

            @Override
            public void onShowSuccess() {

            }
        });

    }


    private void showTXBannerAd() {
        //TX
        mTxBannerAd = new TXBannerAd(mActivity, mBannerContainer);
        mTxBannerAd.showAd();
        mTxBannerAd.setOnShowError(new IShowAdCallback() {
            @Override
            public void onShowError() {
                if (!mAddTengxunAdError_ban) {
                    showTTBannerAd();
                }
                mAddTengxunAdError_ban = true;
            }

            @Override
            public void onShowSuccess() {

            }
        });
    }

    public void releaseAd() {
        //广点通
        if (mTxBannerAd != null) {
            mTxBannerAd.releaseAd();
        }

        if (mTtBannerAd != null) {
            mTtBannerAd.releaseAd();
        }

    }



}
