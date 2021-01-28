package com.example.module_ad.advertisement;

import android.app.Activity;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.example.module_base.base.BaseApplication;
import com.example.module_base.utils.LogUtils;
import com.tamsiree.rxkit.RxNetTool;

import java.util.List;


public class TTInsertAd extends AdWatcher  {

    private Activity mActivity;
    private TTNativeExpressAd mTTAd;
    private final TTAdNative mTTAdNative;
    private boolean mHasShowDownloadActive = false;

    public TTInsertAd(Activity activity) {
        mActivity=activity;
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mActivity);
    }

    public void showAd() {
        if (!RxNetTool.isNetworkAvailable(BaseApplication.Companion.getApplication())) {
            return;
        }
        //设置广告参数
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mKTouTiaoChaPingKey) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(640, 320) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响个性化模板广告的size
                .build();
        //加载广告
        mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                if (mIShowAdCallback != null) {
                    mIShowAdCallback.onShowError();
                }
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                bindAdListener(mTTAd);
                mTTAd.render();//调用render开始渲染广告
                if (mIShowAdCallback != null) {
                    mIShowAdCallback.onShowSuccess();
                }
            }
        });
        //绑定广告行为
    }


    private void bindAdListener(TTNativeExpressAd ad) {


            ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {

                @Override
                public void onAdDismiss() {

                }

                @Override
                public void onAdClicked(View view, int type) {
                    LogUtils.i("广告被点击");

                }

                @Override
                public void onAdShow(View view, int type) {
                    LogUtils.i( "广告展示");

                }

                @Override
                public void onRenderFail(View view, String msg, int code) {
                    LogUtils.i(msg+" code:"+code);

                }

                @Override
                public void onRenderSuccess(View view, float width, float height) {
                    //返回view的宽高 单位 dp
                    LogUtils.i("渲染成功");
                    //在渲染成功回调时展示广告，提升体验
                    mTTAd.showInteractionExpressAd(mActivity);
                }
            });

            if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD){
                return;
            }
            //可选，下载监听设置
            ad.setDownloadListener(new TTAppDownloadListener() {
                @Override
                public void onIdle() {
                    LogUtils.i("点击开始下载");

                }

                @Override
                public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                    if (!mHasShowDownloadActive) {
                        mHasShowDownloadActive = true;
                        LogUtils.i("下载中，点击暂停");

                    }
                }

                @Override
                public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {

                    LogUtils.i("下载暂停，点击继续");
                }

                @Override
                public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                    LogUtils.i("下载失败，点击重新下载");

                }

                @Override
                public void onInstalled(String fileName, String appName) {
                    LogUtils.i("安装完成，点击图片打开");

                }

                @Override
                public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                    LogUtils.i("点击安装");

                }
            });
        }


    @Override
    public void releaseAd() {
        if (mTTAd != null) {
            //调用destroy()方法释放
            mTTAd.destroy();
        }
    }

}
