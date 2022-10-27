

package com.movilix.torrant.service;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.movilix.R;
import com.movilix.torrant.core.exception.UnknownUriException;
import com.movilix.torrant.core.logger.Logger;
import com.movilix.torrant.core.model.TorrentEngine;
import com.movilix.torrant.core.system.FileDescriptorWrapper;
import com.movilix.torrant.core.system.FileSystemFacade;
import com.movilix.torrant.core.system.SystemFacadeHelper;

import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SaveLogWorker extends Worker
{
    private static final String TAG = SaveLogWorker.class.getSimpleName();

    public static final String TAG_FILE_URI = "file_uri";
    public static final String TAG_RESUME_AFTER_SAVE = "resume_after_save";

    private Context appContext;
    private TorrentEngine engine;
    private FileSystemFacade fs;
    Handler handler = new Handler(Looper.getMainLooper());

    public SaveLogWorker(@NonNull Context context, @NonNull WorkerParameters params)
    {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        appContext = getApplicationContext();
        engine = TorrentEngine.getInstance(appContext);
        fs = SystemFacadeHelper.getFileSystemFacade(appContext);

        Data data = getInputData();
        String filePath = data.getString(TAG_FILE_URI);
        if (filePath == null) {
            Log.e(TAG, "Cannot save log: file path is null");
            showFailToast();

            return Result.failure();
        }

        boolean resume = data.getBoolean(TAG_RESUME_AFTER_SAVE, true);

        return saveLog(Uri.parse(filePath), resume);
    }

    private Result saveLog(Uri filePath, boolean resume)
    {
        Logger logger = engine.getSessionLogger();
        logger.pause();

        try (FileDescriptorWrapper w = fs.getFD(filePath);
             FileOutputStream fout = new FileOutputStream(w.open("rw"))) {

            if (logger.isRecording())
                logger.stopRecording(fout, true);
            else
                logger.write(fout, true);

            showSuccessToast(fs.getFilePath(filePath));

        } catch (IOException | UnknownUriException e) {
            Log.e(TAG, "Cannot save log: " + Log.getStackTraceString(e));

            return Result.failure();

        } finally {
            if (resume)
                logger.resume();
        }

        return Result.success();
    }

    private void showFailToast()
    {
        handler.post(() -> Toast.makeText(appContext,
                R.string.journal_save_log_failed,
                Toast.LENGTH_SHORT)
                .show()
        );
    }

    private void showSuccessToast(String fileName)
    {
        handler.post(() -> Toast.makeText(appContext,
                appContext.getString(R.string.journal_save_log_success, fileName),
                Toast.LENGTH_LONG)
                .show()
        );
    }
}
