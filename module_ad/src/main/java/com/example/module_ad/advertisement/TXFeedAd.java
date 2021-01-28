package com.example.module_ad.advertisement;

import android.app.Activity;
import android.widget.FrameLayout;
import com.example.module_base.base.BaseApplication;
import com.example.module_base.utils.LogUtils;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.managers.GDTADManager;
import com.qq.e.comm.util.AdError;
import com.tamsiree.rxkit.RxNetTool;

import java.util.List;


public class TXFeedAd extends AdWatcher {

    private NativeExpressAD nativeExpressAD;
    private NativeExpressADView nativeExpressADView;
    private Activity mActivity;
    private FrameLayout mFeedContainer;


    public TXFeedAd(Activity activity, FrameLayout frameLayout) {
        this.mActivity=activity;

        this.mFeedContainer=frameLayout;
        GDTADManager.getInstance().initWith(activity, mKgdtMobSDKAppKey);
    }


    @Override
    public void showAd() {

        if (!RxNetTool.isNetworkAvailable(BaseApplication.Companion.getApplication())) {
            return;
        }
        nativeExpressAD = new NativeExpressAD(mActivity, new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT),mKgdtMobSDKNativeKey, new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onADLoaded(List<NativeExpressADView> adList) {
                // 释放前一个 NativeExpressADView 的资源
                if (nativeExpressADView != null) {
                    nativeExpressADView.destroy();
                }
                // 3.返回数据后，SDK 会返回可以用于展示 NativeExpressADView 列表
                nativeExpressADView = adList.get(0);
                if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                    nativeExpressADView.setMediaListener(mediaListener);
                }
                nativeExpressADView.render();
               /* if (mFeedContainer.getChildCount() > 0) {

                }*/
                LogUtils.i(  "onADLoaded ----------------->" );
                // 需要保证 View 被绘制的时候是可见的，否则将无法产生曝光和收益。

                mFeedContainer.removeAllViews();
                mFeedContainer.addView(nativeExpressADView);

            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {
                LogUtils.i("NativeExpressADView 渲染广告失败----------------->" );
                if (mIShowAdCallback != null) {
                    mIShowAdCallback.onShowError();
                }
            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                LogUtils.i( "渲染广告成功----------------->" );

            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {
                LogUtils.i(  "广告曝光----------------->" );

            }

            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {
                LogUtils.i(   "当广告点击----------------->" );
            }

            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {
                LogUtils.i( "当广告关闭----------------->" );
            }

            @Override
            public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
                LogUtils.i("因为广告点击等原因离开当前 app 时调用----------------->" );
            }

            @Override
            public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {
                LogUtils.i(  "广告展开遮盖时调用----------------->" );
            }

            @Override
            public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

                LogUtils.i(   "广告关闭遮盖时调用----------------->" );
            }

            @Override
            public void onNoAD(AdError adError) {
                LogUtils.i("无广告填充    ----------------->" );

            }
        }); // 传入Activity
        // 注意：如果您在平台上新建原生模板广告位时，选择了支持视频，那么可以进行个性化设置（可选）
        nativeExpressAD.setVideoOption(new VideoOption.Builder()
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
        nativeExpressAD.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO); // 本次拉回的视频广告，从用户的角度看是自动播放的
        nativeExpressAD.loadAD(1);
    }

    private NativeExpressMediaListener mediaListener = new NativeExpressMediaListener() {
        @Override
        public void onVideoInit(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoLoading(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoCached(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {

        }

        @Override
        public void onVideoStart(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoPause(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoComplete(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {

        }

        @Override
        public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoPageClose(NativeExpressADView nativeExpressADView) {

        }
    };


    @Override
    public void releaseAd() {
        if (nativeExpressADView != null) {
            nativeExpressADView.destroy();
        }
    }

}
