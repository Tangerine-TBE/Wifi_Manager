package com.example.module_ad.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import com.example.module_ad.bean.AdBean;
import com.example.module_ad.ui.activity.BackActivity;
import com.example.module_base.utils.LogUtils;
import com.example.module_base.utils.SPUtil;
import com.tamsiree.rxkit.RxNetTool;

import java.util.List;
public class BaseBackstage {
    private static boolean isShow=false;
    public static boolean isExit=false;
    private static CountDownTimer mStart;
    private static int mShowTime=1000;


    private static boolean isAppOnForeground(Context activity) {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) activity.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = activity.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {

                return true;
            }
        }
        return false;
    }


    public static void setStop(Context activity) {
        AdBean.DataBean adState = AdMsgUtil.getAdState();
        if (adState != null) {
            AdBean.DataBean.StartPageBean.SpreadScreenBean spread_screen = adState.getStart_page().getSpread_screen();
            int times = spread_screen.getTimes();
            mShowTime=times*1000;
            if (!isAppOnForeground(activity)) {
                mStart = new CountDownTimer(mShowTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                    @Override
                    public void onFinish() {
                        isShow = true;
                    }
                }.start();
            }
        }
    }


    public static void setBackstage(Context context) {
        LogUtils.i("------------isExit-----------------"+isExit);
        boolean no_back = SPUtil.getInstance().getBoolean(Contents.NO_BACK, false);
        if (!no_back) {
            if (RxNetTool.isNetworkAvailable(context)) {
                AdBean.DataBean adState = AdMsgUtil.getAdState();
                if (adState != null ) {
                    if (adState.getStart_page()!=null) {
                        if (adState.getStart_page().getSpread_screen().isStatus()) {
                            if (isShow) {
                                if (!isExit) {
                                    Intent intent = new Intent(context, BackActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }

                            }
                            if (mStart != null) {
                                mStart.cancel();
                            }
                            isShow=false;
                        }
                    }
                    isExit=false;

                }
            }
        }

    }
}
