package com.movilix.FCM;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.movilix.SharedPref;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;

/**
 * Created by Jayakumar on 11/09/17.
 */

public class ForceUpdateChecker {

    private static final String TAG = ForceUpdateChecker.class.getSimpleName();

    public static final String AD_OPEN_KEY = "ad_open_key";
    public static final String AD_HOME_KEY = "ad_home_key";
    public static final String AD_NATIVE_1_KEY = "ad_native_adv_1_key";
    public static final String AD_NATIVE_2_KEY = "ad_native_adv_2_key";
    public static final String AD_NATIVE_3_KEY = "ad_native_adv_3_key";
    public static final String AD_NATIVE_4_KEY = "ad_native_adv_4_key";
    public static final String AD_REWARDED_KEY = "ad_rewarded_key";
    public static final String KEY_UPDATE_REQUIRED = "key_updated_required";
    public static final String FB_INTER_KEY_1 = "fb_inter_1_key";
    public static final String FB_INTER_KEY_2 = "fb_inter_2_key";

//    public static final String AD_OPEN_KEY = "ad_open_key";

    private OnUpdateNeededListener onUpdateNeededListener;
    private Context context;

    public interface OnUpdateNeededListener {
        void onUpdateNeeded(String updateUrl);
    }

    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    public ForceUpdateChecker(@NonNull Context context,
                              OnUpdateNeededListener onUpdateNeededListener) {
        this.context = context;
        this.onUpdateNeededListener = onUpdateNeededListener;
    }
    public void check() {
        SharedPref sharedPref = SharedPref.getInstance(context);

        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        Log.d(TAG, "check: " );
        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean updated = task.getResult();
                    Log.d(TAG, "Config params updated: " + updated);
                    Log.d(TAG, "Config params updated: " +   remoteConfig.getBoolean(KEY_UPDATE_REQUIRED));
                    if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
                        String home_key = remoteConfig.getString(AD_HOME_KEY);
                        String native_1_key = remoteConfig.getString(AD_NATIVE_1_KEY);
                        String native_2_key = remoteConfig.getString(AD_NATIVE_2_KEY);
                        String native_3_key = remoteConfig.getString(AD_NATIVE_3_KEY);
                        String native_4_key = remoteConfig.getString(AD_NATIVE_4_KEY);
                        String app_open_key = remoteConfig.getString(AD_OPEN_KEY);
                        String rewarded_key = remoteConfig.getString(AD_REWARDED_KEY);
                        String fb_inter_1_key = remoteConfig.getString(FB_INTER_KEY_1);
                        String fb_inter_2_key = remoteConfig.getString(FB_INTER_KEY_2);

                        sharedPref.saveData("home_key",home_key);
                        sharedPref.saveData("native_1_key",native_1_key);
                        sharedPref.saveData("native_2_key",native_2_key);
                        sharedPref.saveData("native_3_key",native_3_key);
                        sharedPref.saveData("native_4d_key",native_4_key);
                        sharedPref.saveData("app_open_key",app_open_key);
                        sharedPref.saveData("rewarded_key",rewarded_key);
                        sharedPref.saveData("fb_inter_1_key",fb_inter_1_key);
                        sharedPref.saveData("fb_inter_2_key",fb_inter_2_key);
//                        Log.d(TAG, "home_key: " +   remoteConfig.getString(AD_HOME_KEY));
//                        Log.d(TAG, "Config params updated: " +   remoteConfig.getString(KEY_CURRENT_VERSION));
//                        Log.d(TAG, "Config params updated: " +   remoteConfig.getString(KEY_UPDATE_URL));


                    }
                } else {

                }
            }
        });

    }
//    public void check() {
//        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
//
//        if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
//            String currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION);
//            String appVersion = getAppVersion(context);
//            String updateUrl = remoteConfig.getString(KEY_UPDATE_URL);
//
//            if (!TextUtils.equals(currentVersion, appVersion)
//                    && onUpdateNeededListener != null) {
//                onUpdateNeededListener.onUpdateNeeded(updateUrl);
//            }
//        }
//    }
        private String getAppVersion(Context context) {
            String result = "";

            try {
                result = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0)
                        .versionName;
                result = result.replaceAll("[a-zA-Z]|-", "");
                Log.d(TAG, "check: "+result );
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, e.getMessage());
            }

            return result;
        }
//    private String getAppVersion(Context context) {
//        String result = "";
//
//        try {
//            result = context.getPackageManager()
//                    .getPackageInfo(context.getPackageName(), 0)
//                    .versionName;
//            result = result.replaceAll("[a-zA-Z]|-", "");
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.e(TAG, e.getMessage());
//        }
//
//        return result;
//    }

    public static class Builder {

        private Context context;
        private OnUpdateNeededListener onUpdateNeededListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onUpdateNeeded(OnUpdateNeededListener onUpdateNeededListener) {
            this.onUpdateNeededListener = onUpdateNeededListener;
            return this;
        }

        public ForceUpdateChecker build() {
            return new ForceUpdateChecker(context, onUpdateNeededListener);
        }

        public ForceUpdateChecker check() {
            Log.d(TAG, "check: " );
            ForceUpdateChecker forceUpdateChecker = build();
            forceUpdateChecker.check();
            return forceUpdateChecker;
        }
    }
}