package com.example.module_ad.advertisement;

import android.app.Activity;
import com.example.module_base.utils.LogUtils;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.ads.interstitial2.UnifiedInterstitialMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.managers.GDTADManager;
import com.qq.e.comm.util.AdError;


public class TXInsertAd extends AdWatcher implements UnifiedInterstitialADListener, UnifiedInterstitialMediaListener {

    private Activity mActivity;
    private UnifiedInterstitialAD mAd;

    public TXInsertAd(Activity activity) {
        this.mActivity=activity;
        GDTADManager.getInstance().initWith(activity, mKgdtMobSDKAppKey);

    }

    private void setVideoOption() {
        mAd.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI 环境下可以自动播放视频
                .setAutoPlayMuted(true) // 自动播放时为静音
                .build()); //
        /**
         * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前调用setVideoPlayPolicy，有助于提高视频广告的eCPM值 <br/>
         * 如果广告位仅支持图文广告，则无需调用
         */

        /**
         * 设置本次拉取的视频广告，从用户角度看到的视频播放策略<p/>
         *
         * "用户角度"特指用户看到的情况，并非SDK是否自动播放，与自动播放策略AutoPlayPolicy的取值并非一一对应 <br/>
         *
         * 如自动播放策略为AutoPlayPolicy.WIFI，但此时用户网络为4G环境，在用户看来就是手工播放的
         */
        mAd.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO); // 本次拉回的视频广告，从用户的角度看是自动播放的
    }

    public void showAd() {
        mAd = new UnifiedInterstitialAD(mActivity,mKgdtMobSDKChaPingKey, this);
    //    setVideoOption();
        mAd.loadAD();

    }

    @Override
    public void onADReceive() {
        LogUtils.i( "广告加载成功 ！ "+ Thread.currentThread().getName());
        mAd.show();
        if (mIShowAdCallback != null) {
            mIShowAdCallback.onShowSuccess();
        }
        if(mAd.getAdPatternType() == AdPatternType.NATIVE_VIDEO){
            mAd.setMediaListener(this);
        }
    }

    @Override
    public void onVideoCached() {
        LogUtils.i( "onVideoCached ！ ");
    }

    @Override
    public void onNoAD(AdError adError) {
        if (mIShowAdCallback != null) {
            mIShowAdCallback.onShowError();
        }
    }

    @Override
    public void onADOpened() {
        LogUtils.i( "onADOpened ！ ");
    }

    @Override
    public void onADExposure() {
        LogUtils.i("onADExposure ！ ");
    }

    @Override
    public void onADClicked() {
        LogUtils.i("onADClicked ！ ");
    }

    @Override
    public void onADLeftApplication() {
        LogUtils.i( "onADLeftApplication ！ ");
    }

    @Override
    public void onADClosed() {
        LogUtils.i( "onADClosed ！ ");
    }

    @Override
    public void onVideoInit() {

    }

    @Override
    public void onVideoLoading() {

    }

    @Override
    public void onVideoReady(long l) {

    }

    @Override
    public void onVideoStart() {

    }

    @Override
    public void onVideoPause() {

    }

    @Override
    public void onVideoComplete() {

    }

    @Override
    public void onVideoError(AdError adError) {

    }

    @Override
    public void onVideoPageOpen() {

    }

    @Override
    public void onVideoPageClose() {

    }


    @Override
    public void releaseAd() {
        if (mAd != null) {
            mAd.close();
            mAd.destroy();
        }
    }
}
