

package com.movilix.torrant.core.storage;

import android.content.Context;

import com.movilix.torrant.core.model.data.entity.FastResume;
import com.movilix.torrant.core.model.data.entity.TagInfo;
import com.movilix.torrant.core.model.data.entity.Torrent;
import com.movilix.torrant.core.model.data.entity.TorrentTagInfo;
import com.movilix.torrant.core.system.SystemFacadeHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class TorrentRepositoryImpl implements com.movilix.torrant.core.storage.TorrentRepository {
    private static final String TAG = TorrentRepositoryImpl.class.getSimpleName();

    private static final class FileDataModel {
        private static final String TORRENT_SESSION_FILE = "session";
    }

    private Context appContext;
    private com.movilix.torrant.core.storage.AppDatabase db;

    public TorrentRepositoryImpl(@NonNull Context appContext, @NonNull com.movilix.torrant.core.storage.AppDatabase db) {
        this.appContext = appContext;
        this.db = db;
    }

    @Override
    public void addTorrent(@NonNull Torrent torrent) {
        db.torrentDao().add(torrent);
    }

    @Override
    public void updateTorrent(@NonNull Torrent torrent) {
        db.torrentDao().update(torrent);
    }

    @Override
    public void deleteTorrent(@NonNull Torrent torrent) {
        db.torrentDao().delete(torrent);
    }

    @Override
    public Torrent getTorrentById(@NonNull String id) {
        return db.torrentDao().getTorrentById(id);
    }

    @Override
    public Single<Torrent> getTorrentByIdSingle(@NonNull String id) {
        return db.torrentDao().getTorrentByIdSingle(id);
    }

    @Override
    public Flowable<Torrent> observeTorrentById(@NonNull String id) {
        return db.torrentDao().observeTorrentById(id);
    }

    @Override
    public List<Torrent> getAllTorrents() {
        return db.torrentDao().getAllTorrents();
    }

    @Override
    public void addFastResume(@NonNull FastResume fastResume) {
        db.fastResumeDao().add(fastResume);
    }

    @Override
    public FastResume getFastResumeById(@NonNull String torrentId) {
        return db.fastResumeDao().getByTorrentId(torrentId);
    }

    @Override
    public void saveSession(@NonNull byte[] data) throws IOException {
        String dataDir = appContext.getExternalFilesDir(null).getAbsolutePath();
        File sessionFile = new File(dataDir, FileDataModel.TORRENT_SESSION_FILE);

        org.apache.commons.io.FileUtils.writeByteArrayToFile(sessionFile, data);
    }

    @Override
    public String getSessionFile() {
        if (SystemFacadeHelper.getFileSystemFacade(appContext).isStorageReadable()) {
            String dataDir = appContext.getExternalFilesDir(null).getAbsolutePath();
            File session = new File(dataDir, FileDataModel.TORRENT_SESSION_FILE);

            if (session.exists())
                return session.getAbsolutePath();
        }

        return null;
    }

    @Override
    public void replaceTags(@NonNull String torrentId, @NonNull List<TagInfo> tags) {
        ArrayList<TorrentTagInfo> tagInfoList = new ArrayList<>();
        for (TagInfo tag : tags) {
            tagInfoList.add(new TorrentTagInfo(tag.id, torrentId));
        }
        db.torrentDao().replaceTags(torrentId, tagInfoList);
    }

    @Override
    public void addTag(@NonNull String torrentId, @NonNull TagInfo tag) {
        db.torrentDao().addTag(new TorrentTagInfo(tag.id, torrentId));
    }

    @Override
    public void deleteTag(@NonNull String torrentId, @NonNull TagInfo tag) {
        db.torrentDao().deleteTag(new TorrentTagInfo(tag.id, torrentId));
    }

    /*
     * Search directory with data of added torrent (in standard data directory).
     * Returns path to the directory found if successful or null if the directory is not found.
     */

    private String getTorrentDataDir(Context context, String id) {
        if (SystemFacadeHelper.getFileSystemFacade(context).isStorageReadable()) {
            File dataDir = new File(context.getExternalFilesDir(null), id);
            if (dataDir.exists()) {
                return dataDir.getAbsolutePath();
            } else {
                return makeTorrentDataDir(context, id);
            }
        }

        return null;
    }

    /*
     * Create a directory to store data of added torrent (in standard data directory)
     * Returns path to the new directory if successful or null due to an error.
     */

    private String makeTorrentDataDir(Context context, String name) {
        if (!SystemFacadeHelper.getFileSystemFacade(context).isStorageWritable())
            return null;

        String dataDir = context.getExternalFilesDir(null).getAbsolutePath();
        File newDir = new File(dataDir, name);

        return (newDir.mkdir()) ? newDir.getAbsolutePath() : null;
    }
}
