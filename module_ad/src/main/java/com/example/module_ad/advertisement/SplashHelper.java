package com.example.module_ad.advertisement;

import android.app.Activity;
import android.widget.FrameLayout;

import com.example.module_ad.base.IShowAdCallback;
import com.example.module_ad.bean.AdBean;
import com.example.module_ad.utils.AdProbabilityUtil;
import com.example.module_ad.utils.AdMsgUtil;
import com.example.module_ad.utils.StartActivityUtil;
import com.example.module_base.base.BaseApplication;

import com.example.module_user.utils.UserInfoUtil;
import com.tamsiree.rxkit.RxNetTool;


public class SplashHelper {

    private Activity mActivity;
    private FrameLayout mSplashContainer;
    private Class mClass;
    private TTSplashAd mTtSplashAd;
    private TXSplashAd mTxSplashAd;
    private boolean mAddToutiaoAdError = false;
    private boolean mAddTengxunAdError = false;

    public SplashHelper(Activity activity,FrameLayout frameLayout,Class  aClass) {
        this.mActivity=activity;
        this.mSplashContainer=frameLayout;
        this.mClass=aClass;
    }

    public void showAd() {
        if (UserInfoUtil.isVIP()) {
            StartActivityUtil.startActivity(mActivity, mClass, true);
            return;
        }
        if (RxNetTool.isNetworkAvailable(mActivity)) {
            //广告key
            if (AdMsgUtil.isHaveAdData()) {
                final AdBean.DataBean.StartPageBean.SpreadScreenBean spread_screen = AdMsgUtil.getAdState().getStart_page().getSpread_screen();
                if (spread_screen.isStatus()) {
                    BaseApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (Math.random() > AdProbabilityUtil.showAdProbability(spread_screen.getAd_percent())) {
                                showTtSplashAd();
                            } else {
                                showTxSplashAd();
                            }
                        }
                    });
                } else {
                    StartActivityUtil.startActivity(mActivity, mClass, true);
                }
            } else {
                StartActivityUtil.startActivity(mActivity, mClass, true);
            }
        } else {
            StartActivityUtil.startActivity(mActivity, mClass, true);
        }
    }

    // 加载头条广告
    private void showTtSplashAd() {
        // TODO: 2020/7/17
        mTtSplashAd = new TTSplashAd(mActivity,  mSplashContainer, true,mClass);
        mTtSplashAd.showAd();
        mTtSplashAd.setOnShowError(new IShowAdCallback() {
            @Override
            public void onShowError() {
                if (!mAddToutiaoAdError) {
                    showTxSplashAd();
                }

                mAddToutiaoAdError = true;
                showADError();
            }

            @Override
            public void onShowSuccess() {

            }
        });


    }

    // 加载腾讯广告
    private void showTxSplashAd() {
        // TODO: 2020/7/17
        mTxSplashAd = new TXSplashAd(mActivity, mSplashContainer, true,mClass);
        mTxSplashAd.showAd();
        mTxSplashAd.setOnShowError(new IShowAdCallback() {
            @Override
            public void onShowError() {
                if (!mAddTengxunAdError) {
                    showTtSplashAd();
                }
                mAddTengxunAdError=true;
                showADError();
            }

            @Override
            public void onShowSuccess() {

            }
        });

    }

    //都加载失败
    private void showADError() {
        if (mAddTengxunAdError&mAddToutiaoAdError) {
            StartActivityUtil.startActivity(mActivity, mClass, true);
        }
    }
}
