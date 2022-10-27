

package com.movilix.torrant.core.storage;

import android.net.Uri;

import com.movilix.torrant.core.exception.UnknownUriException;
import com.movilix.torrant.core.model.data.entity.FeedChannel;
import com.movilix.torrant.core.model.data.entity.FeedItem;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface FeedRepository
{
    String getSerializeFileFormat();

    String getSerializeMimeType();

    String getFilterSeparator();

    long addFeed(@NonNull FeedChannel channel);

    long[] addFeeds(@NonNull List<FeedChannel> feeds);

    int updateFeed(@NonNull FeedChannel channel);

    void deleteFeed(@NonNull FeedChannel channel);

    void deleteFeeds(@NonNull List<FeedChannel> feeds);

    FeedChannel getFeedById(long id);

    Single<FeedChannel> getFeedByIdSingle(long id);

    Flowable<List<FeedChannel>> observeAllFeeds();

    List<FeedChannel> getAllFeeds();

    Single<List<FeedChannel>> getAllFeedsSingle();

    void serializeAllFeeds(@NonNull Uri file) throws IOException, UnknownUriException;

    List<FeedChannel> deserializeFeeds(@NonNull Uri file) throws IOException, UnknownUriException;

    void addItems(@NonNull List<FeedItem> items);

    void deleteItemsOlderThan(long keepDateBorderTime);

    void markAsRead(@NonNull String itemId);

    void markAsUnread(@NonNull String itemId);

    void markAsReadByFeedId(List<Long> feedId);

    Flowable<List<FeedItem>> observeItemsByFeedId(long feedId);

    Single<List<FeedItem>> getItemsByFeedIdSingle(long feedId);

    List<String> getItemsIdByFeedId(long feedId);

    List<String> findItemsExistingTitles(@NonNull List<String> titles);

    List<FeedItem> getItemsById(@NonNull String... itemsId);
}
