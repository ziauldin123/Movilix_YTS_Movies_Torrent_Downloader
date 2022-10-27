

package com.movilix.torrant.core.storage;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.movilix.torrant.core.exception.UnknownUriException;
import com.movilix.torrant.core.model.data.entity.FeedChannel;
import com.movilix.torrant.core.model.data.entity.FeedItem;
import com.movilix.torrant.core.system.FileDescriptorWrapper;
import com.movilix.torrant.core.system.FileSystemFacade;
import com.movilix.torrant.core.system.SystemFacadeHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class FeedRepositoryImpl implements com.movilix.torrant.core.storage.FeedRepository
{
    private static final String TAG = FeedRepositoryImpl.class.getSimpleName();

    public static final String SERIALIZE_FILE_FORMAT = "json";
    public static final String SERIALIZE_MIME_TYPE = "application/json";
    public static final String FILTER_SEPARATOR = "\\|";

    private Context appContext;
    private com.movilix.torrant.core.storage.AppDatabase db;

    public FeedRepositoryImpl(@NonNull Context appContext, @NonNull com.movilix.torrant.core.storage.AppDatabase db)
    {
        this.appContext = appContext;
        this.db = db;
    }

    @Override
    public String getSerializeFileFormat()
    {
        return SERIALIZE_FILE_FORMAT;
    }

    @Override
    public String getSerializeMimeType()
    {
        return SERIALIZE_MIME_TYPE;
    }

    @Override
    public String getFilterSeparator()
    {
        return FILTER_SEPARATOR;
    }

    @Override
    public long addFeed(@NonNull FeedChannel channel)
    {
        return db.feedDao().addFeed(channel);
    }

    @Override
    public long[] addFeeds(@NonNull List<FeedChannel> feeds)
    {
        return db.feedDao().addFeeds(feeds);
    }

    @Override
    public int updateFeed(@NonNull FeedChannel channel)
    {
        return db.feedDao().updateFeed(channel);
    }

    @Override
    public void deleteFeed(@NonNull FeedChannel channel)
    {
        db.feedDao().deleteFeed(channel);
    }

    @Override
    public void deleteFeeds(@NonNull List<FeedChannel> feeds)
    {
        db.feedDao().deleteFeeds(feeds);
    }

    @Override
    public FeedChannel getFeedById(long id)
    {
        return db.feedDao().getFeedById(id);
    }

    @Override
    public Single<FeedChannel> getFeedByIdSingle(long id)
    {
        return db.feedDao().getFeedByIdSingle(id);
    }

    @Override
    public Flowable<List<FeedChannel>> observeAllFeeds()
    {
        return db.feedDao().observeAllFeeds();
    }

    @Override
    public List<FeedChannel> getAllFeeds()
    {
        return db.feedDao().getAllFeeds();
    }

    @Override
    public Single<List<FeedChannel>> getAllFeedsSingle()
    {
        return db.feedDao().getAllFeedsSingle();
    }

    @Override
    public void serializeAllFeeds(@NonNull Uri file) throws IOException, UnknownUriException
    {
        SystemFacadeHelper.getFileSystemFacade(appContext)
                .write(new Gson().toJson(getAllFeeds()), Charset.forName("UTF-8"), file);
    }

    @Override
    public List<FeedChannel> deserializeFeeds(@NonNull Uri file) throws IOException, UnknownUriException
    {
        List<FeedChannel> feeds;
        FileSystemFacade fs = SystemFacadeHelper.getFileSystemFacade(appContext);
        try (FileDescriptorWrapper w = fs.getFD(file);
             FileInputStream fin = new FileInputStream(w.open("r"));
             InputStreamReader reader = new InputStreamReader(fin, Charset.forName("UTF-8")))
        {
            feeds = new Gson()
                    .fromJson(reader, new TypeToken<ArrayList<FeedChannel>>(){}
                            .getType());
        }

        return feeds;
    }

    @Override
    public void addItems(@NonNull List<FeedItem> items)
    {
        db.feedDao().addItems(items);
    }

    @Override
    public void deleteItemsOlderThan(long keepDateBorderTime)
    {
        db.feedDao().deleteItemsOlderThan(keepDateBorderTime);
    }

    @Override
    public void markAsRead(@NonNull String itemId)
    {
        db.feedDao().markAsRead(itemId);
    }

    @Override
    public void markAsUnread(@NonNull String itemId)
    {
        db.feedDao().markAsUnread(itemId);
    }

    @Override
    public void markAsReadByFeedId(List<Long> feedId)
    {
        db.feedDao().markAsReadByFeedId(feedId);
    }

    @Override
    public Flowable<List<FeedItem>> observeItemsByFeedId(long feedId)
    {
        return db.feedDao().observeItemsByFeedId(feedId);
    }

    @Override
    public Single<List<FeedItem>> getItemsByFeedIdSingle(long feedId)
    {
        return db.feedDao().getItemsByFeedIdSingle(feedId);
    }

    @Override
    public List<String> getItemsIdByFeedId(long feedId)
    {
        return db.feedDao().getItemsIdByFeedId(feedId);
    }

    @Override
    public List<String> findItemsExistingTitles(@NonNull List<String> titles)
    {
        return db.feedDao().findItemsExistingTitles(titles);
    }

    @Override
    public List<FeedItem> getItemsById(@NonNull String... itemsId)
    {
        return db.feedDao().getItemsById(itemsId);
    }
}
