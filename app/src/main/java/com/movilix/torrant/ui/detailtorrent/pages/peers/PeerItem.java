

package com.movilix.torrant.ui.detailtorrent.pages.peers;

import com.movilix.torrant.core.model.data.PeerInfo;

import androidx.annotation.NonNull;

public class PeerItem extends PeerInfo
{
    public PeerItem(@NonNull PeerInfo state)
    {
        super(state.ip, state.client, state.totalDownload,
                state.totalUpload, state.relevance, state.connectionType,
                state.port, state.progress, state.downSpeed, state.upSpeed);
    }

    @Override
    public int hashCode()
    {
        return ip.hashCode();
    }

    public boolean equalsContent(Object o)
    {
        return super.equals(o);
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof PeerItem))
            return false;

        if (o == this)
            return true;

        return ip.equals(((PeerItem)o).ip);
    }
}
