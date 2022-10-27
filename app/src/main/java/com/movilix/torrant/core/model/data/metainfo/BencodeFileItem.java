

package com.movilix.torrant.core.model.data.metainfo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/*
 * The class encapsulates path, index and file size, extracted from bencode.
 */

public class BencodeFileItem implements Parcelable, Comparable<BencodeFileItem>
{
    private String path;
    private int index;
    private long size;

    public BencodeFileItem(String path, int index, long size)
    {
        this.path = path;
        this.index = index;
        this.size = size;
    }

    public BencodeFileItem(Parcel source)
    {
        path = source.readString();
        index = source.readInt();
        size = source.readLong();
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public long getSize()
    {
        return size;
    }

    public void setSize(long size)
    {
        this.size = size;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(path);
        dest.writeInt(index);
        dest.writeLong(size);
    }

    public static final Creator<BencodeFileItem> CREATOR =
            new Creator<BencodeFileItem>()
            {
                @Override
                public BencodeFileItem createFromParcel(Parcel source)
                {
                    return new BencodeFileItem(source);
                }

                @Override
                public BencodeFileItem[] newArray(int size)
                {
                    return new BencodeFileItem[size];
                }
            };

    @Override
    public int compareTo(@NonNull BencodeFileItem anotner)
    {
        return path.compareTo(anotner.path);
    }

    @Override
    public String toString()
    {
        return "BencodeFileItem{" +
                "path='" + path + '\'' +
                ", index=" + index +
                ", size=" + size +
                '}';
    }
}