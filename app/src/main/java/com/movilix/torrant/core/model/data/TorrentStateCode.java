

package com.movilix.torrant.core.model.data;

/*
 * The different overall states a torrent can be in.
 */

public enum TorrentStateCode
{
    UNKNOWN(-1),
    ERROR(0),
    /*
     * In this state the torrent has finished downloading and
     * is a pure seeder.
     */
    SEEDING(1),
    /*
     * The torrent is being downloaded. This is the state
     * most torrents will be in most of the time. The progress
     * meter will tell how much of the files that has been
     * downloaded.
     */
    DOWNLOADING(2),
    PAUSED(3),
    STOPPED(4),
    /*
     * The torrent has not started its download yet, and is
     * currently checking existing files.
     */
    CHECKING(5),
    /*
     * The torrent is trying to download metadata from peers.
     * This assumes the metadata_transfer extension is in use.
     */
    DOWNLOADING_METADATA(6),
    /*
     * In this state the torrent has finished downloading but
     * still doesn't have the entire torrent. i.e. some pieces
     * are filtered and won't get downloaded.
     */
    FINISHED(7);

    private final int value;

    TorrentStateCode(int value)
    {
        this.value = value;
    }

    public static TorrentStateCode fromValue(int value)
    {
        for (TorrentStateCode ev : TorrentStateCode.class.getEnumConstants())
            if (ev.value() == value)
                return ev;

        return UNKNOWN;
    }

    public int value()
    {
        return value;
    }
}
