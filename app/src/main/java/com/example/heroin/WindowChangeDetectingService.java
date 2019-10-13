package com.example.heroin;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class WindowChangeDetectingService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        //Configure these here for compatibility with API 13 and below.
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

        config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        setServiceInfo(config);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {


        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName() != null && event.getClassName() != null) {
                ComponentName componentName = new ComponentName(
                        event.getPackageName().toString(),
                        event.getClassName().toString()
                );

                ActivityInfo activityInfo = tryGetActivity(componentName);
                boolean isActivity = activityInfo != null;
                if (isActivity) {
                    Log.i("ActivityInfo", activityInfo.toString());
                    Log.i("CurrentActivity", componentName.flattenToShortString());
                    if (isNotAllowedApp(componentName)) {
                        Log.i("AccessibilityEvent", "Opening new app");
                        Intent launchHeroin = getPackageManager().getLaunchIntentForPackage("com.example.heroin");
                        startActivity(launchHeroin);
                    }
                }
            }
        }
    }

    private boolean isNotAllowedApp(ComponentName componentName) {
        boolean supportedApp = SupportedApplicationsHelper.getListOfSupportedApplications().stream().anyMatch(n -> componentName.flattenToShortString().startsWith(n));
        Log.d("Test", "Testing if app is allowed. Getting result: " + supportedApp + " for app " + componentName.flattenToShortString());
        boolean ret = !componentName.flattenToShortString().contains("com.example.heroin/.MainActivity")
                && !componentName.flattenToShortString().contains("com.android.settings");
        return !supportedApp && ret;
    }

    private ActivityInfo tryGetActivity(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onInterrupt() {
    }
}