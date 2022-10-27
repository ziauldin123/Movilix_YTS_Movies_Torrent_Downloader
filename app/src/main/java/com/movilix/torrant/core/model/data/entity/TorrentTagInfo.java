

package com.movilix.torrant.core.model.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = TagInfo.class,
                        parentColumns = "id",
                        childColumns = "tagId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = com.movilix.torrant.core.model.data.entity.Torrent.class,
                        parentColumns = "id",
                        childColumns = "torrentId",
                        onDelete = ForeignKey.CASCADE
                ),
        },
        indices = {
                @Index("tagId"),
                @Index("torrentId"),
        },
        primaryKeys = {"tagId", "torrentId"}
)
public class TorrentTagInfo {
    public final long tagId;

    @NonNull
    public final String torrentId;

    public TorrentTagInfo(
            long tagId,
            @NonNull String torrentId
    ) {
        this.tagId = tagId;
        this.torrentId = torrentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TorrentTagInfo that = (TorrentTagInfo) o;

        if (tagId != that.tagId) return false;
        return torrentId.equals(that.torrentId);
    }

    @Override
    public int hashCode() {
        int result = (int) (tagId ^ (tagId >>> 32));
        result = 31 * result + torrentId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TorrentTagInfo{" +
                "tagId='" + tagId + '\'' +
                ", torrentId='" + torrentId + '\'' +
                '}';
    }
}
