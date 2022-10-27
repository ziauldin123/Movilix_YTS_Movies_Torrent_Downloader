

package com.movilix.torrant.core.storage.dao;

import com.movilix.torrant.core.model.data.entity.Torrent;
import com.movilix.torrant.core.model.data.entity.TorrentTagInfo;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public abstract class TorrentDao {
    @Insert
    public abstract void add(Torrent torrent);

    @Update
    public abstract void update(Torrent torrent);

    @Delete
    public abstract void delete(Torrent torrent);

    @Query("SELECT * FROM Torrent")
    public abstract List<Torrent> getAllTorrents();

    @Query("SELECT * FROM Torrent WHERE id = :id")
    public abstract Torrent getTorrentById(String id);

    @Query("SELECT * FROM Torrent WHERE id = :id")
    public abstract Single<Torrent> getTorrentByIdSingle(String id);

    @Query("SELECT * FROM Torrent WHERE id = :id")
    public abstract Flowable<Torrent> observeTorrentById(String id);

    @Insert
    public abstract void addTags(List<TorrentTagInfo> infoList);

    @Query("DELETE FROM TorrentTagInfo WHERE torrentId = :torrentId")
    public abstract void deleteTagsByTorrentId(String torrentId);

    @Transaction
    public void replaceTags(String torrentId, List<TorrentTagInfo> infoList) {
        deleteTagsByTorrentId(torrentId);
        addTags(infoList);
    }

    @Insert
    public abstract void addTag(TorrentTagInfo info);

    @Delete
    public abstract void deleteTag(TorrentTagInfo info);
}
