package com.axfex.dorkout.views.workouts;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.util.BaseActivity;
import com.axfex.dorkout.util.Show;
import com.axfex.dorkout.views.exercises.list.ExercisesActivity;
import com.axfex.dorkout.vm.ViewModelFactory;

import java.util.Currency;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements WorkoutsNavigator {
    private static final String TAG = "MAIN_ACTIVITY";
    private static final String WORKOUT_ID = "workout_id";

    private String currentShowTag;

    @Inject
    public ViewModelFactory<MainViewModel> mViewModelFactory;

    public MainViewModel mMainViewModel;

    private ActionBar mActionBar;
    //    private Menu mMenu;
    private Workout mWorkout;
    private boolean isWorkoutMenuShown = false;
    private boolean isHomeAsUpShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workouts_activity);

        ((WorkoutApplication) getApplication())
                .getAppComponent()
                .inject(this);
        setupToolbar();
        mMainViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel.class);
        mMainViewModel.getShow().observe(this, this::onShowChange);
        attachShow();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        assert mActionBar != null;
    }

    private void onShowChange(Show show) {
        Log.i("VIEW_TYPE", "onViewChange: " + show.getTag());
        attachShow(show.getTag());
    }


    @Override
    public MainViewModel getViewModel() {
        return mMainViewModel;
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        currentShowTag = fragment.getTag();
        Log.i(TAG, "currentShowTag: " + currentShowTag);
        super.onAttachFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
//    private void attachEditWorkoutView() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentByTag(EditWorkoutFragment.TAG);
//        if (fragment == null) {
//            fragment = EditWorkoutFragment.newInstance();
//        }
//        Log.i("ATTACH_EDIT_WORKOUT", "fragment.isAdded: " + fragment.isAdded());
//        if (fragment.isAdded()) return;
//        addFragmentToActivity(fragmentManager,
//                fragment,
//                R.id.contentFrame,
//                EditWorkoutFragment.TAG,
//                true);
////        fragment.attachViewModel(mMainViewModel);
//    }

    private void attachShow() {
        if (!hasAnyFragment()) attachShow(WorkoutsFragment.TAG);
    }

    private boolean hasAnyFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        return currentFragment != null;
    }

    private void attachShow(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        boolean isRootFragment=false;
        if (fragment == null)
            switch (tag) {
                case WorkoutsFragment.TAG: {
                    fragment = WorkoutsFragment.newInstance();
                    isRootFragment = true;
                    break;
                }
                case EditWorkoutFragment.TAG: {
                    fragment = EditWorkoutFragment.newInstance();
                    break;
                }
                case ActionWorkoutFragment.TAG: {
                    fragment = ActionWorkoutFragment.newInstance();
                    break;
                }
                default:
                    throw new IllegalArgumentException("Not valid Show Tag");
            }

//                if (fragment.isAdded()) return;
        addFragmentToActivity(getSupportFragmentManager(),
                fragment,
                R.id.contentFrame,
                tag,
                isRootFragment);
    }


//    @Override
//    public void onBackPressed() {
//        if (mMainViewModel.getViewType().getValue()==MainViewModel.ViewState.WORKOUT_SELECTION)
//        mMainViewModel.pickWorkout(null); else
//            super.onBackPressed();
//    }

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
        String title = mWorkout != null ? mWorkout.getName() : getString(R.string.title_activity_workouts);
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

    private void showNameDialog(@Nullable Workout workout) {

        EditText workoutEditName = new EditText(this);
        String dialogTitle;
        //TODO: create resource
        if (workout == null) {
            dialogTitle = "New Workout";
        } else {
            workoutEditName.setText(workout.getName());
            dialogTitle = "Rename";
        }
        new AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setView(workoutEditName)
                .setPositiveButton(R.string.bt_ok, (d, i) -> onNameDialogOk(workoutEditName.getText().toString(), workout))
                .setNegativeButton(R.string.bt_cancel, (d, i) -> {
                })
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
