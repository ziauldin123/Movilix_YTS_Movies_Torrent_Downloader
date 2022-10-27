

package com.movilix.torrant.core;

import android.os.FileObserver;

import androidx.annotation.Nullable;

/*
 * Watch torrent files in the specified directory and download them.
 */

public abstract class TorrentFileObserver extends FileObserver
{
    private static final int mask = FileObserver.CREATE | FileObserver.MOVED_TO |
                                    FileObserver.MODIFY | FileObserver.ATTRIB;

    public TorrentFileObserver(String pathToDir)
    {
        super(pathToDir, mask);
    }

    public abstract void onEvent(int event, @Nullable String name);
}
