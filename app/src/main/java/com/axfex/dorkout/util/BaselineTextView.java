package com.axfex.dorkout.util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

public class BaselineTextView extends AppCompatTextView {

    public BaselineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int yOffset = getHeight() - getBaseline();
        canvas.translate(0, yOffset);
        super.onDraw(canvas);
    }

}