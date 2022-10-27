

package com.movilix.torrant.ui.settings.customprefs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.movilix.R;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

/*
 * A stub preference only for summary only.
 */

public class SummaryPreference extends Preference
{
    private TextView summaryView;

    public SummaryPreference(Context context)
    {
        this(context, null);
    }

    public SummaryPreference(Context context, AttributeSet attrs)
    {
        /* Use the preferenceStyle as the default style */
        this(context, attrs, R.attr.preferenceStyle);
    }

    public SummaryPreference(Context context, AttributeSet attrs, int defStyleAttr)
    {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public SummaryPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);

        setLayoutResource(R.layout.preference_summary);
        /* Icon stub */
        setIcon(android.R.color.transparent);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder)
    {
        super.onBindViewHolder(holder);

        summaryView = (TextView)holder.findViewById(R.id.summary);

        /* Disable click */
        holder.itemView.setClickable(false);
        holder.itemView.setFocusable(false);

        summaryView.setText(getSummary());
    }

    @Override
    public void setSummary(CharSequence summary)
    {
        super.setSummary(summary);

        summaryView.setText(summary);
    }

    @Override
    public void setSummary(int summaryResId)
    {
        super.setSummary(summaryResId);

        summaryView.setText(summaryResId);
    }
}
