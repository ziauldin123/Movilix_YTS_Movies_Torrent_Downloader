

package com.movilix.torrant.core.exception;

public class FileAlreadyExistsException extends Exception
{
    public FileAlreadyExistsException() { }

    public FileAlreadyExistsException(String message)
    {
        super(message);
    }
}
