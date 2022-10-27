package com.movilix.appInstance;

import android.app.Application;
import android.content.Context;

import com.movilix.MainApplication;
//import com.movilix.torrant.core.utils.Utils;
import com.movilix.services.conReceier;



//@ReportsCrashes(mailTo = "teamandsofts@gmail.com",
//        mode = ReportingInteractionMode.DIALOG,
//        reportDialogClass = ErrorReportActivity.class)

public class application extends Application {

    @SuppressWarnings("unused")
    private static final String TAG = MainApplication.class.getSimpleName();
    private static application mInstance;

    public static synchronized application getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public void setConnectivityListener(conReceier.ConnectivityReceiverListener listener) {
        conReceier.connectivityReceiverListener = listener;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

//        Utils.migrateTray2SharedPreferences(this);

    }
}