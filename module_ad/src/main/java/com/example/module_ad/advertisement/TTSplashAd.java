package com.example.module_ad.advertisement;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.MainThread;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.example.module_ad.utils.DeviceUtils;
import com.example.module_base.base.BaseApplication;

import com.example.module_base.utils.LogUtils;
import com.tamsiree.rxkit.RxNetTool;

public class TTSplashAd extends AdWatcher {

    private TTAdNative mTTAdNative;

    private FrameLayout mSplashContainer;
    private Activity mActivity;
    private boolean mIsClose;
    private Class mClass;
    private static final int AD_TIME_OUT = 2000;


    public TTSplashAd(Activity activity, FrameLayout frameLayout, boolean isClose, Class aClass) {
        this.mSplashContainer=frameLayout;
        this.mActivity=activity;
        this.mIsClose=isClose;
        this.mClass=aClass;
        mTTAdNative= TTAdManagerHolder.get().createAdNative(mActivity);
    }


    @Override
    public void showAd() {
        if (!RxNetTool.isNetworkAvailable(BaseApplication.Companion.getMContext())) {
            return;
        }
        int screenHeight = DeviceUtils.getScreenHeight(mActivity);
        int screenWidth = DeviceUtils.getScreenWidth(mActivity);
        LogUtils.i(screenHeight+"-------------------------->"+screenWidth);
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot;
        adSlot = new AdSlot.Builder()
                .setCodeId(mKTouTiaoKaiPing)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(screenWidth, screenHeight)
                .build();
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                LogUtils.i(code+"onError-------------------------->"+message);
                if (mIShowAdCallback != null) {
                    mIShowAdCallback.onShowError();
                }

            }

            @Override
            @MainThread
            public void onTimeout() {
                goToMainActivity(mIsClose);
                LogUtils.i("onTimeout-------------------------->");
            }

            @Override
            @MainThread
            public void onSplashAdLoad(com.bytedance.sdk.openadsdk.TTSplashAd ad) {
                      //获取SplashView
                View view = ad.getSplashView();
                if (view != null && mSplashContainer != null&& !mActivity.isFinishing()) {
                    mSplashContainer.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                    LogUtils.i("onSplashAdLoad-------------------------->");
                    mSplashContainer.addView(view);
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    //ad.setNotAllowSdkCountdown();
                }else {
                    goToMainActivity(mIsClose);
                    LogUtils.i("onSplashAdLoad-------------------------->");
                }

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new com.bytedance.sdk.openadsdk.TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        LogUtils.i("开屏广告点击-------------------------->");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        LogUtils.i("开屏广告展示-------------------------->");
                    }

                    @Override
                    public void onAdSkip() {
                        LogUtils.i("开屏广告跳过-------------------------->");
                        goToMainActivity(mIsClose);

                    }

                    @Override
                    public void onAdTimeOver() {
                        LogUtils.i("开屏广告倒计时结束-------------------------->");
                        goToMainActivity(mIsClose);
                    }
                });
                if(ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(new TTAppDownloadListener() {
                        boolean hasShow = false;

                        @Override
                        public void onIdle() {
                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            if (!hasShow) {
                                LogUtils.i("下载中------------------>");
                                hasShow = true;
                            }
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {

                            LogUtils.i("下载暂停------------------>");
                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {

                            LogUtils.i("下载失败------------------>");
                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {

                            LogUtils.i("下载完成------------------>");

                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {

                            LogUtils.i("安装完成------------------>");

                        }
                    });
                }
            }
        }, AD_TIME_OUT);

    }

    /**
     * 跳转到主页面
     */
    private void goToMainActivity(boolean b) {
        if (b) {
            mActivity.startActivity(new Intent(mActivity,mClass));
            mActivity.finish();
        } else {
            mActivity.finish();
        }
    }


}
