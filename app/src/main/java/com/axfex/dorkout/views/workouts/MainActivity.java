package com.axfex.dorkout.views.workouts;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.util.BaseActivity;
import com.axfex.dorkout.util.ShowEvent;
import com.axfex.dorkout.views.settings.SettingsActivity;
import com.axfex.dorkout.vm.ViewModelFactory;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainNavigator {
    private static final String TAG = "MAIN_ACTIVITY";
    private static final String WORKOUT_ID = "workout_id";
    private static final int CONTENT_FRAME = R.id.contentFrame;

    @Inject
    public ViewModelFactory<MainViewModel> mViewModelFactory;

    public MainViewModel mMainViewModel;

    private boolean mIsMainMenuShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workouts_activity);

        ((WorkoutApplication) getApplication())
                .getAppComponent()
                .inject(this);
        setupToolbar();
        attachRootShowIfEmpty();
        showMainMenuIfRoot();
        getSupportFragmentManager().addOnBackStackChangedListener(this::onBackStackChanged);
        mMainViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel.class);
        mMainViewModel.getShowEvent().observe(this, this::onShowEvent);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void onShowEvent(ShowEvent showEvent) {
        attachShow(showEvent.getTag(), showEvent.getId());
    }

    @Override
    public MainViewModel getViewModel() {
        return mMainViewModel;
    }


    private void onBackStackChanged() {
         showMainMenuIfRoot();
    }

    private void attachRootShowIfEmpty() {
        if (getCurrentShow() == null) {
            attachShow(WorkoutsFragment.TAG, null);
            mIsMainMenuShown=true;
        }
    }

    private void showMainMenuIfRoot(){
        if (getCurrentShow()!=null) mIsMainMenuShown = WorkoutsFragment.TAG.equals(getCurrentShow().getTag());
    }

    private Fragment getCurrentShow() {
        return getSupportFragmentManager().findFragmentById(CONTENT_FRAME);
    }

    private void attachShow(String tag, @Nullable Long id) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        boolean isRootFragment = false;
        switch (tag) {
            case WorkoutsFragment.TAG: {
                if (fragment == null)
                    fragment = WorkoutsFragment.newInstance();
                isRootFragment = true;
                break;
            }
            case EditWorkoutFragment.TAG: {
                if (fragment == null)
                    fragment = EditWorkoutFragment.newInstance();
                ((EditWorkoutFragment) fragment).setWorkoutId(id);
                break;
            }
            case ActionWorkoutFragment.TAG: {
                if (fragment == null)
                    fragment = ActionWorkoutFragment.newInstance();
                break;
            }
            default:
                throw new IllegalArgumentException("Not valid Show Tag");
        }
//                if (fragment.isAdded()) return;
        addFragmentToActivity(getSupportFragmentManager(),
                fragment,
                CONTENT_FRAME,
                tag,
                isRootFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.setGroupVisible(R.id.main_menu, mIsMainMenuShown);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings: {
                onOpenSettings();
            }
            case R.id.menu_donate: {

            }
            case R.id.menu_about: {

            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOpenSettings() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    @Override
    public void onOpenAbout() {

    }

    @Override
    public void onOpenDonate() {

    }
}

//    @Override
//    public void onBackPressed() {
//        if (mMainViewModel.getViewType().getValue()==MainViewModel.ViewState.WORKOUT_SELECTION)
//        mMainViewModel.pickWorkout(null); else
//            super.onBackPressed();
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.workouts_menu, menu);
////        this.mMenu = menu;
////        updateMenu();
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_workouts_add: {
//                onNewWorkout();
//                break;
//            }
//            case android.R.id.home: {
//                onBackPressed();
//                break;
//            }
//            case R.id.menu_workouts_edit: {
//                onOpenEditWorkout(mWorkout);
//                break;
//            }
//            case R.id.menu_workouts_rename: {
//                onRenameWorkout(mWorkout);
//                break;
//            }
//            case R.id.menu_workouts_delete: {
//                onDeleteWorkout(mWorkout);
//                break;
//            }
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


//    @Override
//    public void onRenameWorkout(Workout workout) {
//        showNameDialog(workout);
//    }
//
//    private void updateActionBar() {
//        mActionBar.setDisplayHomeAsUpEnabled(isHomeAsUpShown);
//        String title = mWorkout != null ? mWorkout.getName() : getString(R.string.title_activity_workouts);
//        mActionBar.setTitle(title);
//    }
//
//    private void updateMenu() {
//        if (mMenu == null) {
//            return;
//        }
//        mMenu.findItem(R.id.menu_workouts_add).setVisible(!isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_copy).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_edit).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_rename).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_delete).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.settings_menu).setVisible(!isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_about).setVisible(!isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_donate).setVisible(!isWorkoutMenuShown);
//    }
//
//
//    @Override
//    public void onNewWorkout() {
//        showNameDialog(null);
//    }

//    private void showNameDialog(@Nullable Workout workout) {
//
//        EditText workoutEditName = new EditText(this);
//        String dialogTitle;
//        //TODO: create resource
//        if (workout == null) {
//            dialogTitle = "New Workout";
//        } else {
//            workoutEditName.setText(workout.getName());
//            dialogTitle = "Rename";
//        }
//        new AlertDialog.Builder(this)
//                .setTitle(dialogTitle)
//                .setView(workoutEditName)
//                .setPositiveButton(R.string.bt_ok, (d, i) -> onNameDialogOk(workoutEditName.getText().toString(), workout))
//                .setNegativeButton(R.string.bt_cancel, (d, i) -> {
//                })
//                .create()
//                .show();
//    }


//    private void onNameDialogOk(String name, Workout workout) {
//        if (workout == null) {
//            mMainViewModel.createWorkoutLD(new Workout(name));
//        } else {
//            workout.setName(name);
//            mMainViewModel.updateWorkout(workout);
//        }
//    }
//
//    @Override
//    public void onDeleteWorkout(Workout workout) {
//        new AlertDialog.Builder(this)
//                //TODO:make resource
//                .setTitle("Delete " + workout.getName() + "?")
//                .setPositiveButton(R.string.bt_ok, (d, i) -> mMainViewModel.onDeleteWorkout(workout))
//                .setNegativeButton(R.string.bt_cancel, (d, i) -> {
//                })
//                .create()
//                .show();
//    }
//
//
//    @Override
//    public void onOpenEditWorkout(Workout workout) {
//        mMainViewModel.openEditWorkout(workout);
//    }
//
//    @Override
//    public void onOpenActionWorkout(Workout workout) {
//        Intent i = new Intent(this, ExercisesActivity.class);
//        i.putExtra(WORKOUT_ID, workout.getId());
//        startActivity(i);
//    }
//
//
//    @Override
//    public void onOpenAbout() {
//
//    }
//
//    @Override
//    public void onOpenSettings() {
//
//    }
//
//    @Override
//    public void onOpenDonate() {
//
//    }

