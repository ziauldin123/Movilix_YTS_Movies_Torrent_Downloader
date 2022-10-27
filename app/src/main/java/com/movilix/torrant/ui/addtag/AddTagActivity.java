

package com.movilix.torrant.ui.addtag;

import android.content.Intent;
import android.os.Bundle;

import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import com.movilix.torrant.core.model.data.entity.TagInfo;
import com.movilix.torrant.core.utils.Utils;
import com.movilix.torrant.ui.FragmentCallback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

public class AddTagActivity extends AppCompatActivity
        implements FragmentCallback, ColorPickerDialogListener {
    private static final String TAG_DIALOG = "dialog";

    public static final String TAG_INIT_INFO = "init_info";

    private com.movilix.torrant.ui.addtag.AddTagDialog dialog;
    private com.movilix.torrant.ui.addtag.AddTagViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Utils.getTranslucentAppTheme(getApplicationContext()));
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(com.movilix.torrant.ui.addtag.AddTagViewModel.class);

        FragmentManager fm = getSupportFragmentManager();
        dialog = (com.movilix.torrant.ui.addtag.AddTagDialog) fm.findFragmentByTag(TAG_DIALOG);
        if (dialog == null) {
            TagInfo initInfo = getIntent().getParcelableExtra(TAG_INIT_INFO);
            dialog = com.movilix.torrant.ui.addtag.AddTagDialog.newInstance(initInfo);
            dialog.show(fm, TAG_DIALOG);
        }
    }

    @Override
    public void onFragmentFinished(
            @NonNull Fragment f,
            Intent intent,
            @NonNull ResultCode code
    ) {
        finish();
    }

    @Override
    public void onBackPressed() {
        dialog.onBackPressed();
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        viewModel.state.setColor(color);
    }

    @Override
    public void onDialogDismissed(int dialogId) {
    }
}