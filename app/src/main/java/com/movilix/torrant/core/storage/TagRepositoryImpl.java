

package com.movilix.torrant.core.storage;

import com.movilix.torrant.core.model.data.entity.TagInfo;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class TagRepositoryImpl implements com.movilix.torrant.core.storage.TagRepository {
    private final @NonNull
    com.movilix.torrant.core.storage.AppDatabase db;

    public TagRepositoryImpl(@NonNull com.movilix.torrant.core.storage.AppDatabase db) {
        this.db = db;
    }

    @Override
    public void insert(@NonNull TagInfo info) {
        db.tagInfoDao().insert(info);
    }

    @Override
    public void update(@NonNull TagInfo info) {
        db.tagInfoDao().update(info);
    }

    @Override
    public void delete(@NonNull TagInfo info) {
        db.tagInfoDao().delete(info);
    }

    @Override
    public TagInfo getByName(@NonNull String name) {
        return db.tagInfoDao().getByName(name);
    }

    @Override
    public Flowable<List<TagInfo>> observeAll() {
        return db.tagInfoDao().observeAll();
    }

    @Override
    public Flowable<List<TagInfo>> observeByTorrentId(String torrentId) {
        return db.tagInfoDao().observeByTorrentId(torrentId);
    }

    @Override
    public Single<List<TagInfo>> getByTorrentIdAsync(String torrentId) {
        return db.tagInfoDao().getByTorrentIdAsync(torrentId);
    }

    @Override
    public List<TagInfo> getByTorrentId(String torrentId) {
        return db.tagInfoDao().getByTorrentId(torrentId);
    }
}
