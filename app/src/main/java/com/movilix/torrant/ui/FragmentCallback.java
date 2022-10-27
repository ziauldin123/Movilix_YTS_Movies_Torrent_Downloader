

package com.movilix.torrant.ui;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/*
 * The basic callback interface with codes and functions, returned by fragments.
 */

public interface FragmentCallback
{
    String TAG = FragmentCallback.class.getSimpleName();

    enum ResultCode {
        OK, CANCEL, BACK
    }

    void onFragmentFinished(@NonNull Fragment f, Intent intent, @NonNull ResultCode code);
}