

package com.movilix.torrant.core.storage.dao;

import com.movilix.torrant.core.model.data.entity.FeedChannel;
import com.movilix.torrant.core.model.data.entity.FeedItem;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface FeedDao
{
    String QUERY_GET_ALL_FEEDS = "SELECT * FROM FeedChannel";
    String QUERY_GET_FEED_BY_ID = "SELECT * FROM FeedChannel WHERE id = :id";
    String QUERY_DELETE_ITEMS_OLDER_THAN = "DELETE FROM FeedItem WHERE fetchDate < :keepDateBorderTime";
    String QUERY_MARK_AS_READ = "UPDATE FeedItem SET read = 1 WHERE id = :itemId";
    String QUERY_MARK_AS_UNREAD = "UPDATE FeedItem SET read = 0 WHERE id = :itemId";
    String QUERY_MARK_AS_READ_BY_FEED_ID = "UPDATE FeedItem SET read = 1 WHERE feedId IN (:feedId)";
    String QUERY_GET_ITEMS_BY_FEED_ID = "SELECT * FROM FeedItem WHERE feedId = :feedId";
    String QUERY_GET_ITEMS_ID_BY_FEED_ID = "SELECT id FROM FeedItem WHERE feedId = :feedId";
    String QUERY_FIND_ITEMS_EXISTING_TITLES = "SELECT title FROM FeedItem WHERE title IN (:titles)";
    String QUERY_GET_ITEMS_BY_ID = "SELECT * FROM FeedItem WHERE id IN (:itemsId)";

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addFeed(FeedChannel channel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] addFeeds(List<FeedChannel> feeds);

    @Update
    int updateFeed(FeedChannel channel);

    @Delete
    void deleteFeed(FeedChannel channel);

    @Delete
    void deleteFeeds(List<FeedChannel> feeds);

    @Query(QUERY_GET_FEED_BY_ID)
    FeedChannel getFeedById(long id);

    @Query(QUERY_GET_FEED_BY_ID)
    Single<FeedChannel> getFeedByIdSingle(long id);

    @Query(QUERY_GET_ALL_FEEDS)
    Flowable<List<FeedChannel>> observeAllFeeds();

    @Query(QUERY_GET_ALL_FEEDS)
    List<FeedChannel> getAllFeeds();

    @Query(QUERY_GET_ALL_FEEDS)
    Single<List<FeedChannel>> getAllFeedsSingle();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addItems(List<FeedItem> items);

    @Query(QUERY_DELETE_ITEMS_OLDER_THAN)
    void deleteItemsOlderThan(long keepDateBorderTime);

    @Query(QUERY_MARK_AS_READ)
    void markAsRead(String itemId);

    @Query(QUERY_MARK_AS_UNREAD)
    void markAsUnread(String itemId);

    @Query(QUERY_MARK_AS_READ_BY_FEED_ID)
    void markAsReadByFeedId(List<Long> feedId);

    @Query(QUERY_GET_ITEMS_BY_FEED_ID)
    Flowable<List<FeedItem>> observeItemsByFeedId(long feedId);

    @Query(QUERY_GET_ITEMS_BY_FEED_ID)
    Single<List<FeedItem>> getItemsByFeedIdSingle(long feedId);

    @Query(QUERY_GET_ITEMS_ID_BY_FEED_ID)
    List<String> getItemsIdByFeedId(long feedId);

    @Query(QUERY_FIND_ITEMS_EXISTING_TITLES)
    List<String> findItemsExistingTitles(List<String> titles);

    @Query(QUERY_GET_ITEMS_BY_ID)
    List<FeedItem> getItemsById(String... itemsId);
}
