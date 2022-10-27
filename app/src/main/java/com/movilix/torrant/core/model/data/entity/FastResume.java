

package com.movilix.torrant.core.model.data.entity;



import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index(value = "torrentId")},
        foreignKeys = @ForeignKey(
                entity = com.movilix.torrant.core.model.data.entity.Torrent.class,
                parentColumns = "id",
                childColumns = "torrentId",
                onDelete = CASCADE))

public class FastResume
{
    @PrimaryKey
    @NonNull
    public String torrentId;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    @NonNull
    public byte[] data;

    public FastResume(@NonNull String torrentId, @NonNull byte[] data)
    {
        this.torrentId = torrentId;
        this.data = data;
    }
}
