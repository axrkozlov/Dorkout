package com.axfex.dorkout.views.workouts;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.util.BaseActivity;
import com.axfex.dorkout.views.exercises.list.ExercisesActivity;
import com.axfex.dorkout.vm.ViewModelFactory;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements WorkoutsNavigator {
    private static final String WORKOUTS_FRAGMENT_TAG = "WORKOUTS_FRAGMENT";
    private static final String EDIT_FRAGMENT_TAG = "EDIT_FRAGMENT";
    private static final String EXECUTION_FRAGMENT_TAG = "EXECUTION_FRAGMENT";

    private static final String WORKOUT_ID = "workout_id";

    @Inject
    public ViewModelFactory<MainViewModel> mViewModelFactory;

    public MainViewModel mMainViewModel;

    private ActionBar mActionBar;
//    private Menu mMenu;
    private Workout mWorkout;
    private boolean isWorkoutMenuShown=false;
    private boolean isHomeAsUpShown=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workouts_activity);

        ((WorkoutApplication) getApplication())
                .getAppComponent()
                .inject(this);
        setupToolbar();
        mMainViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel.class);
        mMainViewModel.getViewType().observe(this,this::onViewChange);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        assert mActionBar != null;
    }

    @Override
    public MainViewModel getViewModel() {
        return mMainViewModel;
    }

    private void onViewChange(MainViewModel.ViewState viewState){
        //Toast.makeText(this, viewState.toString(), Toast.LENGTH_SHORT).show();
//        WorkoutsFragment.attachViewModel(mMainViewModel);
//        EditWorkoutFragment.attachViewModel(mMainViewModel);
//        ExecutionFragment.attachViewModel(mMainViewModel);
        Log.i("VIEW_TYPE", "onViewChange: " + viewState);
        switch(viewState){
            case OPEN_WORKOUTS: {
                attachWorkoutsView();
                break;
            }
            case WORKOUTS:{
                isWorkoutMenuShown=false;
                isHomeAsUpShown=false;
                break;
            }
            case WORKOUT_SELECTION: {
                isWorkoutMenuShown=true;
                isHomeAsUpShown=true;
                break;
            }
            case OPEN_WORKOUT_MODIFICATION: {
                attachEditWorkoutView();
                isWorkoutMenuShown=true;
                isHomeAsUpShown=true;
                break;

            }
            case OPEN_EXECUTION:{
                attachExecutionWorkoutView();
                isWorkoutMenuShown=false;
                isHomeAsUpShown=false;
                break;
            }
        }
        mWorkout= mMainViewModel.getCurrentWorkout();
        //updateActionBar();
        //updateMenu();
    }

    private void attachWorkoutsView() {

        FragmentManager fragmentManager = getSupportFragmentManager();
//        WorkoutsFragment.attachViewModel(mMainViewModel);
        WorkoutsFragment fragment = (WorkoutsFragment) fragmentManager.findFragmentByTag(WORKOUTS_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = WorkoutsFragment.newInstance();
        }
        Log.i("ATTACH_WORKOUTS", "fragment.isAdded: " + fragment.isAdded());
        if (fragment.isAdded()) return;
        addFragmentToActivity(fragmentManager,
                fragment,
                R.id.contentFrame,
                WORKOUTS_FRAGMENT_TAG,
                false);

    }

    private void attachEditWorkoutView() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(EDIT_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = EditWorkoutFragment.newInstance();
        }
        Log.i("ATTACH_EDIT_WORKOUT", "fragment.isAdded: " + fragment.isAdded());
        if (fragment.isAdded()) return;
        addFragmentToActivity(fragmentManager,
                fragment,
                R.id.contentFrame,
                EDIT_FRAGMENT_TAG,
                true);
//        fragment.attachViewModel(mMainViewModel);
    }


    private void attachExecutionWorkoutView() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(EXECUTION_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = ExecutionFragment.newInstance();
        }
        if (fragment.isAdded()) return;
        addFragmentToActivity(fragmentManager,
                fragment,
                R.id.contentFrame,
                EXECUTION_FRAGMENT_TAG,
                true);
    }

    @Override
    public void onBackPressed() {
        if (mMainViewModel.getViewType().getValue()==MainViewModel.ViewState.WORKOUT_SELECTION)
        mMainViewModel.pickWorkout(null); else
            super.onBackPressed();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.menu_workouts, menu);
////        this.mMenu = menu;
////        updateMenu();
//        return super.onCreateOptionsMenu(menu);
//    }

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

    private void updateActionBar() {
            mActionBar.setDisplayHomeAsUpEnabled(isHomeAsUpShown);
            String title=mWorkout!=null? mWorkout.getName():getString(R.string.title_activity_workouts);
            mActionBar.setTitle(title);
    }

    private void updateMenu() {
//        if (mMenu == null) {
//            return;
//        }
//        mMenu.findItem(R.id.menu_workouts_add).setVisible(!isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_copy).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_edit).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_rename).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_delete).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_settings).setVisible(!isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_about).setVisible(!isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_donate).setVisible(!isWorkoutMenuShown);
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
            mMainViewModel.createWorkout(new Workout(name));
        } else {
            workout.setName(name);
            mMainViewModel.updateWorkout(workout);
        }
    }

    @Override
    public void deleteWorkout(Workout workout) {
        new AlertDialog.Builder(this)
                //TODO:make resource
                .setTitle("Delete " + workout.getName() + "?")
                .setPositiveButton(R.string.bt_ok, (d, i) -> mMainViewModel.deleteWorkout(workout))
                .setNegativeButton(R.string.bt_cancel, (d, i) -> {
                })
                .create()
                .show();
    }


    @Override
    public void openEditWorkout(Workout workout) {
        mMainViewModel.editWorkout(workout);
    }

    @Override
    public void onOpenWorkout(Workout workout) {
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
