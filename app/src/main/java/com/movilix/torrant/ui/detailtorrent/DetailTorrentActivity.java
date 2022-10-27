

package com.movilix.torrant.ui.detailtorrent;

import android.content.Intent;
import android.os.Bundle;

import com.movilix.R;
import com.movilix.torrant.core.model.TorrentInfoProvider;
import com.movilix.torrant.core.utils.Utils;
import com.movilix.torrant.ui.FragmentCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailTorrentActivity extends AppCompatActivity
        implements FragmentCallback
{
    private static final String TAG = DetailTorrentActivity.class.getSimpleName();

    public static final String TAG_TORRENT_ID = "torrent_id";

    private com.movilix.torrant.ui.detailtorrent.DetailTorrentFragment detailTorrentFragment;
    private TorrentInfoProvider infoProvider;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        setTheme(Utils.getAppTheme(getApplicationContext()));
        super.onCreate(savedInstanceState);

        if (Utils.isTwoPane(this)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_detail_torrent);

        infoProvider = TorrentInfoProvider.getInstance(getApplicationContext());

        detailTorrentFragment = (com.movilix.torrant.ui.detailtorrent.DetailTorrentFragment)getSupportFragmentManager()
                .findFragmentById(R.id.detail_torrent_fragmentContainer);

        if (detailTorrentFragment != null)
            detailTorrentFragment.setTorrentId(getIntent().getStringExtra(TAG_TORRENT_ID));
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        disposables.clear();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        subscribeTorrentDeleted();
    }

    private void subscribeTorrentDeleted()
    {
        disposables.add(infoProvider.observeTorrentsDeleted()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((id) -> {
                    if (detailTorrentFragment != null &&
                        id.equals(detailTorrentFragment.getTorrentId()))
                        finish();
                }));
    }

    @Override
    public void onFragmentFinished(@NonNull Fragment f, Intent intent,
                                   @NonNull ResultCode code)
    {
        finish();
    }
}
