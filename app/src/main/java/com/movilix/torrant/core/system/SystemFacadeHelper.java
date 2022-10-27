

package com.movilix.torrant.core.system;

import android.content.Context;

import androidx.annotation.NonNull;

public class SystemFacadeHelper
{
    private static com.movilix.torrant.core.system.SystemFacade systemFacade;
    private static FileSystemFacade fileSystemFacade;

    public synchronized static com.movilix.torrant.core.system.SystemFacade getSystemFacade(@NonNull Context appContext)
    {
        if (systemFacade == null)
            systemFacade = new SystemFacadeImpl(appContext);

        return systemFacade;
    }

    public synchronized static FileSystemFacade getFileSystemFacade(@NonNull Context appContext)
    {
        if (fileSystemFacade == null)
            fileSystemFacade = new com.movilix.torrant.core.system.FileSystemFacadeImpl(appContext,
                    new FsModuleResolverImpl(appContext));

        return fileSystemFacade;
    }
}
