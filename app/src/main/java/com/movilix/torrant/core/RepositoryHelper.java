

package com.movilix.torrant.core;

import android.content.Context;

import com.movilix.torrant.core.settings.SettingsRepository;
import com.movilix.torrant.core.settings.SettingsRepositoryImpl;
import com.movilix.torrant.core.storage.AppDatabase;
import com.movilix.torrant.core.storage.FeedRepository;
import com.movilix.torrant.core.storage.FeedRepositoryImpl;
import com.movilix.torrant.core.storage.TagRepository;
import com.movilix.torrant.core.storage.TagRepositoryImpl;
import com.movilix.torrant.core.storage.TorrentRepository;
import com.movilix.torrant.core.storage.TorrentRepositoryImpl;

import androidx.annotation.NonNull;

public class RepositoryHelper
{
    private static FeedRepositoryImpl feedRepo;
    private static TorrentRepositoryImpl torrentRepo;
    private static SettingsRepositoryImpl settingsRepo;
    private static TagRepository tagRepo;

    public synchronized static TorrentRepository getTorrentRepository(@NonNull Context appContext)
    {
        if (torrentRepo == null)
            torrentRepo = new TorrentRepositoryImpl(appContext,
                    AppDatabase.getInstance(appContext));

        return torrentRepo;
    }

    public synchronized static FeedRepository getFeedRepository(@NonNull Context appContext)
    {
        if (feedRepo == null)
            feedRepo = new FeedRepositoryImpl(appContext,
                    AppDatabase.getInstance(appContext));

        return feedRepo;
    }

    public synchronized static SettingsRepository getSettingsRepository(@NonNull Context appContext)
    {
        if (settingsRepo == null)
            settingsRepo = new SettingsRepositoryImpl(appContext);

        return settingsRepo;
    }

    public synchronized static TagRepository getTagRepository(@NonNull Context appContext)
    {
        if (tagRepo == null)
            tagRepo = new TagRepositoryImpl(AppDatabase.getInstance(appContext));

        return tagRepo;
    }
}
