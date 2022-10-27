

package com.movilix.torrant.core.storage;

import com.movilix.torrant.core.model.data.entity.TagInfo;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface TagRepository {
    void insert(@NonNull TagInfo info);

    void update(@NonNull TagInfo info);

    void delete(@NonNull TagInfo info);

    TagInfo getByName(@NonNull String name);

    Flowable<List<TagInfo>> observeAll();

    Flowable<List<TagInfo>> observeByTorrentId(String torrentId);

    Single<List<TagInfo>> getByTorrentIdAsync(String torrentId);

    List<TagInfo> getByTorrentId(String torrentId);
}
