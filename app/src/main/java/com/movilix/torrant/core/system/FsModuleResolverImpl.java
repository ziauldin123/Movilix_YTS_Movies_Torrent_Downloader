package com.movilix.torrant.core.system;

import android.content.Context;
import android.net.Uri;

import com.movilix.torrant.core.exception.UnknownUriException;
import com.movilix.torrant.core.utils.Utils;

import androidx.annotation.NonNull;

class FsModuleResolverImpl implements com.movilix.torrant.core.system.FsModuleResolver {
    private final Context appContext;
    private final SafFsModule safModule;
    private final com.movilix.torrant.core.system.DefaultFsModule defaultModule;

    public FsModuleResolverImpl(@NonNull Context appContext) {
        this.appContext = appContext;
        this.safModule = new SafFsModule(appContext);
        this.defaultModule = new com.movilix.torrant.core.system.DefaultFsModule(appContext);
    }

    @Override
    public com.movilix.torrant.core.system.FsModule resolveFsByUri(@NonNull Uri uri) throws UnknownUriException {
        if (Utils.isSafPath(appContext, uri)) {
            return safModule;
        } else if (Utils.isFileSystemPath(uri)) {
            return defaultModule;
        } else {
            throw new UnknownUriException("Cannot resolve file system for the given uri: " + uri);
        }
    }
}
