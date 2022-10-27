

package com.movilix.torrant.core.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.movilix.torrant.core.model.data.entity.TagInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

/*
 * The class provides a package model with dynamically changing information
 * about state of the torrent, sent from the service.
 */

public class TorrentInfo extends AbstractInfoParcel {
    @NonNull
    public String torrentId;
    public String name = "";
    public com.movilix.torrant.core.model.data.TorrentStateCode stateCode = com.movilix.torrant.core.model.data.TorrentStateCode.UNKNOWN;
    public int progress = 0;
    public long receivedBytes = 0L;
    public long uploadedBytes = 0L;
    public long totalBytes = 0L;
    public long downloadSpeed = 0L;
    public long uploadSpeed = 0L;
    public long ETA = -1L;
    public long dateAdded = 0L;
    public int totalPeers = 0;
    public int peers = 0;
    public String error;
    public boolean sequentialDownload = true;
    public Priority[] filePriorities = new Priority[0];
    public List<TagInfo> tags = new ArrayList<>();

    public TorrentInfo(
            @NonNull String torrentId,
            String name,
            long dateAdded,
            String error,
            @NonNull List<TagInfo> tags
    ) {
        super(torrentId);

        this.torrentId = torrentId;
        this.name = name;
        this.stateCode = com.movilix.torrant.core.model.data.TorrentStateCode.STOPPED;
        this.dateAdded = dateAdded;
        this.error = error;
        this.tags = tags;
    }

    public TorrentInfo(
            @NonNull String torrentId,
            String name,
            com.movilix.torrant.core.model.data.TorrentStateCode stateCode,
            int progress,
            long receivedBytes,
            long uploadedBytes,
            long totalBytes,
            long downloadSpeed,
            long uploadSpeed,
            long ETA,
            long dateAdded,
            int totalPeers,
            int peers,
            String error,
            boolean sequentialDownload,
            Priority[] filePriorities,
            @NonNull List<TagInfo> tags
    ) {
        super(torrentId);

        this.torrentId = torrentId;
        this.name = name;
        this.stateCode = stateCode;
        this.progress = progress;
        this.receivedBytes = receivedBytes;
        this.uploadedBytes = uploadedBytes;
        this.totalBytes = totalBytes;
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
        this.ETA = ETA;
        this.dateAdded = dateAdded;
        this.totalPeers = totalPeers;
        this.peers = peers;
        this.error = error;
        this.sequentialDownload = sequentialDownload;
        this.filePriorities = filePriorities;
        this.tags = tags;
    }

    public TorrentInfo(Parcel source) {
        super(source);

        torrentId = source.readString();
        name = source.readString();
        stateCode = com.movilix.torrant.core.model.data.TorrentStateCode.fromValue(source.readInt());
        progress = source.readInt();
        receivedBytes = source.readLong();
        uploadedBytes = source.readLong();
        totalBytes = source.readLong();
        downloadSpeed = source.readLong();
        uploadSpeed = source.readLong();
        ETA = source.readLong();
        dateAdded = source.readLong();
        totalPeers = source.readInt();
        peers = source.readInt();
        error = source.readString();
        sequentialDownload = source.readByte() != 0;
        filePriorities = (Priority[]) source.readArray(Priority.class.getClassLoader());
        tags = new ArrayList<>();
        source.readTypedList(tags, TagInfo.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeString(torrentId);
        dest.writeString(name);
        dest.writeInt(stateCode.value());
        dest.writeInt(progress);
        dest.writeLong(receivedBytes);
        dest.writeLong(uploadedBytes);
        dest.writeLong(totalBytes);
        dest.writeLong(downloadSpeed);
        dest.writeLong(uploadSpeed);
        dest.writeLong(ETA);
        dest.writeLong(dateAdded);
        dest.writeInt(totalPeers);
        dest.writeInt(peers);
        dest.writeString(error);
        dest.writeByte((byte) (sequentialDownload ? 1 : 0));
        dest.writeArray(filePriorities);
        dest.writeTypedList(tags);
    }

    public static final Parcelable.Creator<TorrentInfo> CREATOR =
            new Parcelable.Creator<TorrentInfo>() {
                @Override
                public TorrentInfo createFromParcel(Parcel source) {
                    return new TorrentInfo(source);
                }

                @Override
                public TorrentInfo[] newArray(int size) {
                    return new TorrentInfo[size];
                }
            };

    @Override
    public int compareTo(@NonNull Object another) {
        return name.compareTo(((TorrentInfo) another).name);
    }

    @Override
    public int hashCode() {
        int prime = 31, result = 1;

        result = prime * result + torrentId.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((stateCode == null) ? 0 : stateCode.hashCode());
        result = prime * result + progress;
        result = prime * result + (int) (receivedBytes ^ (receivedBytes >>> 32));
        result = prime * result + (int) (uploadedBytes ^ (uploadedBytes >>> 32));
        result = prime * result + (int) (totalBytes ^ (totalBytes >>> 32));
        result = prime * result + (int) (downloadSpeed ^ (downloadSpeed >>> 32));
        result = prime * result + (int) (uploadSpeed ^ (uploadSpeed >>> 32));
        result = prime * result + (int) (ETA ^ (ETA >>> 32));
        result = prime * result + (int) (dateAdded ^ (dateAdded >>> 32));
        result = prime * result + totalPeers;
        result = prime * result + peers;
        result = prime * result + ((error == null) ? 0 : error.hashCode());
        result = prime * result + (sequentialDownload ? 1 : 0);
        result = prime * result + Arrays.hashCode(filePriorities);
        result = prime * result + tags.hashCode();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TorrentInfo))
            return false;

        if (o == this)
            return true;

        TorrentInfo info = (TorrentInfo) o;

        return (torrentId.equals(info.torrentId) &&
                (name == null || name.equals(info.name)) &&
                (stateCode == null || stateCode.equals(info.stateCode)) &&
                progress == info.progress &&
                receivedBytes == info.receivedBytes &&
                uploadedBytes == info.uploadedBytes &&
                totalBytes == info.totalBytes &&
                downloadSpeed == info.downloadSpeed &&
                uploadSpeed == info.uploadSpeed &&
                ETA == info.ETA &&
                dateAdded == info.dateAdded &&
                totalPeers == info.totalPeers &&
                peers == info.peers &&
                (error == null || error.equals(info.error)) &&
                sequentialDownload == info.sequentialDownload &&
                Arrays.equals(filePriorities, info.filePriorities) &&
                tags.equals(info.tags));
    }

    @Override
    public String toString() {
        return "TorrentInfo{" +
                "torrentId='" + torrentId + '\'' +
                ", name='" + name + '\'' +
                ", stateCode=" + stateCode +
                ", progress=" + progress +
                ", receivedBytes=" + receivedBytes +
                ", uploadedBytes=" + uploadedBytes +
                ", totalBytes=" + totalBytes +
                ", downloadSpeed=" + downloadSpeed +
                ", uploadSpeed=" + uploadSpeed +
                ", ETA=" + ETA +
                ", dateAdded=" + dateAdded +
                ", totalPeers=" + totalPeers +
                ", peers=" + peers +
                ", error='" + error + '\'' +
                ", sequentialDownload=" + sequentialDownload +
                ", filePriorities=" + Arrays.toString(filePriorities) +
                ", tags=" + tags +
                '}';
    }
}
