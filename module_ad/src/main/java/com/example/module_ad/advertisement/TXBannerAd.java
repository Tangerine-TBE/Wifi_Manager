package com.example.module_ad.advertisement;

import android.app.Activity;
import android.graphics.Point;
import android.view.View;
import android.widget.FrameLayout;
import com.example.module_base.base.BaseApplication;
import com.example.module_base.utils.LogUtils;
import com.example.module_base.utils.Rx.RxNetTool;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.managers.GDTAdSdk;
import com.qq.e.comm.util.AdError;


public class TXBannerAd  extends AdWatcher{

    private UnifiedBannerView bv;
    private Activity mActivity;

    private FrameLayout mBannerContainer;

    public TXBannerAd(Activity activity, FrameLayout frameLayout) {
        this.mActivity=activity;
        this.mBannerContainer=frameLayout;
        GDTAdSdk.init(activity,mKgdtMobSDKAppKey);
    }

    @Override
    public void showAd() {

        if (getBanner()!=null) {
            getBanner().loadAD();
        }
    }

    private UnifiedBannerView getBanner() {
        if (RxNetTool.isNetworkAvailable(BaseApplication.Companion.getApplication())) {
        if(this.bv != null){
            mBannerContainer.removeView(bv);
            bv.destroy();
        }

        this.bv = new UnifiedBannerView(mActivity, mKgdtMobSDKBannerKey, new UnifiedBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {
                LogUtils.i("广告加载失败----------------->" );
                if (mIShowAdCallback != null) {
                    mIShowAdCallback.onShowError();
                }

            }

            @Override
            public void onADReceive() {

                LogUtils.i( "广告加载成功回调----------------->" );
            }

            @Override
            public void onADExposure() {
                LogUtils.i(  "onADExposure----------------->" );
            }

            @Override
            public void onADClosed() {
                LogUtils.i(   "当广告关闭 ----------------->" );
                mBannerContainer.setVisibility(View.GONE);
            }

            @Override
            public void onADClicked() {
                LogUtils.i("当广告点击----------------->" );
            }

            @Override
            public void onADLeftApplication() {
                LogUtils.i("由于广告点击离开 APP 时调用----------------->" );
            }
        });

        // 不需要传递tags使用下面构造函数
        // this.bv = new UnifiedBannerView(this, Constants.APPID, posId, this);
        mBannerContainer.addView(bv, getUnifiedBannerLayoutParams());
        mBannerContainer.setVisibility(View.VISIBLE);
        return this.bv;
        }
        return null;
    }

    private FrameLayout.LayoutParams getUnifiedBannerLayoutParams() {
        Point screenSize = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(screenSize);
        return new FrameLayout.LayoutParams(screenSize.x,  Math.round(screenSize.x / 6.4F));
    }

    @Override
    public void releaseAd() {
        if (bv != null) {
            bv.destroy();
        }
    }

}
