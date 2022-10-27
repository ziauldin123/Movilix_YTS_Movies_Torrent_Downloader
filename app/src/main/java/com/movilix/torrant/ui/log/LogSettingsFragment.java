

package com.movilix.torrant.ui.log;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;

import com.takisoft.preferencex.EditTextPreference;
import com.takisoft.preferencex.PreferenceFragmentCompat;

import com.movilix.R;
import com.movilix.torrant.core.InputFilterRange;
import com.movilix.torrant.core.RepositoryHelper;
import com.movilix.torrant.core.settings.SettingsRepository;

import androidx.preference.Preference;

public class LogSettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener
{
    private static final String TAG = LogSettingsFragment.class.getSimpleName();

    private SettingsRepository pref;

    public static LogSettingsFragment newInstance()
    {
        LogSettingsFragment fragment = new LogSettingsFragment();
        fragment.setArguments(new Bundle());

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Context context = getActivity().getApplicationContext();
        pref = RepositoryHelper.getSettingsRepository(context);

        InputFilter[] maxFilter = new InputFilter[] {
                new InputFilterRange.Builder()
                        .setMin(1)
                        .setMax(Integer.MAX_VALUE)
                        .build()
        };

        String keyMaxLogSize= getString(R.string.pref_key_max_log_size);
        EditTextPreference maxLogSize  = findPreference(keyMaxLogSize);
        if (maxLogSize != null) {
            String value = Integer.toString(pref.maxLogSize());
            maxLogSize.setOnBindEditTextListener((editText) -> editText.setFilters(maxFilter));
            maxLogSize.setSummary(value);
            maxLogSize.setText(value);
            bindOnPreferenceChangeListener(maxLogSize);
        }
    }

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.pref_log, rootKey);
    }

    private void bindOnPreferenceChangeListener(Preference preference)
    {
        preference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        if (preference.getKey().equals(getString(R.string.pref_key_max_log_size))) {
            int value = 1;
            if (!TextUtils.isEmpty((String)newValue))
                value = Integer.parseInt((String)newValue);
            pref.maxLogSize(value);
            preference.setSummary(Integer.toString(value));
        }

        return true;
    }
}
