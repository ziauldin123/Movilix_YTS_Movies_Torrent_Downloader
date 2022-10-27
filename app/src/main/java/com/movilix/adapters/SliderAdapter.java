package com.movilix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.movilix.Model.Model;
import com.movilix.R;
import com.movilix.inteface.bottomReach;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

//import com.movilix.yts.MovieDetails;
//import com.movilix.yts.intefce.bottomReach;

public class SliderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Model> list;
    bottomReach bottomListener;
    int page;

    public SliderAdapter(Context context, ArrayList<Model> list, int page) {
        this.context = context;
        this.list = list;
        this.page=page;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

//        if (page%4 == 0) {
//            view = LayoutInflater.from(context).inflate(R.layout.ad_unified, viewGroup, false);
//            viewHolder = new unifiedAd(view);
////
//        } else  {
           view = LayoutInflater.from(context).inflate(R.layout.slider_layout, viewGroup, false);
            viewHolder = new homeAdapterView(view);

//        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

//        if (page%4 == 0) {
//
//            unifiedAd unifiedAd=(unifiedAd) holder;
//            unifiedAd.closeAd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    unifiedAd.linearLayout.setVisibility(View.GONE);
//                }
//            });
//
//          //  adView = (UnifiedNativeAdView) getLayoutInflater()
//                    //.inflate(R.layout.ad_unified, null);
//            //view = LayoutInflater.from(context).inflate(R.layout.ad_unified, viewGroup, false);
//            //viewHolder = new unifiedAd(view);
////
//        } else {
            homeAdapterView homeAdapterView = (homeAdapterView) holder;
            if (list != null) {
                if (i == list.size() - 1) {

                    bottomListener.onBottomReached(i);

                }
            }
            final Model model = list.get(i);
            Picasso.get().load(model.getMedium_cover_image()).into(homeAdapterView.cover);
//            homeAdapterView.title.setText(model.getTitle());
//            homeAdapterView.year.setText("" + model.getYear());
//            homeAdapterView.rateing.setText("" + Double.toString(model.getRating()));
           // qualityAdapter adapter = new qualityAdapter(context, list.get(i).getTorrents());
           // homeAdapterView.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            //homeAdapterView.recyclerView.setAdapter(adapter);
//            homeAdapterView.cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(view.getContext(), MovieDetails.class);
//                    intent.putExtra("obj", model);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    view.getContext().startActivity(intent);
//                }
//            });


        }







    public void setOnBottomReachedListener(bottomReach onBottomReachedListener) {

        this.bottomListener = onBottomReachedListener;
    }

//    @Override
//    public void onBindViewHolder(@NonNull homeAdapterView homeAdapterView, int i) {
//        if (list != null) {
//            if (i == list.size() - 1) {
//
//                bottomListener.onBottomReached(i);
//
//            }
//        }
//        final Model model = list.get(i);
//        Picasso.get().load(model.getMedium_cover_image()).into(homeAdapterView.cover);
//        homeAdapterView.title.setText(model.getTitle());
//        homeAdapterView.year.setText("" + model.getYear());
//        homeAdapterView.rateing.setText(""+Double.toString(model.getRating()));
//        qualityAdapter adapter = new qualityAdapter(context, list.get(i).getTorrents());
//        homeAdapterView.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//        homeAdapterView.recyclerView.setAdapter(adapter);
//        homeAdapterView.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), MovieDetails.class);
//                intent.putExtra("obj", model);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                view.getContext().startActivity(intent);
//            }
//        });
//
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class homeAdapterView extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView cover,topImage;
        TextView year, title;
        RecyclerView recyclerView;
        TextView  rateing;
        Button closeAd;
        public homeAdapterView(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.cover);
//            cover = itemView.findViewById(R.id.cover);
//            title=itemView.findViewById(R.id.Title_movie);
//            year=itemView.findViewById(R.id.year_movie);



        }
    }


}
