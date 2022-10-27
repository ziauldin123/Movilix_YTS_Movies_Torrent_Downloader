package com.movilix.adapters;

import com.movilix.fragments.home;
import com.movilix.fragments.search;
//import com.movilix.ui.main.MainFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewStatePagerAdapter extends FragmentStateAdapter {

    public ViewStatePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment=null;
        switch (position){
            case 0:
                fragment= new home();

                break;
            case 1:
                fragment= new search();

                break;



        }


        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
