

package com.movilix.torrant.core.model.stream;

import android.os.Parcel;
import android.os.Parcelable;

import com.movilix.torrant.core.utils.Utils;

public class TorrentStream implements Parcelable
{
    public String id;
    public String torrentId;
    public int selectedFileIndex;
    public int firstFilePiece, lastFilePiece;
    /* The last piece may be smaller than the rest */
    public int lastFilePieceSize;
    public long fileOffset, fileSize;
    public int pieceLength;

    public TorrentStream(String torrentId, int selectedFileIndex, int firstFilePiece,
                         int lastFilePiece, int pieceLength, long fileOffset,
                         long fileSize, int lastFilePieceSize)
    {
        this.id = Utils.makeSha1Hash(torrentId + selectedFileIndex);
        this.torrentId = torrentId;
        this.lastFilePiece = lastFilePiece;
        this.firstFilePiece = firstFilePiece;
        this.pieceLength = pieceLength;
        this.selectedFileIndex = selectedFileIndex;
        this.fileOffset = fileOffset;
        this.fileSize = fileSize;
        this.lastFilePieceSize = lastFilePieceSize;
    }

    public TorrentStream(Parcel source)
    {
        id = source.readString();
        torrentId = source.readString();
        selectedFileIndex = source.readInt();
        firstFilePiece = source.readInt();
        lastFilePiece = source.readInt();
        lastFilePieceSize = source.readInt();
        fileOffset = source.readLong();
        fileSize = source.readLong();
        pieceLength = source.readInt();
    }

    public int bytesToPieceIndex(long bytes)
    {
        return firstFilePiece + (int)(bytes / pieceLength);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(torrentId);
        dest.writeInt(selectedFileIndex);
        dest.writeInt(firstFilePiece);
        dest.writeInt(lastFilePiece);
        dest.writeInt(lastFilePieceSize);
        dest.writeLong(fileOffset);
        dest.writeLong(fileSize);
        dest.writeInt(pieceLength);
    }

    public static final Creator<TorrentStream> CREATOR =
            new Creator<TorrentStream>()
            {
                @Override
                public TorrentStream createFromParcel(Parcel source)
                {
                    return new TorrentStream(source);
                }

                @Override
                public TorrentStream[] newArray(int size)
                {
                    return new TorrentStream[size];
                }
            };

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        return o instanceof TorrentStream && (o == this || id.equals(((TorrentStream)o).id));
    }

    @Override
    public String toString()
    {
        return "TorrentStream{" +
                "id='" + id + '\'' +
                ", torrentId='" + torrentId + '\'' +
                ", selectedFileIndex=" + selectedFileIndex +
                ", firstFilePiece=" + firstFilePiece +
                ", lastFilePiece=" + lastFilePiece +
                ", lastFilePieceSize=" + lastFilePieceSize +
                ", fileOffset=" + fileOffset +
                ", fileSize=" + fileSize +
                ", pieceLength=" + pieceLength +
                '}';
    }
}
