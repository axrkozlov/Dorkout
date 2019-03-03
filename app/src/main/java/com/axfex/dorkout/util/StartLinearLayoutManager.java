package com.axfex.dorkout.util;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class StartLinearLayoutManager extends LinearLayoutManager {
    private static final float MILLISECONDS_PER_INCH = 350f;
    private Context mContext;
    private boolean scrollable=true;

    public StartLinearLayoutManager(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView,
                                       RecyclerView.State state, final int position) {

        LinearSmoothScroller smoothScroller =
                new LinearSmoothScroller(mContext) {

                    //This controls the direction in which smoothScroll looks
                    //for your view
                    @Override
                    public PointF computeScrollVectorForPosition
                    (int targetPosition) {
                        return StartLinearLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    //This returns the milliseconds it takes to
                    //scroll one pixel.
                    @Override
                    protected float calculateSpeedPerPixel
                    (DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH/displayMetrics.densityDpi;
                    }

                    @Override
                    protected int getVerticalSnapPreference() {
                        return SNAP_TO_START;
                    }


                };
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }



    @Override
    public boolean canScrollHorizontally() {
        return super.canScrollHorizontally() && scrollable;
    }

    @Override
    public boolean canScrollVertically() {
        return super.canScrollVertically()  && scrollable;
    }
}