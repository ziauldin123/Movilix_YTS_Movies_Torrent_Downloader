

package com.movilix.torrant.ui.createtorrent;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.movilix.torrant.core.utils.Utils;
import com.movilix.torrant.ui.BaseAlertDialog;
import com.movilix.torrant.ui.FragmentCallback;
import com.movilix.torrant.ui.PermissionDeniedDialog;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CreateTorrentActivity extends AppCompatActivity
    implements FragmentCallback
{
    private static final String TAG_CREATE_TORRENT_DIALOG = "create_torrent_dialog";
    private static final String TAG_PERM_DENIED_DIALOG = "perm_denied_dialog";

    private CreateTorrentDialog createTorrentDialog;
    private PermissionDeniedDialog permDeniedDialog;
    private BaseAlertDialog.SharedViewModel dialogViewModel;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        setTheme(Utils.getTranslucentAppTheme(getApplicationContext()));
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(this);
        dialogViewModel = provider.get(BaseAlertDialog.SharedViewModel.class);

        FragmentManager fm = getSupportFragmentManager();
        createTorrentDialog = (CreateTorrentDialog)fm.findFragmentByTag(TAG_CREATE_TORRENT_DIALOG);
        if (createTorrentDialog == null) {
            createTorrentDialog = CreateTorrentDialog.newInstance();
            createTorrentDialog.show(fm, TAG_CREATE_TORRENT_DIALOG);
        }

        if (!Utils.checkStoragePermission(this) && permDeniedDialog == null) {
            storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private final ActivityResultLauncher<String> storagePermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (!isGranted && Utils.shouldRequestStoragePermission(this)) {
                    FragmentManager fm = getSupportFragmentManager();
                    if (fm.findFragmentByTag(TAG_PERM_DENIED_DIALOG) == null) {
                        permDeniedDialog = PermissionDeniedDialog.newInstance();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(permDeniedDialog, TAG_PERM_DENIED_DIALOG);
                        ft.commitAllowingStateLoss();
                    }
                }
            });

    @Override
    public void onStart() {
        super.onStart();

        subscribeAlertDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();

        disposable.clear();
    }

    private void subscribeAlertDialog() {
        Disposable d = dialogViewModel.observeEvents().subscribe(event -> {
            if (event.dialogTag == null) {
                return;
            }
            if (event.dialogTag.equals(TAG_PERM_DENIED_DIALOG)) {
                if (event.type != BaseAlertDialog.EventType.DIALOG_SHOWN) {
                    permDeniedDialog.dismiss();
                }
                if (event.type == BaseAlertDialog.EventType.NEGATIVE_BUTTON_CLICKED) {
                    storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });
        disposable.add(d);
    }

    @Override
    public void onFragmentFinished(@NonNull Fragment f, Intent intent, @NonNull ResultCode code)
    {
        finish();
    }

    @Override
    public void onBackPressed()
    {
        createTorrentDialog.onBackPressed();
    }
}
