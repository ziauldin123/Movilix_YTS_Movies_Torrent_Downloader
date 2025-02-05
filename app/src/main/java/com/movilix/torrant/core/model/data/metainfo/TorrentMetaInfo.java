

package com.movilix.torrant.core.model.data.metainfo;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.io.IOUtils;
import org.libtorrent4j.TorrentInfo;
import com.movilix.torrant.core.exception.DecodeException;
import com.movilix.torrant.core.utils.Utils;

import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import androidx.annotation.NonNull;

/*
 * Provides full information about the torrent, taken from bencode.
 */

public class TorrentMetaInfo implements Parcelable
{
    @NonNull
    public String torrentName = "";
    @NonNull
    public String sha1Hash = "";
    @NonNull
    public String comment = "";
    @NonNull
    public String createdBy = "";
    public long torrentSize = 0L;
    public long creationDate = 0L;
    public int fileCount = 0;
    public int pieceLength = 0;
    public int numPieces = 0;
    public ArrayList<BencodeFileItem> fileList = new ArrayList<>();

    public TorrentMetaInfo(@NonNull String torrentName, @NonNull String sha1hash)
    {
        this.torrentName = torrentName;
        this.sha1Hash = sha1hash;
    }

    public TorrentMetaInfo(@NonNull byte[] data) throws DecodeException
    {
        try {
            getMetaInfo(TorrentInfo.bdecode(data));

        } catch (Exception e) {
            throw new DecodeException(e);
        }
    }

    public TorrentMetaInfo(TorrentInfo info) throws DecodeException
    {
        try {
            getMetaInfo(info);

        } catch (Exception e) {
            throw new DecodeException(e);
        }
    }

    public TorrentMetaInfo(FileInputStream is) throws DecodeException
    {
        FileChannel chan = null;
        try {
            chan = is.getChannel();
            getMetaInfo(new TorrentInfo(chan
                    .map(FileChannel.MapMode.READ_ONLY, 0, chan.size())));

        } catch (Exception e) {
            throw new DecodeException(e);
        } finally {
            IOUtils.closeQuietly(chan);
        }
    }

    private void getMetaInfo(TorrentInfo info)
    {
        torrentName = info.name();
        sha1Hash = info.infoHash().toHex();
        comment = info.comment();
        createdBy = info.creator();
        /* Correct convert UNIX time (time_t) */
        creationDate = info.creationDate() * 1000L;
        torrentSize = info.totalSize();
        fileCount = info.numFiles();
        fileList = Utils.getFileList(info.origFiles());
        pieceLength = info.pieceLength();
        numPieces = info.numPieces();
    }

    public TorrentMetaInfo (Parcel source)
    {
        torrentName = source.readString();
        sha1Hash = source.readString();
        comment = source.readString();
        createdBy = source.readString();
        torrentSize = source.readLong();
        creationDate = source.readLong();
        fileCount = source.readInt();
        fileList = new ArrayList<>();
        source.readTypedList(fileList, BencodeFileItem.CREATOR);
        pieceLength = source.readInt();
        numPieces = source.readInt();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(torrentName);
        dest.writeString(sha1Hash);
        dest.writeString(comment);
        dest.writeString(createdBy);
        dest.writeLong(torrentSize);
        dest.writeLong(creationDate);
        dest.writeInt(fileCount);
        dest.writeTypedList(fileList);
        dest.writeInt(pieceLength);
        dest.writeInt(numPieces);
    }

    public static final Creator<TorrentMetaInfo> CREATOR =
            new Creator<TorrentMetaInfo>()
            {
                @Override
                public TorrentMetaInfo createFromParcel(Parcel source)
                {
                    return new TorrentMetaInfo(source);
                }

                @Override
                public TorrentMetaInfo[] newArray(int size)
                {
                    return new TorrentMetaInfo[size];
                }
            };

    @Override
    public int hashCode()
    {
        return sha1Hash.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof TorrentMetaInfo))
            return false;

        if (o == this)
            return true;

        TorrentMetaInfo info = (TorrentMetaInfo) o;

        return torrentName.equals(info.torrentName) &&
                sha1Hash.equals(info.sha1Hash) &&
                comment.equals(info.comment) &&
                createdBy.equals(info.createdBy) &&
                torrentSize == info.torrentSize &&
                creationDate == info.creationDate &&
                fileCount == info.fileCount &&
                pieceLength == info.pieceLength &&
                numPieces == info.numPieces;
    }

    @Override
    public String toString()
    {
        return "TorrentMetaInfo{" +
                "torrentName='" + torrentName + '\'' +
                ", sha1Hash='" + sha1Hash + '\'' +
                ", comment='" + comment + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", torrentSize=" + torrentSize +
                ", creationDate=" + creationDate +
                ", fileCount=" + fileCount +
                ", pieceLength=" + pieceLength +
                ", numPieces=" + numPieces +
                ", fileList=" + fileList +
                '}';
    }
}
