package com.movilix.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.movilix.Model.Model;
//import com.movilix.MovieDetails;
import com.movilix.MovieDetails;
import com.movilix.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.homeAdapterView> {
    Context context;
    ArrayList<Model> list;

    public SearchAdapter(Context context, ArrayList<Model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public homeAdapterView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_movie_card, viewGroup, false);
        return new homeAdapterView(view);
    }


    @Override
    public void onBindViewHolder(@NonNull homeAdapterView homeAdapterView, int i) {

        final Model model = list.get(i);
        Picasso.get().load(model.getMedium_cover_image()).into(homeAdapterView.cover);
        homeAdapterView.title.setText(model.getTitle());
        homeAdapterView.year.setText("" + model.getYear());
        homeAdapterView.ratingBar.setRating(Float.parseFloat(""+model.getRating()));

        qualityAdapter adapter = new qualityAdapter(context, list.get(i).getTorrents());
        homeAdapterView.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        homeAdapterView.recyclerView.setAdapter(adapter);

        homeAdapterView.movieItem.setOnClickListener(new View.OnClickListener() {
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
        CardView cardView;
        ImageView cover;
        TextView year, title;
        RecyclerView recyclerView;
        RatingBar ratingBar;
        LinearLayout movieItem;

        public homeAdapterView(@NonNull View itemView) {
            super(itemView);
            movieItem = itemView.findViewById(R.id.movieItem);
            cover = itemView.findViewById(R.id.cover);
            year = itemView.findViewById(R.id.year);
            title = itemView.findViewById(R.id.title);
            ratingBar=itemView.findViewById(R.id.rating);
            recyclerView = itemView.findViewById(R.id.qualityrecyle);
        }
    }
}
