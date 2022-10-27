

package com.movilix.torrant.core.model;

import com.movilix.torrant.core.model.data.TorrentStateCode;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposables;

/*
 * Emits an event if all torrents have switched from the download state to
 * the finish state (excluding already seeding torrents).
 */

class DownloadsCompletedListener
{
    private com.movilix.torrant.core.model.TorrentEngine engine;

    DownloadsCompletedListener(@NonNull com.movilix.torrant.core.model.TorrentEngine engine)
    {
        this.engine = engine;
    }

    public Completable listen()
    {
        return Completable.create((emitter) -> {
            ArrayList<String> torrentsInProgress = new ArrayList<>();

            Runnable eventHandler = () -> {
                if (!emitter.isDisposed() && torrentsInProgress.isEmpty())
                    emitter.onComplete();
            };

            com.movilix.torrant.core.model.TorrentEngineListener listener = new com.movilix.torrant.core.model.TorrentEngineListener() {
                @Override
                public void onTorrentStateChanged(@NonNull String id,
                                                  @NonNull TorrentStateCode prevState,
                                                  @NonNull TorrentStateCode curState)
                {
                    if (curState == TorrentStateCode.DOWNLOADING)
                        torrentsInProgress.add(id);
                }

                @Override
                public void onTorrentFinished(@NonNull String id)
                {
                    if (torrentsInProgress.remove(id))
                        eventHandler.run();
                }

                @Override
                public void onTorrentRemoved(@NonNull String id)
                {
                    if (torrentsInProgress.remove(id))
                        eventHandler.run();
                }
            };

            if (!emitter.isDisposed()) {
                engine.addListener(listener);
                emitter.setDisposable(Disposables.fromAction(() ->
                        engine.removeListener(listener)));
            }
        });
    }
}
