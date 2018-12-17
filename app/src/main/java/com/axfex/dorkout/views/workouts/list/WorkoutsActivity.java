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
import com.axfex.dorkout.util.BaseActivity;
import com.axfex.dorkout.views.exercises.list.ExercisesActivity;
import com.axfex.dorkout.views.workouts.edit.EditWorkoutActivity;
import com.axfex.dorkout.vm.ViewModelFactory;

import javax.inject.Inject;

public class WorkoutsActivity extends BaseActivity implements WorkoutsNavigator {
    private static final String FRAGMENT_TAG = "WORKOUTS_FRAGMENT";
    private static final String WORKOUT_ID = "workout_id";

    private  WorkoutsViewModel workoutsViewModel;
    private ActionBar mActionBar;
    private Menu menu;

    private Boolean isPicked =false;
    private String pickedName;
    private Long pickedId;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workouts_activity);

        ((WorkoutApplication) getApplication())
                .getAppComponent()
                .inject(this);

        workoutsViewModel=obtainViewModel();
        WorkoutsFragment.attachViewModel(workoutsViewModel);

        setupViewFragment();
        setupToolbar();
        workoutsViewModel.getPickEvent().observe(this, isPicked-> onPicked(isPicked));
        workoutsViewModel.getOpenWorkoutEvent().observe(this,id->openWorkout(id));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_workouts, menu);
        this.menu=menu;
        setupMenu(isPicked);
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
                workoutsViewModel.unpick();
                break;
            }
            case R.id.menu_workouts_edit: {
                openEditWorkout(pickedId);
                break;
            }
            case R.id.menu_workouts_rename: {
                renameWorkout(pickedName,pickedId);
                break;
            }
            case R.id.menu_workouts_delete: {
                deleteWorkout(pickedName,pickedId);
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void renameWorkout(String pickedName, Long pickedId) {

    }

    private void onPicked(Boolean isPicked) {
        this.isPicked =isPicked;
        pickedName=workoutsViewModel.getPickedName();
        pickedId=workoutsViewModel.getPickedId();
        updateActionBar();
        setupMenu(isPicked);
    }

    private void updateActionBar(){
        if (pickedName != null) {
            isPicked =true;
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(pickedName);
        } else {
            isPicked =false;
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setTitle(R.string.title_activity_workouts);
        }
    }

    private void setupMenu(Boolean isEdit){
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


    private void setupViewFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            fragment = WorkoutsFragment.newInstance();
        }
        addFragmentToActivity(fragmentManager,
                fragment,
                R.id.contentFrame,
                FRAGMENT_TAG);

    }

    private WorkoutsViewModel obtainViewModel(){
         return ViewModelProviders.of(this, viewModelFactory).get(WorkoutsViewModel.class);
    }


    @Override
    public void onBackPressed() {
        if (isPicked){
            workoutsViewModel.unpick();
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
                .setNegativeButton(R.string.bt_cancel,(d,i)->{})
                .create()
                .show();
    }

    private void onNewWorkoutDialogOk(String name) {
        workoutsViewModel.createWorkout(name);
    }

    @Override
    public void deleteWorkout(String name,Long id) {
        new AlertDialog.Builder(this)
                .setTitle("Delete " + name + "?")
                .setPositiveButton(R.string.bt_ok, (d,i) -> onDeleteWorkoutDialogOk(id))
                .setNegativeButton(R.string.bt_cancel,(d,i)->{})
                .create()
                .show();
    }
    private void onDeleteWorkoutDialogOk(Long id) {
        workoutsViewModel.deleteWorkout(id);
        workoutsViewModel.unpick();
    }


    @Override
    public void openEditWorkout(Long id) {
        Intent i = new Intent(this, EditWorkoutActivity.class);
        i.putExtra(WORKOUT_ID, id);
        startActivity(i);
        workoutsViewModel.unpick();
    }



    @Override
    public void openWorkout(Long id) {
        Intent i = new Intent(this, ExercisesActivity.class);
        i.putExtra(WORKOUT_ID, id);
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
