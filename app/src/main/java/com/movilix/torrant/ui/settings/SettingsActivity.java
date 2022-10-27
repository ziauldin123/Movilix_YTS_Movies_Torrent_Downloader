

package com.movilix.torrant.ui.settings;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.movilix.R;
import com.movilix.torrant.core.utils.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

public class SettingsActivity extends AppCompatActivity
{
    private static final String TAG = SettingsActivity.class.getSimpleName();

    private Toolbar toolbar;
    private TextView detailTitle;
    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(Utils.getSettingsTheme(getApplicationContext()));
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.settings));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detailTitle = findViewById(R.id.detail_title);
        viewModel.detailTitleChanged.observe(this, title -> {
            if (title != null && detailTitle != null)
                detailTitle.setText(title);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }
}
