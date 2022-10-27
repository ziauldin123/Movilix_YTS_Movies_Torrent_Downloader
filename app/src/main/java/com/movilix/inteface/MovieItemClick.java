package com.movilix.inteface;

import android.view.View;

import com.movilix.Model.Model;
import com.movilix.Model.Torrent;

public interface MovieItemClick extends View.OnClickListener {
    void onItemClick(Model model);
    
}
