

package com.movilix.torrant.ui.feeditems;

import android.content.Intent;
import android.os.Bundle;

import com.movilix.R;
import com.movilix.torrant.core.utils.Utils;
import com.movilix.torrant.ui.FragmentCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class FeedItemsActivity extends AppCompatActivity
        implements FragmentCallback
{
    private static final String TAG = FeedItemsActivity.class.getSimpleName();

    public static final String TAG_FEED_ID = "feed_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        setTheme(Utils.getAppTheme(getApplicationContext()));
        super.onCreate(savedInstanceState);

        if (Utils.isTwoPane(this)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_feed_items);

        FeedItemsFragment feedItemsFragment = (FeedItemsFragment)getSupportFragmentManager()
                .findFragmentById(R.id.feed_items_fragmentContainer);

        if (feedItemsFragment != null)
            feedItemsFragment.setFeedId(getIntent().getLongExtra(TAG_FEED_ID, -1));
    }

    @Override
    public void onFragmentFinished(@NonNull Fragment f, Intent intent,
                                   @NonNull FragmentCallback.ResultCode code)
    {
        finish();
    }
}
