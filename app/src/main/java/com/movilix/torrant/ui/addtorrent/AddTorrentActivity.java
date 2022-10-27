

package com.movilix.torrant.ui.addtorrent;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;

import com.movilix.R;
import com.movilix.torrant.core.exception.DecodeException;
import com.movilix.torrant.core.exception.FetchLinkException;
import com.movilix.torrant.core.exception.FreeSpaceException;
import com.movilix.torrant.core.exception.NoFilesSelectedException;
import com.movilix.torrant.core.exception.TorrentAlreadyExistsException;
import com.movilix.torrant.core.utils.Utils;
import com.movilix.databinding.ActivityAddTorrentBinding;
import com.movilix.torrant.ui.BaseAlertDialog;
import com.movilix.torrant.ui.PermissionDeniedDialog;
import com.movilix.torrant.ui.errorreport.ErrorReportDialog;

import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/*
 * The dialog for adding torrent. The parent window.
 */

public class AddTorrentActivity extends AppCompatActivity
{
    private static final String TAG = AddTorrentActivity.class.getSimpleName();

    public static final String TAG_URI = "uri";

    private static final String TAG_ERR_REPORT_DIALOG = "io_err_report_dialog";
    private static final String TAG_DECODE_EXCEPT_DIALOG = "decode_except_dialog";
    private static final String TAG_FETCH_EXCEPT_DIALOG = "fetch_except_dialog";
    private static final String TAG_OUT_OF_MEMORY_DIALOG = "out_of_memory_dialog";
    private static final String TAG_ILLEGAL_ARGUMENT_DIALOG = "illegal_argument_dialog";
    private static final String TAG_ADD_ERROR_DIALOG = "add_error_dialog";
    private static final String TAG_PERM_DENIED_DIALOG = "perm_denied_dialog";

    private ActivityAddTorrentBinding binding;
    private com.movilix.torrant.ui.addtorrent.AddTorrentViewModel viewModel;
    private com.movilix.torrant.ui.addtorrent.AddTorrentPagerAdapter adapter;
    private BaseAlertDialog.SharedViewModel dialogViewModel;
    private ErrorReportDialog errReportDialog;
    private CompositeDisposable disposable = new CompositeDisposable();
    private boolean showAddButton;
    private PermissionDeniedDialog permDeniedDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(Utils.getAppTheme(getApplicationContext()));
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_torrent);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(com.movilix.torrant.ui.addtorrent.AddTorrentViewModel.class);
        dialogViewModel = provider.get(BaseAlertDialog.SharedViewModel.class);
        errReportDialog = (ErrorReportDialog)getSupportFragmentManager().findFragmentByTag(TAG_ERR_REPORT_DIALOG);

        if (!Utils.checkStoragePermission(this) && permDeniedDialog == null) {
            storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        initLayout();
        observeDecodeState();
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
    protected void onStop()
    {
        super.onStop();

        disposable.clear();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        subscribeAlertDialog();
    }

    private void subscribeAlertDialog()
    {
        Disposable d = dialogViewModel.observeEvents().subscribe(this::handleAlertDialogEvent);
        disposable.add(d);
    }

    private void handleAlertDialogEvent(BaseAlertDialog.Event event) {
        if (event.dialogTag == null) {
            return;
        }
        if (event.dialogTag.equals(TAG_ERR_REPORT_DIALOG)) {
            switch (event.type) {
                case POSITIVE_BUTTON_CLICKED:
                    if (errReportDialog != null) {
                        Dialog dialog = errReportDialog.getDialog();
                        if (dialog != null) {
                            TextInputEditText editText = dialog.findViewById(R.id.comment);
                            Editable e = editText.getText();
                            String comment = (e == null ? null : e.toString());

                            Utils.reportError(viewModel.errorReport, comment);
                            errReportDialog.dismiss();
                        }
                    }
                    finish();
                    break;
                case NEGATIVE_BUTTON_CLICKED:
                    if (errReportDialog != null) {
                        errReportDialog.dismiss();
                    }
                    finish();
                    break;
            }
        } else if (event.dialogTag.equals(TAG_PERM_DENIED_DIALOG)) {
            if (event.type != BaseAlertDialog.EventType.DIALOG_SHOWN) {
                permDeniedDialog.dismiss();
            }
            if (event.type == BaseAlertDialog.EventType.NEGATIVE_BUTTON_CLICKED) {
                storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private void initLayout()
    {
        binding.toolbar.setTitle(R.string.add_torrent_title);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /* Disable elevation for portrait mode */
        if (!Utils.isTwoPane(this) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            binding.toolbar.setElevation(0);


        adapter = new com.movilix.torrant.ui.addtorrent.AddTorrentPagerAdapter(this);
        binding.viewpager.setAdapter(adapter);
        binding.viewpager.setOffscreenPageLimit(com.movilix.torrant.ui.addtorrent.AddTorrentPagerAdapter.NUM_FRAGMENTS);
        new TabLayoutMediator(binding.tabLayout, binding.viewpager,
                (tab, position) -> {
                    switch (position) {
                        case com.movilix.torrant.ui.addtorrent.AddTorrentPagerAdapter.INFO_FRAG_POS:
                            tab.setText(R.string.torrent_info);
                            break;
                        case com.movilix.torrant.ui.addtorrent.AddTorrentPagerAdapter.FILES_FRAG_POS:
                            tab.setText(R.string.torrent_files);
                            break;
                    }
                }
        ).attach();
    }

    private void observeDecodeState()
    {
        viewModel.getDecodeState().observe(this, (state) -> {
            switch (state.status) {
                case UNKNOWN:
                    Uri uri = getUri();
                    if (uri != null)
                        viewModel.startDecode(uri);
                    break;
                case DECODE_TORRENT_FILE:
                case FETCHING_HTTP:
                case FETCHING_MAGNET:
                    onStartDecode(state.status == com.movilix.torrant.ui.addtorrent.AddTorrentViewModel.Status.DECODE_TORRENT_FILE);
                    break;
                case FETCHING_HTTP_COMPLETED:
                case DECODE_TORRENT_COMPLETED:
                case FETCHING_MAGNET_COMPLETED:
                case ERROR:
                    onStopDecode(state.error);
                    break;
            }
        });
    }

    private void onStartDecode(boolean isTorrentFile)
    {
        binding.progress.setVisibility(View.VISIBLE);
        showAddButton = !isTorrentFile;
        invalidateOptionsMenu();
    }

    private void onStopDecode(Throwable e)
    {
        binding.progress.setVisibility(View.GONE);

        if (e != null) {
            handleDecodeException(e);
            return;
        }

        viewModel.makeFileTree();
        showAddButton = true;
        invalidateOptionsMenu();
    }

    private Uri getUri()
    {
        Intent i = getIntent();
        /* Implicit intent with path to torrent file, http or magnet link */
        if (i.getData() != null)
            return i.getData();
        else if (!TextUtils.isEmpty(i.getStringExtra(Intent.EXTRA_TEXT)))
            return Uri.parse(i.getStringExtra(Intent.EXTRA_TEXT));
        else
            return i.getParcelableExtra(TAG_URI);
    }

    private void addTorrent()
    {
        String name = viewModel.mutableParams.getName();
        if (TextUtils.isEmpty(name)) {
            Snackbar.make(binding.coordinatorLayout,
                    R.string.error_empty_name,
                    Snackbar.LENGTH_LONG)
                    .show();
            return;
        }

        try {
            if (viewModel.addTorrent())
                finish();

        } catch (Exception e) {
            if (e instanceof TorrentAlreadyExistsException) {
                Toast.makeText(getApplication(),
                        R.string.torrent_exist,
                        Toast.LENGTH_SHORT)
                        .show();
                finish();

            } else {
                handleAddException(e);
            }
        }
    }

    private void handleAddException(Throwable e)
    {
        if (e instanceof NoFilesSelectedException) {
            Snackbar.make(binding.coordinatorLayout,
                    R.string.error_no_files_selected,
                    Snackbar.LENGTH_LONG)
                    .show();
            return;
        }

        if (e instanceof FreeSpaceException) {
            Snackbar.make(binding.coordinatorLayout,
                    R.string.error_free_space,
                    Snackbar.LENGTH_LONG)
                    .show();
            return;
        }

        Log.e(TAG, Log.getStackTraceString(e));
        if (e instanceof FileNotFoundException) {
            showAddErrorDialog(getApplication().getString(R.string.error_file_not_found_add_torrent), null);
        } else if (e instanceof IOException) {
            showAddErrorDialog(getApplication().getString(R.string.error_io_add_torrent), null);
        } else {
            showAddErrorDialog(getApplication().getString(R.string.error_add_torrent), e);
        }
    }

    private void showAddErrorDialog(String message, Throwable e)
    {
        FragmentManager fm = getSupportFragmentManager();
        if (e != null) {
            viewModel.errorReport = e;
            if (fm.findFragmentByTag(TAG_ERR_REPORT_DIALOG) == null) {
                errReportDialog = ErrorReportDialog.newInstance(
                        getString(R.string.error),
                        message,
                        Log.getStackTraceString(e)
                );

                FragmentTransaction ft = fm.beginTransaction();
                ft.add(errReportDialog, TAG_ERR_REPORT_DIALOG);
                ft.commitAllowingStateLoss();
            }
        } else if (fm.findFragmentByTag(TAG_ADD_ERROR_DIALOG) == null) {
            BaseAlertDialog errDialog = BaseAlertDialog.newInstance(
                    getString(R.string.error),
                    message,
                    0,
                    getString(R.string.ok),
                    null,
                    null,
                    false
                );

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(errDialog, TAG_ADD_ERROR_DIALOG);
            ft.commitAllowingStateLoss();
        }
    }

    public void handleDecodeException(Throwable e)
    {
        if (e == null)
            return;

        Log.e(TAG, Log.getStackTraceString(e));
        FragmentManager fm = getSupportFragmentManager();

        if (e instanceof DecodeException) {
            if (fm.findFragmentByTag(TAG_DECODE_EXCEPT_DIALOG) == null) {
                BaseAlertDialog errDialog = BaseAlertDialog.newInstance(
                        getString(R.string.error),
                        getString(R.string.error_decode_torrent),
                        0,
                        getString(R.string.ok),
                        null,
                        null,
                        false);

                FragmentTransaction ft = fm.beginTransaction();
                ft.add(errDialog, TAG_DECODE_EXCEPT_DIALOG);
                ft.commitAllowingStateLoss();
            }

        } else if (e instanceof FetchLinkException) {
            if (fm.findFragmentByTag(TAG_FETCH_EXCEPT_DIALOG) == null) {
                BaseAlertDialog errDialog = BaseAlertDialog.newInstance(
                        getString(R.string.error),
                        getString(R.string.error_fetch_link),
                        0,
                        getString(R.string.ok),
                        null,
                        null,
                        false);

                FragmentTransaction ft = fm.beginTransaction();
                ft.add(errDialog, TAG_FETCH_EXCEPT_DIALOG);
                ft.commitAllowingStateLoss();
            }

        } else if (e instanceof IllegalArgumentException) {
            if (fm.findFragmentByTag(TAG_ILLEGAL_ARGUMENT_DIALOG) == null) {
                BaseAlertDialog errDialog = BaseAlertDialog.newInstance(
                        getString(R.string.error),
                        getString(R.string.error_invalid_link_or_path),
                        0,
                        getString(R.string.ok),
                        null,
                        null,
                        false);

                FragmentTransaction ft = fm.beginTransaction();
                ft.add(errDialog, TAG_ILLEGAL_ARGUMENT_DIALOG);
                ft.commitAllowingStateLoss();
            }

        } else if (e instanceof IOException) {
            viewModel.errorReport = e;
            if (fm.findFragmentByTag(TAG_ERR_REPORT_DIALOG) == null) {
                errReportDialog = ErrorReportDialog.newInstance(
                        getString(R.string.error),
                        getString(R.string.error_io_torrent),
                        Log.getStackTraceString(e));

                FragmentTransaction ft = fm.beginTransaction();
                ft.add(errReportDialog, TAG_ERR_REPORT_DIALOG);
                ft.commitAllowingStateLoss();
            }

        } else if (e instanceof OutOfMemoryError) {
            if (fm.findFragmentByTag(TAG_OUT_OF_MEMORY_DIALOG) == null) {
                BaseAlertDialog errDialog = BaseAlertDialog.newInstance(
                        getString(R.string.error),
                        getString(R.string.file_is_too_large_error),
                        0,
                        getString(R.string.ok),
                        null,
                        null,
                        false);

                FragmentTransaction ft = fm.beginTransaction();
                ft.add(errDialog, TAG_OUT_OF_MEMORY_DIALOG);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.add_torrent, menu);

        MenuItem add = menu.findItem(R.id.add_torrent_dialog_add_menu);
        if (add != null)
            add.setVisible(showAddButton);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == R.id.add_torrent_dialog_add_menu) {
            addTorrent();
        }

        return true;
    }

    @Override
    public void finish()
    {
        viewModel.finish();

        super.finish();
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
