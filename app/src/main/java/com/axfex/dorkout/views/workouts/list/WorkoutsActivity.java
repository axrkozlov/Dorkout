package com.axfex.dorkout.views.workouts.list;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.util.BaseActivity;
import com.axfex.dorkout.views.exercises.list.ExercisesActivity;

import javax.inject.Inject;

public class WorkoutsActivity extends BaseActivity implements WorkoutsNavigator {
    private static final String WORKOUTS_FRAGMENT_TAG = "WORKOUTS_FRAGMENT";
    private static final String EDIT_FRAGMENT_TAG = "EDIT_FRAGMENT";
    private static final String WORKOUT_ID = "workout_id";

    @Inject
    public WorkoutsViewModel mWorkoutsViewModel;
    private ActionBar mActionBar;
    private Menu mMenu;
    private Workout mWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workouts_activity);

        ((WorkoutApplication) getApplication())
                .getAppComponent()
                .inject(this);
        setupToolbar();

        mWorkoutsViewModel.getViewType().observe(this,this::onViewChange);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        assert mActionBar != null;
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mWorkoutsViewModel.getPickWorkoutEvent().observe(this, this::onPickWorkout);
//        mWorkoutsViewModel.getOpenWorkoutEvent().observe(this,this::onOpenWorkout);
    }

    private void onViewChange(WorkoutsViewModel.ViewType viewType){
        Toast.makeText(this, viewType.toString(), Toast.LENGTH_SHORT).show();
        switch(viewType){
            case WORKOUTS: {
                attachWorkoutsFragment();
                break;
            }
            case WORKOUT_SELECTION: {
                attachWorkoutsFragment();
                break;
            }
            case WORKOUT_MODIFICATION: {
                attachEditFragment();
                break;
            }
        }
        mWorkout=mWorkoutsViewModel.getWorkout();
        updateActionBar();
    }

    private void attachWorkoutsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        WorkoutsFragment.attachViewModel(mWorkoutsViewModel);
        WorkoutsFragment fragment = (WorkoutsFragment) fragmentManager.findFragmentByTag(WORKOUTS_FRAGMENT_TAG);
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
        //fragment.attachViewModel(mWorkoutsViewModel);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_workouts, menu);
        this.mMenu = menu;
        updateMenu();
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
                openEditWorkout(mWorkout);
                break;
            }
            case R.id.menu_workouts_rename: {
                renameWorkout(mWorkout);
                break;
            }
            case R.id.menu_workouts_delete: {
                deleteWorkout(mWorkout);
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void renameWorkout(Workout workout) {
        showNameDialog(workout);
    }

    private boolean isWorkoutMenuShown(){
        return mWorkout !=null;
    }

    private boolean isWorkoutPicked() {
        return mWorkout != null;
    }

    private void updateActionBar() {
        if (isWorkoutPicked()) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setTitle(mWorkout.getName());
        } else {
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setTitle(R.string.title_activity_workouts);
        }
        if (mMenu!=null) updateMenu();
    }

    private void updateMenu() {
        if (mMenu == null) {
            return;
        }
        mMenu.findItem(R.id.menu_workouts_add).setVisible(!isWorkoutMenuShown());
        mMenu.findItem(R.id.menu_workouts_copy).setVisible(isWorkoutMenuShown());
        mMenu.findItem(R.id.menu_workouts_edit).setVisible(isWorkoutMenuShown());
        mMenu.findItem(R.id.menu_workouts_rename).setVisible(isWorkoutMenuShown());
        mMenu.findItem(R.id.menu_workouts_delete).setVisible(isWorkoutMenuShown());
        mMenu.findItem(R.id.menu_settings).setVisible(!isWorkoutMenuShown());
        mMenu.findItem(R.id.menu_about).setVisible(!isWorkoutMenuShown());
        mMenu.findItem(R.id.menu_donate).setVisible(!isWorkoutMenuShown());
    }

    @Override
    public void onBackPressed() {
        if (isWorkoutPicked()) {
            mWorkoutsViewModel.pickWorkout(null);
            return;
        }
        super.onBackPressed();
    }


    @Override
    public void newWorkout() {
        showNameDialog(null);
    }

    private void showNameDialog(@Nullable Workout workout){

        EditText workoutEditName = new EditText(this);
        String dialogTitle;
        //TODO: create resource
        if (workout==null){
            dialogTitle="New Workout";
        } else {
            workoutEditName.setText(workout.getName());
            dialogTitle="Rename";
        }
        new AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setView(workoutEditName)
                .setPositiveButton(R.string.bt_ok, (d, i) -> onNameDialogOk(workoutEditName.getText().toString(),workout))
                .setNegativeButton(R.string.bt_cancel, (d, i) -> {})
                .create()
                .show();
    }


    private void onNameDialogOk(String name, Workout workout) {
        if (workout == null) {
            mWorkoutsViewModel.createWorkout(new Workout(name));
        } else {
            workout.setName(name);
            mWorkoutsViewModel.updateWorkout(workout);
        }
    }

    @Override
    public void deleteWorkout(Workout workout) {
        new AlertDialog.Builder(this)
                //TODO:make resource
                .setTitle("Delete " + workout.getName() + "?")
                .setPositiveButton(R.string.bt_ok, (d, i) -> mWorkoutsViewModel.deleteWorkout(workout))
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
//        mWorkoutsViewModel.unpick();
        mWorkoutsViewModel.editWorkout(workout);
//        mWorkoutsViewModel.unpick();
    }


    @Override
    public void onOpenWorkout(Workout workout) {
//        Log.i("Activity", "onOpenWorkout: ");
        Intent i = new Intent(this, ExercisesActivity.class);
        i.putExtra(WORKOUT_ID, workout.getId());
        startActivity(i);
//        mNavigator.onOpenWorkout(2L);

    }

    int i;
    void sendMessage(Workout workout){
        i++;
        Toast.makeText(this, "Event!", Toast.LENGTH_SHORT).show();
        if (i>=1) mWorkoutsViewModel.getOpenWorkoutEvent().removeObservers(this);
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
