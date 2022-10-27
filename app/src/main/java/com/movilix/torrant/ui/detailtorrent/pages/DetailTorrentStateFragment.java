

package com.movilix.torrant.ui.detailtorrent.pages;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.movilix.R;
import com.movilix.databinding.FragmentDetailTorrentStateBinding;
import com.movilix.torrant.ui.detailtorrent.DetailTorrentViewModel;

/*
 * The fragment for displaying torrent state. Part of DetailTorrentFragment.
 */

public class DetailTorrentStateFragment extends Fragment
{
    private static final String TAG = DetailTorrentStateFragment.class.getSimpleName();

    private AppCompatActivity activity;
    private DetailTorrentViewModel viewModel;
    private FragmentDetailTorrentStateBinding binding;

    public static DetailTorrentStateFragment newInstance()
    {
        DetailTorrentStateFragment fragment = new DetailTorrentStateFragment();

        Bundle b = new Bundle();
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        if (context instanceof AppCompatActivity)
            activity = (AppCompatActivity)context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_torrent_state, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (activity == null)
            activity = (AppCompatActivity)getActivity();

        viewModel = new ViewModelProvider(activity).get(DetailTorrentViewModel.class);
        binding.setViewModel(viewModel);
    }
}