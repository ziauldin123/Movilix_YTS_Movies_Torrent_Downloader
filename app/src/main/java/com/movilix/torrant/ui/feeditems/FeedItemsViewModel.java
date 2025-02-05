

package com.movilix.torrant.ui.feeditems;

import android.app.Application;

import com.movilix.torrant.core.RepositoryHelper;
import com.movilix.torrant.core.model.data.entity.FeedItem;
import com.movilix.torrant.core.storage.FeedRepository;
import com.movilix.torrant.service.FeedFetcherWorker;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class FeedItemsViewModel extends AndroidViewModel
{
    private FeedRepository repo;
    private long feedId;
    private BehaviorSubject<Boolean> refreshStatus = BehaviorSubject.create();
    private CompositeDisposable disposables = new CompositeDisposable();

    public FeedItemsViewModel(@NonNull Application application)
    {
        super(application);

        repo = RepositoryHelper.getFeedRepository(application);
        feedId = -1;
    }

    @Override
    protected void onCleared()
    {
        super.onCleared();

        disposables.clear();
    }

    public void clearData()
    {
        disposables.clear();
        feedId = -1;
    }

    public void setFeedId(long feedId)
    {
        this.feedId = feedId;
    }

    public Flowable<List<FeedItem>> observeItemsByFeedId()
    {
        return repo.observeItemsByFeedId(feedId);
    }

    public Single<List<FeedItem>> getItemsByFeedIdSingle()
    {
        return repo.getItemsByFeedIdSingle(feedId);
    }

    public Observable<Boolean> observeRefreshStatus()
    {
        return refreshStatus;
    }

    public void refreshChannel()
    {
        if (feedId == -1)
            return;

        Data data = new Data.Builder()
                .putString(FeedFetcherWorker.TAG_ACTION, FeedFetcherWorker.ACTION_FETCH_CHANNEL)
                .putLong(FeedFetcherWorker.TAG_CHANNEL_ID, feedId)
                .build();
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(FeedFetcherWorker.class)
                .setInputData(data)
                .build();

        runFetchWorker(work);
    }

    public void markAllAsRead()
    {
        if (feedId == -1)
            return;

        disposables.add(Completable.fromRunnable(() -> {
            repo.markAsReadByFeedId(Collections.singletonList(feedId));
        }).subscribeOn(Schedulers.io())
          .subscribe());
    }

    public void markAsRead(@NonNull String itemId)
    {
        disposables.add(Completable.fromRunnable(() -> repo.markAsRead(itemId))
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    public void markAsUnread(@NonNull String itemId)
    {
        disposables.add(Completable.fromRunnable(() -> repo.markAsUnread(itemId))
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    private void runFetchWorker(WorkRequest work)
    {
        refreshStatus.onNext(true);

        WorkManager.getInstance(getApplication()).enqueue(work);

        WorkManager.getInstance(getApplication()).getWorkInfoByIdLiveData(work.getId())
                .observeForever(this::observeWorkResult);
    }

    private void observeWorkResult(WorkInfo info)
    {
        boolean finished = info.getState().isFinished();
        if (finished)
            WorkManager.getInstance(getApplication()).getWorkInfoByIdLiveData(info.getId())
                    .removeObserver(this::observeWorkResult);

        refreshStatus.onNext(!finished);
    }
}
