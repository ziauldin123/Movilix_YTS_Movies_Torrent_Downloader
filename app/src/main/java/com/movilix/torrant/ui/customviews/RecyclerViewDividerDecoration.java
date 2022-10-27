

package com.movilix.torrant.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/*
 * Dividers for RecyclerView.
 */

public class RecyclerViewDividerDecoration extends RecyclerView.ItemDecoration
{
    private Drawable divider;

    public RecyclerViewDividerDecoration(Drawable divider)
    {
        this.divider = divider;
    }

    public RecyclerViewDividerDecoration(Context context, int dividerLayout)
    {
        divider = ContextCompat.getDrawable(context, dividerLayout);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state)
    {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}