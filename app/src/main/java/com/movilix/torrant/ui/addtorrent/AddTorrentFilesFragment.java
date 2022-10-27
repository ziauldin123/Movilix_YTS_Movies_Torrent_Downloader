

package com.movilix.torrant.ui.addtorrent;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.movilix.R;
import com.movilix.torrant.core.model.filetree.BencodeFileTree;
import com.movilix.databinding.FragmentAddTorrentFilesBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/*
 * The fragment for list files of torrent. Part of AddTorrentFragment.
 */

public class AddTorrentFilesFragment extends Fragment
        implements
        com.movilix.torrant.ui.addtorrent.DownloadableFilesAdapter.ClickListener
{
    private static final String TAG = AddTorrentFilesFragment.class.getSimpleName();

    private static final String TAG_LIST_FILES_STATE = "list_files_state";

    private AppCompatActivity activity;
    private FragmentAddTorrentFilesBinding binding;
    private com.movilix.torrant.ui.addtorrent.AddTorrentViewModel viewModel;
    private LinearLayoutManager layoutManager;
    private com.movilix.torrant.ui.addtorrent.DownloadableFilesAdapter adapter;
    /* Save state scrolling */
    private Parcelable listFilesState;
    private CompositeDisposable disposable = new CompositeDisposable();

    public static AddTorrentFilesFragment newInstance()
    {
        AddTorrentFilesFragment fragment = new AddTorrentFilesFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_torrent_files, container, false);

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        if (context instanceof AppCompatActivity)
            activity = (AppCompatActivity)context;
    }

    @Override
    public void onStop()
    {
        super.onStop();

        disposable.clear();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        subscribeAdapter();
    }

    private void subscribeAdapter()
    {
        disposable.add(viewModel.children
                .subscribeOn(Schedulers.computation())
                .flatMapSingle((children) ->
                        Flowable.fromIterable(children)
                                .map(com.movilix.torrant.ui.addtorrent.DownloadableFileItem::new)
                                .toList()
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((children) -> {
                    adapter.submitList(children);
                    updateFileSize();
                }));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (activity == null)
            activity = (AppCompatActivity) getActivity();

        viewModel = new ViewModelProvider(activity).get(com.movilix.torrant.ui.addtorrent.AddTorrentViewModel.class);
        binding.setViewModel(viewModel);

        layoutManager = new LinearLayoutManager(activity);
        binding.fileList.setLayoutManager(layoutManager);
        adapter = new com.movilix.torrant.ui.addtorrent.DownloadableFilesAdapter(this);
        binding.fileList.setAdapter(adapter);
    }

    private void updateFileSize()
    {
        if (viewModel.fileTree == null)
            return;

        binding.filesSize.setText(getString(R.string.files_size,
                Formatter.formatFileSize(activity.getApplicationContext(),
                        viewModel.fileTree.selectedFileSize()),
                Formatter.formatFileSize(activity.getApplicationContext(),
                        viewModel.fileTree.size())));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        listFilesState = layoutManager.onSaveInstanceState();
        outState.putParcelable(TAG_LIST_FILES_STATE, listFilesState);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState)
    {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null)
            listFilesState = savedInstanceState.getParcelable(TAG_LIST_FILES_STATE);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (listFilesState != null)
            layoutManager.onRestoreInstanceState(listFilesState);
    }

    @Override
    public void onItemClicked(@NonNull com.movilix.torrant.ui.addtorrent.DownloadableFileItem item)
    {
        if (item.name.equals(BencodeFileTree.PARENT_DIR))
            viewModel.upToParentDirectory();
        else if (!item.isFile)
            viewModel.chooseDirectory(item.name);
    }

    @Override
    public void onItemCheckedChanged(@NonNull com.movilix.torrant.ui.addtorrent.DownloadableFileItem item, boolean selected)
    {
        viewModel.selectFile(item.name, selected);
        updateFileSize();
    }
}
