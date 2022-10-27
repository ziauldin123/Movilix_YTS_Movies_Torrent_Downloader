

package com.movilix.torrant.ui.main;

import com.movilix.torrant.core.model.data.TorrentInfo;

import androidx.annotation.NonNull;

/*
 * Wrapper of TorrentInfo class for TorrentListAdapter, that override Object::equals method
 * Necessary for other behavior in case if item was selected (see SelectionTracker).
 */

public class TorrentListItem extends TorrentInfo {
    public TorrentListItem(@NonNull TorrentInfo state) {
        super(
                state.torrentId,
                state.name,
                state.stateCode,
                state.progress,
                state.receivedBytes,
                state.uploadedBytes,
                state.totalBytes,
                state.downloadSpeed,
                state.uploadSpeed,
                state.ETA,
                state.dateAdded,
                state.totalPeers,
                state.peers,
                state.error,
                state.sequentialDownload,
                state.filePriorities,
                state.tags
        );
    }

    @Override
    public int hashCode() {
        return torrentId.hashCode();
    }

    /*
     * Compare objects by their content
     */

    public boolean equalsContent(TorrentListItem item) {
        return super.equals(item);
    }

    /*
     * Compare objects by torrent id
     */

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TorrentListItem))
            return false;

        if (o == this)
            return true;

        return torrentId.equals(((TorrentListItem) o).torrentId);
    }
}
