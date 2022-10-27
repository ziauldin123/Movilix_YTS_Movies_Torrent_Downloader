

package com.movilix.torrant.ui.settings.sections;

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
import androidx.preference.SwitchPreferenceCompat;

public class StreamingSettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener
{
    private static final String TAG = StreamingSettingsFragment.class.getSimpleName();

    private SettingsRepository pref;

    public static StreamingSettingsFragment newInstance()
    {
        StreamingSettingsFragment fragment = new StreamingSettingsFragment();
        fragment.setArguments(new Bundle());

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        pref = RepositoryHelper.getSettingsRepository(getActivity().getApplicationContext());

        String keyEnable = getString(R.string.pref_key_streaming_enable);
        SwitchPreferenceCompat enable = findPreference(keyEnable);
        if (enable != null) {
            enable.setChecked(pref.enableStreaming());
            bindOnPreferenceChangeListener(enable);
        }

        String keyHostname = getString(R.string.pref_key_streaming_hostname);
        EditTextPreference hostname = findPreference(keyHostname);
        if (hostname != null) {
            String addressValue = pref.streamingHostname();
            hostname.setText(addressValue);
            hostname.setSummary(addressValue);
            bindOnPreferenceChangeListener(hostname);
        }

        String keyPort = getString(R.string.pref_key_streaming_port);
        EditTextPreference port = findPreference(keyPort);
        if (port != null) {
            InputFilter[] portFilter = new InputFilter[] { InputFilterRange.PORT_FILTER };
            int portNumber = pref.streamingPort();
            String portValue = Integer.toString(portNumber);
            port.setOnBindEditTextListener((editText) -> editText.setFilters(portFilter));
            port.setSummary(portValue);
            port.setText(portValue);
            bindOnPreferenceChangeListener(port);
        }
    }

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.pref_streaming, rootKey);
    }

    private void bindOnPreferenceChangeListener(Preference preference)
    {
        preference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        if (preference.getKey().equals(getString(R.string.pref_key_streaming_hostname))) {
            pref.streamingHostname((String)newValue);
            preference.setSummary((String)newValue);

        } else if (preference.getKey().equals(getString(R.string.pref_key_streaming_port))) {
            if (!TextUtils.isEmpty((String)newValue)) {
                int value = Integer.parseInt((String) newValue);
                pref.streamingPort(value);
                preference.setSummary(Integer.toString(value));
            }

        } else if (preference.getKey().equals(getString(R.string.pref_key_streaming_enable))) {
            pref.enableStreaming((boolean)newValue);
        }

        return true;
    }
}
