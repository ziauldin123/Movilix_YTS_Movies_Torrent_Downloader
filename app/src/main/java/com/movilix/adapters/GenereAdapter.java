package com.movilix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.movilix.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GenereAdapter extends RecyclerView.Adapter<GenereAdapter.GenereHolder> {
    Context context;
    ArrayList<String> list;

    public GenereAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GenereHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.generadapter, viewGroup, false);
        return new GenereHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenereHolder genereHolder, int i) {
        if (i == list.size() - 1) {
            genereHolder.textView.setText(list.get(i));
        } else {
            genereHolder.textView.setText(list.get(i) + " \u00b7");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GenereHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public GenereHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.genre);
        }
    }
}
