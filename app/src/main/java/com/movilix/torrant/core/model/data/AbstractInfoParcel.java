

package com.movilix.torrant.core.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

/*
 * The class provides an abstract package model, sent from the service.
 */

public abstract class AbstractInfoParcel
        implements Parcelable, Comparable
{
    public String parcelId;

    protected AbstractInfoParcel()
    {
        parcelId = UUID.randomUUID().toString();
    }

    protected AbstractInfoParcel(String parcelId)
    {
        this.parcelId = parcelId;
    }

    protected AbstractInfoParcel(Parcel source)
    {
        parcelId = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(parcelId);
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object o);
}
