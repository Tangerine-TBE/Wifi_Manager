package com.example.module_ad.advertisement;

import android.app.Activity;
import android.content.Intent;

import com.example.module_ad.base.AdTypeBean;
import com.example.module_ad.base.AdActionBean;
import com.example.module_ad.base.IShowAdCallback;
import com.example.module_ad.service.TimeService;
import com.example.module_ad.utils.AdMsgUtil;
import com.example.module_ad.utils.AdProbabilityUtil;
import com.example.module_ad.utils.Contents;
import com.example.module_user.utils.UserInfoUtil;


public class InsertHelper {

    private Activity mActivity;
    private AdTypeBean mManager_page;
    private AdActionBean mBaseInsert_screen;
    private TTInsertAd mTtInsertAd;
    private boolean mAddToutiaoAdError=false;
    private boolean mAddTengxunAdError=false;
    private TXInsertAd mTxInsertAd;
    private AdType mCurrentType;
    private int showTime;

    public InsertHelper(Activity activity) {
        this.mActivity=activity;
    }


    public void showAd(AdType type) {
        if (UserInfoUtil.isVIP()) {
            return;
        }
        if (AdMsgUtil.isHaveAdData()) {
            mCurrentType=type;
            mManager_page=AdMsgUtil.switchAdType(type, AdMsgUtil.getAdState());
            if (mManager_page != null) {
            mBaseInsert_screen = mManager_page.getBaseInsert_screen();
                if (mBaseInsert_screen != null) {
                    String baseAd_percent = mBaseInsert_screen.getBaseAd_percent();
                    showTime = mBaseInsert_screen.getShowTime();
                    double probability = AdProbabilityUtil.showAdProbability(baseAd_percent);
                    if (mBaseInsert_screen.isBaseStatus()) {
                        double random = Math.random();
                        if (random >probability) {
                            showTTInsertAd();
                        } else {
                            showTXInsertAd();
                        }
                    }
                }
            }
        }

    }


    private void showTTInsertAd() {
        mTtInsertAd = new TTInsertAd(mActivity);
        mTtInsertAd.showAd();
        mTtInsertAd.setOnShowError(new IShowAdCallback() {
            @Override
            public void onShowError() {
                if (!mAddToutiaoAdError) {
                    showTXInsertAd();
                }
                mAddToutiaoAdError=true;
            }

            @Override
            public void onShowSuccess() {
                startTimeCountDown();
            }
        });
    }


    //计时
    private void startTimeCountDown() {
        if (mActivity != null&mCurrentType!=null&showTime!=0) {
            Intent intent = new Intent(mActivity, TimeService.class)
                    .putExtra(Contents.AD_TYPE,mCurrentType.toString())
                    .putExtra(Contents.AD_TIMES,showTime);
            mActivity.startService(intent);
        }
    }


    private void showTXInsertAd() {
        mTxInsertAd = new TXInsertAd(mActivity);
        mTxInsertAd.showAd();
        mTxInsertAd.setOnShowError(new IShowAdCallback() {
            @Override
            public void onShowError() {
                if (!mAddTengxunAdError) {
                    showTTInsertAd();
                }
                mAddTengxunAdError=true;
            }

            @Override
            public void onShowSuccess() {
                startTimeCountDown();
            }
        });

    }

    public void releaseAd() {
        if (mTtInsertAd != null) {
            mTtInsertAd.releaseAd();
        }

        if (mTxInsertAd!=null) {
            mTxInsertAd.releaseAd();
        }
    }

}
