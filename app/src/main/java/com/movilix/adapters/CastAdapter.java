package com.movilix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.movilix.R;
import com.movilix.Model.Cast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.castHolder> {
    Context context;
    ArrayList<Cast> list;

    public CastAdapter(Context context, ArrayList<Cast> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public castHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cast, viewGroup, false);
        return new castHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull castHolder castHolder, int i) {
        Cast cast = list.get(i);
        castHolder.charcter.setText("" + cast.getChracerName());
        castHolder.name.setText("" + cast.getName());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class castHolder extends RecyclerView.ViewHolder {
        TextView name, charcter;

        public castHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            charcter = itemView.findViewById(R.id.chracter);
        }
    }
}
