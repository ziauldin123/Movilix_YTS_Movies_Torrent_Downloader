

package com.movilix.torrant.ui.detailtorrent;

import com.movilix.BR;
import com.movilix.torrant.core.model.data.AdvancedTorrentInfo;
import com.movilix.torrant.core.model.data.TorrentInfo;
import com.movilix.torrant.core.model.data.entity.Torrent;
import com.movilix.torrant.core.model.data.metainfo.TorrentMetaInfo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class TorrentDetailsInfo extends BaseObservable
{
    private Torrent torrent;
    private TorrentMetaInfo metaInfo;
    private TorrentInfo torrentInfo;
    private AdvancedTorrentInfo advancedInfo;
    private String dirName;
    private long storageFreeSpace = -1;

    @Bindable
    public Torrent getTorrent()
    {
        return torrent;
    }

    public void setTorrent(Torrent torrent)
    {
        this.torrent = torrent;
        notifyPropertyChanged(BR.torrent);
    }

    @Bindable
    public TorrentInfo getTorrentInfo()
    {
        return torrentInfo;
    }

    public void setTorrentInfo(TorrentInfo torrentInfo)
    {
        this.torrentInfo = torrentInfo;
        notifyPropertyChanged(BR.torrentInfo);
    }

    @Bindable
    public AdvancedTorrentInfo getAdvancedInfo()
    {
        return advancedInfo;
    }

    public void setAdvancedInfo(AdvancedTorrentInfo advancedInfo)
    {
        this.advancedInfo = advancedInfo;
        notifyPropertyChanged(BR.advancedInfo);
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
    public TorrentMetaInfo getMetaInfo()
    {
        return metaInfo;
    }

    public void setMetaInfo(TorrentMetaInfo metaInfo)
    {
        this.metaInfo = metaInfo;
        notifyPropertyChanged(BR.metaInfo);
    }

    @Override
    public String toString()
    {
        return "TorrentDetailsInfo{" +
                "torrent=" + torrent +
                ", metaInfo=" + metaInfo +
                ", torrentInfo=" + torrentInfo +
                ", advancedInfo=" + advancedInfo +
                ", dirName='" + dirName + '\'' +
                ", storageFreeSpace=" + storageFreeSpace +
                '}';
    }
}
