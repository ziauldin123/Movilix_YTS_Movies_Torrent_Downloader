

package com.movilix.torrant.core.model.session;

import org.libtorrent4j.PeerInfo;
import org.libtorrent4j.PieceIndexBitfield;
import org.libtorrent4j.swig.peer_info;

/*
 * Extension of org.libtorrent4j.PeerInfo class with additional information
 */

public class AdvancedPeerInfo extends PeerInfo
{
    protected int port;
    protected PieceIndexBitfield pieces;
    protected boolean isUtp;

    public AdvancedPeerInfo(peer_info p)
    {
        super(p);

        port = p.getIp().port();
        pieces = new PieceIndexBitfield(p.get_pieces());
        isUtp = p.getFlags().and_(peer_info.utp_socket).non_zero();
    }

    public int port()
    {
        return port;
    }

    /*
     * A bitfield, with one bit per piece in the torrent. Each bit tells you
     * if the peer has that piece (if it's set to 1) or if the peer miss that
     * piece (set to 0).
     */

    public PieceIndexBitfield pieces()
    {
        return pieces;
    }

    public boolean isUtp()
    {
        return isUtp;
    }
}
