package com.simicart.core.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by frank on 05/09/2016.
 */
public class SCApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static boolean isActive;

    public static boolean isActivityVisible() {
        return isActive;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("SCApplication","========================> onCreate");
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.e("SCApplication","========================> onActivityCreated");
        isActive = true;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.e("SCApplication","========================> onActivityStarted");
        isActive = true;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e("SCApplication","========================> onActivityResumed");
        isActive = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e("SCApplication","========================> onActivityPaused");
        isActive = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e("SCApplication","========================> onActivityStopped");
        isActive = false;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e("SCApplication","========================> onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e("SCApplication","========================> onActivityDestroyed");
    }
}
