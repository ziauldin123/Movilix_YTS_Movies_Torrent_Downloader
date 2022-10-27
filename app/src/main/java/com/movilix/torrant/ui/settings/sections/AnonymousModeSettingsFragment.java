

package com.movilix.torrant.ui.settings.sections;

import android.os.Bundle;

import com.takisoft.preferencex.PreferenceFragmentCompat;

import com.movilix.R;
import com.movilix.torrant.core.RepositoryHelper;
import com.movilix.torrant.core.settings.SettingsRepository;
import com.movilix.torrant.ui.settings.customprefs.SwitchBarPreference;

import androidx.preference.Preference;

public class AnonymousModeSettingsFragment extends PreferenceFragmentCompat
        implements
        Preference.OnPreferenceChangeListener
{
    private static final String TAG = AnonymousModeSettingsFragment.class.getSimpleName();

    private SettingsRepository pref;

    public static AnonymousModeSettingsFragment newInstance()
    {
        AnonymousModeSettingsFragment fragment = new AnonymousModeSettingsFragment();

        fragment.setArguments(new Bundle());

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        pref = RepositoryHelper.getSettingsRepository(getActivity().getApplicationContext());

        String keyAnonymousMode = getString(R.string.pref_key_anonymous_mode);
        SwitchBarPreference anonymousMode = findPreference(keyAnonymousMode);
        if (anonymousMode != null) {
            anonymousMode.setChecked(pref.anonymousMode());
            bindOnPreferenceChangeListener(anonymousMode);
        }
    }

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.pref_anonymous_mode, rootKey);
    }

    private void bindOnPreferenceChangeListener(Preference preference)
    {
        preference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        if (preference.getKey().equals(getString(R.string.pref_key_anonymous_mode)))
            pref.anonymousMode((boolean)newValue);

        return true;
    }
}
