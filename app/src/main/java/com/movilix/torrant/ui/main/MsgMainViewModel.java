

package com.movilix.torrant.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MsgMainViewModel extends ViewModel
{
    private PublishSubject<String> torrentDetailsOpenedEvents = PublishSubject.create();
    private PublishSubject<Boolean> torrentDetailsClosedEvents = PublishSubject.create();

    public void torrentDetailsOpened(@NonNull String id)
    {
        torrentDetailsOpenedEvents.onNext(id);
    }

    public Observable<String> observeTorrentDetailsOpened()
    {
        return torrentDetailsOpenedEvents;
    }

    public void torrentDetailsClosed()
    {
        torrentDetailsClosedEvents.onNext(true);
    }

    public Observable<Boolean> observeTorrentDetailsClosed()
    {
        return torrentDetailsClosedEvents;
    }
}
