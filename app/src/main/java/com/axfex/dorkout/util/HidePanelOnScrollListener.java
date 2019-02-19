package com.axfex.dorkout.util;

public class HidePanelOnScrollListener {

    //        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            private static final int SHOWED = -1;
//            private static final int HIDDEN = -2;
//            private int panelState = SHOWED;
//
//            private int DIRECTION_UP =-1;
//            private int DIRECTION_DOWN=1;
//            private int currentDirection=0;
//            private boolean determineDirection=false;
//            private float currentPosition=0;
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                int firstVisibleItem = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
//                if (determineDirection && dy>0) {
//                    currentDirection=DIRECTION_UP;
//                    determineDirection=false;
//                } else if (determineDirection && dy<0) {
//                    currentDirection = DIRECTION_DOWN;
//                    determineDirection=false;
//                }
//
//                Log.i(TAG, "dy: " + dy );
//
//                if (currentPosition==0) currentPosition = mScrollViewPanel.getY();
//                final float newPosition;
//                if (panelState == SHOWED)
//                    newPosition= Math.min(currentPosition + (float) dy /1.5F *currentDirection, currentPosition*1.5F);
//                else newPosition= Math.min(currentPosition - (float) dy /1.5F , mScrollViewPanel.getHeight());
//                currentPosition=(currentPosition+newPosition)/2;
//
//                final float visiblePartPercent = 1 - Math.abs(newPosition / mScrollViewPanel.getHeight());
//                final float bottom = Math.max(0, mScrollViewPanel.getHeight() + newPosition);
//                if (visiblePartPercent < 0) {
//                    panelState = HIDDEN;
//                    return;
//                }
//                final float newAlpha = visiblePartPercent;
//
//                mScrollViewPanel.setY(newPosition);
//                mScrollViewPanel.setAlpha(newAlpha);
//
//
//                final float newPositionRecyclerView = bottom;
//
//                recyclerView.setY(newPositionRecyclerView);
//
//                ViewParent viewparent = recyclerView.getParent();
//
//                final float parentHeight = ((ViewGroup) viewparent).getHeight();
//
//                ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
//                params.height = Math.min(params.height + (int) recyclerView.getY(), (int) parentHeight);
//                recyclerView.setLayoutParams(params);
//
//
//
//            }
//
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                final float visiblePartPercent = 1 - Math.abs(mScrollViewPanel.getY() / mScrollViewPanel.getHeight());
//                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                    determineDirection=true;
//                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    final float visibleThreshold;
//                    if (panelState == SHOWED)
//                        visibleThreshold = 0.8F;
//                    else visibleThreshold = 0.2F;
//
//                    if (visiblePartPercent > visibleThreshold) {
//                        mScrollViewPanel.animate().translationY(0);
//                        mScrollViewPanel.animate().alpha(1);
//                        mRecyclerView.animate().translationY(0);
//                        ViewParent viewparent = recyclerView.getParent();
//                        final float parentHeight = ((ViewGroup) viewparent).getHeight();
//                        ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
//                        params.height = (int) parentHeight - mScrollViewPanel.getHeight();
//                        recyclerView.setLayoutParams(params);
//                        Log.i(TAG, "onScrollStateChanged: " + params.height);
//                        panelState = SHOWED;
//                    } else {
//                        mScrollViewPanel.animate().translationY(-mScrollViewPanel.getHeight());
//                        mScrollViewPanel.animate().alpha(0);
//                        panelState = HIDDEN;
//
//                        ViewParent viewparent = recyclerView.getParent().getParent();
//                        final float parentHeight = ((ViewGroup) viewparent).getHeight();
//                        mRecyclerView.animate().y(0);
//                        ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
//
//                        params.height = (int) parentHeight;
//                        recyclerView.setLayoutParams(params);
//                        Log.i(TAG, "onScrollStateChanged: " + params.height);
//                    }
//
////                    if (Math.abs(mViewSwitcher.getBottom()/mViewSwitcher.getHeight())>0.5)
////                        Log.i(TAG, "onScrollStateChanged: Hide" + mScrollViewPanel.getY() + "/"+mViewSwitcher.getHeight());
//                }
//
//            }
//
//
//        });


}
