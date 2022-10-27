

package com.movilix.torrant.core.exception;

/*
 * Torrent file decode exception.
 */

public class DecodeException extends Exception
{
    public DecodeException() { }

    public DecodeException(String message)
    {
        super(message);
    }

    public DecodeException(Exception e)
    {
        super(e.getMessage());
        super.setStackTrace(e.getStackTrace());
    }
}
