package com.movilix.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.movilix.inteface.MovieItemClick;
import com.squareup.picasso.Picasso;
import com.movilix.MovieDetails;
import com.movilix.R;
import com.movilix.Model.Model;
import com.movilix.inteface.bottomReach;
//import com.movilix.yts.MovieDetails;
//import com.movilix.yts.intefce.bottomReach;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ExtendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<Model> list;
    bottomReach bottomListener;
    int page,viewNo;
    MovieItemClick movieItemClick;



    public ExtendAdapter(Context context, ArrayList<Model> list, int page,int mView,MovieItemClick itemClick) {
        this.context = context;
        this.list = list;
        this.page=page;
        this.viewNo=mView;
        this.movieItemClick=itemClick;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        if (viewNo == 2) {
            view = LayoutInflater.from(context).inflate(R.layout.slider_layout, viewGroup, false);
            viewHolder = new homeSliderView(view);
//
        } else  {
           view = LayoutInflater.from(context).inflate(R.layout.movie_cardview, viewGroup, false);
            viewHolder = new homeAdapterView(view);

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

//
        if(viewNo==1) {
            homeAdapterView homeAdapterView = (homeAdapterView) holder;

            if (list != null) {
                if (i == list.size() - 1) {

                    bottomListener.onBottomReached(i);

                }
            }
            final Model model = list.get(i);
            Picasso.get().load(model.getMedium_cover_image()).into(homeAdapterView.cover);
//
            homeAdapterView.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieItemClick.onItemClick(model);
                }
            });

//            homeAdapterView.cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, MovieDetails.class);
//                    intent.putExtra("obj", model);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    view.getContext().startActivity(intent);
//                }
//            });
//            homeAdapterView.cardView.onIte,
        }else {
            homeSliderView homeSliderView= (homeSliderView) holder;
            final Model model = list.get(i);
            Picasso.get().load(model.getLarge_cover_image()).placeholder(R.drawable.placeholder_movie).into(homeSliderView.cover);
            homeSliderView.title.setText(model.getTitle());
            homeSliderView.year.setText("" + model.getYear());
            homeSliderView.ratingBar.setRating(Float.parseFloat(""+(model.getRating()/2)));
            homeSliderView.rating.setText(""+Float.parseFloat(""+model.getRating()));

            qualityAdapter adapter = new qualityAdapter(context, list.get(i).getTorrents());
            homeSliderView.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            homeSliderView.recyclerView.setAdapter(adapter);

            GenereAdapter genereAdapter = new GenereAdapter(context, model.getGener());
            homeSliderView.genere.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            homeSliderView.genere.setAdapter(genereAdapter);
            Log.d("extend_adapter", "quality adapter:  "+ adapter.getItemCount());
            if (adapter.getItemCount() > 0) {
                homeSliderView.recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Call smooth scroll
                        homeSliderView.recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    }
                });
            }

            homeSliderView.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieDetails.class);
                intent.putExtra("obj", model);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });

        }
//

        }







    public void setOnBottomReachedListener(bottomReach onBottomReachedListener) {

        this.bottomListener = onBottomReachedListener;
    }

//

    @Override
    public int getItemCount() {
        return list.size();
    }




    class homeAdapterView extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView cover, topImage;
        TextView year, title;
        RecyclerView recyclerView;
        TextView rateing;
        Button closeAd;

        public homeAdapterView(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.homeview);
            cover = itemView.findViewById(R.id.cover);


        }
    }
    class homeSliderView extends RecyclerView.ViewHolder   {
            CardView cardView;
            ImageView cover, topImage;
            TextView year, title,rating;
            RecyclerView recyclerView,genere;
            RatingBar ratingBar;
            Button closeAd;

            public homeSliderView(@NonNull View itemView)  {
                super(itemView);
                cardView = itemView.findViewById(R.id.homeview);
                cover = itemView.findViewById(R.id.slidercover);
                title = itemView.findViewById(R.id.title_movie);
                year = itemView.findViewById(R.id.year_movie);
                ratingBar=itemView.findViewById(R.id.rating);
                rating=itemView.findViewById(R.id.imdRating);
                recyclerView = itemView.findViewById(R.id.qualityrecyle);
                genere = itemView.findViewById(R.id.genre_movie_recyle);


            }
        }


}
