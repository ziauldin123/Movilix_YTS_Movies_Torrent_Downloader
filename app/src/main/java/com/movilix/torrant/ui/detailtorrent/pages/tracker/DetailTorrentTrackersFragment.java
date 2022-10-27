

package com.movilix.torrant.ui.detailtorrent.pages.tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.movilix.R;
import com.movilix.torrant.core.utils.Utils;
import com.movilix.databinding.FragmentDetailTorrentTrackerListBinding;
import com.movilix.torrant.ui.BaseAlertDialog;
import com.movilix.torrant.ui.customviews.RecyclerViewDividerDecoration;
import com.movilix.torrant.ui.detailtorrent.DetailTorrentViewModel;
import com.movilix.torrant.ui.detailtorrent.MsgDetailTorrentViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.MutableSelection;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/*
 * The fragment for displaying bittorrent trackers list. Part of DetailTorrentFragment.
 */

public class DetailTorrentTrackersFragment extends Fragment
{
    private static final String TAG = DetailTorrentTrackersFragment.class.getSimpleName();

    private static final String SELECTION_TRACKER_ID = "selection_tracker_0";
    private static final String TAG_LIST_TRACKER_STATE = "list_tracker_state";
    private static final String TAG_DELETE_TRACKERS_DIALOG = "delete_trackers_dialog";

    private AppCompatActivity activity;
    private FragmentDetailTorrentTrackerListBinding binding;
    private DetailTorrentViewModel viewModel;
    private MsgDetailTorrentViewModel msgViewModel;
    private LinearLayoutManager layoutManager;
    private SelectionTracker<TrackerItem> selectionTracker;
    private ActionMode actionMode;
    private TrackerListAdapter adapter;
    /* Save state scrolling */
    private Parcelable listTrackerState;
    private CompositeDisposable disposables = new CompositeDisposable();
    private BaseAlertDialog deleteTrackersDialog;
    private BaseAlertDialog.SharedViewModel dialogViewModel;

    public static DetailTorrentTrackersFragment newInstance()
    {
        DetailTorrentTrackersFragment fragment = new DetailTorrentTrackersFragment();

        fragment.setArguments(new Bundle());

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_torrent_tracker_list, container, false);

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
    public void onDetach()
    {
        super.onDetach();

        if (actionMode != null)
            actionMode.finish();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        disposables.clear();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        subscribeAdapter();
        subscribeAlertDialog();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (activity == null)
            activity = (AppCompatActivity) getActivity();

        ViewModelProvider provider = new ViewModelProvider(activity);
        viewModel = provider.get(DetailTorrentViewModel.class);
        msgViewModel = provider.get(MsgDetailTorrentViewModel.class);
        dialogViewModel = provider.get(BaseAlertDialog.SharedViewModel.class);

        layoutManager = new LinearLayoutManager(activity);
        binding.trackerList.setLayoutManager(layoutManager);
        binding.trackerList.setEmptyView(binding.emptyViewTrackerList);
        adapter = new TrackerListAdapter();
        /*
         * A RecyclerView by default creates another copy of the ViewHolder in order to
         * fade the views into each other. This causes the problem because the old ViewHolder gets
         * the payload but then the new one doesn't. So needs to explicitly tell it to reuse the old one.
         */
        DefaultItemAnimator animator = new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        };
        binding.trackerList.setItemAnimator(animator);
        TypedArray a = activity.obtainStyledAttributes(new TypedValue().data, new int[]{R.attr.divider});
        binding.trackerList.addItemDecoration(new RecyclerViewDividerDecoration(a.getDrawable(0)));
        a.recycle();
        binding.trackerList.setAdapter(adapter);

        selectionTracker = new SelectionTracker.Builder<>(
                SELECTION_TRACKER_ID,
                binding.trackerList,
                new TrackerListAdapter.KeyProvider(adapter),
                new TrackerListAdapter.ItemLookup(binding.trackerList),
                StorageStrategy.createParcelableStorage(TrackerItem.class))
                .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build();

        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<TrackerItem>() {
            @Override
            public void onSelectionChanged()
            {
                super.onSelectionChanged();

                if (selectionTracker.hasSelection() && actionMode == null) {
                    actionMode = activity.startSupportActionMode(actionModeCallback);
                    setActionModeTitle(selectionTracker.getSelection().size());

                } else if (!selectionTracker.hasSelection()) {
                    if (actionMode != null)
                        actionMode.finish();
                    actionMode = null;

                } else {
                    setActionModeTitle(selectionTracker.getSelection().size());

                    /* Show/hide menu items after change selected files */
                    int size = selectionTracker.getSelection().size();
                    if (size == 1 || size == 2)
                        actionMode.invalidate();
                }
            }

            @Override
            public void onSelectionRestored()
            {
                super.onSelectionRestored();

                actionMode = activity.startSupportActionMode(actionModeCallback);
                setActionModeTitle(selectionTracker.getSelection().size());
            }
        });

        if (savedInstanceState != null)
            selectionTracker.onRestoreInstanceState(savedInstanceState);
        adapter.setSelectionTracker(selectionTracker);

        FragmentManager fm = getChildFragmentManager();
        deleteTrackersDialog = (BaseAlertDialog)fm.findFragmentByTag(TAG_DELETE_TRACKERS_DIALOG);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        listTrackerState = layoutManager.onSaveInstanceState();
        outState.putParcelable(TAG_LIST_TRACKER_STATE, listTrackerState);
        selectionTracker.onSaveInstanceState(outState);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState)
    {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null)
            listTrackerState = savedInstanceState.getParcelable(TAG_LIST_TRACKER_STATE);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (listTrackerState != null)
            layoutManager.onRestoreInstanceState(listTrackerState);
    }

    private void subscribeAdapter()
    {
        disposables.add(viewModel.observeTrackers()
                .subscribeOn(Schedulers.io())
                .flatMapSingle((children) ->
                        Flowable.fromIterable(children)
                                .map(TrackerItem::new)
                                .toList()
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((children) -> adapter.submitList(children)));
    }

    private void subscribeAlertDialog()
    {
        Disposable d = dialogViewModel.observeEvents()
                .subscribe((event) -> {
                    if (event.dialogTag == null || !event.dialogTag.equals(TAG_DELETE_TRACKERS_DIALOG) || deleteTrackersDialog == null)
                        return;
                    switch (event.type) {
                        case POSITIVE_BUTTON_CLICKED:
                            deleteTrackers();
                            deleteTrackersDialog.dismiss();
                            break;
                        case NEGATIVE_BUTTON_CLICKED:
                            deleteTrackersDialog.dismiss();
                            break;
                    }
                });
        disposables.add(d);
    }

    private void setActionModeTitle(int itemCount)
    {
        actionMode.setTitle(String.valueOf(itemCount));
    }

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback()
    {
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            mode.getMenuInflater().inflate(R.menu.detail_torrent_trackers_action_mode, menu);
            Utils.showActionModeStatusBar(activity, true);
            msgViewModel.fragmentInActionMode(true);

            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            int itemId = item.getItemId();
            if (itemId == R.id.delete_tracker_url) {
                deleteTrackersDialog();
            } else if (itemId == R.id.share_url_menu) {
                shareUrl();
                mode.finish();
            } else if (itemId == R.id.select_all_trackers_menu) {
                selectAllTrackers();
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            selectionTracker.clearSelection();
            msgViewModel.fragmentInActionMode(false);
            Utils.showActionModeStatusBar(activity, false);
        }
    };

    private void deleteTrackersDialog()
    {
        if (!isAdded())
            return;

        FragmentManager fm = getChildFragmentManager();
        if (fm.findFragmentByTag(TAG_DELETE_TRACKERS_DIALOG) == null) {
            deleteTrackersDialog = BaseAlertDialog.newInstance(
                    getString(R.string.deleting),
                    (selectionTracker.getSelection().size() > 1 ?
                            getString(R.string.delete_selected_trackers) :
                            getString(R.string.delete_selected_tracker)),
                    0,
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    null,
                    false);

            deleteTrackersDialog.show(fm, TAG_DELETE_TRACKERS_DIALOG);
        }
    }

    private void deleteTrackers()
    {
        MutableSelection<TrackerItem> selections = new MutableSelection<>();
        selectionTracker.copySelection(selections);

        disposables.add(Observable.fromIterable(selections)
                .map((selection -> selection.url))
                .toList()
                .subscribe((urls) -> viewModel.deleteTrackers(urls)));

        if (actionMode != null)
            actionMode.finish();
    }

    private void shareUrl()
    {
        MutableSelection<TrackerItem> selections = new MutableSelection<>();
        selectionTracker.copySelection(selections);

        disposables.add(Observable.fromIterable(selections)
                .map((selection -> selection.url))
                .toList()
                .subscribe((urls) -> {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "url");

                    if (urls.size() == 1)
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, urls.get(0));
                    else
                        sharingIntent.putExtra(Intent.EXTRA_TEXT,
                                TextUtils.join(Utils.getLineSeparator(), urls));

                    startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
                }));
    }

    @SuppressLint("RestrictedApi")
    private void selectAllTrackers()
    {
        int n = adapter.getItemCount();
        if (n > 0) {
            selectionTracker.startRange(0);
            selectionTracker.extendRange(adapter.getItemCount() - 1);
        }
    }
}
