

package com.movilix.torrant.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.takisoft.preferencex.PreferenceFragmentCompat;

import com.movilix.R;
import com.movilix.torrant.core.utils.Utils;
import com.movilix.torrant.ui.settings.sections.AppearanceSettingsFragment;
import com.movilix.torrant.ui.settings.sections.BehaviorSettingsFragment;
import com.movilix.torrant.ui.settings.sections.FeedSettingsFragment;
import com.movilix.torrant.ui.settings.sections.LimitationsSettingsFragment;
import com.movilix.torrant.ui.settings.sections.NetworkSettingsFragment;
import com.movilix.torrant.ui.settings.sections.SchedulingSettingsFragment;
import com.movilix.torrant.ui.settings.sections.StorageSettingsFragment;
import com.movilix.torrant.ui.settings.sections.StreamingSettingsFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;

public class SettingsFragment extends PreferenceFragmentCompat
{
    private static final String TAG = SettingsFragment.class.getSimpleName();

    private static final String AppearanceSettings = "AppearanceSettingsFragment";
    private static final String BehaviorSettings = "BehaviorSettingsFragment";
    private static final String NetworkSettings = "NetworkSettingsFragment";
    private static final String StorageSettings = "StorageSettingsFragment";
    private static final String LimitationsSettings = "LimitationsSettingsFragment";
    private static final String SchedulingSettings = "SchedulingSettingsFragment";
    private static final String FeedSettings = "FeedSettingsFragment";
    private static final String StreamingSettings = "StreamingSettingsFragment";

    private AppCompatActivity activity;
    private SettingsViewModel viewModel;

    public static SettingsFragment newInstance()
    {
        SettingsFragment fragment = new SettingsFragment();

        fragment.setArguments(new Bundle());

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        if (context instanceof AppCompatActivity)
            activity = (AppCompatActivity)context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (activity == null)
            activity = (AppCompatActivity)getActivity();

        viewModel = new ViewModelProvider(activity).get(SettingsViewModel.class);

        if (Utils.isTwoPane(activity)) {
            Fragment f = activity.getSupportFragmentManager()
                    .findFragmentById(R.id.detail_fragment_container);
            if (f == null)
                setFragment(AppearanceSettingsFragment.newInstance(),
                        getString(R.string.pref_header_appearance));
        }

        Preference appearance = findPreference(AppearanceSettingsFragment.class.getSimpleName());
        if (appearance != null)
            appearance.setOnPreferenceClickListener(prefClickListener);

        Preference behavior = findPreference(BehaviorSettingsFragment.class.getSimpleName());
        if (behavior != null)
            behavior.setOnPreferenceClickListener(prefClickListener);

        Preference storage = findPreference(StorageSettingsFragment.class.getSimpleName());
        if (storage != null)
            storage.setOnPreferenceClickListener(prefClickListener);

        Preference limitations = findPreference(LimitationsSettingsFragment.class.getSimpleName());
        if (limitations != null)
            limitations.setOnPreferenceClickListener(prefClickListener);

        Preference network = findPreference(NetworkSettingsFragment.class.getSimpleName());
        if (network != null)
            network.setOnPreferenceClickListener(prefClickListener);

        Preference scheduling = findPreference(SchedulingSettingsFragment.class.getSimpleName());
        if (scheduling != null)
            scheduling.setOnPreferenceClickListener(prefClickListener);

        Preference feed = findPreference(FeedSettingsFragment.class.getSimpleName());
        if (feed != null)
            feed.setOnPreferenceClickListener(prefClickListener);

        Preference streaming = findPreference(StreamingSettingsFragment.class.getSimpleName());
        if (streaming != null)
            streaming.setOnPreferenceClickListener(prefClickListener);
    }

    private Preference.OnPreferenceClickListener prefClickListener = (preference) -> {
        openPreference(preference.getKey());
        return true;
    };

    private void openPreference(String prefName)
    {
        switch (prefName) {
            case AppearanceSettings:
                if (Utils.isLargeScreenDevice(activity)) {
                    setFragment(AppearanceSettingsFragment.newInstance(),
                            getString(R.string.pref_header_appearance));
                } else {
                    startActivity(AppearanceSettingsFragment.class,
                            getString(R.string.pref_header_appearance));
                }
                break;
            case BehaviorSettings:
                if (Utils.isLargeScreenDevice(activity)) {
                    setFragment(BehaviorSettingsFragment.newInstance(),
                            getString(R.string.pref_header_behavior));
                } else {
                    startActivity(BehaviorSettingsFragment.class,
                            getString(R.string.pref_header_behavior));
                }
                break;
            case NetworkSettings:
                if (Utils.isLargeScreenDevice(activity)) {
                    setFragment(NetworkSettingsFragment.newInstance(),
                            getString(R.string.pref_header_network));
                } else {
                    startActivity(NetworkSettingsFragment.class,
                            getString(R.string.pref_header_network));
                }
                break;
            case StorageSettings:
                if (Utils.isLargeScreenDevice(activity)) {
                    setFragment(StorageSettingsFragment.newInstance(),
                            getString(R.string.pref_header_storage));
                } else {
                    startActivity(StorageSettingsFragment.class,
                            getString(R.string.pref_header_storage));
                }
                break;
            case LimitationsSettings:
                if (Utils.isLargeScreenDevice(activity)) {
                    setFragment(LimitationsSettingsFragment.newInstance(),
                            getString(R.string.pref_header_limitations));
                } else {
                    startActivity(LimitationsSettingsFragment.class,
                            getString(R.string.pref_header_limitations));
                }
                break;
            case SchedulingSettings:
                if (Utils.isLargeScreenDevice(activity)) {
                    setFragment(SchedulingSettingsFragment.newInstance(),
                            getString(R.string.pref_header_scheduling));
                } else {
                    startActivity(SchedulingSettingsFragment.class,
                            getString(R.string.pref_header_scheduling));
                }
                break;
            case FeedSettings:
                if (Utils.isLargeScreenDevice(activity)) {
                    setFragment(FeedSettingsFragment.newInstance(),
                            getString(R.string.pref_header_feed));
                } else {
                    startActivity(FeedSettingsFragment.class,
                            getString(R.string.pref_header_feed));
                }
                break;
            case StreamingSettings:
                if (Utils.isLargeScreenDevice(activity)) {
                    setFragment(StreamingSettingsFragment.newInstance(),
                            getString(R.string.pref_header_streaming));
                } else {
                    startActivity(StreamingSettingsFragment.class,
                            getString(R.string.pref_header_streaming));
                }
                break;
        }
    }

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.pref_headers, rootKey);
    }

    private <F extends PreferenceFragmentCompat> void setFragment(F fragment, String title)
    {
        viewModel.detailTitleChanged.setValue(title);

        if (Utils.isLargeScreenDevice(activity)) {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }

    private <F extends PreferenceFragmentCompat> void startActivity(Class<F> fragment, String title)
    {
        Intent i = new Intent(activity, com.movilix.torrant.ui.settings.PreferenceActivity.class);
        com.movilix.torrant.ui.settings.PreferenceActivityConfig config = new com.movilix.torrant.ui.settings.PreferenceActivityConfig(
                fragment.getSimpleName(),
                title);

        i.putExtra(com.movilix.torrant.ui.settings.PreferenceActivity.TAG_CONFIG, config);
        startActivity(i);
    }
}
