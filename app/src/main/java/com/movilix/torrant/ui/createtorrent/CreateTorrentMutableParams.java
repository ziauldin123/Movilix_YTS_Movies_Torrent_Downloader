

package com.movilix.torrant.ui.createtorrent;

import android.net.Uri;

import com.movilix.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

public class CreateTorrentMutableParams extends BaseObservable
{
    public static final String FILTER_SEPARATOR = "\\|";

    private ObservableField<Uri> seedPath = new ObservableField<>();
    private String seedPathName;
    private String skipFiles;
    private String trackerUrls;
    private String webSeedUrls;
    private int pieceSizeIndex = 0;
    private String comments;
    private boolean startSeeding = false;
    private boolean privateTorrent = false;
    private Uri savePath;

    public ObservableField<Uri> getSeedPath()
    {
        return seedPath;
    }

    @Bindable
    public String getSeedPathName()
    {
        return seedPathName;
    }

    public void setSeedPathName(String seedPathName)
    {
        this.seedPathName = seedPathName;
        notifyPropertyChanged(BR.seedPathName);
    }

    @Bindable
    public String getSkipFiles()
    {
        return skipFiles;
    }

    public void setSkipFiles(String skipFiles)
    {
        this.skipFiles = skipFiles;
        notifyPropertyChanged(BR.skipFiles);
    }

    @Bindable
    public String getTrackerUrls()
    {
        return trackerUrls;
    }

    public void setTrackerUrls(String trackerUrls)
    {
        this.trackerUrls = trackerUrls;
        notifyPropertyChanged(BR.trackerUrls);
    }

    @Bindable
    public String getWebSeedUrls()
    {
        return webSeedUrls;
    }

    public void setWebSeedUrls(String webSeedUrls)
    {
        this.webSeedUrls = webSeedUrls;

    }

    @Bindable
    public int getPieceSizeIndex()
    {
        return pieceSizeIndex;
    }

    public void setPieceSizeIndex(int pieceSizeIndex)
    {
        this.pieceSizeIndex = pieceSizeIndex;
        notifyPropertyChanged(BR.pieceSizeIndex);
    }

    @Bindable
    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
        notifyPropertyChanged(BR.comments);
    }

    @Bindable
    public boolean isStartSeeding()
    {
        return startSeeding;
    }

    public void setStartSeeding(boolean startSeeding)
    {
        this.startSeeding = startSeeding;
        notifyPropertyChanged(BR.startSeeding);
    }

    @Bindable
    public boolean isPrivateTorrent()
    {
        return privateTorrent;
    }

    public void setPrivateTorrent(boolean privateTorrent)
    {
        this.privateTorrent = privateTorrent;
        notifyPropertyChanged(BR.privateTorrent);
    }

    @Bindable
    public Uri getSavePath()
    {
        return savePath;
    }

    public void setSavePath(Uri savePath)
    {
        this.savePath = savePath;
        notifyPropertyChanged(BR.savePath);
    }

    @Override
    public String toString()
    {
        return "CreateTorrentMutableParams{" +
                "seedPath=" + seedPath +
                ", seedPathName='" + seedPathName + '\'' +
                ", skipFiles='" + skipFiles + '\'' +
                ", trackerUrls='" + trackerUrls + '\'' +
                ", webSeedUrls='" + webSeedUrls + '\'' +
                ", pieceSizeIndex=" + pieceSizeIndex +
                ", comments='" + comments + '\'' +
                ", startSeeding=" + startSeeding +
                ", privateTorrent=" + privateTorrent +
                ", savePath=" + savePath +
                '}';
    }
}
