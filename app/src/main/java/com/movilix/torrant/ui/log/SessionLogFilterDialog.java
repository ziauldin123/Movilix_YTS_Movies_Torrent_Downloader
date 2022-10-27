

package com.movilix.torrant.ui.log;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.movilix.R;
import com.movilix.databinding.DialogLogFilterBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

public class SessionLogFilterDialog extends DialogFragment
{
    private static final String TAG = SessionLogFilterDialog.class.getSimpleName();

    private AlertDialog alert;
    private AppCompatActivity activity;
    private DialogLogFilterBinding binding;
    private com.movilix.torrant.ui.log.LogViewModel viewModel;

    public static SessionLogFilterDialog newInstance()
    {
        SessionLogFilterDialog frag = new SessionLogFilterDialog();

        Bundle args = new Bundle();
        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        if (context instanceof AppCompatActivity)
            activity = (AppCompatActivity)context;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        /* Back button handle */
        getDialog().setOnKeyListener((DialogInterface dialog, int keyCode, KeyEvent event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return true;
                } else {
                    onBackPressed();
                    return true;
                }
            } else {
                return false;
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        if (activity == null)
            activity = (AppCompatActivity) getActivity();

        viewModel = new ViewModelProvider(activity).get(com.movilix.torrant.ui.log.LogViewModel.class);

        LayoutInflater i = LayoutInflater.from(activity);
        binding = DataBindingUtil.inflate(i, R.layout.dialog_log_filter, null, false);
        binding.setViewModel(viewModel);

        initLayoutView(binding.getRoot());

        return alert;
    }

    private void initLayoutView(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(R.string.filter)
                .setNegativeButton(R.string.cancel, (dialog, which) -> onBackPressed())
                .setView(view);

        alert = builder.create();
    }

    private void onBackPressed()
    {
        alert.dismiss();
    }
}
