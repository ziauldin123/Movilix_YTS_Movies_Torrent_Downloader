package com.movilix.torrant.ui.addlink;

import android.content.Intent;
import android.os.Bundle;

import com.movilix.torrant.core.utils.Utils;
import com.movilix.torrant.ui.FragmentCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AddLinkActivity extends AppCompatActivity
        implements FragmentCallback
{
    private static final String TAG_ADD_LINK_DIALOG = "add_link_dialog";

    private AddLinkDialog addLinkDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        setTheme(Utils.getTranslucentAppTheme(getApplicationContext()));
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        addLinkDialog = (AddLinkDialog)fm.findFragmentByTag(TAG_ADD_LINK_DIALOG);
        if (addLinkDialog == null) {
            addLinkDialog = AddLinkDialog.newInstance();
            addLinkDialog.show(fm, TAG_ADD_LINK_DIALOG);
        }
    }

    @Override
    public void onFragmentFinished(@NonNull Fragment f, Intent intent, @NonNull ResultCode code)
    {
        finish();
    }

    @Override
    public void onBackPressed()
    {
        addLinkDialog.onBackPressed();
    }
}
