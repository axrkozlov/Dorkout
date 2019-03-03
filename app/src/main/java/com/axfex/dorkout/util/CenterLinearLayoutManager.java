package com.axfex.dorkout.util;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class CenterLinearLayoutManager extends LinearLayoutManager {
    private static final float MILLISECONDS_PER_INCH = 360f;
    private Context mContext;
    private boolean scrollable = true;

    public CenterLinearLayoutManager(Context context) {
        super(context);
    }

    public CenterLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CenterLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(recyclerView.getContext());
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }



    private static class CenterSmoothScroller extends LinearSmoothScroller {

        CenterSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return (boxStart + (boxEnd - boxStart) /2) - (viewStart + (viewEnd - viewStart) / 2);
        }


        //This returns the milliseconds it takes to
        //scroll one pixel.
        @Override
        protected float calculateSpeedPerPixel
        (DisplayMetrics displayMetrics) {
            return MILLISECONDS_PER_INCH/displayMetrics.densityDpi;
        }
    }

}