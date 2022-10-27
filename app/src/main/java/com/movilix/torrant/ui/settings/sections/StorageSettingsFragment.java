

package com.movilix.torrant.ui.settings.sections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.movilix.torrant.ui.filemanager.FileManagerConfig;
import com.movilix.torrant.ui.filemanager.FileManagerDialog;
import com.takisoft.preferencex.PreferenceFragmentCompat;

import com.movilix.R;
import com.movilix.torrant.core.RepositoryHelper;
import com.movilix.torrant.core.exception.UnknownUriException;
import com.movilix.torrant.core.settings.SettingsRepository;
import com.movilix.torrant.core.system.FileSystemFacade;
import com.movilix.torrant.core.system.SystemFacadeHelper;
import com.movilix.torrant.core.utils.Utils;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

public class StorageSettingsFragment extends PreferenceFragmentCompat
    implements Preference.OnPreferenceChangeListener
{
    private static final String TAG = StorageSettingsFragment.class.getSimpleName();

    private static final String TAG_DIR_CHOOSER_BIND_PREF = "dir_chooser_bind_pref";

    private SettingsRepository pref;
    private FileSystemFacade fs;
    /* Preference that is associated with the current dir selection dialog */
    private String dirChooserBindPref;

    public static StorageSettingsFragment newInstance()
    {
        StorageSettingsFragment fragment = new StorageSettingsFragment();
        fragment.setArguments(new Bundle());

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            dirChooserBindPref = savedInstanceState.getString(TAG_DIR_CHOOSER_BIND_PREF);

        Context context = getActivity().getApplicationContext();
        fs = SystemFacadeHelper.getFileSystemFacade(context);
        pref = RepositoryHelper.getSettingsRepository(context);

        String keySaveTorrentsIn = getString(R.string.pref_key_save_torrents_in);
        Preference saveTorrentsIn = findPreference(keySaveTorrentsIn);
        if (saveTorrentsIn != null) {
            String path = pref.saveTorrentsIn();
            if (path != null) {
                Uri uri = Uri.parse(path);
                try {
                    saveTorrentsIn.setSummary(fs.getDirPath(uri));
                } catch (UnknownUriException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
                saveTorrentsIn.setOnPreferenceClickListener((preference) -> {
                    dirChooserBindPref = getString(R.string.pref_key_save_torrents_in);
                    dirChooseDialog(uri);

                    return true;
                });
            }
        }

        String keyMoveAfterDownload = getString(R.string.pref_key_move_after_download);
        SwitchPreferenceCompat moveAfterDownload = findPreference(keyMoveAfterDownload);
        if (moveAfterDownload != null) {
            moveAfterDownload.setChecked(pref.moveAfterDownload());
            bindOnPreferenceChangeListener(moveAfterDownload);
        }

        String keyMoveAfterDownloadIn = getString(R.string.pref_key_move_after_download_in);
        Preference moveAfterDownloadIn = findPreference(keyMoveAfterDownloadIn);
        if (moveAfterDownloadIn != null) {
            String path = pref.moveAfterDownloadIn();
            if (path != null) {
                Uri uri = Uri.parse(path);
                try {
                    moveAfterDownloadIn.setSummary(fs.getDirPath(uri));
                } catch (UnknownUriException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
                moveAfterDownloadIn.setOnPreferenceClickListener((preference) -> {
                    dirChooserBindPref = getString(R.string.pref_key_move_after_download_in);
                    dirChooseDialog(uri);

                    return true;
                });
            }
        }

        String keySaveTorrentFiles = getString(R.string.pref_key_save_torrent_files);
        SwitchPreferenceCompat saveTorrentFiles = findPreference(keySaveTorrentFiles);
        if (saveTorrentFiles != null) {
            saveTorrentFiles.setChecked(pref.saveTorrentFiles());
            bindOnPreferenceChangeListener(saveTorrentFiles);
        }

        String keySaveTorrentFilesIn = getString(R.string.pref_key_save_torrent_files_in);
        Preference saveTorrentFilesIn = findPreference(keySaveTorrentFilesIn);
        if (saveTorrentFilesIn != null) {
            String path = pref.saveTorrentFilesIn();
            if (path != null) {
                Uri uri = Uri.parse(path);
                try {
                    saveTorrentFilesIn.setSummary(fs.getDirPath(uri));
                } catch (UnknownUriException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
                saveTorrentFilesIn.setOnPreferenceClickListener((preference) -> {
                    dirChooserBindPref = getString(R.string.pref_key_save_torrent_files_in);
                    dirChooseDialog(uri);

                    return true;
                });
            }
        }

        String keyWatchDir = getString(R.string.pref_key_watch_dir);
        SwitchPreferenceCompat watchDir = findPreference(keyWatchDir);
        if (watchDir != null) {
            watchDir.setChecked(pref.watchDir());
            bindOnPreferenceChangeListener(watchDir);
        }

        String keyDirToWatch = getString(R.string.pref_key_dir_to_watch);
        Preference dirToWatch = findPreference(keyDirToWatch);
        if (dirToWatch != null) {
            String path = pref.dirToWatch();
            if (path != null) {
                Uri uri = Uri.parse(path);
                try {
                    dirToWatch.setSummary(fs.getDirPath(uri));
                } catch (UnknownUriException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
                dirToWatch.setOnPreferenceClickListener((preference) -> {
                    dirChooserBindPref = getString(R.string.pref_key_dir_to_watch);
                    dirChooseDialog(uri);

                    return true;
                });
            }
        }

        String keyWatchDirDeleteFile = getString(R.string.pref_key_watch_dir_delete_file);
        SwitchPreferenceCompat watchDirDeleteFile = findPreference(keyWatchDirDeleteFile);
        if (watchDirDeleteFile != null) {
            watchDirDeleteFile.setChecked(pref.watchDirDeleteFile());
            bindOnPreferenceChangeListener(watchDirDeleteFile);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putString(TAG_DIR_CHOOSER_BIND_PREF, dirChooserBindPref);
    }

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.pref_storage, rootKey);
    }

    private void bindOnPreferenceChangeListener(Preference preference)
    {
        preference.setOnPreferenceChangeListener(this);
    }

    private void dirChooseDialog(Uri path)
    {
        String dirPath = null;
        if (path != null && Utils.isFileSystemPath(path))
            dirPath = path.getPath();

        Intent i = new Intent(getActivity(), FileManagerDialog.class);
        FileManagerConfig config = new FileManagerConfig(dirPath,
                null,
                FileManagerConfig.DIR_CHOOSER_MODE);
        i.putExtra(FileManagerDialog.TAG_CONFIG, config);

        downloadDirChoose.launch(i);
    }
    final ActivityResultLauncher<Intent> downloadDirChoose = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (result.getResultCode() != Activity.RESULT_OK) {
                   return;
                }
                if (data.getData() == null || dirChooserBindPref == null)
                    return;

                Uri path = data.getData();

                Preference p = findPreference(dirChooserBindPref);
                if (p == null)
                    return;

                if (dirChooserBindPref.equals(getString(R.string.pref_key_dir_to_watch))) {
                    pref.dirToWatch(path.toString());

                } else if (dirChooserBindPref.equals(getString(R.string.pref_key_move_after_download_in))) {
                    pref.moveAfterDownloadIn(path.toString());

                } else if (dirChooserBindPref.equals(getString(R.string.pref_key_save_torrent_files_in))) {
                    pref.saveTorrentFilesIn(path.toString());

                } else if (dirChooserBindPref.equals(getString(R.string.pref_key_save_torrents_in))) {
                    pref.saveTorrentsIn(path.toString());
                }

                try {
                    p.setSummary(fs.getDirPath(path));
                } catch (UnknownUriException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
    );

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        if (preference.getKey().equals(getString(R.string.pref_key_watch_dir))) {
            pref.watchDir((boolean)newValue);
        } else if (preference.getKey().equals(getString(R.string.pref_key_move_after_download))) {
            pref.moveAfterDownload((boolean)newValue);
        } else if (preference.getKey().equals(getString(R.string.pref_key_save_torrent_files))) {
            pref.saveTorrentFiles((boolean)newValue);
        } else if (preference.getKey().equals(getString(R.string.pref_key_watch_dir_delete_file))) {
            pref.watchDirDeleteFile((boolean) newValue);
        }

        return true;
    }
}
