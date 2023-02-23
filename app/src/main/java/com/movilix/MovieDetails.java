package com.movilix;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.ads.Ad;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.movilix.Model.Torrent;
import com.movilix.ads.populateNativeAdView;
import com.movilix.inteface.DownloaditemClick;
import com.movilix.torrant.ui.AboutAlertDialog;
import com.movilix.torrant.ui.addtorrent.AddTorrentActivity;
import com.squareup.picasso.Picasso;
import com.movilix.adapters.CastAdapter;
import com.movilix.adapters.GenereAdapter;
import com.movilix.adapters.TorrentAdaper;
import com.movilix.adapters.homeAdapter;
import com.movilix.adapters.qualityAdapter;
import com.movilix.Model.Model;
import com.movilix.YoutubeKey.PlayerConfig;
import com.movilix.appInstance.Singleton;
//import com.movilix.appInstance.application;
import com.movilix.dialog.image;
import com.movilix.praser.Praser;
import com.movilix.services.conReceier;
import com.movilix.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.VISIBLE;
import static com.movilix.MainApplication.AD_NATIVE_3_KEY;
import static com.movilix.MainApplication.AD_NATIVE_4_KEY;
import static com.movilix.MainApplication.AD_REWARDED_KEY;
import static com.movilix.torrant.ui.AboutAlertDialog.newInstance;

public class MovieDetails extends YouTubeBaseActivity implements View.OnClickListener,
        conReceier.ConnectivityReceiverListener,View.OnTouchListener, DownloaditemClick {
    Model model;
    Snackbar snackbar;
    TextView name, year, imdrrating, downloads, likes, synopsis, language, runtime,comming_soon;
    String trailer_code;
    ImageView backimage, coverimage, shot, shot1, shot3;
    RecyclerView genere, quality, torents, castt, sugestion;
    //Quailty Adapter
    qualityAdapter qualityAdapter;
    ProgressBar progressBar;
    GenereAdapter genereAdapter;
    TorrentAdaper torrentAdaper;
    homeAdapter homeAdapter;
    CastAdapter castAdapter;
    public AboutAlertDialog rewardDialog;
    private RewardedAd rewardedAd = null;
    private static final String TAG_PERM_DIALOG_IS_SHOW = "perm_dialog_is_show";
    private static final String TAG_ABOUT_DIALOG = "about_dialog";
    private Torrent mTorrent;
    //ArrayLIst
    ArrayList<Model> sugesstionlist = new ArrayList<>();
    Praser p;
    YouTubePlayerView youTubePlayerView;
    LinearLayout trailer_lyout;
    RelativeLayout youtube_layout;
    ScrollView main_scroll;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    YouTubePlayer youTube_Player;
    View trailer_layout;
    private AdView adView,adView1,adView2;

    populateNativeAdView nativeAdView,nativeAdView1;
    NativeAd nativeAd;
    FrameLayout fl_adplaceholder,fl_adplaceholder1;
    final Activity activity=null ;
    SharedPref sharedPref;
    Integer rem_points, reward_points;

    InterstitialAd interstitialAd;
    public final String TAG = "facebook";
    public AdListener interstitialAdListener;
    public static InterstitialAd rewardedVideoAd;
    public  static RewardedVideoAdListener rewardedVideoAdListener;

    private ShimmerFrameLayout mShimmerViewContainer1,mShimmerViewContainer2,
            mShimmerViewContainer3,mShimmerViewContainer4,
            mShimmerViewContainer5,mShimmerViewContainer6,mShimmerViewContainer7;
    private ScrollView scrollView_detail,shimmerScroll;

    float dX;
    float dY;
    int lastAction;
    DownloaditemClick downloaditemClick;


    //Trailer
    String torrentURl,cover_img;
    private Button trailer_btn, trailer_close_btn,watch_btn;

    private com.google.android.gms.ads.interstitial.InterstitialAd mInter_Admob,mInter_Admob2;

    private View.OnClickListener trailer_btn_listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


                    youtube_open();

                    // Code to be executed when when the interstitial ad is closed.


                //Toast.makeText(getApplicationContext(),""+mtraileradd.isLoaded(),Toast.LENGTH_LONG).show();



        }
    };
    private View.OnClickListener trailer_close_btn_listner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            youtube_close();
        }
    };

    public void loadAd1(){
        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(MovieDetails.this,
                getResources().getString(R.string.download_ads), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull @NotNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        mInter_Admob = interstitialAd;
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInter_Admob = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        mInter_Admob = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                        Log.i("admob", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.i("admob", loadAdError.getMessage());
                        mInter_Admob = null;
                    }
                });
    }


//    private String TAG="fbads";
    //private com.facebook.ads.InterstitialAd fbinterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details_v1);
// In Activity's onCreate() for instance
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        loadAd1();

        Intent intent = getIntent();
        model = (Model) intent.getSerializableExtra("obj");
        p = new Praser(getApplicationContext());
        if (model != null) {
            init();

//            checkConnection();
            addreqDe("https://yts.lt/api/v2/movie_details.json?movie_id=" + model.getId() + "&with_images=true&with_cast=true");
            setDetails();
            loadImge();
            requestSuggestion();
            trailer_btn.setOnClickListener(trailer_btn_listner);
            trailer_close_btn.setOnClickListener(trailer_close_btn_listner);
            sharedPref = SharedPref.getInstance(this);
            interstitialAd = new InterstitialAd(this, "194509545726647_195904818920453");
            interstitialAdListener= new AdListener();

            interstitialAd.loadAd(
                    interstitialAd.buildLoadAdConfig()
                            .withAdListener(new InterstitialAdListener() {
                                @Override
                                public void onInterstitialDisplayed(Ad ad) {

                                }

                                @Override
                                public void onInterstitialDismissed(Ad ad) {

                                    sharedPref.saveData("points", "10");
                                    Intent torrentIntent = new Intent(getApplicationContext(), AddTorrentActivity.class);
                                    torrentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    torrentIntent.setData(Uri.parse(mTorrent.getUrl()));
                                    startActivity(torrentIntent);
//                                    Toast.makeText(MovieDetails.this,"Coins Granted",Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(Ad ad, com.facebook.ads.AdError adError) {

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
                            })
                            .build());


            loadRewardedAd();


            //watch_btn.setOnClickListener(watch_btn_listner);
        }
    }

    void init() {

        mShimmerViewContainer1 = findViewById(R.id.shimmer_detail_container1);
        mShimmerViewContainer2 = findViewById(R.id.shimmer_detail_container2);
        mShimmerViewContainer3 = findViewById(R.id.shimmer_detail_container3);
        mShimmerViewContainer4 = findViewById(R.id.shimmer_detail_container4);
        mShimmerViewContainer5 = findViewById(R.id.shimmer_detail_container5);
        mShimmerViewContainer6 = findViewById(R.id.shimmer_detail_container6);
        mShimmerViewContainer7 = findViewById(R.id.shimmer_detail_container7);
        shimmerScroll= findViewById(R.id.shimmer_detail_scroll);
        scrollView_detail= findViewById(R.id.scrollView_detail);
        mShimmerViewContainer1.startShimmer();
        mShimmerViewContainer2.startShimmer();
        mShimmerViewContainer3.startShimmer();
        mShimmerViewContainer4.startShimmer();
        mShimmerViewContainer5.startShimmer();
        mShimmerViewContainer6.startShimmer();
        mShimmerViewContainer7.startShimmer();
        downloaditemClick = this;

//        rewardDialog= (AboutAlertDialog)getSupportFragmentManager().findFragmentByTag(TAG_ABOUT_DIALOG);
        sharedPref = SharedPref.getInstance(this);
        rem_points = Integer.parseInt(sharedPref.getData("points"));
        trailer_lyout=findViewById(R.id.trailer_btn_lyout);
        name = findViewById(R.id.Title_movie);
        year = findViewById(R.id.year_movie);
        imdrrating = findViewById(R.id.imdRating);
        downloads = findViewById(R.id.downloads);
        likes = findViewById(R.id.likes);
        runtime = findViewById(R.id.rutime);
        language = findViewById(R.id.language);
        synopsis = findViewById(R.id.synopsis_text);
        comming_soon = findViewById(R.id.comming_soon);
        fl_adplaceholder=findViewById(R.id.fl_adplaceholder);
        fl_adplaceholder1=findViewById(R.id.fl_adplaceholder1);
        progressBar = findViewById(R.id.progressBar_cyclic1);
//images
        backimage = findViewById(R.id.backimg);
        coverimage = findViewById(R.id.overlapImage);
//Recyle Views
        quality = findViewById(R.id.qualityrecyle);
        quality.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        //Gerner recyle
        genere = findViewById(R.id.genre_movie_recyle);
        genere.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
// Torrents Adapter
        torents = findViewById(R.id.torrentrecyle);
        torents.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        // Suggetion

//Cast
        castt = findViewById(R.id.castrecyle);
        castt.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        //
        shot = findViewById(R.id.shot1);

        shot3 = findViewById(R.id.shot3);
        shot1 = findViewById(R.id.shot2);
        shot.setOnClickListener(this);
        shot1.setOnClickListener(this);
        shot3.setOnClickListener(this);
//Trailer Views Settings
        trailer_btn = findViewById(R.id.trailer_btn);
        trailer_close_btn = findViewById(R.id.trailer_close_btn);
        youTubePlayerView = findViewById(R.id.youtube_player);
        youtube_layout = findViewById(R.id.youtube_layout);
        main_scroll = findViewById(R.id.scrollView_detail);
        watch_btn=findViewById(R.id.download_btn);
        trailer_layout= (this.<View>findViewById(R.id.trailer));

        final View trailerView = findViewById(R.id.trailer);
        trailerView.setOnTouchListener(this);
//        adView = new AdView(this, "194509545726647_195892325588369", AdSize.BANNER_HEIGHT_50);
//        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
//        adContainer.addView(adView);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mShimmerViewContainer1.stopShimmer();
                mShimmerViewContainer2.stopShimmer();
                mShimmerViewContainer3.stopShimmer();
                mShimmerViewContainer4.stopShimmer();
                mShimmerViewContainer5.stopShimmer();
                mShimmerViewContainer6.stopShimmer();
                mShimmerViewContainer7.stopShimmer();
                shimmerScroll.setVisibility(View.GONE);
                scrollView_detail.setVisibility(View.VISIBLE);

            }
        }, 1000  * 3);

        nativeAdView=new populateNativeAdView();
        try{
            nativeAdView.loadNativeAd(this,AD_NATIVE_3_KEY,nativeAd,fl_adplaceholder);
        }
        catch (Exception e){
            Log.d(TAG, "onNativeAdload: error");
            e.printStackTrace();
        }
        nativeAdView1=new populateNativeAdView();
        try{
            nativeAdView1.loadNativeAd(this,AD_NATIVE_4_KEY,nativeAd,fl_adplaceholder1);
        }
        catch (Exception e){
            Log.d(TAG, "onNativeAdload: error");
            e.printStackTrace();
        }
    }

    void setDetails() {
        name.setText("" + model.getTitle_english());
        year.setText("" + model.getYear());
        imdrrating.setText("" + model.getRating());
        synopsis.setText("" + model.getSynopsis());
        language.setText("" + model.getLanguage());
        runtime.setText("" + model.getRuntime());
        qualityAdapter = new qualityAdapter(getApplicationContext(), model.getTorrents());
        quality.setAdapter(qualityAdapter);
        cover_img=model.getLarge_cover_image();
        //genr adapter
        genereAdapter = new GenereAdapter(getApplicationContext(), model.getGener());
        genere.setAdapter(genereAdapter);
        //Torrent
        if (model.getTorrents().isEmpty()){
            comming_soon.setVisibility(VISIBLE);
        }
        torrentAdaper = new TorrentAdaper(this, getApplicationContext(), model,downloaditemClick);
        torents.setAdapter(torrentAdaper);
        synopsis.setMovementMethod(new ScrollingMovementMethod());
        //trailer_code
        trailer_code = model.getYts_trailer_code();
        if(!trailer_code.isEmpty()){
            trailer_lyout.setVisibility(VISIBLE);
            PlayerInit();
            youTubePlayerView.initialize(PlayerConfig.API_KEY,onInitializedListener);
        }
        else {
            trailer_lyout.setVisibility(View.GONE);
        }
//        PlayerInit();

//        adView.loadAd();
    }

    void loadImge() {
        Picasso.get().load(model.getLarge_cover_image()).into(coverimage);
        Picasso.get().load(model.getLarge_cover_image()).into(backimage);


    }

    void requestSuggestion() {
       // addreq("https://yts.lt/api/v2/movie_suggestions.json?movie_id=" + model.getId());
    }

    void addreq(String url) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                               p.JsonPrase(response);
                            homeAdapter.notifyDataSetChanged();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();


            }
        });

        Singleton.getInstance(this).addToRequestQueue(request);
    }

    void addreqDe(String url) {
//        boolean isConnected = conReceier.isConnected();
//        if (isConnected) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (response != null) {
                                Model model = p.JsonCast(response);
                                if (model != null) {
                                    downloads.setText("" + model.getDownload());
                                    likes.setText("" + model.getLike());
                                    Picasso.get().load(model.getShot1()).into(shot);
                                    Picasso.get().load(model.getShot2()).into(shot1);
                                    Picasso.get().load(model.getShot3()).into(shot3);
                                    castAdapter = new CastAdapter(getApplicationContext(), model.getCasts());
                                    castt.setAdapter(castAdapter);
                                    castAdapter.notifyDataSetChanged();
                                }
                            }
                        }


                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            Singleton.getInstance(this).addToRequestQueue(request);
//        } else
//            showSnack(isConnected);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shot1:


                Bitmap bitmap = ((BitmapDrawable) shot.getDrawable()).getBitmap();
                image im = new image(MovieDetails.this, bitmap);
                im.show();
                break;
            case R.id.shot2:
                Bitmap bitmap1 = ((BitmapDrawable) shot1.getDrawable()).getBitmap();
                image im1 = new image(MovieDetails.this, bitmap1);
                im1.show();

                break;
            case R.id.shot3:
                Bitmap bitmap2 = ((BitmapDrawable) shot3.getDrawable()).getBitmap();
                image im2 = new image(MovieDetails.this, bitmap2);
                im2.show();

                break;
        }
    }

    void PlayerInit() {

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {



                    youTube_Player = youTubePlayer;


            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onInitializationFailure: Youtube error " +youTubeInitializationResult.toString());
            }

        };
    }




    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }


    // Method to manually check connection status
//    private void checkConnection() {
////        boolean isConnected = conReceier.isConnected();
//        showSnack(isConnected);
//    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        if (isConnected) {

        } else if (!isConnected) {
            snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

//                            checkConnection();

                        }
                    });
            snackbar.setActionTextColor(Color.RED);

            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
//        application.getInstance().setConnectivityListener(this);

    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                break;

            default:
                return false;
        }
        return true;
    }

    public void youtube_open(){
//        youTubePlayerView.initialize(PlayerConfig.API_KEY,onInitializedListener);

        if (trailer_code != null) {
            if(youTube_Player != null) {
                trailer_layout.setVisibility(VISIBLE);
                main_scroll.setAlpha(0.9f);
                youTube_Player.loadVideo(trailer_code);
            }else {
                youTubePlayerView.initialize(PlayerConfig.API_KEY,onInitializedListener);
            }
//                    youTubePlayer.cueVideo(trailer_code);

        }
//        youTube_Player.play();

    }
    public void youtube_close(){
        if(youTube_Player!=null) {
            if (youTube_Player.isPlaying()) {
                youTube_Player.pause();
            }
        }
        trailer_layout.setVisibility(View.GONE);
        main_scroll.setAlpha(1);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            youtube_open();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            youtube_open();
        }
    }




    @Override
    protected void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        if (youTube_Player != null) {
            youTube_Player.release();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }

    @Override
    public void onItemDownload(Torrent torrent) {
        mTorrent=torrent;
        sharedPref = SharedPref.getInstance(this);
        rem_points = Integer.parseInt(sharedPref.getData("points"));
//        if( rem_points > 2) {
////            Log.d("torrent", torrent.getHash().toString());
//            sharedPref.saveData("points", String.valueOf(rem_points - 3));
//            Intent torrentIntent = new Intent(this, AddTorrentActivity.class);
//            torrentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            torrentIntent.setData(Uri.parse(torrent.getUrl()));
//            startActivity(torrentIntent);
//        }
//        else{
        showCustomDialog();
//            Toast.makeText(this,"Points Low, please watch video to get more points",Toast.LENGTH_LONG).show();
//        }
    }
//    private void showAboutDialog()
//    {
//        FragmentManager fm = getSupportFragmentManager();
//        if (fm.findFragmentByTag(TAG_ABOUT_DIALOG) == null) {
//            rewardDialog = newInstance(
//                    getString(R.string.about_title),
//                    null,
//                    R.layout.dialog_about,
//                    getString(R.string.ok),
//                    getString(R.string.about_changelog),
//                    null,
//                    true);
//            rewardDialog.show(fm, TAG_ABOUT_DIALOG);
//
//        }
//    }
    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.reward_dialog, viewGroup, false);
//        Button ok = dialogView.findViewById(R.id.buttonOk);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setCancelable(false);

        builder.setPositiveButton("Let me watch", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressBar.setVisibility(View.VISIBLE);
                showRewardedVideo();
            }
        });


        builder.setNegativeButton("Cancle", null);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        TextView titleView = new TextView(this);
        titleView.setText("Alert");
        titleView.setGravity(Gravity.CENTER);
        titleView.setPadding(20, 20, 20, 20);
        titleView.setTextSize(20F);
        titleView.setTypeface(Typeface.DEFAULT_BOLD);
        titleView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        titleView.setTextColor(ContextCompat.getColor(this, R.color.background));
        alertDialog.setCustomTitle(titleView);


        alertDialog.show();
        alertDialog.setOnShowListener((DialogInterface dialogInterface) -> {
            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            if (positiveButton != null) {
                positiveButton.setOnClickListener((v) -> {
//                   alertDialog.dismiss();
                    progressBar.setVisibility(View.VISIBLE);
                    showRewardedVideo();
                });
            }
            if (negativeButton != null) {
                negativeButton.setOnClickListener((v) -> {
                    alertDialog.dismiss();
                });
            }

        });
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//            }
//        });

    }

    private void showRewardedVideo() {

        if (rewardedAd != null) {
            progressBar.setVisibility(View.GONE);
            rewardedAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d(TAG, "rewarded onAdShowedFullScreenContent");
//                        Toast.makeText(this, "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
//                                .show();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when ad fails to show.
                            Log.d(TAG, "rewarded onAdFailedToShowFullScreenContent");
                            // Don't forget to set the ad reference to null so you
                            // don't show the ad a second time.
                            rewardedAd = null;
//                        Toast.makeText(
//                                MainActivity.this, "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
//                                .show();
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Don't forget to set the ad reference to null so you
                            // don't show the ad a second time.
                            rewardedAd = null;
                            Log.d(TAG, "rewarded onAdDismissedFullScreenContent");
//                        Toast.makeText(this, "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT)
//                                .show();
                            // Preload the next rewarded ad.
                            Intent torrentIntent = new Intent(getApplicationContext(), AddTorrentActivity.class);
                            torrentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            torrentIntent.setData(Uri.parse(mTorrent.getUrl()));
                            startActivity(torrentIntent);
                            loadRewardedAd();
                        }
                    });
            Activity activityContext = this;
            rewardedAd.show(
                    activityContext,
                    new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            Log.d("TAG", "The user earned the reward.");
                            sharedPref.saveData("points", "10");


                        }
                    });
            return;
        }
        else if(interstitialAd != null && interstitialAd.isAdLoaded())  {

            progressBar.setVisibility(View.GONE);
            interstitialAd.show();


        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
//                    sharedPref.saveData("points", "10");
//                    Toast.makeText(MovieDetails.this,"Coins Granted",Toast.LENGTH_LONG).show();
                    Intent torrentIntent = new Intent(getApplicationContext(), AddTorrentActivity.class);
                    torrentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    torrentIntent.setData(Uri.parse(mTorrent.getUrl()));
                    startActivity(torrentIntent);
                }
            }, 3000);

        }



    }

    private void loadRewardedAd() {

        if (rewardedAd == null) {

            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    this,
                    AD_REWARDED_KEY,
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            Log.d(TAG, loadAdError.getMessage());
                            rewardedAd = null;


                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            MovieDetails.this.rewardedAd = rewardedAd;
                            Log.d(TAG, "onAdLoaded");


                        }
                    });
        }
    }
}
