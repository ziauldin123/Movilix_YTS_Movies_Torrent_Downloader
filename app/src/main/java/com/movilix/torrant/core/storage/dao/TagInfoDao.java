

package com.movilix.torrant.core.storage.dao;

import com.movilix.torrant.core.model.data.entity.TagInfo;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface TagInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TagInfo info);

    @Update
    void update(TagInfo info);

    @Delete
    void delete(TagInfo info);

    @Query("SELECT * FROM TagInfo WHERE name = :name")
    TagInfo getByName(String name);

    @Query("SELECT * FROM TagInfo")
    Flowable<List<TagInfo>> observeAll();

    @Query("SELECT * FROM TagInfo WHERE id IN " +
            "(SELECT tagId FROM TorrentTagInfo WHERE torrentId = :torrentId)")
    Flowable<List<TagInfo>> observeByTorrentId(String torrentId);

    @Query("SELECT * FROM TagInfo WHERE id IN " +
            "(SELECT tagId FROM TorrentTagInfo WHERE torrentId = :torrentId)")
    Single<List<TagInfo>> getByTorrentIdAsync(String torrentId);

    @Query("SELECT * FROM TagInfo WHERE id IN " +
            "(SELECT tagId FROM TorrentTagInfo WHERE torrentId = :torrentId)")
    List<TagInfo> getByTorrentId(String torrentId);
}
