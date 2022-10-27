package com.movilix.torrant.ui.filemanager;

import android.app.Application;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;


import com.movilix.R;


import com.movilix.torrant.core.exception.UnknownUriException;
import com.movilix.torrant.core.system.FileSystemFacade;
import com.movilix.torrant.core.system.SystemFacadeHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import io.reactivex.subjects.BehaviorSubject;

import static com.movilix.torrant.core.model.filetree.FileNode.Type.DIR;
import static com.movilix.torrant.core.model.filetree.FileNode.Type.FILE;
import static com.movilix.torrant.ui.filemanager.FileManagerConfig.FILE_CHOOSER_MODE;
import static com.movilix.torrant.ui.filemanager.FileManagerNode.PARENT_DIR;
import static com.movilix.torrant.ui.filemanager.FileManagerNode.ROOT_DIR;

public class FileManagerViewModel extends AndroidViewModel
{
    private static final String TAG = FileManagerViewModel.class.getSimpleName();

    private FileSystemFacade fs;
    public String startDir;
    /* Current directory */
    public ObservableField<String> curDir = new ObservableField<>();
    public FileManagerConfig config;
    public BehaviorSubject<List<FileManagerNode>> childNodes = BehaviorSubject.create();
    public Exception errorReport;

    public FileManagerViewModel(
            @NonNull Application application,
            FileManagerConfig config,
            String startDir
    ){
        super(application);

        this.config = config;
        this.fs = SystemFacadeHelper.getFileSystemFacade(application);
        this.startDir = startDir;

        String path = config.path;
        if (TextUtils.isEmpty(path)) {
            if (startDir != null) {
                File dir = new File(startDir);
                boolean accessMode = config.showMode == FileManagerConfig.FILE_CHOOSER_MODE ?
                        dir.canRead() :
                        dir.canWrite();
                if (!(dir.exists() && accessMode))
                    startDir = fs.getDefaultDownloadPath();
            } else {
                startDir = fs.getDefaultDownloadPath();
            }

        } else {
            startDir = path;
        }

        try {
            if (startDir != null) {
                startDir = new File(startDir).getCanonicalPath();
            }
            updateCurDir(startDir);

        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void refreshCurDirectory()
    {
        childNodes.onNext(getChildItems());
    }

    private void updateCurDir(String newPath)
    {
        if (newPath == null)
            return;
        curDir.set(newPath);
        childNodes.onNext(getChildItems());
    }

    /*
     * Get subfolders or files.
     */

    private List<FileManagerNode> getChildItems()
    {
        List<FileManagerNode> items = new ArrayList<>();
        String dir = curDir.get();
        if (dir == null)
            return items;

        try {
            File dirFile = new File(dir);
            if (!(dirFile.exists() && dirFile.isDirectory()))
                return items;

            /* Adding parent dir for navigation */
            if (!dirFile.getPath().equals(ROOT_DIR))
                items.add(0, new FileManagerNode(PARENT_DIR, DIR, true));

            File[] files = dirFile.listFiles();
            if (files == null)
                return items;
            for (File file : files) {
                if (file.isDirectory())
                    items.add(new FileManagerNode(file.getName(), DIR, true));
                else
                    items.add(new FileManagerNode(file.getName(), FILE,
                            config.showMode == FILE_CHOOSER_MODE));
            }

        } catch (Exception e) {
            /* Ignore */
        }

        return items;
    }

    public boolean createDirectory(String name)
    {
        if (TextUtils.isEmpty(name))
            return false;

        File newDir = new File(curDir.get(), name);

        return !newDir.exists() && newDir.mkdir();
    }

    public void openDirectory(String name) throws IOException, SecurityException
    {
        File dir = new File(curDir.get(), name);
        String path = dir.getCanonicalPath();

        if (!(dir.exists() && dir.isDirectory()))
            path = startDir;
        else if (!dir.canRead())
            throw new SecurityException("Permission denied");

        updateCurDir(path);
    }

    public void jumpToDirectory(String path) throws SecurityException
    {
        File dir = new File(path);

        if (!(dir.exists() && dir.isDirectory()))
            path = startDir;
        else if (!dir.canRead())
            throw new SecurityException("Permission denied");

        updateCurDir(path);
    }

    /*
     * Navigate back to an upper directory.
     */

    public void upToParentDirectory() throws SecurityException
    {
        String path = curDir.get();
        if (path == null)
            return;
        File dir = new File(path);
        File parentDir = dir.getParentFile();
        if (parentDir != null && !parentDir.canRead())
            throw new SecurityException("Permission denied");

        updateCurDir(dir.getParent());
    }

    public boolean fileExists(String fileName)
    {
        if (fileName == null)
            return false;

        fileName = appendExtension(fileName);

        return new File(curDir.get(), fileName).exists();
    }

    private String appendExtension(String fileName)
    {
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String extension = fs.getExtension(fileName);

        if (TextUtils.isEmpty(extension)) {
            extension = mimeTypeMap.getExtensionFromMimeType(config.mimeType);
        } else {
            String mimeType = mimeTypeMap.getMimeTypeFromExtension(extension);
            if (mimeType == null || !mimeType.equals(config.mimeType))
                extension = mimeTypeMap.getExtensionFromMimeType(config.mimeType);
        }

        if (extension != null && !fileName.endsWith(extension))
            fileName += fs.getExtensionSeparator() + extension;

        return fileName;
    }

    public Uri createFile(String fileName) throws SecurityException
    {
        if (TextUtils.isEmpty(fileName))
            fileName = config.fileName;

        fileName = appendExtension(fs.buildValidFatFilename(fileName));

        File f = new File(curDir.get(), fileName);
        File parent = f.getParentFile();
        if (parent != null && !parent.canWrite())
            throw new SecurityException("Permission denied");
        try {
            if (f.exists() && !f.delete())
                return null;
            if (!f.createNewFile())
                return null;

        } catch (IOException e) {
            return null;
        }

        return Uri.fromFile(f);
    }

    public Uri getCurDirectoryUri() throws SecurityException
    {
        String path = curDir.get();
        if (path == null)
            return null;

        File dir = new File(path);
        if (!(dir.canWrite() && dir.canRead()))
            throw new SecurityException("Permission denied");

        return Uri.fromFile(dir);
    }

    public Uri getFileUri(String fileName) throws SecurityException
    {
        String path = curDir.get();
        if (path == null)
            return null;

        File f = new File(path, fileName);
        if (!f.canRead())
            throw new SecurityException("Permission denied");

        return Uri.fromFile(f);
    }

    private List<Uri> getExtSdCardPaths() {
        List<Uri> uriList = new ArrayList<>();
        File[] externals = ContextCompat.getExternalFilesDirs(getApplication(), "external");
        File external = getApplication().getExternalFilesDir("external");
        for (File file : externals) {
            if (file != null && !file.equals(external)) {
                String absolutePath = file.getAbsolutePath();
                String path = getBaseSdCardPath(absolutePath);
                if (path == null || !checkSdCardPermission(new File(path), config)) {
                    path = getSdCardDataPath(absolutePath);
                    if (path == null || !checkSdCardPermission(new File(path), config)) {
                        Log.w(TAG, "Ext sd card path wrong: " + absolutePath);
                        continue;
                    }
                }
                uriList.add(Uri.parse("file://" + path));
            }
        }

        return uriList;
    }

    private String getBaseSdCardPath(String absolutePath) {
        int index = absolutePath.lastIndexOf("/Android/data");
        if (index >= 0) {
            return tryGetCanonicalPath(absolutePath.substring(0, index));
        } else {
            return null;
        }
    }

    private String getSdCardDataPath(String absolutePath) {
        int index = absolutePath.lastIndexOf("/external");
        if (index >= 0) {
            return tryGetCanonicalPath(absolutePath.substring(0, index));
        } else {
            return null;
        }
    }

    private String tryGetCanonicalPath(String absolutePath) {
        try {
            return new File(absolutePath).getCanonicalPath();
        } catch (IOException e) {
            // Keep non-canonical path.
            return absolutePath;
        }
    }

    private boolean checkSdCardPermission(File file, FileManagerConfig config) {
        switch (config.showMode) {
            case FileManagerConfig.FILE_CHOOSER_MODE:
                return file.canRead();
            case FileManagerConfig.DIR_CHOOSER_MODE:
                return file.canRead() && file.canWrite();
            case FileManagerConfig.SAVE_FILE_MODE:
                return file.canWrite();
        }

        throw new IllegalArgumentException("Unknown mode: " + config.showMode);
    }

    public List<FileManagerSpinnerAdapter.StorageSpinnerItem> getStorageList()
    {
        ArrayList<FileManagerSpinnerAdapter.StorageSpinnerItem> items = new ArrayList<>();
        List<Uri> storageList = getExtSdCardPaths();

        Uri primaryStorage = Uri.fromFile(Environment.getExternalStorageDirectory());
        try {
            items.add(new FileManagerSpinnerAdapter.StorageSpinnerItem(
                    getApplication().getString(R.string.internal_storage_name),
                    fs.getDirPath(primaryStorage),
                    fs.getDirAvailableBytes(primaryStorage))
            );
        } catch (UnknownUriException e) {
            e.printStackTrace();
        }

        if (!storageList.isEmpty()) {
            for (int i = 0; i < storageList.size(); i++) {
                String template = getApplication().getString(R.string.external_storage_name);
                try {
                    items.add(new FileManagerSpinnerAdapter.StorageSpinnerItem(
                            String.format(template, i + 1),
                            storageList.get(i).getPath(),
                            fs.getDirAvailableBytes(storageList.get(i)))
                    );
                } catch (UnknownUriException e) {
                    e.printStackTrace();
                }
            }
        }

        return items;
    }
}