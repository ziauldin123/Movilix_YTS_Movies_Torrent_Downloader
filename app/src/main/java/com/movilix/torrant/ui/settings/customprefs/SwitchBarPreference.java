

package com.movilix.torrant.ui.settings.customprefs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import com.takisoft.preferencex.SwitchPreferenceCompat;

import com.movilix.R;
import com.movilix.torrant.ui.customviews.SwitchBar;

import androidx.preference.PreferenceViewHolder;

/*
 * A preference with SwitchBar like in Android settings.
 */

public class SwitchBarPreference extends SwitchPreferenceCompat
{
    private SwitchBar switchButton;

    public SwitchBarPreference(Context context)
    {
        this(context, null);
    }

    public SwitchBarPreference(Context context, AttributeSet attrs)
    {
        /* Use the preferenceStyle as the default style */
        this(context, attrs, R.attr.preferenceStyle);
    }

    public SwitchBarPreference(Context context, AttributeSet attrs, int defStyleAttr)
    {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public SwitchBarPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);

        setLayoutResource(R.layout.preference_switchbar);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder)
    {
        super.onBindViewHolder(holder);

        switchButton = (SwitchBar)holder.findViewById(R.id.switchButton);

        switchButton.setOnCheckedChangeListener(listener);
        switchButton.setChecked(isChecked());
    }

    private final CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if (!callChangeListener(isChecked)) {
                /*
                 * Listener didn't like it, change it back.
                 * CompoundButton will make sure we don't recurse.
                 */
                switchButton.setChecked(!isChecked);
                return;
            }

            setChecked(isChecked);
        }
    };
}
