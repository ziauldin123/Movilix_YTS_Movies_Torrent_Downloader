package com.movilix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.movilix.R;
import com.movilix.Model.Torrent;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class qualityAdapter extends RecyclerView.Adapter<qualityAdapter.Qulity> {
    Context context;
    ArrayList<Torrent> list;

    public qualityAdapter(Context context, ArrayList<Torrent> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Qulity onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.qualityview, viewGroup, false);

        return new Qulity(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Qulity qulity, final int i) {
        qulity.quality.setText(list.get(i).getQuality());

    }

    @Override
    public int getItemCount() {
        if(list.size()>4) {
            return 3;
        }else {
            return list.size();
        }
    }

    class Qulity extends RecyclerView.ViewHolder {
        TextView quality;

        public Qulity(@NonNull View itemView) {
            super(itemView);
            quality = itemView.findViewById(R.id.quality);
        }
    }

}
