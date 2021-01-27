package com.example.module_base.cleanbase;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.example.module_base.base.BaseApplication;
import com.example.module_base.utils.LogUtils;
import com.example.module_base.utils.SPUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * Created by hzwangchenyan on 2017/9/20.
 */
public class ForegroundObserver implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ForegroundObserver";
    private static final long CHECK_TASK_DELAY = 500;

    private List<Observer> observerList;
    private Handler handler;
    private boolean isForeground;
    private int resumeActivityCount;

    public interface Observer {
        /**
         * 进入前台
         *
         * @param activity 当前处于栈顶的Activity
         */
        void onForeground(Activity activity);

        /**
         * 进入后台
         *
         * @param activity 当前处于栈顶的Activity
         */
        void onBackground(Activity activity);
    }

    public static void init(Application application) {
        application.registerActivityLifecycleCallbacks(getInstance());
    }

    public static ForegroundObserver getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static ForegroundObserver sInstance = new ForegroundObserver();
    }

    private ForegroundObserver() {
        observerList = Collections.synchronizedList(new ArrayList<Observer>());
        handler = new Handler(Looper.getMainLooper());
    }

    public static void addObserver(Observer observer) {
        if (observer == null) {
            return;
        }

        if (getInstance().observerList.contains(observer)) {
            return;
        }

        getInstance().observerList.add(observer);
    }

    public static void removeObserver(Observer observer) {
        if (observer == null) {
            return;
        }

        getInstance().observerList.remove(observer);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        UserActivityManager.getInstance().setCurrentActivity(activity);
        UserActivityManager.getInstance().mActivityStack.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
      /*  UserActivityManager.getInstance().setCurrentActivity(activity);
        resumeActivityCount++;
        if (!isForeground && resumeActivityCount > 0) {
            isForeground = true;
            // 从后台进入前台
            Log.i(TAG, "app in foreground");
            notify(activity, true);
            BaseConstant.INSTANCE.setForeground(true);
            if (!BaseApplication.isFromStart){
                gotoSplashADActivity(activity);
            }
        }*/
    }

    public static boolean isShowAd=true;
    private void gotoSplashADActivity(Activity activity) {
      /*  LogUtils.INSTANCE.i("ForegroundObserver：进入前台getLocalClassName："+activity.getLocalClassName());
        if (SPUtil.getInstance().getBoolean(ADConstants.AD_SPLASH_STATUS) && needSplashAD()&&isShowAd) {
            Class cls=ActivityList.SplashActivityAD.getCls();
            if (cls!=null)
                activity.startActivity(new Intent(activity,cls));
        }else {
            isShowAd=true;
        }*/
    }

    /**
     * 进入后台，再次进入app是否展示开屏广告
     * 时间超过间隔，并且有网络才展示
     *
     * @return
     */
/*    public boolean needSplashAD() {
        long current = System.currentTimeMillis();
        long background = SPUtil.getInstance().getLong(AD_APP_BACKGROUND_TIME, 0);
        long gapTime = (current - background) / 1000;
        long serverTime = SPUtil.getInstance().getLong(ADConstants.AD_SPREAD_PERIOD, 5);
        LogUtils.INSTANCE.i("ForegroundObserver gapTime==" + gapTime + ",serverTime===" + serverTime);
        return gapTime >= serverTime && NetworkUtils.isConnected(BaseApplication.getApplication());
    }*/


    @Override
    public void onActivityPaused(final Activity activity) {
       /* resumeActivityCount--;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isForeground && resumeActivityCount == 0) {
                    isForeground = false;
                    // 从前台进入后台
                    Log.i(TAG, "app in background");
                    com.feisukj.base.baseclass.ForegroundObserver.this.notify(activity, false);
                    SPUtil.getInstance().putLong(AD_APP_BACKGROUND_TIME, System.currentTimeMillis());
                    BaseConstant.INSTANCE.setForeground(false);
                }
            }
        }, CHECK_TASK_DELAY);*/
    }

    private void notify(Activity activity, boolean foreground) {
        for (Observer observer : observerList) {
            if (foreground) {
                observer.onForeground(activity);
            } else {
                observer.onBackground(activity);
            }
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        UserActivityManager.getInstance().mActivityStack.remove(activity);
    }

}
