

package com.movilix.torrant.core.model;

import com.movilix.torrant.core.model.data.ReadPieceInfo;
import com.movilix.torrant.core.model.data.SessionStats;
import com.movilix.torrant.core.model.data.TorrentStateCode;

import androidx.annotation.NonNull;

public abstract class TorrentEngineListener
{
    public void onTorrentAdded(@NonNull String id) {}

    public void onTorrentLoaded(@NonNull String id) {}

    public void onTorrentStateChanged(@NonNull String id,
                                      @NonNull TorrentStateCode prevState,
                                      @NonNull TorrentStateCode curState) {}

    public void onTorrentFinished(@NonNull String id) {}

    public void onTorrentRemoved(@NonNull String id) {}

    public void onTorrentPaused(@NonNull String id) {}

    public void onTorrentResumed(@NonNull String id) {}

    public void onSessionStarted() {}

    public void onSessionStopped() {}

    public void onTorrentMoving(@NonNull String id) {}

    public void onTorrentMoved(@NonNull String id, boolean success) {}

    public void onIpFilterParsed(int ruleCount) {}

    public void onMagnetLoaded(@NonNull String hash, byte[] bencode) {}

    public void onTorrentMetadataLoaded(@NonNull String id, Exception err) {}

    public void onRestoreSessionError(@NonNull String id) {}

    public void onTorrentError(@NonNull String id, Exception e) {}

    public void onSessionError(@NonNull String errorMsg) {}

    public void onNatError(@NonNull String errorMsg) {}

    public void onReadPiece(@NonNull String id, ReadPieceInfo info) {}

    public void onPieceFinished(@NonNull String id, int piece) {}

    public void onSessionStats(@NonNull SessionStats stats) {}
}
