package com.axfex.dorkout.workouts.list;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.axfex.dorkout.R;
import com.axfex.dorkout.util.BaseActivity;

public class WorkoutsActivity extends BaseActivity {
    private static final String WORKOUTS_FRAG = "WORKOUTS_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);


        FragmentManager fragmentManager = getSupportFragmentManager();
        WorkoutsFragment workoutsFragment = (WorkoutsFragment) fragmentManager.findFragmentByTag(WORKOUTS_FRAG);


        if (workoutsFragment == null) {
            workoutsFragment = WorkoutsFragment.newInstance();
        }
        addFragmentToActivity(fragmentManager,
                workoutsFragment,
                R.id.root_activity_workouts,
                WORKOUTS_FRAG);


    }
}
