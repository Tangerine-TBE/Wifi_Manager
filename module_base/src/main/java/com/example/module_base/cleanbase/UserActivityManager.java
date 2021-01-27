package com.example.module_base.cleanbase;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class UserActivityManager {

    private static UserActivityManager sInstance;
    public final List<Activity> mActivityStack = new ArrayList<>();
    private WeakReference<Activity> sCurrentActivityWeakRef;

    private UserActivityManager() {

    }

    public static UserActivityManager getInstance() {
        if (sInstance == null){
            sInstance = new UserActivityManager();
        }
        return sInstance;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }

}
