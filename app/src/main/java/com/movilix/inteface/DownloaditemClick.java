package com.movilix.inteface;

import android.view.View;

import com.movilix.Model.Model;
import com.movilix.Model.Torrent;

public interface DownloaditemClick extends View.OnClickListener {
    void onItemDownload(Torrent torrent);
}
