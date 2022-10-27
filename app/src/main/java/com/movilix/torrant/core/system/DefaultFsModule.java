package com.movilix.torrant.core.system;

import android.content.Context;
import android.net.Uri;
import android.os.StatFs;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;

class DefaultFsModule implements com.movilix.torrant.core.system.FsModule
{
    private Context appContext;

    public DefaultFsModule(@NonNull Context appContext)
    {
        this.appContext = appContext;
    }

    @Override
    public String getName(@NonNull Uri filePath)
    {
        return new File(filePath.getPath()).getName();
    }

    @Override
    public String getDirPath(@NonNull Uri dir)
    {
        return dir.getPath();
    }

    @Override
    public String getFilePath(@NonNull Uri filePath)
    {
        return filePath.getPath();
    }

    @Override
    public Uri getFileUri(@NonNull Uri dir, @NonNull String fileName, boolean create) throws IOException
    {
        File f = new File(dir.getPath(), fileName);
        if (create)
            f.createNewFile();

        return (f.exists() ? Uri.fromFile(f) : null);
    }

    @Override
    public Uri getFileUri(@NonNull String relativePath, @NonNull Uri dir)
    {
        if (!relativePath.startsWith(File.separator))
            relativePath = File.separator + relativePath;
        File f = new File(dir.getPath() + relativePath);

        return (f.exists() ? Uri.fromFile(f) : null);
    }

    @Override
    public boolean delete(@NonNull Uri filePath)
    {
        return new File(filePath.getPath()).delete();
    }

    @Override
    public com.movilix.torrant.core.system.FileDescriptorWrapper openFD(@NonNull Uri path)
    {
        return new com.movilix.torrant.core.system.FileDescriptorWrapperImpl(appContext, path);
    }

    @Override
    public long getDirAvailableBytes(@NonNull Uri dir)
    {
        long availableBytes;

        try {
            File file = new File(dir.getPath());
            availableBytes = file.getUsableSpace();

        } catch (Exception e) {
            /* This provides invalid space on some devices */
            StatFs stat = new StatFs(dir.getPath());
            availableBytes = stat.getAvailableBytes();
        }

        return availableBytes;
    }

    @Override
    public boolean fileExists(@NonNull Uri filePath)
    {
        return new File(filePath.getPath()).exists();
    }

    @Override
    public long lastModified(@NonNull Uri filePath)
    {
        return new File(filePath.getPath()).lastModified();
    }

    @Override
    public String makeFileSystemPath(@NonNull Uri uri, String relativePath)
    {
        return uri.getPath();
    }

    @Override
    public Uri getParentDirUri(@NonNull Uri filePath)
    {
        File parent = new File(filePath.getPath()).getParentFile();

        return (parent != null && parent.exists() ? Uri.fromFile(parent) : null);
    }
}
