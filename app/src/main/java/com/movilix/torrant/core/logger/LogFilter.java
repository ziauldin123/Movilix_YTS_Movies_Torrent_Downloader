

package com.movilix.torrant.core.logger;

public interface LogFilter
{
    boolean apply(com.movilix.torrant.core.logger.LogEntry entry);
}
