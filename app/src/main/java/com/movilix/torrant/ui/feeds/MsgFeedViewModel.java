

package com.movilix.torrant.ui.feeds;

import java.util.List;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MsgFeedViewModel extends ViewModel
{
    private PublishSubject<Long> feedItemsOpenedEvents = PublishSubject.create();
    private PublishSubject<Boolean> feedItemsClosedEvents = PublishSubject.create();
    private PublishSubject<List<Long>> feedsDeletedEvents = PublishSubject.create();

    public void feedItemsOpened(long feedId)
    {
        feedItemsOpenedEvents.onNext(feedId);
    }

    public Observable<Long> observeFeedItemsOpened()
    {
        return feedItemsOpenedEvents;
    }

    public void feedItemsClosed()
    {
        feedItemsClosedEvents.onNext(true);
    }

    public Observable<Boolean> observeFeedItemsClosed()
    {
        return feedItemsClosedEvents;
    }

    public void feedsDeleted(List<Long> ids)
    {
        feedsDeletedEvents.onNext(ids);
    }

    public Observable<List<Long>> observeFeedsDeleted()
    {
        return feedsDeletedEvents;
    }
}
