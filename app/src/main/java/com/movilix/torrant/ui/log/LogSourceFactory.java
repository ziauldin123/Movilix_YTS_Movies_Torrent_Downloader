

package com.movilix.torrant.ui.log;

import com.movilix.torrant.core.logger.LogEntry;
import com.movilix.torrant.core.logger.Logger;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

class LogSourceFactory extends LogDataSource.Factory<Integer, LogEntry>
{
    private Logger logger;

    public LogSourceFactory(@NonNull Logger logger)
    {
        this.logger = logger;
    }

    @NonNull
    @Override
    public DataSource<Integer, LogEntry> create()
    {
        return new com.movilix.torrant.ui.log.LogDataSource(logger);
    }
}
