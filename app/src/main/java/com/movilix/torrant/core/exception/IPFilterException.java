

package com.movilix.torrant.core.exception;

public class IPFilterException extends Exception
{
    public IPFilterException() { }

    public IPFilterException(String message)
    {
        super(message);
    }

    public IPFilterException(Exception e)
    {
        super(e);
    }
}
