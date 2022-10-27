package com.movilix.torrant.core.system;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.system.Os;
import android.system.StructStatVfs;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.NonNull;

class SafFsModule implements com.movilix.torrant.core.system.FsModule
{
    private Context appContext;

    public SafFsModule(@NonNull Context appContext)
    {
        this.appContext = appContext;
    }

    @Override
    public String getName(@NonNull Uri filePath)
    {
        com.movilix.torrant.core.system.SafFileSystem fs = com.movilix.torrant.core.system.SafFileSystem.getInstance(appContext);
        com.movilix.torrant.core.system.SafFileSystem.Stat stat = fs.stat(filePath);

        return (stat == null ? null : stat.name);
    }

    @Override
    public String getDirPath(@NonNull Uri dir)
    {
        com.movilix.torrant.core.system.SafFileSystem.Stat stat = com.movilix.torrant.core.system.SafFileSystem.getInstance(appContext).statSafRoot(dir);

        return (stat == null || stat.name == null ? dir.getPath() : stat.name);
    }

    @Override
    public String getFilePath(@NonNull Uri filePath)
    {
        com.movilix.torrant.core.system.SafFileSystem.Stat stat = com.movilix.torrant.core.system.SafFileSystem.getInstance(appContext).stat(filePath);

        return (stat == null || stat.name == null ? filePath.getPath() : stat.name);
    }

    @Override
    public Uri getFileUri(@NonNull Uri dir, @NonNull String fileName, boolean create)
    {
        return com.movilix.torrant.core.system.SafFileSystem.getInstance(appContext).getFileUri(dir, fileName, create);
    }

    @Override
    public Uri getFileUri(@NonNull String relativePath, @NonNull Uri dir)
    {
        return com.movilix.torrant.core.system.SafFileSystem.getInstance(appContext)
                .getFileUri(new com.movilix.torrant.core.system.SafFileSystem.FakePath(dir, relativePath), false);
    }

    @Override
    public boolean delete(@NonNull Uri filePath) throws FileNotFoundException
    {
        com.movilix.torrant.core.system.SafFileSystem fs = com.movilix.torrant.core.system.SafFileSystem.getInstance(appContext);

        return fs.delete(filePath);
    }

    @Override
    public com.movilix.torrant.core.system.FileDescriptorWrapper openFD(@NonNull Uri path)
    {
        return new com.movilix.torrant.core.system.FileDescriptorWrapperImpl(appContext, path);
    }

    @Override
    public long getDirAvailableBytes(@NonNull Uri dir) throws IOException
    {
        long availableBytes = -1;
        ContentResolver contentResolver = appContext.getContentResolver();
        com.movilix.torrant.core.system.SafFileSystem fs = com.movilix.torrant.core.system.SafFileSystem.getInstance(appContext);
        Uri dirPath = fs.makeSafRootDir(dir);

        try (ParcelFileDescriptor pfd = contentResolver.openFileDescriptor(dirPath, "r")) {
            if (pfd == null)
                return availableBytes;

            availableBytes = getAvailableBytes(pfd.getFileDescriptor());

        }

        return availableBytes;
    }

    /*
     * Return the number of bytes that are free on the file system
     * backing the given FileDescriptor
     *
     * TODO: maybe there is analog for KitKat?
     */

    @TargetApi(21)
    private long getAvailableBytes(@NonNull FileDescriptor fd) throws IOException
    {
        try {
            StructStatVfs stat = Os.fstatvfs(fd);

            return stat.f_bavail * stat.f_bsize;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public boolean fileExists(@NonNull Uri filePath)
    {
        return com.movilix.torrant.core.system.SafFileSystem.getInstance(appContext).exists(filePath);
    }

    @Override
    public long lastModified(@NonNull Uri filePath)
    {
        com.movilix.torrant.core.system.SafFileSystem.Stat stat = com.movilix.torrant.core.system.SafFileSystem.getInstance(appContext)
                .stat(filePath);

        return (stat == null ? -1 : stat.lastModified);
    }

    @Override
    public String makeFileSystemPath(@NonNull Uri uri, String relativePath)
    {
        return new com.movilix.torrant.core.system.SafFileSystem.FakePath(uri, (relativePath == null ? "" : relativePath))
                .toString();
    }

    @Override
    public Uri getParentDirUri(@NonNull Uri filePath)
    {
        return com.movilix.torrant.core.system.SafFileSystem.getInstance(appContext).getParentDirUri(filePath);
    }
}
