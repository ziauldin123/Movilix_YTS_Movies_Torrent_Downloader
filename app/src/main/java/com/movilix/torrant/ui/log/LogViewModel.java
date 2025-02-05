

package com.movilix.torrant.ui.log;

import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.net.Uri;

import com.movilix.R;
import com.movilix.torrant.core.RepositoryHelper;
import com.movilix.torrant.core.logger.LogEntry;
import com.movilix.torrant.core.logger.Logger;
import com.movilix.torrant.core.model.TorrentEngine;
import com.movilix.torrant.core.settings.SettingsRepository;
import com.movilix.torrant.service.SaveLogWorker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class LogViewModel extends AndroidViewModel
{
    private static final int PAGE_SIZE = 20;

    private TorrentEngine engine;
    private SettingsRepository pref;
    public LogMutableParams mutableParams = new LogMutableParams();
    private LogSourceFactory sourceFactory;
    private PagedList.Config pageConfig = new PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build();
    private boolean logPaused;
    private boolean recordingStopped;

    public LogViewModel(@NonNull Application application)
    {
        super(application);

        engine = TorrentEngine.getInstance(application);
        pref = RepositoryHelper.getSettingsRepository(application);

        Logger sessionLogger = engine.getSessionLogger();
        logPaused = sessionLogger.isPaused();
        recordingStopped = !sessionLogger.isRecording();
        sourceFactory = new LogSourceFactory(sessionLogger);

        initMutableParams();
    }

    private void initMutableParams()
    {
        mutableParams.setLogging(pref.logging());
        mutableParams.setLogSessionFilter(pref.logSessionFilter());
        mutableParams.setLogDhtFilter(pref.logDhtFilter());
        mutableParams.setLogPeerFilter(pref.logPeerFilter());
        mutableParams.setLogPortmapFilter(pref.logPortmapFilter());
        mutableParams.setLogTorrentFilter(pref.logTorrentFilter());

        mutableParams.addOnPropertyChangedCallback(paramsCallback);
    }

    LiveData<PagedList<LogEntry>> observeLog()
    {
        return new LivePagedListBuilder<>(sourceFactory, pageConfig)
                .setInitialLoadKey(Integer.MAX_VALUE) /* Start from the last entry */
                .build();
    }

    @Override
    protected void onCleared()
    {
        super.onCleared();

        mutableParams.removeOnPropertyChangedCallback(paramsCallback);
    }

    private final androidx.databinding.Observable.OnPropertyChangedCallback paramsCallback =
            new androidx.databinding.Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(androidx.databinding.Observable sender, int propertyId)
        {
            switch (propertyId) {
                case BR.logging:
                    boolean logging = mutableParams.isLogging();
                    if (!logging) {
                        logPaused = false;
                        recordingStopped = false;
                    }
                    pref.logging(logging);
                    break;
                case BR.logSessionFilter:
                    pref.logSessionFilter(mutableParams.isLogSessionFilter());
                    break;
                case BR.logDhtFilter:
                    pref.logDhtFilter(mutableParams.isLogDhtFilter());
                    break;
                case BR.logPeerFilter:
                    pref.logPeerFilter(mutableParams.isLogPeerFilter());
                    break;
                case BR.logPortmapFilter:
                    pref.logPortmapFilter(mutableParams.isLogPortmapFilter());
                    break;
                case BR.logTorrentFilter:
                    pref.logTorrentFilter(mutableParams.isLogTorrentFilter());
                    break;
            }
        }
    };

    void pauseLog()
    {
        engine.getSessionLogger().pause();
    }

    void pauseLogManually()
    {
        pauseLog();
        logPaused = true;
    }

    void resumeLog()
    {
        engine.getSessionLogger().resume();
    }

    void resumeLogManually()
    {
        resumeLog();
        logPaused = false;
    }

    int getLogEntriesCount()
    {
        return engine.getSessionLogger().getNumEntries();
    }

    boolean logPausedManually()
    {
        return logPaused;
    }

    void startLogRecording()
    {
        recordingStopped = false;

        engine.getSessionLogger().startRecording();
    }

    void stopLogRecording()
    {
        recordingStopped = true;

        engine.getSessionLogger().stopRecording();
    }

    boolean logRecording()
    {
        return !recordingStopped && engine.getSessionLogger().isRecording();
    }

    String getSaveLogFileName()
    {
        String timeStamp = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss", Locale.getDefault())
                .format(new Date());

        return getApplication().getString(R.string.app_name) + "_log_" + timeStamp + ".txt";
    }

    void saveLog(@NonNull Uri filePath)
    {
        recordingStopped = true;

        Data data = new Data.Builder()
                .putString(SaveLogWorker.TAG_FILE_URI, filePath.toString())
                .putBoolean(SaveLogWorker.TAG_RESUME_AFTER_SAVE, !logPausedManually())
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SaveLogWorker.class)
                .setInputData(data)
                .build();

        WorkManager.getInstance(getApplication()).enqueue(request);
    }

    boolean copyLogEntryToClipboard(@NonNull LogEntry entry)
    {
        ClipboardManager clipboard = (ClipboardManager)getApplication().getSystemService(Activity.CLIPBOARD_SERVICE);
        if (clipboard == null)
            return false;

        ClipData clip;
        clip = ClipData.newPlainText("Log entry", entry.toString());
        clipboard.setPrimaryClip(clip);

        return true;
    }
}
