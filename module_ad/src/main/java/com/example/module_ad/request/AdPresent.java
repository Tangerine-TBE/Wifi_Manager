package com.example.module_ad.request;

import com.example.module_ad.bean.AdBean;
import com.example.module_ad.utils.Contents;
import com.example.module_base.base.BaseApplication;
import com.example.module_base.utils.PackageUtil;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wujinming QQ:1245074510
 * @name td_horoscope
 * @class name：com.example.module_ad.request
 * @class describe
 * @time 2020/11/10 11:06
 * @class describe
 */
public class AdPresent implements IAdPresent {

    private static AdPresent  sInstance;
    private final AdService adService;
    private final Map<String, String> mAdParameter;

    public static AdPresent getInstance() {
        if (sInstance == null) {
            sInstance = new AdPresent();
        }
        return sInstance;
    }
     private AdPresent() {
         adService = RetrofitUtil.getInstance().getAdService();
         mAdParameter = new HashMap<>();
         mAdParameter.put(Contents.AD_NAME, Contents.APP_PACKAGE);
         mAdParameter.put(Contents.AD_VERSION, Contents.AD_VERSION_VALUES);
         mAdParameter.put(Contents.AD_CHANNEL, PackageUtil.getAppMetaData(BaseApplication.Companion.getApplication(), Contents.PLATFORM_KEY));

     }
    @Override
    public void getAdMsg() {
        adService.getAdMessage(mAdParameter).enqueue(new Callback<AdBean>() {
            @Override
            public void onResponse(Call<AdBean> call, Response<AdBean> response) {
                if (response.code()== HttpURLConnection.HTTP_OK) {
                    AdBean body = response.body();
                    for (IAdCallback iAdCallback : iAdCallbacks) {
                        iAdCallback.onLoadAdSuccess(body);
                    }
                }
            }

            @Override
            public void onFailure(Call<AdBean> call, Throwable t) {
                for (IAdCallback iAdCallback : iAdCallbacks) {
                    iAdCallback.onLoadError();
                }
            }
        });
    }

    private List<IAdCallback> iAdCallbacks=new ArrayList<>();
    @Override
    public void registerCallback(IAdCallback iAdCallback) {
        if (!iAdCallbacks.contains(iAdCallback)) {
            iAdCallbacks.add(iAdCallback);
        }
    }

    @Override
    public void unregisterCallback(IAdCallback iAdCallback) {
        iAdCallbacks.remove(iAdCallback);
    }


}
