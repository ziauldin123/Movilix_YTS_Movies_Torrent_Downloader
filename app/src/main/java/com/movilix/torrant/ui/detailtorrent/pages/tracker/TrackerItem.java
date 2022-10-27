

package com.movilix.torrant.ui.detailtorrent.pages.tracker;

import com.movilix.torrant.core.model.data.TrackerInfo;

import androidx.annotation.NonNull;

public class TrackerItem extends TrackerInfo
{
    public TrackerItem(@NonNull TrackerInfo state)
    {
        super(state.url, state.message, state.tier, state.status);
    }

    @Override
    public int hashCode()
    {
        return url.hashCode();
    }

    public boolean equalsContent(Object o)
    {
        return super.equals(o);
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof TrackerItem))
            return false;

        if (o == this)
            return true;

        return url.equals(((TrackerItem)o).url);
    }
}
