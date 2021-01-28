package com.example.module_ad.advertisement;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.example.module_ad.base.IBaseAdBean;
import com.example.module_ad.base.IBaseXXBean;
import com.example.module_ad.base.IShowAdCallback;
import com.example.module_ad.utils.AdProbabilityUtil;
import com.example.module_ad.utils.AdMsgUtil;
import com.example.module_user.utils.SpUtil;


public class BanFeedHelper {

    private boolean mAddToutiaoAdBanError = false;
    private boolean mAddTengxunAdBanError = false;
    private boolean mAddToutiaoAdFeedError = false;
    private boolean mAddTengxunAdFeedError = false;


    private Activity mActivity;
    private TXBannerAd mTxBannerAd;
    private TXFeedAd mTxFeedAd;
    private TTBannerAd mTtBannerAd;
    private TTFeedAd mTtFeedAd;
    private FrameLayout mFeedContainer;
    private FrameLayout mBannerContainer;
    private IBaseXXBean mBanner_screen;
    private IBaseXXBean mNative_screen;
    private IBaseAdBean mManager_page;


    public BanFeedHelper(Activity activity, FrameLayout banner, FrameLayout feed) {
        this.mActivity = activity;
        this.mBannerContainer = banner;
        this.mFeedContainer = feed;
    }




    public void releaseAd() {
        //穿山甲
        if (mTtFeedAd != null) {
            mTtFeedAd.releaseAd();
        }

        if (mTtBannerAd != null) {
            mTtBannerAd.releaseAd();
        }


        //广点通
        if (mTxBannerAd != null) {
            mTxBannerAd.releaseAd();
        }

        if (mTxFeedAd != null) {
            mTxFeedAd.releaseAd();
        }
    }





    public void showAd(AdType type) {
        if (SpUtil.isVIP()) {
            return;
        }
        mFeedContainer.setVisibility(View.VISIBLE);
        if (AdMsgUtil.isHaveAdData()) {
            //状态信息
            mManager_page=AdMsgUtil.switchAdType(type, AdMsgUtil.getAdState());
            if (mManager_page != null) {
            mBanner_screen = mManager_page.getBaseBanner_screen();
            mNative_screen = mManager_page.getBaseNative_screen();
                if (mBanner_screen != null&&mNative_screen!=null) {
            // 显示比例
            double bannerProbability = AdProbabilityUtil.showAdProbability(mBanner_screen.getBaseAd_percent());
            double nativeProbability = AdProbabilityUtil.showAdProbability(mNative_screen.getBaseAd_percent());
            // TODO: 2020/7/16
            //按比例展示banner
            double random = Math.random();
            if (mBanner_screen.isBaseStatus()) {
                if (random >= bannerProbability) {
                    showTTBannerAd();
                } else {
                    showTXBannerAd();
                }
            }

            //按比例展示feed
            if (mNative_screen.isBaseStatus()) {
                if (random >= nativeProbability) {
                    showTTFeedAd();
                } else {
                    showTXFeedAd();
                }
            }
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
                if (!mAddToutiaoAdBanError) {
                    showTXBannerAd();
                }
                mAddToutiaoAdBanError = true;
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
                if (!mAddTengxunAdBanError) {
                    showTTBannerAd();
                }
                mAddTengxunAdBanError = true;
            }

            @Override
            public void onShowSuccess() {

            }
        });
    }


    private void showTTFeedAd() {
        mTtFeedAd = new TTFeedAd(mActivity, mFeedContainer);
        mTtFeedAd.showAd();
        mTtFeedAd.setOnShowError(new IShowAdCallback() {
            @Override
            public void onShowError() {
                if (!mAddToutiaoAdFeedError) {
                    showTXFeedAd();
                }
                mAddToutiaoAdFeedError = true;
            }

            @Override
            public void onShowSuccess() {

            }
        });
    }

    private void showTXFeedAd() {
        mTxFeedAd = new TXFeedAd(mActivity, mFeedContainer);
        mTxFeedAd.showAd();
        mTxFeedAd.setOnShowError(new IShowAdCallback() {
            @Override
            public void onShowError() {
                if (!mAddTengxunAdFeedError) {
                    showTTFeedAd();
                }
                mAddTengxunAdFeedError = true;
            }

            @Override
            public void onShowSuccess() {

            }
        });
    }

}
