

package com.movilix;

import android.app.Service;
import android.util.Log;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.ads.mediationtestsuite.MediationTestSuite;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.movilix.FCM.ForceUpdateChecker;
import com.movilix.torrant.core.utils.Utils;
import com.movilix.torrant.ui.TorrentNotifier;
import com.movilix.torrant.ui.errorreport.ErrorReportActivity;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;



public class MainApplication extends MultiDexApplication
{
    @SuppressWarnings("unused")
    private static final String TAG = MainApplication.class.getSimpleName();
    public  static int homeGAdsClic=1;
    public  static int homeFbAdsClic=1;
    Service

    public static String AD_OPEN_KEY = "";
    public static String AD_HOME_KEY = "";
    public static String AD_NATIVE_1_KEY = "";
    public static String AD_NATIVE_2_KEY = "";
    public static String AD_NATIVE_3_KEY = "";
    public static String AD_NATIVE_4_KEY = "";
    public static String AD_REWARDED_KEY = "";


    public  static boolean is_first_open=false;
    private static AppOpenManager appOpenManager;
    static {
        /* Vector Drawable support in ImageView for API < 21 */
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        Utils.migrateTray2SharedPreferences(this);
        FirebaseAnalytics.getInstance(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("495eaf9e-d396-420e-9714-f489d4ace99a");
//        new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("B607EF521ED26E1F9866009C9CC991F0"));
//        List<String> testDeviceIds = Arrays.asList("B607EF521ED26E1F9866009C9CC991F0");
//        RequestConfiguration configuration =
//                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
//        MobileAds.setRequestConfiguration(configuration);
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//                Log.d(TAG, "onInitialize admob Complete: "+initializationStatus);
//            }
//        });
//        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("B607EF521ED26E1F9866009C9CC991F0")).build();
//        MobileAds.setRequestConfiguration(configuration);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.d("MyApp", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status.getDescription(), status.getLatency()));
                }



            }
        });
//        MediationTestSuite.launch(this);
        appOpenManager = new AppOpenManager(this);
        SharedPref sharedPref = SharedPref.getInstance(this);
        String first = sharedPref.getData("first");
        if (first == "" ){
            sharedPref.saveData("first", "false");
            sharedPref.saveData("points", "10");
        }
        TorrentNotifier.getInstance(this).makeNotifyChans();
        ForceUpdateChecker.with(this).onUpdateNeeded(new ForceUpdateChecker.OnUpdateNeededListener() {
            @Override
            public void onUpdateNeeded(String updateUrl) {

            }
        }).check();

        AD_HOME_KEY=sharedPref.getData("home_key");
        AD_NATIVE_1_KEY=sharedPref.getData("native_1_key");
        AD_NATIVE_2_KEY=sharedPref.getData("native_2_key");
        AD_NATIVE_3_KEY=sharedPref.getData("native_3_key");
        AD_NATIVE_4_KEY=sharedPref.getData("native_4d_key");
        AD_OPEN_KEY=sharedPref.getData("app_open_key");
        AD_REWARDED_KEY=sharedPref.getData("rewarded_key");

    }
}