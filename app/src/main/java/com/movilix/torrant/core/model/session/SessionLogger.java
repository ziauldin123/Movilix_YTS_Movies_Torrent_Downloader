package com.movilix.torrant.core.model.session;

import org.libtorrent4j.alerts.Alert;
import org.libtorrent4j.alerts.DhtLogAlert;
import org.libtorrent4j.alerts.LogAlert;
import org.libtorrent4j.alerts.PeerLogAlert;
import org.libtorrent4j.alerts.PortmapLogAlert;
import org.libtorrent4j.alerts.TorrentLogAlert;
import com.movilix.torrant.core.logger.LogEntry;
import com.movilix.torrant.core.logger.LogFilter;
import com.movilix.torrant.core.logger.Logger;

class SessionLogger extends Logger
{
    private static int nextLogEntryId = 0;

    public enum SessionLogEntryType {
        /*
         * Posts some session events
         */
        SESSION_LOG,

        /*
         * Posts DHT events
         */
        DHT_LOG,

        /*
         * Posts events specific to a peer
         */
        PEER_LOG,

        /*
         * Posts informational events related to either
         * UPnP or NAT-PMP
         */
        PORTMAP_LOG,

        /*
         * Posts torrent events
         */
        TORRENT_LOG,
    }

    public enum SessionLogFilter
    {
        SESSION((entry) -> entry == null || !entry.getTag().equals(SessionLogEntryType.SESSION_LOG.name())),

        DHT((entry) -> entry == null || !entry.getTag().equals(SessionLogEntryType.DHT_LOG.name())),

        PEER((entry) -> entry == null || !entry.getTag().equals(SessionLogEntryType.PEER_LOG.name())),

        PORTMAP((entry) -> entry == null || !entry.getTag().equals(SessionLogEntryType.PORTMAP_LOG.name())),

        TORRENT((entry) -> entry == null || !entry.getTag().equals(SessionLogEntryType.TORRENT_LOG.name()));

        private final NewFilter filter;

        SessionLogFilter(LogFilter filter)
        {
            this.filter = new NewFilter(name(), filter);
        }

        public NewFilter filter()
        {
            return filter;
        }
    }

    public static class SessionFilterParams
    {
        final boolean filterSessionLog;
        final boolean filterDhtLog;
        final boolean filterPeerLog;
        final boolean filterPortmapLog;
        final boolean filterTorrentLog;

        SessionFilterParams(boolean filterSessionLog,
                            boolean filterDhtLog,
                            boolean filterPeerLog,
                            boolean filterPortmapLog,
                            boolean filterTorrentLog)
        {
            this.filterSessionLog = filterSessionLog;
            this.filterDhtLog = filterDhtLog;
            this.filterPeerLog = filterPeerLog;
            this.filterPortmapLog = filterPortmapLog;
            this.filterTorrentLog = filterTorrentLog;
        }
    }

    SessionLogger()
    {
        /* Default stub */
        super(1);
    }

    void send(Alert<?> alert)
    {
        long time = System.currentTimeMillis();
        String msg;
        LogEntry entry = null;
        switch (alert.type()) {
            case LOG:
                entry = new LogEntry(nextLogEntryId++,
                        SessionLogEntryType.SESSION_LOG.name(),
                        ((LogAlert)alert).logMessage(),
                        time);
                break;
            case DHT_LOG:
                DhtLogAlert dhtLogAlert = (DhtLogAlert)alert;
                msg = "[" + dhtLogAlert.module().name() + "] " + dhtLogAlert.logMessage();
                entry = new LogEntry(nextLogEntryId++,
                        SessionLogEntryType.DHT_LOG.name(),
                        msg,
                        time);
                break;
            case PEER_LOG:
                PeerLogAlert peerLogAlert = (PeerLogAlert)alert;

                msg = "[" + peerLogAlert.direction() + "] " +
                        "[" + peerLogAlert.eventType() + "] " +
                        peerLogAlert.logMessage();

                entry = new LogEntry(nextLogEntryId++,
                        SessionLogEntryType.PEER_LOG.name(),
                        msg,
                        time);
                break;
            case PORTMAP_LOG:
                PortmapLogAlert portmapLogAlert = (PortmapLogAlert)alert;
                msg = "[" + portmapLogAlert.mapType().name() + "] " + portmapLogAlert.logMessage();
                entry = new LogEntry(nextLogEntryId++,
                        SessionLogEntryType.PORTMAP_LOG.name(),
                        msg,
                        time);
                break;
            case TORRENT_LOG:
                entry = new LogEntry(nextLogEntryId++,
                        SessionLogEntryType.TORRENT_LOG.name(),
                        ((TorrentLogAlert)alert).logMessage(),
                        time);
                break;
        }

        if (entry != null)
            send(entry);
    }

    void applyFilterParams(SessionFilterParams params)
    {
        Logger.NewFilter[] addFilters = new Logger.NewFilter[5];
        String[] removeFilters = new String[5];

        if (params.filterSessionLog)
            addFilters[0] = SessionLogFilter.SESSION.filter();
        else
            removeFilters[0] = SessionLogFilter.SESSION.name();

        if (params.filterDhtLog)
            addFilters[1] = SessionLogFilter.DHT.filter();
        else
            removeFilters[1] = SessionLogFilter.DHT.name();

        if (params.filterPeerLog)
            addFilters[2] = SessionLogFilter.PEER.filter();
        else
            removeFilters[2] = SessionLogFilter.PEER.name();

        if (params.filterPortmapLog)
            addFilters[3] = SessionLogFilter.PORTMAP.filter();
        else
            removeFilters[3] = SessionLogFilter.PORTMAP.name();

        if (params.filterTorrentLog)
            addFilters[4] = SessionLogFilter.TORRENT.filter();
        else
            removeFilters[4] = SessionLogFilter.TORRENT.name();

        removeFilter(removeFilters);
        addFilter(addFilters);
    }
}
