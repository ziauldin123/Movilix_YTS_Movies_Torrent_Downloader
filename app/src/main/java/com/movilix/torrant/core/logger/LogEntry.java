

package com.movilix.torrant.core.logger;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;

public class LogEntry
{
    private static final String defaultTimeStampFormatter = "yyyy-MM-dd HH:mm:ss.SSS";

    private int id;
    @NonNull
    private String tag;
    @NonNull
    private String msg;
    private long timeStamp;
    private SimpleDateFormat timeStampFormatter;

    public LogEntry(int id, @NonNull String tag,@NonNull String msg, long timeStamp)
    {
        this.id = id;
        this.tag = tag;
        this.msg = msg;
        this.timeStamp = timeStamp;
        this.timeStampFormatter = new SimpleDateFormat(defaultTimeStampFormatter,
                Locale.getDefault());
    }

    public int getId()
    {
        return id;
    }

    @NonNull
    public String getTag()
    {
        return tag;
    }

    @NonNull
    public String getMsg()
    {
        return msg;
    }

    public long getTimeStamp()
    {
        return timeStamp;
    }

    public String getTimeStampAsString()
    {
        return timeStampFormatter.format(this.timeStamp);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        LogEntry entry = (LogEntry)o;

        return id == entry.id &&
                timeStamp == entry.timeStamp &&
                tag.equals(entry.tag) &&
                msg.equals(entry.msg);
    }

    @Override
    public int hashCode()
    {
        int result = id;
        result = 31 * result + tag.hashCode();
        result = 31 * result + msg.hashCode();
        result = 31 * result + (int) (timeStamp ^ (timeStamp >>> 32));

        return result;
    }

    public String toStringWithTimeStamp()
    {
        return getTimeStampAsString() + " " + toString();
    }

    @Override
    public String toString()
    {
        return "[" + tag + "] " + msg;
    }
}
