

package com.movilix.torrant.ui.tag;

import android.content.Intent;
import android.os.Bundle;

import com.movilix.torrant.core.utils.Utils;
import com.movilix.torrant.ui.FragmentCallback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SelectTagActivity extends AppCompatActivity implements FragmentCallback {
    public static final String TAG_EXCLUDE_TAGS_ID = "exclude_tags_id";
    public static final String TAG_RESULT_SELECTED_TAG = "result_selected_tag";

    private static final String TAG_DIALOG = "dialog";

    private SelectTagDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Utils.getTranslucentAppTheme(getApplicationContext()));
        super.onCreate(savedInstanceState);


        FragmentManager fm = getSupportFragmentManager();
        dialog = (SelectTagDialog) fm.findFragmentByTag(TAG_DIALOG);
        if (dialog == null) {
            long[] excludeTagsId = getIntent().getLongArrayExtra(TAG_EXCLUDE_TAGS_ID);
            dialog = SelectTagDialog.newInstance(excludeTagsId);
            dialog.show(fm, TAG_DIALOG);
        }
    }

    @Override
    public void onFragmentFinished(
            @NonNull Fragment f,
            Intent intent,
            @NonNull ResultCode code
    ) {
        if (code == ResultCode.OK) {
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        dialog.onBackPressed();
    }
}