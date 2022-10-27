package com.movilix.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.movilix.MainActivity;
import com.movilix.R;
import com.movilix.ads.populateNativeAdView;

import static android.content.ContentValues.TAG;

public class ExitDialog extends Dialog
{
    NativeAd nativeAd;
    FrameLayout fl_adplaceholder;
    populateNativeAdView nativeAdView;
    Activity activity;
    NativeAd nativeloadedAd;
    public ExitDialog(Activity activity, NativeAd native_Ad)
    {
        super(activity);
        this.activity = activity;
        this.nativeloadedAd = native_Ad;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exit_dialog);
        Button yes =findViewById(R.id.btn_yes);
        Button no =findViewById(R.id.btn_no);

        fl_adplaceholder=(FrameLayout) findViewById(R.id.fl_adplaceholder2);
        nativeAdView=new populateNativeAdView();
        if(nativeloadedAd !=null){
            nativeAdView.loadNativeAd2(activity,nativeloadedAd,fl_adplaceholder);
        }else {
            try{
                nativeAdView.loadNativeAd(activity,activity.getResources().getString(R.string.main_list_native),nativeAd,fl_adplaceholder);
            }
            catch (Exception e){
                Log.d(TAG, "onNativeAdload: error");
                e.printStackTrace();
            }
        }

        yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                activity.finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dismiss();
            }
        });

    }
}
