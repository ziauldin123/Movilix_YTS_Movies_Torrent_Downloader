

package com.movilix.torrant.core.model.data;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class SessionStats extends com.movilix.torrant.core.model.data.AbstractInfoParcel
{
    public long dhtNodes;
    public long totalDownload;
    public long totalUpload;
    public long downloadSpeed;
    public long uploadSpeed;
    public int listenPort;

    public SessionStats(long dhtNodes, long totalDownload,
                        long totalUpload, long downloadSpeed,
                        long uploadSpeed, int listenPort)
    {
        this.dhtNodes = dhtNodes;
        this.totalDownload = totalDownload;
        this.totalUpload = totalUpload;
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
        this.listenPort = listenPort;
    }

    public SessionStats(Parcel source)
    {
        super(source);

        dhtNodes = source.readLong();
        totalDownload = source.readLong();
        totalUpload = source.readLong();
        downloadSpeed = source.readLong();
        uploadSpeed = source.readLong();
        listenPort = source.readInt();
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

        dest.writeLong(dhtNodes);
        dest.writeLong(totalDownload);
        dest.writeLong(totalUpload);
        dest.writeLong(downloadSpeed);
        dest.writeLong(uploadSpeed);
        dest.writeInt(listenPort);
    }

    public static final Creator<SessionStats> CREATOR =
            new Creator<SessionStats>()
            {
                @Override
                public SessionStats createFromParcel(Parcel source)
                {
                    return new SessionStats(source);
                }

                @Override
                public SessionStats[] newArray(int size)
                {
                    return new SessionStats[size];
                }
            };

    @Override
    public int hashCode()
    {
        int prime = 31, result = 1;

        result = prime * result + (int) (dhtNodes ^ (dhtNodes >>> 32));
        result = prime * result + (int) (totalDownload ^ (totalDownload >>> 32));
        result = prime * result + (int) (totalUpload ^ (totalUpload >>> 32));
        result = prime * result + (int) (downloadSpeed ^ (downloadSpeed >>> 32));
        result = prime * result + (int) (uploadSpeed ^ (uploadSpeed >>> 32));
        result = prime * result + listenPort;

        return result;
    }

    @Override
    public int compareTo(@NonNull Object o)
    {
        return parcelId.compareTo(((SessionStats)o).parcelId);
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof SessionStats))
            return false;

        if (o == this)
            return true;

        SessionStats stats = (SessionStats)o;

        return dhtNodes == stats.dhtNodes &&
                totalDownload == stats.totalDownload &&
                totalUpload == stats.totalUpload &&
                downloadSpeed == stats.downloadSpeed &&
                uploadSpeed == stats.uploadSpeed &&
                listenPort == stats.listenPort;
    }

    @Override
    public String toString()
    {
        return "SessionStats{" +
                "dhtNodes=" + dhtNodes +
                ", totalDownload=" + totalDownload +
                ", totalUpload=" + totalUpload +
                ", downloadSpeed=" + downloadSpeed +
                ", uploadSpeed=" + uploadSpeed +
                ", listenPort=" + listenPort +
                '}';
    }
}
