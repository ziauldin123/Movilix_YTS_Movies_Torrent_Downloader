

package com.movilix.torrant.core.model.session;

import com.movilix.torrant.core.exception.IPFilterException;

import androidx.annotation.NonNull;

interface IPFilter
{
    void addRange(@NonNull String first, @NonNull String last) throws IPFilterException;
}
