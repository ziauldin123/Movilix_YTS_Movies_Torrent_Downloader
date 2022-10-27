

package com.movilix.torrant.core.model.data;

import android.os.Parcel;

import org.libtorrent4j.AnnounceEndpoint;
import org.libtorrent4j.AnnounceEntry;
import org.libtorrent4j.AnnounceInfohash;

import java.util.List;

import androidx.annotation.NonNull;

/*
 * The class provides a package model with information
 * about the state of the bittorrent tracker, sent from the service.
 */

public class TrackerInfo extends com.movilix.torrant.core.model.data.AbstractInfoParcel
{
    public String url;
    public String message;
    public int tier;
    public int status;

    public class Status
    {
        public static final int UNKNOWN = -1;
        public static final int WORKING = 0;
        public static final int UPDATING = 1;
        public static final int NOT_CONTACTED = 2;
        public static final int NOT_WORKING = 3;
    }

    public TrackerInfo(AnnounceEntry entry)
    {
        super(entry.url());

        url = entry.url();
        tier = entry.tier();

        List<AnnounceEndpoint> endpoints = entry.endpoints();
        if (endpoints.size() == 0) {
            status = Status.NOT_WORKING;
            message = "";
        } else {
            AnnounceInfohash bestEndpoint = getBestEndpoint(endpoints);
            message = bestEndpoint.message();
            status = makeStatus(entry, bestEndpoint);
        }
    }

    public TrackerInfo(String url, String message, int tier, int status)
    {
        super(url);

        this.url = url;
        this.message = message;
        this.tier = tier;
        this.status = status;
    }

    private int makeStatus(AnnounceEntry entry, AnnounceInfohash infoHash)
    {
        if (entry == null)
            return Status.UNKNOWN;

        if (entry.isVerified() && infoHash.isWorking())
            return Status.WORKING;
        else if ((infoHash.fails() == 0) && infoHash.updating())
            return Status.UPDATING;
        else if (infoHash.fails() == 0)
            return Status.NOT_CONTACTED;
        else
            return Status.NOT_WORKING;
    }

    private AnnounceInfohash getBestEndpoint(List<AnnounceEndpoint> endpoints)
    {
        if (endpoints.size() == 1)
            return endpoints.get(0).infohashV1();

        AnnounceInfohash bestEndpoint = endpoints.get(0).infohashV1();
        for (int i = 0; i < endpoints.size(); i++)
            if (endpoints.get(i).infohashV1().fails() < bestEndpoint.fails())
                bestEndpoint = endpoints.get(i).infohashV1();

        return bestEndpoint;
    }

    public TrackerInfo(Parcel source)
    {
        super(source);

        url = source.readString();
        message = source.readString();
        tier = source.readInt();
        status = source.readInt();
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

        dest.writeString(url);
        dest.writeString(message);
        dest.writeInt(tier);
        dest.writeInt(status);
    }

    public static final Creator<TrackerInfo> CREATOR =
            new Creator<TrackerInfo>()
            {
                @Override
                public TrackerInfo createFromParcel(Parcel source)
                {
                    return new TrackerInfo(source);
                }

                @Override
                public TrackerInfo[] newArray(int size)
                {
                    return new TrackerInfo[size];
                }
            };


    @Override
    public int compareTo(@NonNull Object another)
    {
        return url.compareTo(((TrackerInfo)another).url);
    }

    @Override
    public int hashCode()
    {
        int prime = 31, result = 1;

        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + tier;
        result = prime * result + status;

        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof TrackerInfo))
            return false;

        if (o == this)
            return true;

        TrackerInfo state = (TrackerInfo) o;

        return (url == null || url.equals(state.url)) &&
                (message == null || message.equals(state.message)) &&
                tier == state.tier &&
                status == state.status;
    }

    @Override
    public String toString()
    {
        String status;

        switch (this.status) {
            case Status.NOT_CONTACTED:
                status = "NOT_CONTACTED";
                break;
            case Status.WORKING:
                status = "WORKING";
                break;
            case Status.UPDATING:
                status = "UPDATING";
                break;
            case Status.NOT_WORKING:
                status = "NOT_WORKING";
                break;
            default:
                status = "UNKNOWN";
        }

        return "TrackerInfo{" +
                "url='" + url + '\'' +
                ", message='" + message + '\'' +
                ", tier=" + tier +
                ", status=" + status +
                '}';
    }
}
