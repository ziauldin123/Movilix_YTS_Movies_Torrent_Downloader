package com.movilix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import in.myinnos.inappupdate.InAppUpdate;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.format.Formatter;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.InterstitialAd;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.movilix.FCM.ForceUpdateChecker;
import com.movilix.adapters.ViewPageAdapter;
import com.movilix.adapters.ViewStatePagerAdapter;


import com.movilix.ads.populateNativeAdView;
import com.movilix.dialog.ExitDialog;
import com.movilix.services.conReceier;
import com.movilix.torrant.core.model.TorrentInfoProvider;
import com.movilix.torrant.core.model.data.SessionStats;

import com.movilix.torrant.core.utils.Utils;
import com.movilix.torrant.receiver.NotificationReceiver;
import com.movilix.torrant.ui.AboutAlertDialog;
import com.movilix.torrant.ui.BaseAlertDialog;
import com.movilix.torrant.ui.FragmentCallback;
import com.movilix.torrant.ui.detailtorrent.BlankFragment;
import com.movilix.torrant.ui.detailtorrent.DetailTorrentActivity;
import com.movilix.torrant.ui.detailtorrent.DetailTorrentFragment;
import com.movilix.torrant.ui.log.LogActivity;
import com.movilix.torrant.ui.main.MainViewModel;
import com.movilix.torrant.ui.main.MsgMainViewModel;
import com.movilix.torrant.ui.main.drawer.DrawerExpandableAdapter;
import com.movilix.torrant.ui.main.drawer.DrawerGroup;
import com.movilix.torrant.ui.main.drawer.DrawerGroupItem;


import java.util.List;

import static com.movilix.MainApplication.AD_NATIVE_2_KEY;
import static com.movilix.MainApplication.is_first_open;
import static com.movilix.services.conReceier.isConnected;
import static com.movilix.torrant.ui.AboutAlertDialog.newInstance;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
        , NavigationView.OnNavigationItemSelectedListener, FragmentCallback,
        conReceier.ConnectivityReceiverListener{


    public DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    String TAG = "COVID";
    ViewPager viewPager;
    ViewPager2 viewPager2;
    BottomNavigationView navigationView;
    ImageView drawerImg;
    ViewPageAdapter pagerAdapter;
    ViewStatePagerAdapter statePagerAdapter;
    Snackbar snackbar;
    RelativeLayout start_torrent;
    populateNativeAdView nativeAdView;
    NativeAd nativeAd;
    FrameLayout fl_adplaceholder;
    androidx.appcompat.app.AlertDialog alertDialog;
//    torrent_main
    UnifiedNativeAd adobj;

    private static final String TAG_PERM_DIALOG_IS_SHOW = "perm_dialog_is_show";
    private static final String TAG_ABOUT_DIALOG = "about_dialog";

    public static final String ACTION_ADD_TORRENT_SHORTCUT = "com.movilix.ADD_TORRENT_SHORTCUT";

    /* Android data binding doesn't work with layout aliases */
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
//    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private RecyclerView drawerItemsList;
    private LinearLayoutManager layoutManager;
    private DrawerExpandableAdapter drawerAdapter;
    private RecyclerView.Adapter wrappedDrawerAdapter;
    private RecyclerViewExpandableItemManager drawerItemManager;
    private SearchView searchView;
    public static TextView sessionDhtNodesStat, sessionDownloadStat,
            sessionUploadStat, sessionListenPortStat;

    public static MainViewModel viewModel;
    private MsgMainViewModel msgViewModel;
    private static CompositeDisposable disposables = new CompositeDisposable();
    private BaseAlertDialog.SharedViewModel dialogViewModel;

    private boolean permDialogIsShow = false;
    private TorrentInfoProvider infoProvider;
    private RelativeLayout about_us,share_btn,rate_btn,download;

    private InterstitialAd interstitialAd;
    private BroadcastReceiver conReceiver;
    private ShimmerFrameLayout mShimmerViewContainer,mShimmerViewContainer1,
            mShimmerViewContainer2,mShimmerViewContainer3,mShimmerViewContainer4,
            mShimmerViewContainer5,mShimmerViewContainer6;
    public  RelativeLayout scrollView;
    public  ScrollView shimmerScroll;
    private RelativeLayout shimer_layout;
    public AboutAlertDialog aboutDialog;
    Boolean isConnected=false;
    private static Context context;
    ExitDialog exitDialog;
    private AppUpdateManager appUpdateManager;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Utils.getAppTheme(getApplicationContext()));
        super.onCreate(savedInstanceState);
        if (getIntent().getAction() != null &&
                getIntent().getAction().equals(NotificationReceiver.NOTIFY_ACTION_SHUTDOWN_APP)) {

            if (viewModel != null) {
                viewModel.requestStopEngine();

            }
            finish();

            return;
        }

        sharedPref = SharedPref.getInstance(this);
        infoProvider = TorrentInfoProvider.getInstance(getApplicationContext());
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(MainViewModel.class);
        msgViewModel = provider.get(MsgMainViewModel.class);
        dialogViewModel = provider.get(BaseAlertDialog.SharedViewModel.class);
        aboutDialog = (AboutAlertDialog)getSupportFragmentManager().findFragmentByTag(TAG_ABOUT_DIALOG);

        if (savedInstanceState != null)
            permDialogIsShow = savedInstanceState.getBoolean(TAG_PERM_DIALOG_IS_SHOW);

//        if (!Utils.checkStoragePermission(getApplicationContext()) && !permDialogIsShow) {
//            permDialogIsShow = true;
//            startActivity(new Intent(this, RequestPermissions.class));
//        }

        setContentView(R.layout.activity_main);
        context=MainActivity.this;
        loadCloseDialog();
        init();
        initDrawer();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_ATTACHED_IN_DECOR);
        }
        cleanGarbageFragments();
//        fl_adplaceholder=(FrameLayout) findViewById(R.id.fl_adplaceholder2);
//        nativeAdView=new populateNativeAdView();
//        try{
//            nativeAdView.loadNativeAd(MainActivity.this,getResources().getString(R.string.main_list_native),nativeAd,fl_adplaceholder);
//        }
//        catch (Exception e){
//            Log.d(TAG, "onNativeAdload: error");
//            e.printStackTrace();
//        }
        exitDialog = new ExitDialog(MainActivity.this,null);
        load_native();
        appUpdateManager = AppUpdateManagerFactory.create(this);
        InAppUpdate.setImmediateUpdate(appUpdateManager, this);


    }
    public void load_native(){
        AdLoader.Builder builder = new AdLoader.Builder(MainActivity.this, AD_NATIVE_2_KEY);

        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener(){
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onNativeAdLoaded(NativeAd unifiedNativeAd) {
                // If this callback occurs after the activity is destroyed, you must call
                // destroy and return or you may get a memory leak.


                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                if (nativeAd != null) {
                    nativeAd.destroy();
                }

                exitDialog = new ExitDialog(MainActivity.this,unifiedNativeAd);
            }

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader =
                builder
                        .withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {

                                    }
                                })
                        .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void initDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.syncState();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        isConnected=isConnected(getApplicationContext());
//        Toast.makeText(this,isConnected+"",Toast.LENGTH_LONG).show();

//        checkConnection();

    }

    public void opendrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    public void init() {


//        viewPager = findViewById(R.id.framelayout);
        pagerAdapter = new ViewPageAdapter(getApplicationContext(), getSupportFragmentManager(), 0);
//        viewPager.setAdapter(pagerAdapter);
        statePagerAdapter=new ViewStatePagerAdapter(this);
        viewPager2=findViewById(R.id.framelayout);
        viewPager2.setAdapter(statePagerAdapter);
        viewPager2.setUserInputEnabled(false);
        navigationView = findViewById(R.id.bottom);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(0);
//        viewPager.setCurrentItem(0);
        viewPager2.setCurrentItem(0);
        about_us=findViewById(R.id.about_us);
        start_torrent=findViewById(R.id.start_btn);
        share_btn=findViewById(R.id.share);
        rate_btn=findViewById(R.id.rate_us);
        download=findViewById(R.id.download_menu);

        shimmerScroll= findViewById(R.id.shimmer_scroll);
        scrollView= findViewById(R.id.main);


        mShimmerViewContainer = findViewById(R.id.shimmer_view_container1);
        mShimmerViewContainer1 = findViewById(R.id.shimmer_view_container2);
        mShimmerViewContainer2 = findViewById(R.id.shimmer_view_container3);
        mShimmerViewContainer3 = findViewById(R.id.shimmer_view_container4);
        mShimmerViewContainer4 = findViewById(R.id.shimmer_view_container5);
        mShimmerViewContainer5 = findViewById(R.id.shimmer_view_container6);
        mShimmerViewContainer6 = findViewById(R.id.shimmer_view_container7);
        if(!is_first_open) {
            mShimmerViewContainer.startShimmer();
            mShimmerViewContainer1.startShimmer();
            mShimmerViewContainer2.startShimmer();
            mShimmerViewContainer3.startShimmer();
            mShimmerViewContainer4.startShimmer();
            mShimmerViewContainer5.startShimmer();
            mShimmerViewContainer6.startShimmer();
            if (!isConnected) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer1.stopShimmer();
                        mShimmerViewContainer2.stopShimmer();
                        mShimmerViewContainer3.stopShimmer();
                        mShimmerViewContainer4.stopShimmer();
                        mShimmerViewContainer5.stopShimmer();
                        mShimmerViewContainer6.stopShimmer();
                        shimmerScroll.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        is_first_open=true;

                    }
                }, 1000 * 5);
                // Show the ad
            }
        }else{
            shimmerScroll.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }


        layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically()
            {
                /* Disable scroll, because RecyclerView is wrapped in ScrollView */
                return false;
            }
        };
        sessionDhtNodesStat = findViewById(R.id.session_dht_nodes_stat);
        sessionDownloadStat = findViewById(R.id.session_download_stat);
        sessionUploadStat = findViewById(R.id.session_upload_stat);
        sessionListenPortStat = findViewById(R.id.session_listen_port_stat);
        sessionDhtNodesStat.setText(getString(R.string.session_stats_dht_nodes, 0));
        String downloadUploadFmt = getString(R.string.session_stats_download_upload,
                Formatter.formatFileSize(this, 0),
                Formatter.formatFileSize(this, 0));
        sessionDownloadStat.setText(downloadUploadFmt);
        sessionUploadStat.setText(downloadUploadFmt);
        sessionListenPortStat.setText(getString(R.string.session_stats_listen_port,
                getString(R.string.not_available)));

        viewModel.resetSearch();

        start_torrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                subscribeAlertDialog();
                subscribeMsgViewModel();
                subscribeSessionStats();
//                subscribeNeedStartEngine();
                viewModel.resetSearch();
                viewModel.startEngine();
            }
        });

        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,com.movilix.torrant.ui.main.MainActivity.class));
            }
        });
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Movilix");
                    String shareMessage= "\n"+getString(R.string.about_app)+"\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
        rate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }
    public  void start(){
        subscribeAlertDialog();
        subscribeMsgViewModel();
        subscribeSessionStats();
        subscribeNeedStartEngine();
//        viewModel.resetSearch();
//        viewModel.startEngine();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_home:
                viewPager2.setCurrentItem(0);

                break;
            case R.id.action_search:
                viewPager2.setCurrentItem(1);

                break;
            case R.id.action_download:
                startActivity(new Intent(this,com.movilix.torrant.ui.main.MainActivity.class));
//                viewPager2.setCurrentItem(3);

                break;
            case R.id.action_more:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        InAppUpdate.setImmediateUpdateOnResume(appUpdateManager, this);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putBoolean(TAG_PERM_DIALOG_IS_SHOW, permDialogIsShow);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        if (toggle != null)
            toggle.syncState();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        start();
//
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        disposables.clear();
    }

    @Override
    protected void onDestroy()
    {
        if (viewModel != null)
            viewModel.requestStopEngine();

        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
//        alertDialog.show();
//        if (exitDialog !=null) {
            exitDialog.show();
            Window window = exitDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        }else{
//            this.finish();
//        }
//        ViewGroup viewGroup = findViewById(android.R.id.content);
//
//        //then we will inflate the custom alert dialog xml that we created
//        View dialogView = LayoutInflater.from(this).inflate(R.layout.close_native, viewGroup, false);
//        new AlertDialog.Builder(this)
//                .setMessage("Are you sure you want to exit?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        MainActivity.super.onBackPressed();
//                    }
//                })
//                .setView(dialogView)
//                .setNegativeButton("No", null)
//                .show();
    }
    private void loadCloseDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.close_native, viewGroup, false);
//        Button ok = dialogView.findViewById(R.id.buttonOk);

        //Now we need an AlertDialog.Builder object
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.super.onBackPressed();
            }
        });


        builder.setNegativeButton("Cancle", null);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        TextView titleView = new TextView(this);
        titleView.setText("Are you sure you want to exit?");
        titleView.setGravity(Gravity.CENTER);
        titleView.setPadding(20, 20, 20, 20);
        titleView.setTextSize(20F);
        titleView.setTypeface(Typeface.DEFAULT_BOLD);
        titleView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        titleView.setTextColor(ContextCompat.getColor(this, R.color.background));
        alertDialog.setCustomTitle(titleView);



        alertDialog.setOnShowListener((DialogInterface dialogInterface) -> {
            Button positiveButton = alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);

            if (positiveButton != null) {
                positiveButton.setOnClickListener((v) -> {
//                   alertDialog.dismiss();
                    MainActivity.super.onBackPressed();
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
    private void subscribeAlertDialog()
    {
        Disposable d = dialogViewModel.observeEvents()
                .subscribe((event) -> {
                    if (event.dialogTag == null || !event.dialogTag.equals(TAG_ABOUT_DIALOG))
                        return;
                    switch (event.type) {
                        case NEGATIVE_BUTTON_CLICKED:
                            openChangelogLink();
                            break;
                        case DIALOG_SHOWN:
//                            initAboutDialog();
                            break;
                    }
                });
        disposables.add(d);
    }

    private void subscribeMsgViewModel()
    {
        disposables.add(msgViewModel.observeTorrentDetailsOpened()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showDetailTorrent));

        disposables.add(msgViewModel.observeTorrentDetailsClosed()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((__) -> showBlankFragment()));

        disposables.add(viewModel.observeTorrentsDeleted()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((id) -> {
                    DetailTorrentFragment f = getCurrentDetailFragment();
                    if (f != null && id.equals(f.getTorrentId()))
                        showBlankFragment();
                }));
    }

    public void subscribeSessionStats()
    {
        disposables.add(infoProvider.observeSessionStats()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateSessionStats));
    }

    private void subscribeNeedStartEngine()
    {
        disposables.add(viewModel.observeNeedStartEngine()
                .subscribeOn(Schedulers.io())
                .filter((needStart) -> needStart)
                .subscribe((needStart) -> viewModel.startEngine()));
    }

    private void updateSessionStats(SessionStats stats)
    {
        long dhtNodes = 0;
        long totalDownload = 0;
        long totalUpload = 0;
        long downloadSpeed = 0;
        long uploadSpeed = 0;
        int listenPort = -1;

        if (stats != null) {
            dhtNodes = stats.dhtNodes;
            totalDownload = stats.totalDownload;
            totalUpload = stats.totalUpload;
            downloadSpeed = stats.downloadSpeed;
            uploadSpeed = stats.uploadSpeed;
            listenPort = stats.listenPort;
        }else {
            dhtNodes = 0;
            totalDownload = 0;
            totalUpload = 0;
            downloadSpeed = 0;
            uploadSpeed = 0;
            listenPort = -1;
        }

        sessionDhtNodesStat.setText(getString(R.string.session_stats_dht_nodes, dhtNodes));
        sessionDownloadStat.setText(getString(R.string.session_stats_download_upload,
                Formatter.formatFileSize(this, totalDownload),
                Formatter.formatFileSize(this, downloadSpeed)));
        sessionUploadStat.setText(getString(R.string.session_stats_download_upload,
                Formatter.formatFileSize(this, totalUpload),
                Formatter.formatFileSize(this, uploadSpeed)));
        sessionListenPortStat.setText(getString(R.string.session_stats_listen_port,
                listenPort <= 0 ?
                        getString(R.string.not_available) :
                        Integer.toString(listenPort)));
    }

    private void saveGroupExpandState(int groupPosition, boolean expanded)
    {
        DrawerGroup group = drawerAdapter.getGroup(groupPosition);
        if (group == null)
            return;

        Resources res = getResources();
        String prefKey = null;
        if (group.id == res.getInteger(R.integer.drawer_status_id))
            prefKey = getString(R.string.drawer_status_is_expanded);

        else if (group.id == res.getInteger(R.integer.drawer_sorting_id))
            prefKey = getString(R.string.drawer_sorting_is_expanded);

        else if (group.id == res.getInteger(R.integer.drawer_date_added_id))
            prefKey = getString(R.string.drawer_time_is_expanded);

        if (prefKey != null)
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putBoolean(prefKey, expanded)
                    .apply();
    }

    private void onDrawerGroupsCreated()
    {
        for (int pos = 0; pos < drawerAdapter.getGroupCount(); pos++) {
            DrawerGroup group = drawerAdapter.getGroup(pos);
            if (group == null)
                return;

            Resources res = getResources();
            if (group.id == res.getInteger(R.integer.drawer_status_id)) {
                viewModel.setStatusFilter(
                        Utils.getDrawerGroupStatusFilter(this, group.getSelectedItemId()), false);

            } else if (group.id == res.getInteger(R.integer.drawer_sorting_id)) {
                viewModel.setSort(Utils.getDrawerGroupItemSorting(this, group.getSelectedItemId()), false);
            } else if (group.id == res.getInteger(R.integer.drawer_date_added_id)) {
                viewModel.setDateAddedFilter(Utils.getDrawerGroupDateAddedFilter(this, group.getSelectedItemId()), false);
            }

            applyExpandState(group, pos);
        }
    }

    private void applyExpandState(DrawerGroup group, int pos)
    {
        if (group.getDefaultExpandState())
            drawerItemManager.expandGroup(pos);
        else
            drawerItemManager.collapseGroup(pos);
    }

    private void onDrawerItemSelected(DrawerGroup group, DrawerGroupItem item)
    {
        Resources res = getResources();
        String prefKey = null;
        if (group.id == res.getInteger(R.integer.drawer_status_id)) {
            prefKey = getString(R.string.drawer_status_selected_item);
            viewModel.setStatusFilter(Utils.getDrawerGroupStatusFilter(this, item.id), true);

        } else if (group.id == res.getInteger(R.integer.drawer_sorting_id)) {
            prefKey = getString(R.string.drawer_sorting_selected_item);
            viewModel.setSort(Utils.getDrawerGroupItemSorting(this, item.id), true);

        } else if (group.id == res.getInteger(R.integer.drawer_date_added_id)) {
            prefKey = getString(R.string.drawer_time_selected_item);
            viewModel.setDateAddedFilter(Utils.getDrawerGroupDateAddedFilter(this, item.id), true);
        }

        if (prefKey != null)
            saveSelectionState(prefKey, item);

        if (drawerLayout != null)
            drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void saveSelectionState(String prefKey, DrawerGroupItem item)
    {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putLong(prefKey, item.id)
                .apply();
    }

    private void cleanGarbageFragments()
    {
        /* Clean detail and blank fragments after rotate for tablets */
        if (Utils.isLargeScreenDevice(this)) {
            FragmentManager fm = getSupportFragmentManager();
            List<Fragment> fragments = fm.getFragments();
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : fragments)
                if (f instanceof DetailTorrentFragment || f instanceof BlankFragment)
                    ft.remove(f);
            ft.commitAllowingStateLoss();
        }
    }

    private void showDetailTorrent(String id)
    {
        if (Utils.isTwoPane(this)) {
            FragmentManager fm = getSupportFragmentManager();
            DetailTorrentFragment detail = DetailTorrentFragment.newInstance(id);
            Fragment fragment = fm.findFragmentById(R.id.detail_torrent_fragmentContainer);

            if (fragment instanceof DetailTorrentFragment) {
                String oldId = ((DetailTorrentFragment)fragment).getTorrentId();
                if (id.equals(oldId))
                    return;
            }
            fm.beginTransaction()
                    .replace(R.id.detail_torrent_fragmentContainer, detail)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

        } else {
            Intent i = new Intent(this, DetailTorrentActivity.class);
            i.putExtra(DetailTorrentActivity.TAG_TORRENT_ID, id);
            startActivity(i);
        }
    }

    private void showBlankFragment()
    {
        if (Utils.isTwoPane(this)) {
            FragmentManager fm = getSupportFragmentManager();
            BlankFragment blank = BlankFragment.newInstance(getString(R.string.select_or_add_torrent));
            fm.beginTransaction()
                    .replace(R.id.detail_torrent_fragmentContainer, blank)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .commitAllowingStateLoss();
        }
    }

    public DetailTorrentFragment getCurrentDetailFragment()
    {
        if (!Utils.isTwoPane(this))
            return null;

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.detail_torrent_fragmentContainer);

        return (fragment instanceof DetailTorrentFragment ? (DetailTorrentFragment)fragment : null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        toolbar.inflateMenu(R.menu.main);
//        searchView = (SearchView)toolbar.getMenu().findItem(R.id.search).getActionView();
        initSearch();
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);

        return true;
    }

    private void initSearch()
    {
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnCloseListener(() -> {
            viewModel.resetSearch();

            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                viewModel.setSearchQuery(query);
                /* Submit the search will hide the keyboard */
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                viewModel.setSearchQuery(newText);

                return true;
            }
        });
        searchView.setQueryHint(getString(R.string.search));
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        /* Assumes current activity is the searchable activity */
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {

            case R.id.about_menu:
                showAboutDialog();
                break;


        }

        return true;
    }

    public void showAboutDialog()
    {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(TAG_ABOUT_DIALOG) == null) {
            aboutDialog = newInstance(
                    getString(R.string.about_title),
                    null,
                    R.layout.dialog_about,
                    getString(R.string.ok),
                    getString(R.string.about_changelog),
                    null,
                    true);
            aboutDialog.show(fm, TAG_ABOUT_DIALOG);

        }
    }

//    private void initAboutDialog()
//    {
//        if (aboutDialog == null)
//            return;
//
//        Dialog dialog = aboutDialog.getDialog();
//
//        if (dialog != null) {
////            TextView versionTextView = dialog.findViewById(R.id.about_version);
//            TextView descriptionTextView = dialog.findViewById(R.id.about_description);
//            String versionName = SystemFacadeHelper.getSystemFacade(getApplicationContext())
//                    .getAppVersionName();
////            if (versionName != null)
////                versionTextView.setText(versionName);
////            descriptionTextView.setText(Html.fromHtml(getString(R.string.about_description)));
//            descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
//        }
//    }

    private void openChangelogLink()
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(getString(R.string.about_changelog_link)));
        startActivity(i);
    }

    private void showLog()
    {
        startActivity(new Intent(this, LogActivity.class));
    }

    @Override
    public void onFragmentFinished(@NonNull Fragment f, Intent intent,
                                   @NonNull FragmentCallback.ResultCode code)
    {
        if (f instanceof DetailTorrentFragment && Utils.isTwoPane(this))
            msgViewModel.torrentDetailsClosed();
    }
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
    public void onNetworkConnectionChanged(boolean isCon) {
        showSnack(isCon);
        isConnected=isCon;
        Toast.makeText(this,"Not Connecte",Toast.LENGTH_LONG).show();
    }
}
