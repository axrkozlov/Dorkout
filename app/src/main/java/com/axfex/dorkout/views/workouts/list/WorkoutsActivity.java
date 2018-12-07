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
    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    private  WorkoutsViewModel workoutsViewModel;
    private ActionBar mActionBar;
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

        workoutsViewModel=obtainViewModel();
        setupViewFragment(workoutsViewModel);
        setupToolbar();

        workoutsViewModel.onPickEvent().observe(this, isPicked->onSwitchEditMode(isPicked));


    }

    private void onSwitchEditMode(Boolean isEditMode) {
        mActionBar.setDisplayHomeAsUpEnabled(isEditMode);
    }

    private void setupViewFragment(WorkoutsViewModel viewModel){
        FragmentManager fragmentManager = getSupportFragmentManager();
        WorkoutsFragment fragment = (WorkoutsFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);

        if (fragment == null) {
            fragment = fragment.newInstance();
        }
        addFragmentToActivity(fragmentManager,
                fragment,
                R.id.contentFrame,
                FRAGMENT_TAG);
        fragment.attachViewmodel(viewModel);
    }

    private WorkoutsViewModel obtainViewModel(){
         return ViewModelProviders.of(this, viewModelFactory).get(WorkoutsViewModel.class);
    }


    @Override
    public void onBackPressed() {
        if (workoutsViewModel.isPicked()){
            workoutsViewModel.unPick();
            return;
        }
        super.onBackPressed();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();
        assert mActionBar != null;
    }

    private void onEditMode(){

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
