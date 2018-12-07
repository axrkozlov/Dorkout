package com.axfex.dorkout.views.workouts.list;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.util.BaseActivity;
import com.axfex.dorkout.vm.ViewModelFactory;
import com.axfex.dorkout.vm.WorkoutsViewModel;

import javax.inject.Inject;

public class WorkoutsActivity extends BaseActivity implements WorkoutsNavigator {
    private static final String WORKOUTS_FRAG = "WORKOUTS_FRAG";
    private  WorkoutsViewModel workoutsViewModel;
    //private WorkoutsFragment workoutsFragment;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);

        ((WorkoutApplication) getApplication())
                .getAppComponent()
                .inject(this);

        setupViewModel();
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
        workoutsFragment.attachViewmodel(workoutsViewModel);

    }

    private void setupViewModel(){
        workoutsViewModel = ViewModelProviders.of(this, viewModelFactory).get(WorkoutsViewModel.class);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
