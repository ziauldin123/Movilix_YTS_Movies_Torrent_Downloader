

package com.movilix.torrant.ui.detailtorrent;

import com.movilix.torrant.ui.detailtorrent.pages.DetailTorrentInfoFragment;
import com.movilix.torrant.ui.detailtorrent.pages.DetailTorrentStateFragment;
import com.movilix.torrant.ui.detailtorrent.pages.files.DetailTorrentFilesFragment;
import com.movilix.torrant.ui.detailtorrent.pages.peers.DetailTorrentPeersFragment;
import com.movilix.torrant.ui.detailtorrent.pages.pieces.DetailTorrentPiecesFragment;
import com.movilix.torrant.ui.detailtorrent.pages.tracker.DetailTorrentTrackersFragment;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class DetailPagerAdapter extends FragmentStateAdapter
{
    @ViewPager2.OffscreenPageLimit
    public static final int NUM_FRAGMENTS = 6;

    public static final int INFO_FRAG_POS = 0;
    public static final int STATE_FRAG_POS = 1;
    public static final int FILES_FRAG_POS = 2;
    public static final int TRACKERS_FRAG_POS = 3;
    public static final int PEERS_FRAG_POS = 4;
    public static final int PIECES_FRAG_POS = 5;


    public DetailPagerAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case INFO_FRAG_POS:
                return DetailTorrentInfoFragment.newInstance();
            case STATE_FRAG_POS:
                return DetailTorrentStateFragment.newInstance();
            case FILES_FRAG_POS:
                return DetailTorrentFilesFragment.newInstance();
            case TRACKERS_FRAG_POS:
                return DetailTorrentTrackersFragment.newInstance();
            case PEERS_FRAG_POS:
                return DetailTorrentPeersFragment.newInstance();
            case PIECES_FRAG_POS:
                return DetailTorrentPiecesFragment.newInstance();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_FRAGMENTS;
    }
}
