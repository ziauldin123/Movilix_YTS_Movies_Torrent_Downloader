

package com.movilix.torrant.core.model.session;

import org.libtorrent4j.ErrorCode;

import androidx.annotation.NonNull;

class SessionErrors
{
    private static class Error
    {
        int errCode;
        String errMsg;

        Error(int errCode, String errMsg)
        {
            this.errCode = errCode;
            this.errMsg = errMsg;
        }
    }

    private static final Error[] errors = new Error[] {
            new Error(11, "Try again"),
            new Error(22, "Invalid argument"),
    };

    static boolean isNonCritical(@NonNull ErrorCode error)
    {
        if (!error.isError())
            return true;

        for (Error nonCriticalError : errors) {
            if (error.getValue() == nonCriticalError.errCode &&
                nonCriticalError.errMsg.equalsIgnoreCase(error.getMessage()))
                return true;
        }

        return false;
    }

    static String getErrorMsg(ErrorCode error)
    {
        return (error == null ? "" : error.getMessage() + ", code " + error.getValue());
    }
}
