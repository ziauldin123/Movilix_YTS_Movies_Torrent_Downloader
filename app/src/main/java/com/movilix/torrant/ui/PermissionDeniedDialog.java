

package com.movilix.torrant.ui;

import android.app.Dialog;
import android.os.Bundle;

import com.movilix.R;

import androidx.annotation.NonNull;

public class PermissionDeniedDialog extends com.movilix.torrant.ui.BaseAlertDialog {
    public static PermissionDeniedDialog newInstance() {
        PermissionDeniedDialog frag = new PermissionDeniedDialog();

        Bundle args = new Bundle();
        frag.setArguments(args);

        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);

        String title =  getString(R.string.perm_denied_title);
        String message = getString(R.string.perm_denied_warning);
        String positiveText = getString(R.string.yes);
        String negativeText = getString(R.string.no);

        return buildDialog(
                title,
                message,
                null,
                positiveText,
                negativeText,
                null,
                false
        );
    }
}
