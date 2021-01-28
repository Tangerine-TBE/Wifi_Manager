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
import com.example.module_base.utils.SizeUtils;
import com.tamsiree.rxkit.RxDeviceTool;
import com.tamsiree.rxkit.RxNetTool;

import java.util.List;

public class TTFeedAd extends AdWatcher {

    private TTAdNative mTTAdNative;
    private TTNativeExpressAd mTTAd;
    private Activity mActivity;

    private FrameLayout mFeedAdContainer;
    private float mExpressViewWidth;


    public TTFeedAd( Activity activity,FrameLayout frameLayout) {
        this.mActivity=activity;
        this.mFeedAdContainer=frameLayout;

        mTTAdNative = TTAdManagerHolder.get().createAdNative(mActivity);
    }

    @Override
    public void showAd() {

        if (!RxNetTool.isNetworkAvailable(BaseApplication.Companion.getApplication())) {
            return;
        }
        mFeedAdContainer.removeAllViews();
        mExpressViewWidth = RxDeviceTool.getScreenWidth(mActivity);
        int screenHeight = RxDeviceTool.getScreenHeight(mActivity);
        float expressViewHeight = SizeUtils.fitFeedHeight(screenHeight);
        LogUtils.i("高-------------->"+screenHeight);
        LogUtils.i("宽-------------->"+ mExpressViewWidth);
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(mKTouTiaoSeniorKey) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(mExpressViewWidth,expressViewHeight) //期望模板广告view的size,单位dp
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                LogUtils.i(code+"onError------------------------>"+message);
                mFeedAdContainer.removeAllViews();
                if (mIShowAdCallback != null) {
                    mIShowAdCallback.onShowError();
                }
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0){
                    return;
                }
                mTTAd = ads.get(0);
                bindAdListener(mTTAd);
                mTTAd.render();
            }
        });
    }
    private boolean mHasShowDownloadActive = false;
    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                LogUtils.i("广告被点击------------------------>");
            }

            @Override
            public void onAdShow(View view, int type) {

                LogUtils.i("广告展示------------------------>");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                LogUtils.i(msg+"------------------------>"+code);
                if (mIShowAdCallback != null) {
                    mIShowAdCallback.onShowError();
                }

            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
                LogUtils.i("宽：--"+width+"-----------onRenderSuccess------------->"+"高：--"+height);
                mFeedAdContainer.removeAllViews();
                mFeedAdContainer.addView(view);
            }
        });
        //dislike设置
        bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD){
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                LogUtils.i("点击开始下载------------------------>");
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    LogUtils.i("下载中，点击暂停------------------------>");
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {

                LogUtils.i("下载暂停，点击继续------------------------>");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {

                LogUtils.i("下载失败，点击重新下载------------------------>");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                LogUtils.i("安装完成，点击图片打开------------------------>");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {

                LogUtils.i("点击安装------------------------>");
            }
        });
    }

    /**
     * 设置广告的不喜欢，注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(mActivity, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                //用户选择不喜欢原因后，移除广告展示
                mFeedAdContainer.removeAllViews();
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
        if (mTTAd != null) {
            mTTAd.destroy();
        }
    }
}
