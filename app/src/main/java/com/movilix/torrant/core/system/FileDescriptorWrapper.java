

package com.movilix.torrant.core.system;

import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;

import androidx.annotation.NonNull;

public interface FileDescriptorWrapper extends Closeable
{
    FileDescriptor open(@NonNull String mode) throws FileNotFoundException;
}
