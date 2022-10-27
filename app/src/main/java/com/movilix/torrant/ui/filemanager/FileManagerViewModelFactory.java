package com.movilix.torrant.ui.filemanager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class FileManagerViewModelFactory extends ViewModelProvider.NewInstanceFactory
{
    private final Application application;
    private final FileManagerConfig config;
    private final String startDir;

    public FileManagerViewModelFactory(@NonNull Application application,
                                       FileManagerConfig config,
                                       String startDir)
    {
        this.application = application;
        this.config = config;
        this.startDir = startDir;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        if (modelClass.isAssignableFrom(FileManagerViewModel.class))
            return (T)new FileManagerViewModel(application, config, startDir);

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
