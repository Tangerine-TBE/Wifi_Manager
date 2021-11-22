package com.example.module_ad.advertisement;

import android.content.Context;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTCustomController;
import com.bytedance.sdk.openadsdk.TTLocation;
import com.example.module_ad.R;
import com.example.module_ad.utils.Contents;
import com.example.module_ad.utils.AdMsgUtil;
import java.util.Map;


/**
 * 可以用一个单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 */
public class TTAdManagerHolder {

    private static boolean sInit;
    private static String mId= Contents.CSJ_APPID;

    public static TTAdManager get() {
        Map<String, String> adKey = AdMsgUtil.getADKey();
        if (adKey != null) {
            mId = adKey.get(Contents.KT_OUTIAO_APPKEY);
        }
        if (!sInit) {
            throw new RuntimeException("TTAdSdk is not init, please check.");
        }
        return TTAdSdk.getAdManager();
    }

    public static void init(Context context) {
        doInit(context);
    }

    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(Context context) {
        if (!sInit) {
            TTAdSdk.init(context, buildConfig(context));
            sInit = true;
        }
    }



    private static TTAdConfig buildConfig(Context context) {
        System.out.println("TTAdConfig-------------------------->"+mId);
        return new TTAdConfig.Builder()
                .appId(mId)
                .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .appName(context.getResources().getString(R.string.app_name))
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .debug(false) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合
                .supportMultiProcess(false)
                .customController(new TTCustomController() {
                    @Override
                    public boolean isCanUseLocation() {
                        return false;
                    }

                    @Override
                    public boolean alist() {
                        return false;
                    }

                    @Override
                    public boolean isCanUsePhoneState() {
                        return false;
                    }

                    @Override
                    public String getDevImei() {
                        return super.getDevImei();
                    }

                    @Override
                    public boolean isCanUseWifiState() {
                        return false;
                    }

                    @Override
                    public boolean isCanUseWriteExternal() {
                        return false;
                    }

                    @Override
                    public String getDevOaid() {
                        return super.getDevOaid();
                    }
                })
                .build();
    }
}
