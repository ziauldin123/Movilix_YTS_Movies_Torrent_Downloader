

package com.movilix.torrant.core.system;

import android.net.Uri;

import com.movilix.torrant.core.exception.UnknownUriException;

import androidx.annotation.NonNull;

/*
 * An FsModule provider.
 */

interface FsModuleResolver
{
    com.movilix.torrant.core.system.FsModule resolveFsByUri(@NonNull Uri uri) throws UnknownUriException;
}
