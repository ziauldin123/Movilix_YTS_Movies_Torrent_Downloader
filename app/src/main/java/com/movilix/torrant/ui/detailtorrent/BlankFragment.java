

package com.movilix.torrant.ui.detailtorrent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.movilix.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class BlankFragment extends Fragment
{
    private String text;
    public static BlankFragment newInstance(String text)
    {
        BlankFragment fragment = new BlankFragment();
        fragment.text = text;

        fragment.setArguments(new Bundle());

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_blank, container, false);
        TextView textView = v.findViewById(R.id.blank_text);
        textView.setText(text);

        return v;
    }
}
