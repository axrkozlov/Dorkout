package com.axfex.dorkout.views.workouts;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import androidx.fragment.app.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.navigation.NavigationEvent;
import com.axfex.dorkout.services.WorkoutPerformingManager;
import com.axfex.dorkout.services.WorkoutPerformingService;
import com.axfex.dorkout.util.BaseActivity;
import com.axfex.dorkout.views.settings.SettingsActivity;
import com.axfex.dorkout.vm.ViewModelFactory;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainNavigator {
    private static final String TAG = "MAIN_ACTIVITY";
    private static final String ROOT = "ROOT";

    private static final String WORKOUT_ID = "WORKOUT_ID";
    private static final String PERFORMING_WORKOUT_ID = "PERFORMING_WORKOUT_ID";

    private static final int CONTENT_FRAME = R.id.contentFrame;

    @Inject
    public ViewModelFactory<MainViewModel> mViewModelFactory;

    public MainViewModel mMainViewModel;

    private boolean mMainMenuShown = false;

    private Long mPerformingWorkoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ((WorkoutApplication) getApplication())
                .getAppComponent()
                .inject(this);

        setupToolbar();
//        showMainMenu();
        getSupportFragmentManager().addOnBackStackChangedListener(this::onBackStackChanged);


        mMainViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel.class);

        mPerformingWorkoutId = getPreferences(MODE_PRIVATE).getLong(PERFORMING_WORKOUT_ID, 0);

        showMainFragment();

    }

    @Override
    protected void onStart() {
        super.onStart();

        mMainViewModel.getNavigationEvent().observe(this, this::onNewNavigationEvent);
        mMainViewModel.getStartingWorkoutEvent().observe(this, this::onStartWorkout);
        mMainViewModel.getStoppingWorkoutEvent().observe(this, id->showStopWorkoutDialog());
    }

    private void onStartWorkout(Long id) {
        mPerformingWorkoutId = id;
        startPerformingService(id);
    }

    private void onStopWorkout(Long id) {
        stopPerformingService();

        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (mPerformingWorkoutId!=0) showStopWorkoutDialog();
        else super.onBackPressed();
    }

    private void showStopWorkoutDialog() {
        new AlertDialog.Builder(this)
                //TODO:make resource
                .setTitle("Exit workout?")
                .setPositiveButton(R.string.bt_ok, (d, i) -> onStopWorkoutDialogOk())
                .setNegativeButton(R.string.bt_cancel, (d, i) -> {
                })
                .create()
                .show();
    }

    private void onStopWorkoutDialogOk() {
        onStopWorkout(mPerformingWorkoutId);
    }


    private void startPerformingService(Long workoutId) {
        WorkoutPerformingService.startActionWorkoutService(this, workoutId);
        savePerformingWorkoutId(workoutId);
    }

    private void stopPerformingService() {
        WorkoutPerformingService.stopActionWorkoutService(this);
        clearPerformingWorkoutId();
    }

    private void savePerformingWorkoutId(Long performingWorkoutId){
        getPreferences(MODE_PRIVATE).edit().putLong(PERFORMING_WORKOUT_ID,performingWorkoutId).apply();
    }

    private void clearPerformingWorkoutId(){
        mPerformingWorkoutId=0L;
        getPreferences(MODE_PRIVATE).edit().remove(PERFORMING_WORKOUT_ID).apply();
    }

    private void showPerformingWorkout() {
        String tag = WorkoutFragment.TAG;

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null)
            fragment = WorkoutFragment.newInstance();

        addFragmentToActivity(getSupportFragmentManager(),
                fragment,
                CONTENT_FRAME,
                tag,
                false);
    }

    private void showWorkout(Long workoutId) {
        Log.i(TAG, "showWorkout: ");
        String tag = WorkoutFragment.TAG;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null)
            fragment = WorkoutFragment.newInstance();
        ((WorkoutFragment) fragment).setWorkoutId(workoutId);
        addFragmentToActivity(getSupportFragmentManager(),
                fragment,
                CONTENT_FRAME,
                tag,
                false);
    }

    private void showEditWorkout(Long workoutId) {
        String tag = EditWorkoutFragment.TAG;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null)
            fragment = EditWorkoutFragment.newInstance();
        ((EditWorkoutFragment) fragment).setWorkoutId(workoutId);
        addFragmentToActivity(getSupportFragmentManager(),
                fragment,
                CONTENT_FRAME,
                tag,
                false);
    }

    private void showWorkouts() {
        Log.i(TAG, "showWorkouts: ");
        String tag = WorkoutsFragment.TAG;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null)
            fragment = WorkoutsFragment.newInstance();


        addFragmentToActivity(getSupportFragmentManager(),
                fragment,
                CONTENT_FRAME,
                tag,
                true);
    }

//    private void onWorkoutStart(Long workoutId) {
//        startPerformingService(workoutId);
//        bindPerformingService();
//    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


//    private void onNewNavigationEvent(ShowEvent showEvent) {
//        attachShow(showEvent.getTag(), showEvent.getId());
//    }

    private void onNewNavigationEvent(NavigationEvent navigationEvent) {
        NavigationEvent.EventType eventType = navigationEvent.getEventType();
        switch (eventType) {
            case WORKOUTS: {
                showWorkouts();
                break;
            }
            case WORKOUT: {
                showWorkout(navigationEvent.getId());
                break;
            }
            case EDIT_WORKOUT: {
                showEditWorkout(navigationEvent.getId());
                break;
            }
//            case WORKOUT_PERFORMING: {
//                showPerformingWorkout(navigationEvent.getId());
//                break;
//            }
            default: {

            }
        }

    }


    @Override
    public MainViewModel getViewModel() {
        return mMainViewModel;
    }


    private void onBackStackChanged() {
        showMainMenu();
    }

    private void showMainFragment() {
        if (getCurrentShow() == null) {
            showWorkouts();
            if (mPerformingWorkoutId != 0) showPerformingWorkout();
        }

    }

    private void showMainMenu() {
        if (getCurrentShow() != null)
            mMainMenuShown = WorkoutsFragment.TAG.equals(getCurrentShow().getTag());
    }

    private Fragment getCurrentShow() {
        return getSupportFragmentManager().findFragmentById(CONTENT_FRAME);
    }

    private boolean isShow(String tag){
        if (getCurrentShow()==null) return false;
        return tag.equals(getCurrentShow().getTag());
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.setGroupVisible(R.id.main_menu, mMainMenuShown);
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


//    private void attachShow(String tag, @Nullable Long id) {
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
//        boolean isRootFragment = false;
//        switch (tag) {
//            case WorkoutsFragment.TAG: {
//                if (fragment == null)
//                    fragment = WorkoutsFragment.newInstance();
//                isRootFragment = true;
//                break;
//            }
//            case EditWorkoutFragment.TAG: {
//                if (fragment == null)
//                    fragment = EditWorkoutFragment.newInstance();
//                ((EditWorkoutFragment) fragment).setWorkoutId(id);
//                break;
//            }
//            case WorkoutFragment.TAG: {
//                if (fragment == null)
//                    fragment = WorkoutFragment.newInstance();
//                ((WorkoutFragment) fragment).setWorkoutId(id);
//                break;
//            }
//
//            default:
//                throw new IllegalArgumentException("Not valid Show Tag");
//        }
////                if (fragment.isAdded()) return;
//        addFragmentToActivity(getSupportFragmentManager(),
//                fragment,
//                CONTENT_FRAME,
//                tag,
//                isRootFragment);
//    }


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

