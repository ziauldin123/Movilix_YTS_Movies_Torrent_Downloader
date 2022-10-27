package com.movilix.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.movilix.R;
import com.movilix.Model.Model;
import com.movilix.MovieDetails;
//import com.movilix.intefce.bottomReach;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class homeAdapter extends RecyclerView.Adapter<homeAdapter.homeAdapterView> {
    Context context;
    ArrayList<Model> list;
//    bottomReach bottomListener;

    public homeAdapter(Context context, ArrayList<Model> list) {

        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public homeAdapterView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_cardview_v1, viewGroup, false);
        return new homeAdapterView(view);
    }

//    public void setOnBottomReachedListener(bottomReach onBottomReachedListener) {
//
//        this.bottomListener = onBottomReachedListener;
//    }

    @Override
    public void onBindViewHolder(@NonNull homeAdapterView homeAdapterView, int i) {

        final Model model = list.get(i);
        Picasso.get().load(model.getMedium_cover_image()).into(homeAdapterView.cover);
        homeAdapterView.title.setText(model.getTitle());
        homeAdapterView.year.setText("" + model.getYear());
        homeAdapterView.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MovieDetails.class);
                intent.putExtra("obj", model);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class homeAdapterView extends RecyclerView.ViewHolder {
        ImageView cover;
        CardView cardView;
        TextView year, title;

        public homeAdapterView(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.cover);
            cardView = itemView.findViewById(R.id.homeview);
            year = itemView.findViewById(R.id.year);
            title = itemView.findViewById(R.id.title);

        }
    }
}
