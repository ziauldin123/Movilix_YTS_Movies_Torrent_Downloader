

package com.movilix.torrant.ui.addtorrent;

import android.os.Parcel;
import android.os.Parcelable;

import com.movilix.torrant.core.model.filetree.BencodeFileTree;
import com.movilix.torrant.ui.FileItem;

import androidx.annotation.NonNull;

public class DownloadableFileItem extends FileItem
{
    public boolean selected;

    public DownloadableFileItem(@NonNull BencodeFileTree tree)
    {
        super(tree.getIndex(), tree.getName(), tree.isFile(), tree.size());

        selected = tree.isSelected();
    }

    public DownloadableFileItem(Parcel source)
    {
        super(source);

        selected = source.readByte() != 0;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        super.writeToParcel(dest, flags);

        dest.writeByte((byte)(selected ? 1 : 0));
    }

    public static final Parcelable.Creator<DownloadableFileItem> CREATOR =
            new Parcelable.Creator<DownloadableFileItem>()
            {
                @Override
                public DownloadableFileItem createFromParcel(Parcel source)
                {
                    return new DownloadableFileItem(source);
                }

                @Override
                public DownloadableFileItem[] newArray(int size)
                {
                    return new DownloadableFileItem[size];
                }
            };

    @Override
    public String toString()
    {
        return "DownloadableFileItem{" +
                super.toString() +
                "selected=" + selected +
                '}';
    }
}
