

package com.movilix.torrant.core;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import org.libtorrent4j.Pair;
import com.movilix.torrant.core.exception.UnknownUriException;
import com.movilix.torrant.core.system.SystemFacadeHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

public class TorrentBuilder
{
    private Context context;
    private org.libtorrent4j.TorrentBuilder builder;
    private Predicate<String> fileNameFilter;
    private BehaviorSubject<Progress> progress = BehaviorSubject.create();

    public static class Tracker
    {
        public final String url;
        public final int tier;

        public Tracker(@NonNull String url, int tier)
        {
            this.url = url;
            this.tier = tier;
        }
    }

    public static final class Progress
    {
        public final int piece;
        public final int numPieces;

        public Progress(int piece, int numPieces)
        {
            this.piece = piece;
            this.numPieces = numPieces;
        }
    }

    public TorrentBuilder(@NonNull Context context)
    {
        this.context = context;
        builder = new org.libtorrent4j.TorrentBuilder();
    }

    public TorrentBuilder setSeedPath(Uri path) throws UnknownUriException
    {
        String seedPathStr = SystemFacadeHelper.getFileSystemFacade(context)
                .makeFileSystemPath(path);
        builder.path(new File(seedPathStr));

        return this;
    }

    /*
     * The size of each piece in bytes. It must
     * be a multiple of 16 kiB. If a piece size of 0 is specified, a
     * piece size will be calculated such that the torrent file is roughly 40 kB
     */

    public TorrentBuilder setPieceSize(int size)
    {
        builder.pieceSize(size);

        return this;
    }

    public TorrentBuilder addTrackers(@NonNull List<Tracker> trackers)
    {
        ArrayList<Pair<String, Integer>> list = new ArrayList<>();
        for (Tracker tracker : trackers)
            list.add(new Pair<>(tracker.url, tracker.tier));

        builder.addTrackers(list);

        return this;
    }

    public TorrentBuilder addUrlSeeds(@NonNull List<String> urls)
    {
        builder.addUrlSeeds(urls);

        return this;
    }

    public TorrentBuilder setAsPrivate(boolean isPrivate)
    {
        builder.setPrivate(isPrivate);

        return this;
    }

    public TorrentBuilder setCreator(String creator)
    {
        builder.creator(creator);

        return this;
    }

    public TorrentBuilder setComment(String comment)
    {
        builder.comment(comment);

        return this;
    }

    public TorrentBuilder setFileNameFilter(Predicate<String> fileNameFilter)
    {
        this.fileNameFilter = fileNameFilter;

        return this;
    }

    public Observable<Progress> observeProgress()
    {
        return progress;
    }

    public Single<byte[]> build()
    {
        subscribeProgress();

        return Single.fromCallable(() -> builder.generate().entry().bencode());
    }

    private void subscribeProgress()
    {
        builder.listener(new org.libtorrent4j.TorrentBuilder.Listener() {
            @Override
            public boolean accept(String filename)
            {
                try {
                    return fileNameFilter == null || fileNameFilter.test(filename);

                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public void progress(int pieceIndex, int numPieces)
            {
                progress.onNext(new Progress(pieceIndex, numPieces));
            }
        });
    }
}
