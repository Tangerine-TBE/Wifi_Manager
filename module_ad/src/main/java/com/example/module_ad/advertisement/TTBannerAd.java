package com.example.module_ad.advertisement;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.example.module_base.base.BaseApplication;
import com.example.module_base.utils.LogUtils;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxNetTool;

import java.util.List;


public class TTBannerAd extends AdWatcher {

    private TTAdNative mTTAdNative;
    private Activity mActivity;
    private FrameLayout mBannerContainer;
    private boolean mHasShowDownloadActive = false;
    private TTNativeExpressAd mTtBannerAd;


    public TTBannerAd(Activity activity, FrameLayout frameLayout) {
        this.mActivity=activity;
        this.mBannerContainer=frameLayout;
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mActivity);
    }


    @Override
    //banner广告申请
    public void showAd() {
        if (!RxNetTool.isNetworkAvailable(BaseApplication.Companion.getApplication())) {
            return;
        }

        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mKTouTiaoBannerKey)
                .setSupportDeepLink(true)
                .setAdCount(3)
                .setExpressViewAcceptedSize(RxDeviceTool.getScreenWidth(mActivity),80) //期望模板广告view的size,单位dp
                .build();


        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int i, String s) {
                LogUtils.i("onError-------------------------->"+s);
                mBannerContainer.removeAllViews();
                if (mIShowAdCallback != null) {
                    mIShowAdCallback.onShowError();
                }

            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
                LogUtils.i("onNativeExpressAdLoad-------------------------->"+list.size());
                if (list==null&&list.size()==0) {
                    return;
                }
                mTtBannerAd = list.get(0);
                mBannerContainer.setVisibility(View.VISIBLE);
                bindAdListener(mTtBannerAd);
                mTtBannerAd.setSlideIntervalTime(30 * 1000);
                bindAdListener(mTtBannerAd);
                mTtBannerAd.render();

            }
        });

    }


    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                LogUtils.i("广告被点击-------------------------->");
            }

            @Override
            public void onAdShow(View view, int type) {
                LogUtils.i("广告展示-------------------------->");

            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                LogUtils.i(msg+"-------------------------->"+code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
                LogUtils.i(height+"onRenderSuccess-------------------------->"+width);
                if (view != null && mBannerContainer != null && !mActivity.isFinishing()) {
                    mBannerContainer.addView(view);
                }
            }
        });


        //dislike设置
        bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {

                LogUtils.i("点击开始下载-------------------------->");
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    LogUtils.i("下载中，点击暂停-------------------------->");
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {

                LogUtils.i("下载暂停，点击继续-------------------------->");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {

                LogUtils.i("下载失败，点击重新下载-------------------------->");
            }

            @Override
            public void onInstalled(String fileName, String appName) {

                LogUtils.i("安装完成，点击图片打开-------------------------->");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {

                LogUtils.i("点击安装-------------------------->");
            }
        });
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {

        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(mActivity, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                //用户选择不喜欢原因后，移除广告展示
                mBannerContainer.removeAllViews();
                mBannerContainer.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onRefuse() {

            }

        });
    }

    @Override
    public void releaseAd() {
        if (mTtBannerAd != null) {
            mTtBannerAd.destroy();
        }
    }
}
