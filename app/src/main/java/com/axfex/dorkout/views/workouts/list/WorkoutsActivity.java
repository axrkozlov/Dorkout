package com.axfex.dorkout.views.workouts.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.util.BaseActivity;
import com.axfex.dorkout.views.exercises.list.ExercisesActivity;
import com.axfex.dorkout.views.workouts.edit.EditWorkoutActivity;
import com.axfex.dorkout.vm.ViewModelFactory;

import javax.inject.Inject;

public class WorkoutsActivity extends BaseActivity implements WorkoutsNavigator {
    private static final String WORKOUTS_FRAGMENT_TAG = "WORKOUTS_FRAGMENT";
    private static final String EDIT_FRAGMENT_TAG = "EDIT_FRAGMENT";
    private static final String WORKOUT_ID = "workout_id";

    private WorkoutsViewModel workoutsViewModel;
    private ActionBar mActionBar;
    private Menu menu;

    private Workout mPickedWorkout;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workouts_activity);

        ((WorkoutApplication) getApplication())
                .getAppComponent()
                .inject(this);

        workoutsViewModel = obtainViewModel();
        WorkoutsFragment.attachViewModel(workoutsViewModel);

        setupViewFragment();
        setupToolbar();
        workoutsViewModel.getPickedWorkout().observe(this, this::onPickedWorkout);
        workoutsViewModel.getOpenWorkoutEvent().observe(this, this::openWorkout);

    }

    private void setupViewFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(WORKOUTS_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = WorkoutsFragment.newInstance();
        }
        addFragmentToActivity(fragmentManager,
                fragment,
                R.id.contentFrame,
                WORKOUTS_FRAGMENT_TAG,
                false);

    }

    private void attachEditFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(EDIT_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = EditWorkoutFragment.newInstance();
        }

        addFragmentToActivity(fragmentManager,
                fragment,
                R.id.contentFrame,
                EDIT_FRAGMENT_TAG,
                true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_workouts, menu);
        this.menu = menu;
        setupMenu(false);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_workouts_add: {
                newWorkout();
                break;
            }
            case android.R.id.home: {

                onBackPressed();
                break;
            }
            case R.id.menu_workouts_edit: {
                openEditWorkout(mPickedWorkout);
                break;
            }
            case R.id.menu_workouts_rename: {
                renameWorkout(mPickedWorkout);
                break;
            }
            case R.id.menu_workouts_delete: {
                deleteWorkout(mPickedWorkout);
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void renameWorkout(Workout workout) {

    }

    private void onPickedWorkout(Workout workout) {
        mPickedWorkout = workout;
        updateActionBar(workout);
        setupMenu(mPickedWorkout != null);
    }

    private boolean isWorkoutPicked() {
        return mPickedWorkout != null;
    }

    private void updateActionBar(Workout workout) {
        if (isWorkoutPicked()) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(workout.getName());
        } else {
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setTitle(R.string.title_activity_workouts);
        }
    }

    private void setupMenu(Boolean isEdit) {
        if (menu == null) {
            return;
        }
        menu.findItem(R.id.menu_workouts_add).setVisible(!isEdit);
        menu.findItem(R.id.menu_workouts_copy).setVisible(isEdit);
        menu.findItem(R.id.menu_workouts_edit).setVisible(isEdit);
        menu.findItem(R.id.menu_workouts_delete).setVisible(isEdit);
        menu.findItem(R.id.menu_settings).setVisible(!isEdit);
        menu.findItem(R.id.menu_about).setVisible(!isEdit);
        menu.findItem(R.id.menu_donate).setVisible(!isEdit);
    }


    private WorkoutsViewModel obtainViewModel() {
        return ViewModelProviders.of(this, viewModelFactory).get(WorkoutsViewModel.class);
    }


    @Override
    public void onBackPressed() {
        if (isWorkoutPicked()) {
            workoutsViewModel.pickWorkout(null);
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


    @Override
    public void newWorkout() {
        EditText workoutName = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("New Workout")
                .setView(workoutName)
                .setPositiveButton(R.string.bt_ok, (d, i) -> onNewWorkoutDialogOk(workoutName.getText().toString()))
                .setNegativeButton(R.string.bt_cancel, (d, i) -> {
                })
                .create()
                .show();
    }

    private void onNewWorkoutDialogOk(String name) {
        workoutsViewModel.createWorkout(name);
    }

    @Override
    public void deleteWorkout(Workout workout) {
        new AlertDialog.Builder(this)
                .setTitle("Delete " + workout.getName() + "?")
                .setPositiveButton(R.string.bt_ok, (d, i) -> workoutsViewModel.deleteWorkout(workout))
                .setNegativeButton(R.string.bt_cancel, (d, i) -> {
                })
                .create()
                .show();
    }


    @Override
    public void openEditWorkout(Workout workout) {
//        Intent i = new Intent(this, EditWorkoutActivity.class);
//        i.putExtra(WORKOUT_ID, id);
//        startActivity(i);
//        workoutsViewModel.unpick();
        attachEditFragment();
//        workoutsViewModel.unpick();
    }


    @Override
    public void openWorkout(Workout workout) {
        Intent i = new Intent(this, ExercisesActivity.class);
        i.putExtra(WORKOUT_ID, workout.getId());
        startActivity(i);
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
