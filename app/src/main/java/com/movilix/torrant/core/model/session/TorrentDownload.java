

package com.movilix.torrant.core.model.session;

import android.net.Uri;

import com.movilix.torrant.core.exception.DecodeException;
import com.movilix.torrant.core.model.data.PeerInfo;
import com.movilix.torrant.core.model.data.Priority;
import com.movilix.torrant.core.model.data.TorrentStateCode;
import com.movilix.torrant.core.model.data.TrackerInfo;
import com.movilix.torrant.core.model.data.metainfo.TorrentMetaInfo;
import com.movilix.torrant.core.model.stream.TorrentStream;

import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import io.reactivex.Completable;

/*
 * An interface that represents one stream with running torrent.
 */

public interface TorrentDownload
{
    String getTorrentId();

    Completable requestStop();

    void pause();

    void resume();

    void pauseManually();

    void resumeManually();

    void setAutoManaged(boolean autoManaged);

    boolean isAutoManaged();

    /*
     * A value in the range [0, 100], that represents the progress of the torrent's
     * current task. It may be checking files or downloading
     */

    int getProgress();

    void prioritizeFiles(@NonNull Priority[] priorities);

    long getSize();

    long getDownloadSpeed();

    long getUploadSpeed();

    void remove(boolean withFiles);

    long getActiveTime();

    long getSeedingTime();

    long getReceivedBytes();

    long getTotalSentBytes();

    int getConnectedPeers();

    int getConnectedSeeds();

    int getConnectedLeechers();

    int getTotalPeers();

    int getTotalSeeds();

    int getTotalLeechers();

    void requestTrackerAnnounce();

    Set<String> getTrackersUrl();

    List<TrackerInfo> getTrackerInfoList();

    List<PeerInfo> getPeerInfoList();

    long getTotalWanted();

    void replaceTrackers(@NonNull Set<String> trackers);

    void addTrackers(@NonNull Set<String> trackers);

    void addWebSeeds(@NonNull List<String> urls);

    boolean[] pieces();

    String makeMagnet(boolean includePriorities);

    void setSequentialDownload(boolean sequential);

    void setTorrentName(@NonNull String name);

    long getETA();

    TorrentMetaInfo getTorrentMetaInfo() throws DecodeException;

    String getTorrentName();

    void setDownloadPath(@NonNull Uri path);

    long[] getFilesReceivedBytes();

    void forceRecheck();

    int getNumDownloadedPieces();

    double getShareRatio();

    Uri getPartsFile();

    void setDownloadSpeedLimit(int limit);

    int getDownloadSpeedLimit();

    void setUploadSpeedLimit(int limit);

    int getUploadSpeedLimit();

    String getInfoHash();

    TorrentStateCode getStateCode();

    boolean isPaused();

    boolean isSeeding();

    boolean isFinished();

    boolean isDownloading();

    boolean isSequentialDownload();

    void setMaxConnections(int connections);

    int getMaxConnections();

    void setMaxUploads(int uploads);

    int getMaxUploads();

    double getAvailability(int[] piecesAvailability);

    double[] getFilesAvailability(int[] piecesAvailability);

    int[] getPiecesAvailability();

    boolean havePiece(int pieceIndex);

    void readPiece(int pieceIndex);

    void setInterestedPieces(@NonNull TorrentStream stream, int startPiece, int numPieces);

    TorrentStream getStream(int fileIndex);

    boolean isValid();

    boolean isStopped();

    Priority[] getFilePriorities();

    byte[] getBencode();

    void saveResumeData(boolean force);

    boolean hasMissingFiles();
}
