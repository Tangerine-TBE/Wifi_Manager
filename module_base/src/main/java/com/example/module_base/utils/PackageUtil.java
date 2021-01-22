package com.example.module_base.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.example.module_base.domain.AppInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PackageUtil {

    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    public static String packageCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return "当前版本号：" + info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "无法获取";
        }
    }

    //版本号
    public static String packageCode2(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return  info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "无法获取";
        }
    }


    /**
     * 获取当前应用程序的包名
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

    /**
     * 获取程序 图标
     * @param context
     * @param packName 应用包名
     * @return
     */
    public Drawable getAppIcon(Context context, String packName){
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            //获取到应用信息
            ApplicationInfo info = pm.getApplicationInfo(packName, 0);
            return info.loadIcon(pm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     *
     * @param sign  1、本机全部app的信息 2、系统应用的信息 3、非系统应用的信息
     * @return app的信息
     */
    public List<AppInfo> getAppInfo(Context context,int sign) {
        List<AppInfo> appList = new ArrayList(); //用来存储获取的应用信息数据　　　　　
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
            tmpInfo.setPackageName(packageInfo.packageName);
            tmpInfo.setVersionName( packageInfo.versionName);
            tmpInfo.setVersionCode(packageInfo.versionCode);
            tmpInfo.setAppIcon( packageInfo.applicationInfo.loadIcon(context.getPackageManager()));
            if (sign == 1) {//全手机全部应用
                appList.add(tmpInfo);
            } else if (sign == 2) {
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    appList.add(tmpInfo);//系统应用的信息，则添加至appList
                }
            } else if (sign == 3) {
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    appList.add(tmpInfo);//如果非系统应用，则添加至appList
                }
            }
        }
        return appList;
    }


    //检查是否需要联网
    public  static  boolean isConnectNetWorkApp(Context context,String packname){
        boolean isConnect=false;
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo =    pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
            //获取到所有的权限
            String[] requestedPermissions = packinfo.requestedPermissions;
            isConnect= Arrays.asList(requestedPermissions).contains("android.permission.INTERNET");
        } catch (Exception e) {
            e.printStackTrace();
            return isConnect;
        }
        return isConnect;
    }



    public static String  difPlatformName(Activity activity,int resource) {
           return String.format(activity.getString(resource),getAppMetaData(activity,"APP_NAME"));
    }

}
