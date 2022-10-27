

package com.movilix.torrant.core.storage;

import android.content.Context;

import com.movilix.torrant.core.model.data.entity.FastResume;
import com.movilix.torrant.core.model.data.entity.FeedChannel;
import com.movilix.torrant.core.model.data.entity.FeedItem;
import com.movilix.torrant.core.model.data.entity.TagInfo;
import com.movilix.torrant.core.model.data.entity.Torrent;
import com.movilix.torrant.core.model.data.entity.TorrentTagInfo;
import com.movilix.torrant.core.storage.converter.UriConverter;
import com.movilix.torrant.core.storage.dao.FastResumeDao;
import com.movilix.torrant.core.storage.dao.FeedDao;
import com.movilix.torrant.core.storage.dao.TagInfoDao;
import com.movilix.torrant.core.storage.dao.TorrentDao;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        entities = {
                Torrent.class,
                FastResume.class,
                FeedChannel.class,
                FeedItem.class,
                TagInfo.class,
                TorrentTagInfo.class,
        },
        version = 7
)
@TypeConverters({UriConverter.class})

public abstract class AppDatabase extends RoomDatabase
{
    private static final String DATABASE_NAME = "libretorrent.db";

    private static volatile AppDatabase INSTANCE;

    public abstract TorrentDao torrentDao();

    public abstract FastResumeDao fastResumeDao();

    public abstract FeedDao feedDao();

    public abstract TagInfoDao tagInfoDao();

    public static AppDatabase getInstance(@NonNull Context appContext)
    {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null)
                    INSTANCE = buildDatabase(appContext);
            }
        }

        return INSTANCE;
    }

    private static AppDatabase buildDatabase(Context appContext)
    {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addMigrations(DatabaseMigration.getMigrations(appContext))
                .build();
    }
}