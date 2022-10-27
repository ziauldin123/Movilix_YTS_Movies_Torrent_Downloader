

package com.movilix.torrant.ui.addtorrent;

import android.net.Uri;

import com.movilix.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

public class AddTorrentMutableParams extends BaseObservable
{
    /* File path or magnet link */
    private String source;
    private boolean fromMagnet;
    private String name;
    private ObservableField<Uri> dirPath = new ObservableField<>();
    private String dirName;
    private long storageFreeSpace = -1;
    private boolean sequentialDownload=true;
    private boolean startAfterAdd = true;
    private boolean ignoreFreeSpace = false;

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public boolean isFromMagnet()
    {
        return fromMagnet;
    }

    public void setFromMagnet(boolean fromMagnet)
    {
        this.fromMagnet = fromMagnet;
    }

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

    public ObservableField<Uri> getDirPath()
    {
        return dirPath;
    }

    @Bindable
    public String getDirName()
    {
        return dirName;
    }

    public void setDirName(String dirName)
    {
        this.dirName = dirName;
        notifyPropertyChanged(BR.dirName);
    }

    @Bindable
    public long getStorageFreeSpace()
    {
        return storageFreeSpace;
    }

    public void setStorageFreeSpace(long storageFreeSpace)
    {
        this.storageFreeSpace = storageFreeSpace;
        notifyPropertyChanged(BR.storageFreeSpace);
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
    public boolean isStartAfterAdd()
    {
        return startAfterAdd;
    }

    public void setStartAfterAdd(boolean startAfterAdd)
    {
        this.startAfterAdd = startAfterAdd;
        notifyPropertyChanged(BR.startAfterAdd);
    }

    @Bindable
    public boolean isIgnoreFreeSpace()
    {
        return ignoreFreeSpace;
    }

    public void setIgnoreFreeSpace(boolean ignoreFreeSpace)
    {
        this.ignoreFreeSpace = ignoreFreeSpace;
        notifyPropertyChanged(BR.ignoreFreeSpace);
    }

    @Override
    public String toString()
    {
        return "AddTorrentMutableParams{" +
                "source='" + source + '\'' +
                ", fromMagnet=" + fromMagnet +
                ", name='" + name + '\'' +
                ", dirPath=" + dirPath +
                ", dirName='" + dirName + '\'' +
                ", storageFreeSpace=" + storageFreeSpace +
                ", sequentialDownload=" + sequentialDownload +
                ", startAfterAdd=" + startAfterAdd +
                ", ignoreFreeSpace=" + ignoreFreeSpace +
                '}';
    }
}
