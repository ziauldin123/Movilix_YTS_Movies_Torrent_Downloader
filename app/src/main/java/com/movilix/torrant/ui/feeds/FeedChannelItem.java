

package com.movilix.torrant.ui.feeds;

import android.text.TextUtils;

import com.movilix.torrant.core.model.data.entity.FeedChannel;

import androidx.annotation.NonNull;

public class FeedChannelItem extends FeedChannel implements Comparable<FeedChannelItem>
{
    public FeedChannelItem(@NonNull FeedChannel channel)
    {
        super(channel.url, channel.name,
              channel.lastUpdate, channel.autoDownload,
              channel.filter, channel.isRegexFilter, channel.fetchError);

        id = channel.id;
    }

    @Override
    public int hashCode()
    {
        return (int)(id ^ (id >>> 32));
    }

    @Override
    public int compareTo(@NonNull FeedChannelItem o)
    {
        String cmp1 = (TextUtils.isEmpty(name) ? url : name);
        String cmp2 = (TextUtils.isEmpty(o.name) ? o.url : o.name);

        return cmp1.compareTo(cmp2);
    }

    @Override
    public boolean equals(Object o)
    {
        return super.equals(o);
    }

    public boolean equalsContent(Object o)
    {
        if (!(o instanceof FeedChannelItem))
            return false;

        if (o == this)
            return true;

        FeedChannelItem item = (FeedChannelItem)o;

        return id == item.id &&
                url.equals(item.url) &&
                (name == null || name.equals(item.name)) &&
                lastUpdate == item.lastUpdate &&
                autoDownload == item.autoDownload &&
                (filter == null || filter.equals(item.filter)) &&
                isRegexFilter == item.isRegexFilter &&
                (fetchError == null || fetchError.equals(item.fetchError));

   }
}
