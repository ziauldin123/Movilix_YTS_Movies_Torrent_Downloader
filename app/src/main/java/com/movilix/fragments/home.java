package com.movilix.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.ads.Ad;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.kingfisher.easyviewindicator.RecyclerViewIndicator;
import com.movilix.AdListener;
import com.movilix.HomeAdListener;
import com.movilix.Model.Model;
import com.movilix.Model.SliderModel;
import com.movilix.MovieDetails;
import com.movilix.R;
import com.movilix.SharedPref;
import com.movilix.adapters.CoverFlowAdapter;
import com.movilix.adapters.ExtendAdapter;
import com.movilix.adapters.SliderAdapter;
import com.movilix.ads.populateNativeAdView;
import com.movilix.appInstance.Singleton;
import com.movilix.inteface.MovieItemClick;
import com.movilix.inteface.bottomReach;
import com.movilix.praser.Praser;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import static com.movilix.MainApplication.AD_HOME_KEY;
import static com.movilix.MainApplication.AD_NATIVE_1_KEY;
import static com.movilix.MainApplication.homeGAdsClic;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home extends Fragment implements MovieItemClick{


    private InterstitialAd interstitialAd2;
    private InterstitialAdListener interstitialAdListener2;
    ProgressBar bar;
    Context context;
    int homeFbAdsClick,homeGAdsClick;


//    RecyclerView recyclerView;
    private CoverFlowAdapter adapter;

    private DiscreteScrollView itemPicker;
    private InfiniteScrollAdapter<?> infiniteAdapter;

    public ProgressBar progressBar;

    private ArrayList<SliderModel> games;
    ExtendAdapter latest_adapter,toprated_adapter,popular_adapter,
        slider_Adapter,t3d_adapter,action_adapter,adventure_adapter,sciFi_adapter,
        crime_adapter,fantasy_adapter,horror_adapter;
    SliderAdapter sliderAdapter;
    Praser latest,top_rated,popular,slider,t3d,action,adventure,sciFi,crime,fantasy,horror;
    int page1= 1,page2 = 1,page3 = 1,page4 = 1,page5 = 1,page6 = 1,page7 = 1,page8 = 1,page9 = 1,page10 = 1,page11 = 1,page12 = 1,
    page13 = 1,page14 = 1,page15 = 1,page16 = 1,page17 = 1,page18 = 1,page19 = 1;
    ArrayList<Model> slider_list = new ArrayList<>();
    ImageView TopImage;
    populateNativeAdView nativeAdView;
    NativeAd nativeAd;
    SharedPref sharedPref;
    //admob
    private com.google.android.gms.ads.interstitial.InterstitialAd mInter_Admob1,mInter_Admob2,mInter_Admob3,
            mInter_Admob4,mInter_Admob5,mInter_Admob6,mInter_Admob7,
            mInter_Admob8,mInter_Admob9,mInter_Admob10,mInter_Admob11;
    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;

    private AdView adView,adView1,adView2;
    FrameLayout fl_adplaceholder;

    private InterstitialAd interstitialAd;
    private final String TAG = "facebook";
    private HomeAdListener interstitialAdListener;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    RecyclerView toprated_RecyvlerView,popular_RecyvlerView,
            latest_RecyvlerView,slider_RecyvlerView,
            t3d_recyclerView,action_recycle,adventure_recycle,sciFi_recycle,
            crime_recycle,fantasy_recycle,horror_recycle;
    RecyclerViewIndicator recyclerViewIndicator;

//    RecommendedAdapter recomnded_adapter,latest_adapter,popular_adapter;

    private ShimmerFrameLayout mShimmerViewContainer,mShimmerViewContainer1,
            mShimmerViewContainer2,mShimmerViewContainer3,mShimmerViewContainer4,
            mShimmerViewContainer5,mShimmerViewContainer6;
    private ScrollView scrollView,shimmerScroll;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @SuppressLint("ValidFragment")
    public home(Context mContext) {
        this.context = mContext;
    }

    public home() {
        // Required empty public constructor
    }




    // TODO: Rename and change types and number of parameters
    public static home newInstance(int page,Context context) {
        home fragmentFirst = new home(context);
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);



        toprated_RecyvlerView = root.findViewById(R.id.topratedrecycle);
        latest_RecyvlerView = root.findViewById(R.id.latestrecycle);
        popular_RecyvlerView = root.findViewById(R.id.popularrecycle);
        slider_RecyvlerView = root.findViewById(R.id.sliderRecyle);
        t3d_recyclerView = root.findViewById(R.id.t3drecycle);
        action_recycle = root.findViewById(R.id.actionrecycle);
        adventure_recycle = root.findViewById(R.id.adventurerecycle);
        sciFi_recycle = root.findViewById(R.id.sciFi_recycle);
        crime_recycle = root.findViewById(R.id.crime_recycle);
        fantasy_recycle = root.findViewById(R.id.fantsy_recycle);
        horror_recycle = root.findViewById(R.id.horror_recycle);
        fl_adplaceholder=root.findViewById(R.id.fl_adplaceholder);
        recyclerViewIndicator=root.findViewById(R.id.recyclerViewIndicator);
        progressBar = root.findViewById(R.id.progressBar_cyclic);
        homeFbAdsClick=homeGAdsClic;
        homeGAdsClick=homeGAdsClic;
        sharedPref = SharedPref.getInstance(context);


//        mShimmerViewContainer = root.findViewById(R.id.shimmer_view_container1);
//        mShimmerViewContainer1 = root.findViewById(R.id.shimmer_view_container2);
//        mShimmerViewContainer2 = root.findViewById(R.id.shimmer_view_container3);
//        mShimmerViewContainer3 = root.findViewById(R.id.shimmer_view_container4);
//        mShimmerViewContainer4 = root.findViewById(R.id.shimmer_view_container5);
//        mShimmerViewContainer5 = root.findViewById(R.id.shimmer_view_container6);
//        mShimmerViewContainer6 = root.findViewById(R.id.shimmer_view_container7);

//        mShimmerViewContainer.startShimmer();
//        mShimmerViewContainer1.startShimmer();
//        mShimmerViewContainer2.startShimmer();
//        mShimmerViewContainer3.startShimmer();
//        mShimmerViewContainer4.startShimmer();
//        mShimmerViewContainer5.startShimmer();
//        mShimmerViewContainer6.startShimmer();
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//
//                mShimmerViewContainer.stopShimmer();
//                mShimmerViewContainer1.stopShimmer();
//                mShimmerViewContainer2.stopShimmer();
//                mShimmerViewContainer3.stopShimmer();
//                mShimmerViewContainer4.stopShimmer();
//                mShimmerViewContainer5.stopShimmer();
//                mShimmerViewContainer6.stopShimmer();
//                MainActivity.stop_shimmer();
//
//            }
//        }, 1000  * 8);


//        AdLoader adLoader = new AdLoader.Builder(getActivity(), "ca-app-pub-3940256099942544/2247696110")
//                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
//                    @Override
//                    public void onNativeAdLoaded(NativeAd nativeAd) {
//                        NativeTemplateStyle styles = new
//                                NativeTemplateStyle.Builder()
//                                .withMainBackgroundColor(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)))
//                                .withPrimaryTextTypefaceColor(getResources().getColor(R.color.colorPrimaryDark))
//                                .build();
//                        TemplateView template = root.findViewById(R.id.native1);
//                        template.setStyles(styles);
//                        template.setNativeAd(nativeAd);
//                    }
//                })
//                .build();
//
//        adLoader.loadAd(new AdRequest.Builder().build());


        //Admob load
        loadGAd();
        nativeAdView=new populateNativeAdView();
        try{
            nativeAdView.loadNativeAd(getActivity(),AD_NATIVE_1_KEY,nativeAd,fl_adplaceholder);
        }
        catch (Exception e){
            Log.d(TAG, "onNativeAdload: error");
           e.printStackTrace();
        }

//        coverFlow = (FeatureCoverFlow) root.findViewById(R.id.coverflow);
        adView = new AdView(getContext(), "194509545726647_195075539003381", AdSize.BANNER_HEIGHT_50);
//        adView1 = new AdView(getContext(), "194509545726647_195074659003469", AdSize.BANNER_HEIGHT_50);
        adView2 = new AdView(getContext(), "194509545726647_195075215670080", AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) root.findViewById(R.id.banner_container);
//        LinearLayout adContainer1 = (LinearLayout) root.findViewById(R.id.banner_container1);
        LinearLayout adContainer2 = (LinearLayout) root.findViewById(R.id.banner_container2);



        // Add the ad view to your activity layout
        adContainer.addView(adView);
//        adContainer1.addView(adView1);
        adContainer2.addView(adView2);







//        interstitialAd = new InterstitialAd(getContext(), "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");
        interstitialAd = new InterstitialAd(getContext(), "194509545726647_194511612393107");
//        interstitialAdListener= new AdListener(getActivity());
//        interstitialAd2 = new InterstitialAd(getContext(), "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");
        interstitialAd2 = new InterstitialAd(getContext(), "194509545726647_195076359003299");
//        interstitialAdListener2= new AdListener(getActivity(), model);

//        interstitialAd.loadAd(
//                interstitialAd.buildLoadAdConfig()
//                        .withAdListener(interstitialAdListener)
//                        .build());
        interstitialAd2.loadAd(
                interstitialAd2.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener2)
                        .build());
//        interstitialAd.show();
//        if(interstitialAd == null || !interstitialAd.isAdLoaded()) {
//            return;
//        }
        // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.




        latest_RecyvlerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//

        toprated_RecyvlerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//
//
        popular_RecyvlerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        t3d_recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        slider_RecyvlerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//
        action_recycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        adventure_recycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        sciFi_recycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        crime_recycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        fantasy_recycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        horror_recycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));



        latest = new Praser(context);
        top_rated = new Praser(context);
        popular = new Praser(context);
        slider=new Praser(context);
        t3d=new Praser(context);
        action=new Praser(context);
        adventure=new Praser(context);
        sciFi=new Praser(context);
        crime=new Praser(context);
        fantasy=new Praser(context);
        horror=new Praser(context);
        MovieItemClick movieItemClick=this;

        bar = root.findViewById(R.id.determinateBar);



        slider_Adapter = new ExtendAdapter(getActivity(),slider.list,page1,2,movieItemClick);
        latest_adapter = new ExtendAdapter(getActivity(), latest.list,page2,1,movieItemClick);
        toprated_adapter = new ExtendAdapter(getActivity(), top_rated.list,page3,1,movieItemClick);
        popular_adapter = new ExtendAdapter(getActivity(), popular.list,page4,1,movieItemClick);
        t3d_adapter = new ExtendAdapter(getActivity(), t3d.list,page5,1,movieItemClick);
        action_adapter= new ExtendAdapter(getActivity(), action.list,page6,1,movieItemClick);
        adventure_adapter= new ExtendAdapter(getActivity(), adventure.list,page7,1,movieItemClick);
        sciFi_adapter= new ExtendAdapter(getActivity(), sciFi.list,page8,1,movieItemClick);
        crime_adapter= new ExtendAdapter(getActivity(), crime.list,page9,1,movieItemClick);
        fantasy_adapter= new ExtendAdapter(getActivity(), fantasy.list,page10,1,movieItemClick);
        horror_adapter= new ExtendAdapter(getActivity(), horror.list,page11,1,movieItemClick);

        slider_RecyvlerView.setAdapter(slider_Adapter);

        recyclerViewIndicator.setRecyclerView(slider_RecyvlerView);
        recyclerViewIndicator.setItemCount(5);
        recyclerViewIndicator.setCurrentPosition(0);



        slider_RecyvlerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                        recyclerViewIndicator.setCurrentPosition(position);
                        break;
                }
            }

        });


        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(slider_RecyvlerView);



        latest_RecyvlerView.setAdapter(latest_adapter);
        toprated_RecyvlerView.setAdapter(toprated_adapter);
        popular_RecyvlerView.setAdapter(popular_adapter);
        t3d_recyclerView.setAdapter(t3d_adapter);
        action_recycle.setAdapter(action_adapter);
        adventure_recycle.setAdapter(adventure_adapter);
        sciFi_recycle.setAdapter(sciFi_adapter);
        crime_recycle.setAdapter(crime_adapter);
        fantasy_recycle.setAdapter(fantasy_adapter);
        horror_recycle.setAdapter(horror_adapter);



//        toprated_adapter.setOnBottomReachedListener(new bottomReach() {
//            @Override
//            public void onBottomReached(int position) {
//
////                if (conReceier.isConnected()) {
////                    addreq("https://yts.lt/api/v2/list_movies.json?page=" + page + "&sort_by=year&order_by=desc");
////                }
//                addreq("https://yts.lt/api/v2/list_movies.json?page=" + page + "&sort_by=year&order_by=desc");
//
//            }
//        });
////        addreq("https://yts.lt/api/v2/list_movies.json?page=1&sort_by=year&order_by=desc");
//
        latest_adapter.setOnBottomReachedListener(new bottomReach() {
            @Override
            public void onBottomReached(int position) {

//                if (conReceier.isConnected()) {
//                    addreq("https://yts.lt/api/v2/list_movies.json?page=" + page + "&sort_by=year&order_by=desc");
//                }
                latest_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page2 + "&genre=action&sort_by=latest");
//                addreq("https://yts.lt/api/v2/list_movies.json?page=" + page + "&order_by=featured");

            }
        });
        toprated_adapter.setOnBottomReachedListener(new bottomReach() {
            @Override
            public void onBottomReached(int position) {

//                if (conReceier.isConnected()) {
//                    addreq("https://yts.lt/api/v2/list_movies.json?page=" + page + "&sort_by=year&order_by=desc");
//                }
//                latest_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page + "&genre=action&sort_by=latest");
               top_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page3 + "&genre=action&sort_by=year&sort_by=like_count");

            }
        });
        popular_adapter.setOnBottomReachedListener(new bottomReach() {
            @Override
            public void onBottomReached(int position) {

//                if (conReceier.isConnected()) {
//                    addreq("https://yts.lt/api/v2/list_movies.json?page=" + page + "&sort_by=year&order_by=desc");
//                }
                pop_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page4 + "&genre=action&sort_by=year&order_by=downloads");
//                top_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page + "&genre=action&sort_by=year&sort_by=like_count");

            }
        });
        t3d_adapter.setOnBottomReachedListener(new bottomReach() {
            @Override
            public void onBottomReached(int position) {

//                if (conReceier.isConnected()) {
//                    addreq("https://yts.lt/api/v2/list_movies.json?page=" + page + "&sort_by=year&order_by=desc");
//                }
//                pop_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page + "&genre=action&sort_by=year&order_by=downloads");
                t3d_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page5 + "&quality=3D&sort_by=year");

            }
        });
        action_adapter.setOnBottomReachedListener(new bottomReach() {
            @Override
            public void onBottomReached(int position) {

//                if (conReceier.isConnected()) {
//                    addreq("https://yts.lt/api/v2/list_movies.json?page=" + page + "&sort_by=year&order_by=desc");
//                }
//                pop_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page + "&genre=action&sort_by=year&order_by=downloads");
                action_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page6 + "&genre=action&sort_by=year");

            }
        });
        adventure_adapter.setOnBottomReachedListener(new bottomReach() {
            @Override
            public void onBottomReached(int position) {

//                if (conReceier.isConnected()) {
//                    addreq("https://yts.lt/api/v2/list_movies.json?page=" + page + "&sort_by=year&order_by=desc");
//                }
//                pop_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page + "&genre=action&sort_by=year&order_by=downloads");
                adventure_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page7 + "&genre=sci-fi&sort_by=year");

            }
        });
        sciFi_adapter.setOnBottomReachedListener(new bottomReach() {
            @Override
            public void onBottomReached(int position) {

//                if (conReceier.isConnected()) {
//                    addreq("https://yts.lt/api/v2/list_movies.json?page=" + page + "&sort_by=year&order_by=desc");
//                }
//                pop_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page + "&genre=action&sort_by=year&order_by=downloads");
                sciFi_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page8 + "&genre=adventure&sort_by=year");

            }
        });
        crime_adapter.setOnBottomReachedListener(new bottomReach() {
            @Override
            public void onBottomReached(int position) {

//                if (conReceier.isConnected()) {
//                    addreq("https://yts.lt/api/v2/list_movies.json?page=" + page + "&sort_by=year&order_by=desc");
//                }
//                pop_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page + "&genre=action&sort_by=year&order_by=downloads");
                crime_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page9 + "&genre=crime&sort_by=year");

            }
        });
        fantasy_adapter.setOnBottomReachedListener(new bottomReach() {
            @Override
            public void onBottomReached(int position) {

//                if (conReceier.isConnected()) {
//                    addreq("https://yts.lt/api/v2/list_movies.json?page=" + page + "&sort_by=year&order_by=desc");
//                }
//                pop_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page + "&genre=action&sort_by=year&order_by=downloads");
                fantasy_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page10 + "&genre=fantasy&sort_by=year");

            }
        });
        horror_adapter.setOnBottomReachedListener(new bottomReach() {
            @Override
            public void onBottomReached(int position) {

//                if (conReceier.isConnected()) {
//                    addreq("https://yts.lt/api/v2/list_movies.json?page=" + page + "&sort_by=year&order_by=desc");
//                }
//                pop_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page + "&genre=action&sort_by=year&order_by=downloads");
                horror_addreq("https://yts.mx/api/v2/list_movies.json?page=" + page11 + "&genre=horror&sort_by=year");

            }
        });
//        latest_addreq("https://yts.mx/api/v2/list_movies.json?genre=action&year=2020");
//        top_addreq("https://yts.mx/api/v2/list_movies.json?genre=action&sort_by=year&sort_by=like_count");
//        pop_addreq("https://yts.mx/api/v2/list_movies.json?genre=action&sort_by=year&order_by=downloads");
//        t3d_addreq("https://yts.mx/api/v2/list_movies.json?quality=3D&sort_by=year");
//        slider_addreq("https://yts.mx/api/v2/list_movies.json?year=2020&sort_by=year");
//        action_addreq("https://yts.mx/api/v2/list_movies.json?genre=action&sort_by=year");
//        sciFi_addreq("https://yts.mx/api/v2/list_movies.json?genre=adventure&sort_by=year");
//        adventure_addreq("https://yts.mx/api/v2/list_movies.json?genre=sci-fi&sort_by=year");
//        crime_addreq("https://yts.mx/api/v2/list_movies.json?genre=crime&sort_by=year");
//        fantasy_addreq("https://yts.mx/api/v2/list_movies.json?genre=fantasy&sort_by=year");
//        horror_addreq("https://yts.mx/api/v2/list_movies.json?genre=horror&sort_by=year");
        slider_addreq("https://yts.mx/api/v2/list_movies.json?genre=action&limit=5");
        latest_addreq("https://yts.mx/api/v2/list_movies.json?");
        top_addreq("https://yts.mx/api/v2/list_movies.json?sort_by=rating&order_by=desc");
        pop_addreq("https://yts.mx/api/v2/list_movies.json?&sort_by=like_count&order_by=desc");
        t3d_addreq("https://yts.mx/api/v2/list_movies.json?quality=3D&sort_by=year&order_by=desc");
        action_addreq("https://yts.mx/api/v2/list_movies.json?genre=action&order_by=desc");
        sciFi_addreq("https://yts.mx/api/v2/list_movies.json?genre=adventure&order_by=desc");
        adventure_addreq("https://yts.mx/api/v2/list_movies.json?genre=sci-fi&order_by=desc");
        crime_addreq("https://yts.mx/api/v2/list_movies.json?genre=crime&order_by=desc");
        fantasy_addreq("https://yts.mx/api/v2/list_movies.json?genre=fantasy&order_by=desc");
        horror_addreq("https://yts.mx/api/v2/list_movies.json?genre=horror&order_by=desc");
        return root;
    }


    private void showInterstitial() {
        if (mInter_Admob1 != null) {
            mInter_Admob1.show(getActivity());
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
//    void addreq(String url) {
//
//        HashMap<String, String> map = new HashMap<>();
//        map.put("limit", "30");
////        map.put("quality", "1080p");
////        map.put("sort_by", "date_added");
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(map),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        if (response != null) {
//                            top_rated.JsonPrase(response);
//                            bar.setVisibility(View.INVISIBLE);
//                            toprated_adapter.notifyDataSetChanged();
//                            page = page + 1;
//
//                        }
////                        if(page%3 ==0) {
////
////                            if (home.interstitialAd.isLoaded()) {
////                                home.interstitialAd.show();
////                            } else {
////                                home.interstitialAd.loadAd(new AdRequest.Builder().addTestDevice("08A99517802DB12E8F50CD0C9C8F2E49").build());
////                                Log.d("TAG", "The interstitial wasn't loaded yet.");
////                            }
////                            nativeAds=new NativeAds(context,frameLayout,adView);
////                                    nativeAds.refreshAd();
////                        }
//
//
//                    }
//
//
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("Error", "onErrorResponse: "+error);
//                error.printStackTrace();
//            }
//        });
//
//        Singleton.getInstance(context).addToRequestQueue(request);
//    }
    void latest_addreq(String url) {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "30");
//        map.put("quality", "1080p");
//        map.put("sort_by", "date_added");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            latest.JsonPrase(response);
                            bar.setVisibility(View.INVISIBLE);
                            latest_adapter.notifyDataSetChanged();
                            page2 = page2 + 1;
                            Log.d(TAG, "onResponse1: "+page2);


                        }
//                        if(page2%3 ==0) {
//                            mShowFbInterstitial2();
//                        }else {
//                            interstitialAd2.loadAd(
//                                    interstitialAd2.buildLoadAdConfig()
//                                            .withAdListener(interstitialAdListener2)
//                                            .build());
//                        }


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "onErrorResponse: "+error);
                error.printStackTrace();
            }
        });

        Singleton.getInstance(getActivity()).addToRequestQueue(request);
    }
    void slider_addreq(String url) {

        HashMap<String, String> map = new HashMap<>();


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            slider.JsonPrase(response);
                            bar.setVisibility(View.INVISIBLE);
                            slider_Adapter.notifyDataSetChanged();
                            recyclerViewIndicator.forceUpdateItemCount();
//                            adapter.notifyDataSetChanged();
                            page1 = page1 + 1;
//                            Log.d(TAG, "onResponse2: "+page1);
//                            adView.loadAd();
//                            adView1.loadAd();
//                            adView2.loadAd();

                        }
//                        if(page1%3 ==0) {
//                            mShowFbInterstitial2();
//                        }else {
//                            interstitialAd2.loadAd(
//                                    interstitialAd2.buildLoadAdConfig()
//                                            .withAdListener(interstitialAdListener2)
//                                            .build());
//                        }


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "onErrorResponse: "+error);
                error.printStackTrace();
            }
        });

        Singleton.getInstance(getContext()).addToRequestQueue(request);
    }
    void top_addreq(String url) {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "30");
//        map.put("quality", "1080p");
//        map.put("sort_by", "date_added");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            top_rated.JsonPrase(response);
                            bar.setVisibility(View.INVISIBLE);
                            toprated_adapter.notifyDataSetChanged();
                            page3 = page3 + 1;

                        }
//                        if(page3%3 ==0) {
//                            mShowFbInterstitial2();
//                        }else {
//                            interstitialAd2.loadAd(
//                                    interstitialAd2.buildLoadAdConfig()
//                                            .withAdListener(interstitialAdListener2)
//                                            .build());
//                        }


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "onErrorResponse: "+error);
                error.printStackTrace();
            }
        });

        Singleton.getInstance(context).addToRequestQueue(request);
    }
    void pop_addreq(String url) {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "30");
//        map.put("quality", "1080p");
//        map.put("sort_by", "date_added");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            popular.JsonPrase(response);
                            bar.setVisibility(View.INVISIBLE);
                            popular_adapter.notifyDataSetChanged();
                            page4 = page4 + 1;

                        }
//                        if(page4%3 ==0) {
//                            mShowFbInterstitial2();
//                        }else {
//                            interstitialAd2.loadAd(
//                                    interstitialAd2.buildLoadAdConfig()
//                                            .withAdListener(interstitialAdListener2)
//                                            .build());
//                        }



                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "onErrorResponse: "+error);
                error.printStackTrace();
            }
        });

        Singleton.getInstance(context).addToRequestQueue(request);
    }
    void t3d_addreq(String url) {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "30");
//        map.put("quality", "1080p");
//        map.put("sort_by", "date_added");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            t3d.JsonPrase(response);
                            bar.setVisibility(View.INVISIBLE);
                            t3d_adapter.notifyDataSetChanged();
                            page5 = page5 + 1;

                        }
//                        if(page5%3 ==0) {
//                            mShowFbInterstitial2();
//                        }else {
//                            interstitialAd2.loadAd(
//                                    interstitialAd2.buildLoadAdConfig()
//                                            .withAdListener(interstitialAdListener2)
//                                            .build());
//                        }



                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "onErrorResponse: "+error);
                error.printStackTrace();
            }
        });

        Singleton.getInstance(context).addToRequestQueue(request);
    }
    void action_addreq(String url) {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "30");
//        map.put("quality", "1080p");
//        map.put("sort_by", "date_added");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            action.JsonPrase(response);
                            bar.setVisibility(View.INVISIBLE);
                            action_adapter.notifyDataSetChanged();
                            page6 = page6 + 1;

                        }

//                        if(page6%3 ==0) {
//                            mShowFbInterstitial2();
//                        }else {
//                            interstitialAd2.loadAd(
//                                    interstitialAd2.buildLoadAdConfig()
//                                            .withAdListener(interstitialAdListener2)
//                                            .build());
//                        }


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "onErrorResponse: "+error);
                error.printStackTrace();
            }
        });

        Singleton.getInstance(context).addToRequestQueue(request);
    }
    void adventure_addreq(String url) {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "30");
//        map.put("quality", "1080p");
//        map.put("sort_by", "date_added");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            adventure.JsonPrase(response);
                            bar.setVisibility(View.INVISIBLE);
                            adventure_adapter.notifyDataSetChanged();
                            page7 = page7 + 1;

                        }
//                        if(page7%3 ==0) {
//                            mShowFbInterstitial2();
//                        }else {
//                            interstitialAd2.loadAd(
//                                    interstitialAd2.buildLoadAdConfig()
//                                            .withAdListener(interstitialAdListener2)
//                                            .build());
//                        }


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "onErrorResponse: "+error);
                error.printStackTrace();
            }
        });

        Singleton.getInstance(context).addToRequestQueue(request);
    }
    void sciFi_addreq(String url) {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "30");
//        map.put("quality", "1080p");
//        map.put("sort_by", "date_added");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            sciFi.JsonPrase(response);
                            bar.setVisibility(View.INVISIBLE);
                            sciFi_adapter.notifyDataSetChanged();
                            page8 = page8 + 1;

                        }
//                        if(page8%3 ==0) {
//                            mShowFbInterstitial2();
//                        }else {
//                            interstitialAd2.loadAd(
//                                    interstitialAd2.buildLoadAdConfig()
//                                            .withAdListener(interstitialAdListener2)
//                                            .build());
//                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "onErrorResponse: "+error);
                error.printStackTrace();
            }
        });

        Singleton.getInstance(context).addToRequestQueue(request);
    }
    void crime_addreq(String url) {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "30");
//        map.put("quality", "1080p");
//        map.put("sort_by", "date_added");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            crime.JsonPrase(response);
                            bar.setVisibility(View.INVISIBLE);
                            crime_adapter.notifyDataSetChanged();
                            page9 = page9 + 1;

                        }
//                        if(page9%3 ==0) {
//                            mShowFbInterstitial2();
//                        }else {
//                            interstitialAd2.loadAd(
//                                    interstitialAd2.buildLoadAdConfig()
//                                            .withAdListener(interstitialAdListener2)
//                                            .build());
//                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "onErrorResponse: "+error);
                error.printStackTrace();
            }
        });

        Singleton.getInstance(context).addToRequestQueue(request);
    }
    void fantasy_addreq(String url) {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "30");
//        map.put("quality", "1080p");
//        map.put("sort_by", "date_added");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            fantasy.JsonPrase(response);
                            bar.setVisibility(View.INVISIBLE);
                            fantasy_adapter.notifyDataSetChanged();
                            page10 = page10 + 1;

                        }
//                        if(page10%3 ==0) {
//                            mShowFbInterstitial2();
//                        }else {
//                            interstitialAd2.loadAd(
//                                    interstitialAd2.buildLoadAdConfig()
//                                            .withAdListener(interstitialAdListener2)
//                                            .build());
//                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "onErrorResponse: "+error);
                error.printStackTrace();
            }
        });

        Singleton.getInstance(context).addToRequestQueue(request);
    }
    void horror_addreq(String url) {

        HashMap<String, String> map = new HashMap<>();
        map.put("limit", "30");
//        map.put("quality", "1080p");
//        map.put("sort_by", "date_added");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            horror.JsonPrase(response);
                            bar.setVisibility(View.INVISIBLE);
                            horror_adapter.notifyDataSetChanged();
                            page11 = page11 + 1;

                        }
//                        if(page11%3 ==0) {
//                            mShowFbInterstitial2();
//                        }else {
//                            interstitialAd2.loadAd(
//                                    interstitialAd2.buildLoadAdConfig()
//                                            .withAdListener(interstitialAdListener2)
//                                            .build());
//                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "onErrorResponse: "+error);
                error.printStackTrace();
            }
        });

        Singleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        if (adView1 != null) {
            adView1.destroy();
        }
        if (adView2 != null) {
            adView2.destroy();
        }
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        if (interstitialAd2 != null) {
            interstitialAd2.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {

        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {

        super.onPause();

    }


    @Override
    public void onItemClick(Model model) {
        mShowGInterstitial(model);
        progressBar.setVisibility(View.VISIBLE);
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(Ad ad) {
                                Log.e(TAG, "Interstitial ad dismissed.");

                                Intent intent = new Intent(getActivity(), MovieDetails.class);
                                intent.putExtra("obj", model);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getContext().startActivity(intent);
                                progressBar.setVisibility(View.GONE);
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

//        if(homeGAdsClick%5 ==0) {
//            loadAd();
//        }
//        if(homeFbAdsClick%5 ==0) {
//
//            interstitialAd.loadAd(
//                    interstitialAd.buildLoadAdConfig()
//                            .withAdListener(interstitialAdListener)
//                            .build());
//        }
//        if(homeFbAdsClick%3 ==0) {
//            mShowFbInterstitial(model);
//        }
        homeGAdsClick= homeGAdsClick+1;
        homeFbAdsClick=homeFbAdsClick+1;


//        Intent intent = new Intent(getActivity(), MovieDetails.class);
//        intent.putExtra("obj", model);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getContext().startActivity(intent);
    }

    @Override
    public void onClick(View view) {

    }
    private void mShowGInterstitial(Model model) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(getActivity());

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.d("TAG", "The ad was dismissed.");

                    Intent intent = new Intent(getActivity(), MovieDetails.class);
                    intent.putExtra("obj", model);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when fullscreen content failed to show.
                    Log.d("TAG", "The ad failed to show.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when fullscreen content is shown.
                    // Make sure to set your reference to null so you don't
                    // show it a second time.
                    mInterstitialAd = null;
                    Log.d("TAG", "The ad was shown.");
                }
            });


        }else {
            mShowFbInterstitial(model);
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
    private void mShowFbInterstitial(Model model) {
//        interstitialAdListener= new HomeAdListener(getActivity(),model,progressBar);


       if(interstitialAd != null && interstitialAd.isAdLoaded())  {

            interstitialAd.show();


        }else {
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(getActivity(), MovieDetails.class);
            intent.putExtra("obj", model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
    private void mShowFbInterstitial2() {
        if(interstitialAd2 != null && interstitialAd2.isAdLoaded())  {
            interstitialAd2.show();


        }else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
    private void loadGAd() {

        AdRequest adRequest = new AdRequest.Builder().build();

        com.google.android.gms.ads.
                interstitial.InterstitialAd.load(getActivity(),
                AD_HOME_KEY,
                adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");


            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });

    }



}
