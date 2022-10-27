

package com.movilix.torrant.ui.addtorrent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class AddTorrentPagerAdapter extends FragmentStateAdapter {
    @ViewPager2.OffscreenPageLimit
    public static final int NUM_FRAGMENTS = 2;

    public static final int INFO_FRAG_POS = 0;
    public static final int FILES_FRAG_POS = 1;

    public AddTorrentPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case INFO_FRAG_POS:
                return com.movilix.torrant.ui.addtorrent.AddTorrentInfoFragment.newInstance();
            case FILES_FRAG_POS:
                return AddTorrentFilesFragment.newInstance();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_FRAGMENTS;
    }
}
