

package com.movilix.torrant.core.model.session;

import android.net.Uri;

import com.movilix.torrant.core.exception.DecodeException;
import com.movilix.torrant.core.exception.TorrentAlreadyExistsException;
import com.movilix.torrant.core.exception.UnknownUriException;
import com.movilix.torrant.core.logger.Logger;
import com.movilix.torrant.core.model.AddTorrentParams;
import com.movilix.torrant.core.model.TorrentEngineListener;
import com.movilix.torrant.core.model.data.MagnetInfo;
import com.movilix.torrant.core.model.data.entity.Torrent;
import com.movilix.torrant.core.settings.SessionSettings;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;

public interface TorrentSession
{
    Logger getLogger();

    void addListener(TorrentEngineListener listener);

    void removeListener(TorrentEngineListener listener);

    TorrentDownload getTask(String id);

    void setSettings(@NonNull SessionSettings settings);

    SessionSettings getSettings();

    byte[] getLoadedMagnet(String hash);

    void removeLoadedMagnet(String hash);

    Torrent addTorrent(
            @NonNull AddTorrentParams params,
            boolean removeFile
    ) throws
            IOException,
            TorrentAlreadyExistsException,
            DecodeException,
            UnknownUriException;

    void deleteTorrent(@NonNull String id, boolean withFiles);

    void restoreTorrents();

    MagnetInfo fetchMagnet(@NonNull String uri) throws Exception;

    MagnetInfo parseMagnet(@NonNull String uri);

    void cancelFetchMagnet(@NonNull String infoHash);

    long getDownloadSpeed();

    long getUploadSpeed();

    long getTotalDownload();

    long getTotalUpload();

    int getDownloadSpeedLimit();

    int getUploadSpeedLimit();

    int getListenPort();

    long getDhtNodes();

    void enableIpFilter(@NonNull Uri path);

    void disableIpFilter();

    void pauseAll();

    void resumeAll();

    void pauseAllManually();

    void resumeAllManually();

    void setMaxConnectionsPerTorrent(int connections);

    void setMaxUploadsPerTorrent(int uploads);

    void setAutoManaged(boolean autoManaged);

    boolean isDHTEnabled();

    boolean isPeXEnabled();

    void start();

    void requestStop();

    boolean isRunning();

    long dhtNodes();

    int[] getPieceSizeList();

    void download(@NonNull String magnetUri, File saveDir, boolean paused);

    void setDefaultTrackersList(@NonNull String[] trackersList);
}
