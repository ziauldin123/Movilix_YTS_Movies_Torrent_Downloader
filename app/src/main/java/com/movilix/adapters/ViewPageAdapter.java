package com.movilix.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.movilix.fragments.home;
import com.movilix.fragments.search;


public class ViewPageAdapter extends FragmentPagerAdapter {

    private Context mContext;
    public ViewPageAdapter(Context context,@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mContext=context;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0:
                fragment= new home(mContext);

                break;
            case 1:
                fragment= new search();

                break;
            case 2:
//                fragment= new MainFragment();
                break;


        }


        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        switch (position){
            case 0:

                title="Stattictics";
                break;
            case 1:

                title="Map";

                break;
            case 2:

                title="History";

                break;

        }

        return title;
    }
}