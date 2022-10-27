package com.movilix;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.movilix.Model.Model;

import androidx.fragment.app.FragmentActivity;

public class HomeAdListener implements InterstitialAdListener {
    private final String TAG = "Facebook Ads";
    Context context;
    Model model;
    ProgressBar progress_Bar;
    public HomeAdListener(FragmentActivity activity, Model mModel,ProgressBar progressBar) {
        context=activity;
        model=mModel;
        progress_Bar=progressBar;
    }

    @Override
    public void onInterstitialDisplayed(Ad ad) {
        // Interstitial ad displayed callback
        Log.e(TAG, "Interstitial ad displayed.");
    }

    @Override
    public void onInterstitialDismissed(Ad ad) {
        // Interstitial dismissed callback
        Log.e(TAG, "Interstitial ad dismissed.");
        progress_Bar.setVisibility(View.GONE);
        Intent intent = new Intent(context, MovieDetails.class);
        intent.putExtra("obj", model);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        // Ad error callback
        Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
    }



    @Override
    public void onAdLoaded(Ad ad) {
        // Interstitial ad is loaded and ready to be displayed
        Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
        // Show the ad


    }

    @Override
    public void onAdClicked(Ad ad) {
        // Ad clicked callback
        Log.d(TAG, "Interstitial ad clicked!");
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        // Ad impression logged callback
        Log.d(TAG, "Interstitial ad impression logged!");
    }
}
