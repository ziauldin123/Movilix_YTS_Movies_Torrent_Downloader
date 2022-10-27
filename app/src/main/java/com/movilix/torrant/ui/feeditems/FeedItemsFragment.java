

package com.movilix.torrant.ui.feeditems;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import com.movilix.R;
import com.movilix.torrant.core.utils.Utils;
import com.movilix.databinding.FragmentFeedItemsBinding;
import com.movilix.torrant.ui.FragmentCallback;
import com.movilix.torrant.ui.addtorrent.AddTorrentActivity;
import com.movilix.torrant.ui.customviews.RecyclerViewDividerDecoration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FeedItemsFragment extends Fragment
    implements com.movilix.torrant.ui.feeditems.FeedItemsAdapter.ClickListener
{
    private static final String TAG = FeedItemsFragment.class.getSimpleName();

    private static final String TAG_ITEMS_LIST_STATE = "items_list_state";

    private AppCompatActivity activity;
    private FragmentFeedItemsBinding binding;
    private com.movilix.torrant.ui.feeditems.FeedItemsViewModel viewModel;
    private com.movilix.torrant.ui.feeditems.FeedItemsAdapter adapter;
    private LinearLayoutManager layoutManager;
    /* Save state scrolling */
    private Parcelable itemsListState;
    private long feedId;
    private CompositeDisposable disposables = new CompositeDisposable();

    public static FeedItemsFragment newInstance(long feedId)
    {
        FeedItemsFragment fragment = new FeedItemsFragment();
        fragment.setFeedId(feedId);
        fragment.setArguments(new Bundle());

        return fragment;
    }

    public long getFeedId()
    {
        return feedId;
    }

    public void setFeedId(long feedId)
    {
        this.feedId = feedId;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed_items, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (activity == null)
            activity = (AppCompatActivity)getActivity();

        viewModel = new ViewModelProvider(activity).get(com.movilix.torrant.ui.feeditems.FeedItemsViewModel.class);

        /* Remove previous data if fragment changed */
        if (Utils.isTwoPane(activity))
            viewModel.clearData();
        viewModel.setFeedId(feedId);

        if (Utils.isTwoPane(activity)) {
            binding.toolbar.inflateMenu(R.menu.feed_items);
            binding.toolbar.setNavigationIcon(ContextCompat.getDrawable(activity.getApplicationContext(),
                    R.drawable.ic_arrow_back_white_24dp));
            binding.toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);

        } else {
            binding.toolbar.setTitle(R.string.details);
            activity.setSupportActionBar(binding.toolbar);
            setHasOptionsMenu(true);
            if (activity.getSupportActionBar() != null)
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener((v) -> onBackPressed());

        layoutManager = new LinearLayoutManager(activity);
        binding.feedItemsList.setLayoutManager(layoutManager);
        TypedArray a = activity.obtainStyledAttributes(new TypedValue().data, new int[]{ R.attr.divider });
        binding.feedItemsList.addItemDecoration(new RecyclerViewDividerDecoration(a.getDrawable(0)));
        a.recycle();
        binding.feedItemsList.setEmptyView(binding.emptyViewFeedItems);

        adapter = new com.movilix.torrant.ui.feeditems.FeedItemsAdapter(this);
        binding.feedItemsList.setAdapter(adapter);

        binding.swipeContainer.setOnRefreshListener(() -> viewModel.refreshChannel());
    }

    @Override
    public void onStart()
    {
        super.onStart();

        subscribeAdapter();
        subscribeRefreshStatus();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        disposables.clear();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (itemsListState != null)
            layoutManager.onRestoreInstanceState(itemsListState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        itemsListState = layoutManager.onSaveInstanceState();
        outState.putParcelable(TAG_ITEMS_LIST_STATE, itemsListState);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState)
    {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null)
            itemsListState = savedInstanceState.getParcelable(TAG_ITEMS_LIST_STATE);
    }

    private void subscribeAdapter()
    {
        getAllFeedItemsSingle();
        disposables.add(observeFeedItems());
    }

    private Disposable observeFeedItems()
    {
        return viewModel.observeItemsByFeedId()
                .subscribeOn(Schedulers.io())
                .flatMapSingle((itemList) ->
                        Flowable.fromIterable(itemList)
                                .map(com.movilix.torrant.ui.feeditems.FeedItemsListItem::new)
                                .toList()
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::submitList,
                        (Throwable t) -> {
                            Log.e(TAG, "Getting item list error: " +
                                    Log.getStackTraceString(t));
                        });
    }

    private void getAllFeedItemsSingle()
    {
        disposables.add(viewModel.getItemsByFeedIdSingle()
                .subscribeOn(Schedulers.io())
                .flatMap((itemList) ->
                        Observable.fromIterable(itemList)
                                .map(com.movilix.torrant.ui.feeditems.FeedItemsListItem::new)
                                .toList()
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::submitList,
                        (Throwable t) -> {
                            Log.e(TAG, "Getting item list error: " +
                                    Log.getStackTraceString(t));
                        }));
    }

    private void subscribeRefreshStatus()
    {
        disposables.add(viewModel.observeRefreshStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(binding.swipeContainer::setRefreshing));
    }

    @Override
    public void onItemClicked(@NonNull com.movilix.torrant.ui.feeditems.FeedItemsListItem item)
    {
        openDownloadUrl(item);
    }

    @Override
    public void onItemMenuClicked(int menuId, @NonNull com.movilix.torrant.ui.feeditems.FeedItemsListItem item)
    {
        if (menuId == R.id.open_article_menu) {
            openArticle(item);
        } else if (menuId == R.id.mark_as_read_menu) {
            viewModel.markAsRead(item.id);
        } else if (menuId == R.id.mark_as_unread_menu) {
            viewModel.markAsUnread(item.id);
        }
    }

    private void openArticle(com.movilix.torrant.ui.feeditems.FeedItemsListItem item)
    {
        if (TextUtils.isEmpty(item.articleUrl)) {
            Snackbar.make(binding.coordinatorLayout,
                    R.string.feed_item_url_not_found,
                    Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }

        viewModel.markAsRead(item.id);

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(item.articleUrl));
        startActivity(i);
    }

    private void openDownloadUrl(com.movilix.torrant.ui.feeditems.FeedItemsListItem item)
    {
        if (TextUtils.isEmpty(item.downloadUrl)) {
            Snackbar.make(binding.coordinatorLayout,
                    R.string.feed_item_url_not_found,
                    Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }

        viewModel.markAsRead(item.id);

        Intent i = new Intent(activity, AddTorrentActivity.class);
        i.putExtra(AddTorrentActivity.TAG_URI, Uri.parse(item.downloadUrl));
        startActivity(i);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.feed_items, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem)
    {
        int itemId = menuItem.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        } else if (itemId == R.id.refresh_feed_channel_menu) {
            viewModel.refreshChannel();
        } else if (itemId == R.id.mark_as_read_menu) {
            viewModel.markAllAsRead();
        }

        return true;
    }

    public void onBackPressed()
    {
        finish(new Intent(), FragmentCallback.ResultCode.BACK);
    }

    private void finish(Intent intent, FragmentCallback.ResultCode code)
    {
        ((FragmentCallback)activity).onFragmentFinished(this, intent, code);
    }
}
