

package com.movilix.torrant.ui.errorreport;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;

import com.google.android.material.textfield.TextInputEditText;


import com.movilix.R;
import com.movilix.torrant.ui.BaseAlertDialog;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ErrorReportActivity extends AppCompatActivity
{
    private static final String TAG = ErrorReportActivity.class.getSimpleName();

    private static final String TAG_ERROR_DIALOG = "error_dialog";
    private ErrorReportDialog errDialog;
    private BaseAlertDialog.SharedViewModel dialogViewModel;
    private CompositeDisposable disposables = new CompositeDisposable();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        dialogViewModel = new ViewModelProvider(this).get(BaseAlertDialog.SharedViewModel.class);
        errDialog = (ErrorReportDialog)getSupportFragmentManager().findFragmentByTag(TAG_ERROR_DIALOG);

        if (errDialog == null) {
            errDialog = ErrorReportDialog.newInstance(
                    getString(R.string.error),
                    getString(R.string.app_error_occurred),
                    getStackTrace());

            errDialog.show(getSupportFragmentManager(), TAG_ERROR_DIALOG);
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();

        disposables.clear();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        subscribeAlertDialog();
    }

    private void subscribeAlertDialog()
    {
        Disposable d = dialogViewModel.observeEvents().subscribe(this::handleAlertDialogEvent);
        disposables.add(d);
    }

    private void handleAlertDialogEvent(BaseAlertDialog.Event event)
    {
        if (event.dialogTag == null || !event.dialogTag.equals(TAG_ERROR_DIALOG) || errDialog == null)
            return;
        switch (event.type) {
            case POSITIVE_BUTTON_CLICKED:
                Dialog dialog = errDialog.getDialog();
                if (dialog != null) {
                    TextInputEditText editText = dialog.findViewById(R.id.comment);
                    Editable e = editText.getText();
                    String comment = (e == null ? null : e.toString());


                    finish();
                }
                break;
            case NEGATIVE_BUTTON_CLICKED:

                finish();
                break;
        }
    }

    @Override
    public void finish()
    {
        if (errDialog != null)
            errDialog.dismiss();

        super.finish();
    }

    private String getStackTrace()
    {
        Intent i = getIntent();
        if (i == null)
            return null;





        return null;
    }
}
