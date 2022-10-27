package com.movilix.torrant.core.system;

import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.NonNull;

/*
 * A platform dependent filesystem interface, that uses in FileSystemFacade.
 */

interface FsModule
{
    String getName(@NonNull Uri filePath);

    /*
     * Returns path (if present) or directory name
     */

    String getDirPath(@NonNull Uri dir);

    /*
     * Returns path (if present) or file name
     */

    String getFilePath(@NonNull Uri filePath);

    /*
     * Returns Uri of the file by the given file name or
     * null if the file doesn't exists
     */

    Uri getFileUri(@NonNull Uri dir, @NonNull String fileName, boolean create) throws IOException;

    /*
     * Returns a file (if exists) Uri by relative path (e.g foo/bar.txt)
     * from the pointed directory
     */

    Uri getFileUri(@NonNull String relativePath, @NonNull Uri dir);

    boolean delete(@NonNull Uri filePath) throws FileNotFoundException;

    com.movilix.torrant.core.system.FileDescriptorWrapper openFD(@NonNull Uri path);

    /*
     * Return the number of bytes that are free on the file system
     * backing the given Uri
     */

    long getDirAvailableBytes(@NonNull Uri dir) throws IOException;

    boolean fileExists(@NonNull Uri filePath);

    long lastModified(@NonNull Uri filePath);

    /*
     * If the uri is a file system path, returns the path as is
     */

    String makeFileSystemPath(@NonNull Uri uri, String relativePath);

    Uri getParentDirUri(@NonNull Uri filePath);
}
