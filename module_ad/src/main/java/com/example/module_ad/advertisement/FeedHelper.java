package com.example.module_ad.advertisement;

import android.app.Activity;
import android.widget.FrameLayout;

import com.example.module_ad.base.AdTypeBean;
import com.example.module_ad.base.AdActionBean;
import com.example.module_ad.base.IShowAdCallback;
import com.example.module_ad.utils.AdProbabilityUtil;
import com.example.module_ad.utils.AdMsgUtil;
import com.example.module_user.utils.UserInfoUtil;


public class FeedHelper {

    private Activity mActivity;
    private FrameLayout mFeedAdContainer;
    private boolean mAddToutiaoAdError=false;
    private boolean mAddTengxunAdError=false;
    private TTFeedAd mTTFeedAd;
    private TXFeedAd mTxFeedAd;
    private AdTypeBean mManager_page;

    public FeedHelper(Activity activity,FrameLayout frameLayout) {
        this.mActivity=activity;
        this.mFeedAdContainer=frameLayout;
    }




    public void showAd(AdType type) {
        if (UserInfoUtil.isVIP()) {
            return;}
        //拿到缓存接口信息
        if (AdMsgUtil.isHaveAdData()) {
            mManager_page=AdMsgUtil.switchAdType(type, AdMsgUtil.getAdState());
            if (mManager_page != null) {
            AdActionBean baseNative_screen = mManager_page.getBaseNative_screen();
                if (baseNative_screen != null) {
            //判断时候展示广告
            if (baseNative_screen.isBaseStatus()) {
                if (Math.random() >AdProbabilityUtil.showAdProbability( baseNative_screen.getBaseAd_percent())) {
                    //初始化广告
                    showTTFeedAd();
                } else {
                    showTXFeedAd();
                }
            }
                }
        }
        }
    }



    //头条
    private void showTTFeedAd() {
        mTTFeedAd = new TTFeedAd(mActivity, mFeedAdContainer);
        mTTFeedAd.showAd();
        mTTFeedAd.setOnShowError(new IShowAdCallback() {
            @Override
            public void onShowError() {
                if (!mAddToutiaoAdError) {
                    showTXFeedAd();
                }
                mAddToutiaoAdError=true;
            }

            @Override
            public void onShowSuccess() {

            }
        });

    }

    //腾讯
    private void showTXFeedAd() {
        mTxFeedAd = new TXFeedAd(mActivity, mFeedAdContainer);
        mTxFeedAd.showAd();
        mTxFeedAd.setOnShowError(new IShowAdCallback() {
            @Override
            public void onShowError() {
                if (!mAddTengxunAdError) {
                    showTTFeedAd();
                }
                mAddTengxunAdError=true;
            }

            @Override
            public void onShowSuccess() {

            }
        });

    }

    public void releaseAd() {
        if (mTTFeedAd != null) {
            mTTFeedAd.releaseAd();
        }

        if (mTxFeedAd!=null) {
            mTxFeedAd.releaseAd();
        }
    }


}
