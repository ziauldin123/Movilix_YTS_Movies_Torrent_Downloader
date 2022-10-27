

package com.movilix.torrant.ui.addfeed;



import com.movilix.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class AddFeedMutableParams extends BaseObservable
{
    private long feedId = -1;
    private String url;
    private String name;
    private boolean autoDownload = false;
    private boolean notDownloadImmediately = true;
    private String filter;
    private boolean regexFilter = false;

    public long getFeedId()
    {
        return feedId;
    }

    public void setFeedId(long feedId)
    {
        this.feedId = feedId;
    }

    @Bindable
    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
        notifyPropertyChanged(BR.url);
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

    @Bindable
    public boolean isAutoDownload()
    {
        return autoDownload;
    }

    public void setAutoDownload(boolean autoDownload)
    {
        this.autoDownload = autoDownload;
        notifyPropertyChanged(BR.autoDownload);
    }

    @Bindable
    public boolean isNotDownloadImmediately()
    {
        return notDownloadImmediately;
    }

    public void setNotDownloadImmediately(boolean notDownloadImmediately)
    {
        this.notDownloadImmediately = notDownloadImmediately;
        notifyPropertyChanged(BR.notDownloadImmediately);
    }

    @Bindable
    public String getFilter()
    {
        return filter;
    }

    public void setFilter(String filter)
    {
        this.filter = filter;
        notifyPropertyChanged(BR.filter);
    }

    @Bindable
    public boolean isRegexFilter()
    {
        return regexFilter;
    }

    public void setRegexFilter(boolean regexFilter)
    {
        this.regexFilter = regexFilter;
        notifyPropertyChanged(BR.regexFilter);
    }
}
