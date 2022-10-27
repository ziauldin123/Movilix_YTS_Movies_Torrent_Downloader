

package com.movilix.torrant.ui.feeditems;

import com.movilix.torrant.core.model.data.entity.FeedItem;

import androidx.annotation.NonNull;

public class FeedItemsListItem extends FeedItem implements Comparable<FeedItemsListItem>
{
    public FeedItemsListItem(@NonNull FeedItem item)
    {
        super(item.id, item.feedId, item.downloadUrl,
              item.articleUrl, item.title, item.pubDate);

        read = item.read;
        fetchDate = item.fetchDate;
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public int compareTo(@NonNull FeedItemsListItem o)
    {

        return Long.compare(o.pubDate, pubDate);
    }

    @Override
    public boolean equals(Object o)
    {
        return super.equals(o);
    }

    public boolean equalsContent(Object o)
    {
        if (!(o instanceof FeedItemsListItem))
            return false;

        if (o == this)
            return true;

        FeedItemsListItem item = (FeedItemsListItem) o;

        return id.equals(item.id) &&
                title.equals(item.title) &&
                feedId == item.feedId &&
                (downloadUrl == null || downloadUrl.equals(item.downloadUrl)) &&
                (articleUrl == null || articleUrl.equals(item.articleUrl)) &&
                pubDate == item.pubDate &&
                fetchDate == item.fetchDate &&
                read == item.read;
    }
}
