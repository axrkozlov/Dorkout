package com.axfex.dorkout.views.workouts.list;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.axfex.dorkout.R;
import com.axfex.dorkout.util.BaseActivity;

public class WorkoutsActivity extends BaseActivity implements WorkoutsNavigator {
    private static final String WORKOUTS_FRAG = "WORKOUTS_FRAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        setupViewFragment();

        setupToolbar();

    }

    private void setupViewFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        WorkoutsFragment workoutsFragment = (WorkoutsFragment) fragmentManager.findFragmentByTag(WORKOUTS_FRAG);

        if (workoutsFragment == null) {
            workoutsFragment = WorkoutsFragment.newInstance();
        }
        addFragmentToActivity(fragmentManager,
                workoutsFragment,
                R.id.contentFrame,
                WORKOUTS_FRAG);

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
    }

    @Override
    public void openEditWorkout(Long id) {

    }

    @Override
    public void startWorkout(Long id) {

    }

    @Override
    public void openAbout() {

    }

    @Override
    public void openSettings() {

    }

    @Override
    public void openDonate() {

    }
}
