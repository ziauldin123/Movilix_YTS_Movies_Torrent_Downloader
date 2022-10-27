

package com.movilix.torrant.ui.tag;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.google.android.material.chip.Chip;

import com.movilix.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddTagButton extends Chip {
    public AddTagButton(@NonNull Context context) {
        super(context);

        init(context);
    }

    public AddTagButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public AddTagButton(
            @NonNull Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(@NonNull Context context) {
        setText(R.string.add_tag);
        setChipIconResource(R.drawable.ic_add_18);

        TypedArray a = context.obtainStyledAttributes(
                new TypedValue().data,
                new int[]{
                        R.attr.colorPrimary,
                        R.attr.colorOnPrimary,
                });
        setChipBackgroundColor(new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_enabled}},
                new int[]{a.getColor(0, Color.WHITE)})
        );
        setChipIconTint(new ColorStateList(
                new int[][]{new int[]{}},
                new int[]{a.getColor(1, Color.WHITE)})
        );
        setTextColor(a.getColor(1, Color.WHITE));
        a.recycle();
    }
}
