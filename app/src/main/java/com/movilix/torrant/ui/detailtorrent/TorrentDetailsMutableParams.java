

package com.movilix.torrant.ui.detailtorrent;

import android.net.Uri;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class TorrentDetailsMutableParams extends BaseObservable
{
    private String name;
    private Uri dirPath;
    private boolean sequentialDownload = false;
    private boolean prioritiesChanged = false;

    @Bindable
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public boolean isSequentialDownload()
    {
        return sequentialDownload;
    }

    public void setSequentialDownload(boolean sequentialDownload)
    {
        this.sequentialDownload = sequentialDownload;
        notifyPropertyChanged(BR.sequentialDownload);
    }

    @Bindable
    public Uri getDirPath()
    {
        return dirPath;
    }

    public void setDirPath(Uri dirPath)
    {
        this.dirPath = dirPath;
        notifyPropertyChanged(BR.dirPath);
    }

    @Bindable
    public boolean isPrioritiesChanged()
    {
        return prioritiesChanged;
    }

    public void setPrioritiesChanged(boolean prioritiesChanged)
    {
        this.prioritiesChanged = prioritiesChanged;
        notifyPropertyChanged(BR.prioritiesChanged);
    }

    @Override
    public String toString()
    {
        return "TorrentDetailsMutableParams{" +
                "name='" + name + '\'' +
                ", dirPath=" + dirPath +
                ", sequentialDownload=" + sequentialDownload +
                ", prioritiesChanged=" + prioritiesChanged +
                '}';
    }
}
