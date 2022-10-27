

package com.movilix.torrant.ui.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.movilix.R;

/*
 * Display the selected color
 */

public class ColorView extends View {
    private static final float BORDER_WIDTH = 2.0f;

    private final Paint circlePaint;
    private final Paint borderPaint;
    private int w, h;

    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ColorView,
                0, 0
        );

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        try {
            circlePaint.setColor(a.getColor(R.styleable.ColorView_color, Color.WHITE));
        } finally {
            a.recycle();
        }
        circlePaint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        a = context.getTheme().obtainStyledAttributes(
                attrs,
                new int[]{R.attr.colorControlNormal},
                0, 0
        );
        try {
            borderPaint.setColor(a.getColor(0, Color.BLACK));
        } finally {
            a.recycle();
        }
        borderPaint.setStrokeWidth(BORDER_WIDTH);
        borderPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = (float) w / 2;
        float centerY = (float) h / 2;
        int width = getWidth() - (int) BORDER_WIDTH * 2;

        canvas.drawCircle(centerX, centerY, width / 2.0f, circlePaint);
        canvas.drawCircle(centerX, centerY, width / 2.0f, borderPaint);
    }

    public void setColor(int color) {
        circlePaint.setColor(color);

        invalidate();
        requestLayout();
    }

    public int getColor() {
        return circlePaint.getColor();
    }
}
