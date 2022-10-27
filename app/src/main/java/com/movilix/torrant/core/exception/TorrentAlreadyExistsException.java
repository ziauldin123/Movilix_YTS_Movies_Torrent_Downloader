

package com.movilix.torrant.core.exception;

public class TorrentAlreadyExistsException extends Exception
{
    public TorrentAlreadyExistsException() { }

    public TorrentAlreadyExistsException(String message)
    {
        super(message);
    }
}
