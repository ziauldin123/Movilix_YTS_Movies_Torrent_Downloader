

package com.movilix.torrant.core.system;

import android.net.Uri;

import com.movilix.torrant.core.exception.UnknownUriException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface FileSystemFacade
{
    com.movilix.torrant.core.system.FileDescriptorWrapper getFD(@NonNull Uri path) throws UnknownUriException;

    String getExtensionSeparator();

    @Nullable
    String getDefaultDownloadPath();

    @Nullable
    String getUserDirPath();

    boolean deleteFile(@NonNull Uri path) throws FileNotFoundException, UnknownUriException;

    Uri getFileUri(@NonNull Uri dir,
                   @NonNull String fileName) throws UnknownUriException;

    Uri getFileUri(@NonNull String relativePath,
                   @NonNull Uri dir) throws UnknownUriException;

    boolean fileExists(@NonNull Uri filePath) throws UnknownUriException;

    long lastModified(@NonNull Uri filePath) throws UnknownUriException;

    boolean isStorageWritable();

    boolean isStorageReadable();

    Uri createFile(@NonNull Uri dir,
                   @NonNull String fileName,
                   boolean replace) throws IOException, UnknownUriException;

    void write(@NonNull byte[] data,
               @NonNull Uri destFile) throws IOException, UnknownUriException;

    void write(@NonNull CharSequence data,
               @NonNull Charset charset,
               @NonNull Uri destFile) throws IOException, UnknownUriException;

    String makeFileSystemPath(@NonNull Uri uri) throws UnknownUriException;

    String makeFileSystemPath(@NonNull Uri uri,
                              String relativePath) throws UnknownUriException;

    long getDirAvailableBytes(@NonNull Uri dir) throws UnknownUriException;

    File getTempDir();

    void cleanTempDir() throws IOException;

    File makeTempFile(@NonNull String postfix);

    String getExtension(String fileName);

    boolean isValidFatFilename(String name);

    String buildValidFatFilename(String name);

    String normalizeFileSystemPath(String path);

    String getDirPath(@NonNull Uri dir) throws UnknownUriException;

    String getFilePath(@NonNull Uri filePath) throws UnknownUriException;

    Uri getParentDirUri(@NonNull Uri filePath) throws UnknownUriException;
}
