package com.example.module_ad.utils;

import android.app.Activity;
import android.content.Intent;

public class StartActivityUtil  {

    public static void startActivity(Activity activity,Class aClass,boolean isFinish) {
        Intent intent = new Intent(activity, aClass);
        activity.startActivity(intent);
        if (isFinish) {
            activity.finish();
        }
    }

}
