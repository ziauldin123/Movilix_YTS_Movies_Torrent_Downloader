package com.movilix.torrant.core.storage.dao;

import com.movilix.torrant.core.model.data.entity.FastResume;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FastResumeDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(FastResume fastResume);

    @Query("SELECT * FROM FastResume WHERE torrentId = :torrentId")
    FastResume getByTorrentId(String torrentId);
}
