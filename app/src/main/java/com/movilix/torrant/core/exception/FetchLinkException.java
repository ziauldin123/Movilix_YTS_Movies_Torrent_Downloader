

package com.movilix.torrant.core.exception;

/**
 * Torrent file or magnet fetch exception.
 */

public class FetchLinkException extends Exception
{
    public FetchLinkException() { }

    public FetchLinkException(String message)
    {
        super(message);
    }

    public FetchLinkException(Exception e)
    {
        super(e);
    }
}
