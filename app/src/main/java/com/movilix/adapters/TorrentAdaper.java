package com.movilix.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.movilix.AdListener;
import com.movilix.MovieDetails;
import com.movilix.R;
//import com.movilix.AddTorrentActivity;
import com.movilix.Model.Model;
import com.movilix.Model.Torrent;
import com.movilix.inteface.DownloaditemClick;
import com.movilix.torrant.ui.addtorrent.AddTorrentActivity;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TorrentAdaper extends RecyclerView.Adapter<TorrentAdaper.Holder> {
    Context context;
    Model model;
    Activity activity;
    ArrayList<Torrent> list;
    public final String TAG = "facebook";
    public AdListener interstitialAdListener1;
    String torrent_link;
    InterstitialAd interstitialAd;
    RewardedAd mRewardedAd;
    DownloaditemClick downloaditemClick;
    private com.google.android.gms.ads.interstitial.InterstitialAd mInter_Admob,mInter_Admob2;

    public TorrentAdaper(Activity activity, Context context, Model model,DownloaditemClick downloaditemClick) {
        this.context = context;
        this.model = model;
        this.activity = activity;
        list = this.model.getTorrents();
        this.downloaditemClick=downloaditemClick;
        //Interstitial Download button


    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.torrents, viewGroup, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
        loadAd2();
        Torrent torrent = list.get(i);
        if(torrent.getUrl().isEmpty()){
            holder.download_lyout.setVisibility(View.GONE);
        }
        else {
            holder.download_lyout.setVisibility(View.VISIBLE);
        }
        holder.size.setText(torrent.getSize());
        //holder.qualitty.setText(torrent.getQuality());
        if(torrent.getQuality().equalsIgnoreCase("720p")){
            holder.quality_img.setImageResource(R.drawable.quality_720pp);
        }else  if(torrent.getQuality().equalsIgnoreCase("1080p")){
            holder.quality_img.setImageResource(R.drawable.quality_1080pp);
        }else  if(torrent.getQuality().equalsIgnoreCase("3D")){
            holder.quality_img.setImageResource(R.drawable.quality_3dd);
        }

        MovieDetails.rewardedVideoAd = new InterstitialAd(context, context.getResources().getString(R.string.download_ads));
        interstitialAdListener1= new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                Intent torrentIntent = new Intent(context, AddTorrentActivity.class);
                torrentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                torrentIntent.setData(Uri.parse(torrent.getUrl()));
                activity.startActivity(torrentIntent);
            }
        };

        torrent_link=torrent.getUrl();



        holder.watch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloaditemClick.onItemDownload(torrent);

//                    if(mRewardedAd!=null){
//                        mRewardedAd.show(activity, new OnUserEarnedRewardListener() {
//                            @Override
//                            public void onUserEarnedReward(@NonNull @NotNull RewardItem rewardItem) {
//                                mRewardedAd=null;
//                                Intent torrentIntent = new Intent(context, AddTorrentActivity.class);
//                                torrentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                torrentIntent.setData(Uri.parse(torrent.getUrl()));
//                                activity.startActivity(torrentIntent);
//                            }
//                        });
//                    }
//                    else {
//                        if(MovieDetails.rewardedVideoAd.isAdLoaded()) {
//                            MovieDetails.rewardedVideoAd.show();
//
//                        }else{
//
//                            Intent torrentIntent = new Intent(context, AddTorrentActivity.class);
//                            torrentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            torrentIntent.setData(Uri.parse(torrent.getUrl()));
//                            activity.startActivity(torrentIntent);
//                        }
//
//                    }
//


                }




        });

        holder.subtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSubtitle();
            }
        });
    }


    void getSubtitle() {
        try {
            Intent i = new Intent("android.intent.action.MAIN");
            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            i.addCategory("android.intent.category.LAUNCHER");
            i.setData(Uri.parse("http://www.yifysubtitles.com/movie-imdb/" + model.getImdb_code()));
            context.startActivity(i);
        } catch (ActivityNotFoundException e) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yifysubtitles.com/movie-imdb/tt3501632"));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            context.startActivity(i);
        }

    }
    public void loadAd2(){
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(activity, context.getResources().getString(R.string.download_ads_test),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull @NotNull RewardedAd rewardedAd) {
                        super.onAdLoaded(rewardedAd);
                        mRewardedAd=rewardedAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        mRewardedAd=null;
                    }
                });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView qualitty, subtitle,size;
      Button watch_btn;
      CardView download_lyout;
      ImageView quality_img;
        public Holder(@NonNull View itemView) {
            super(itemView);
           // qualitty = itemView.findViewById(R.id.quality);
            subtitle = itemView.findViewById(R.id.subtitle);
            size = itemView.findViewById(R.id.size);
            watch_btn=itemView.findViewById(R.id.download_btn);
            quality_img=itemView.findViewById(R.id.quality_img);
            download_lyout=itemView.findViewById(R.id.download_lyout);

        }
    }
}
