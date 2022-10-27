

package com.movilix.torrant.ui.log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class LogMutableParams extends BaseObservable
{
    private boolean logging;
    private boolean logSessionFilter;
    private boolean logDhtFilter;
    private boolean logPeerFilter;
    private boolean logPortmapFilter;
    private boolean logTorrentFilter;

    @Bindable
    public boolean isLogging()
    {
        return logging;
    }

    public void setLogging(boolean logging)
    {
        this.logging = logging;
        notifyPropertyChanged(BR.logging);
    }

    @Bindable
    public boolean isLogSessionFilter()
    {
        return logSessionFilter;
    }

    public void setLogSessionFilter(boolean logSessionFilter)
    {
        this.logSessionFilter = logSessionFilter;
        notifyPropertyChanged(BR.logSessionFilter);
    }

    @Bindable
    public boolean isLogDhtFilter()
    {
        return logDhtFilter;
    }

    public void setLogDhtFilter(boolean logDhtFilter)
    {
        this.logDhtFilter = logDhtFilter;
        notifyPropertyChanged(BR.logDhtFilter);
    }

    @Bindable
    public boolean isLogPeerFilter()
    {
        return logPeerFilter;
    }

    public void setLogPeerFilter(boolean logPeerFilter)
    {
        this.logPeerFilter = logPeerFilter;
        notifyPropertyChanged(BR.logPeerFilter);
    }

    @Bindable
    public boolean isLogPortmapFilter()
    {
        return logPortmapFilter;
    }

    public void setLogPortmapFilter(boolean logPortmapFilter)
    {
        this.logPortmapFilter = logPortmapFilter;
        notifyPropertyChanged(BR.logPeerFilter);
    }

    @Bindable
    public boolean isLogTorrentFilter()
    {
        return logTorrentFilter;
    }

    public void setLogTorrentFilter(boolean logTorrentFilter)
    {
        this.logTorrentFilter = logTorrentFilter;
        notifyPropertyChanged(BR.logTorrentFilter);
    }

    @Override
    public String toString()
    {
        return "LogMutableParams{" +
                "logging=" + logging +
                ", logSessionFilter=" + logSessionFilter +
                ", logDhtFilter=" + logDhtFilter +
                ", logPeerFilter=" + logPeerFilter +
                ", logPortmapFilter=" + logPortmapFilter +
                ", logTorrentFilter=" + logTorrentFilter +
                '}';
    }
}
