

package com.movilix.torrant.ui;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*
 * A base item for the file list adapters.
 */

public class FileItem implements Parcelable, Comparable<FileItem>
{
    public int index;
    public String name;
    public boolean isFile;
    public long size;

    public FileItem(int index, String name, boolean isFile, long size)
    {
        this.index = index;
        this.name = name;
        this.isFile = isFile;
        this.size = size;
    }

    public FileItem(Parcel source)
    {
        index = source.readInt();
        name = source.readString();
        isFile = source.readByte() != 0;
        size = source.readLong();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(index);
        dest.writeString(name);
        dest.writeByte((byte)(isFile ? 1 : 0));
        dest.writeLong(size);
    }

    public static final Creator<FileItem> CREATOR =
            new Creator<FileItem>()
            {
                @Override
                public FileItem createFromParcel(Parcel source)
                {
                    return new FileItem(source);
                }

                @Override
                public FileItem[] newArray(int size)
                {
                    return new FileItem[size];
                }
            };

    @Override
    public boolean equals(@Nullable Object o)
    {
        if (!(o instanceof FileItem))
            return false;

        if (o == this)
            return true;

        FileItem item = (FileItem) o;

        return index == item.index &&
                (name == null || name.equals(item.name)) &&
                isFile == item.isFile;
    }

    @Override
    public int hashCode()
    {
        int prime = 31, result = 1;

        result = prime * result + ((name == null) ? 0 : name.hashCode());
        if (isFile)
            result = prime * result + index;

        return result;
    }

    @Override
    public int compareTo(@NonNull FileItem o)
    {
        return name.compareTo(o.name);
    }

    @Override
    public String toString()
    {
        return "FileItem{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", isFile=" + isFile +
                ", size=" + size +
                '}';
    }
}
