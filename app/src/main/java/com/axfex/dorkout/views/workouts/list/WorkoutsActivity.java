package com.axfex.dorkout.views.workouts.list;

import android.arch.lifecycle.ViewModelProviders;
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
import com.axfex.dorkout.util.BaseActivity;
import com.axfex.dorkout.vm.ViewModelFactory;
import com.axfex.dorkout.vm.WorkoutsViewModel;

import javax.inject.Inject;

public class WorkoutsActivity extends BaseActivity implements WorkoutsNavigator {
    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
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
        setContentView(R.layout.activity_workouts);

        ((WorkoutApplication) getApplication())
                .getAppComponent()
                .inject(this);

        workoutsViewModel=obtainViewModel();
        setupViewFragment(workoutsViewModel);
        setupToolbar();
        workoutsViewModel.onPick().observe(this, isPicked-> onPick(isPicked));

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
                showNameWorkoutDialog();
                break;
            }
            case android.R.id.home: {
                workoutsViewModel.unpick();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void onPick(Boolean isPicked) {
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

    private void showNameWorkoutDialog() {
        EditText workoutName = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("New Workout")
                .setView(workoutName)
                .setPositiveButton(R.string.bt_ok, (d, i) -> checkOutDialog(i, workoutName))
                .setNegativeButton(R.string.bt_cancel, (d, i) -> {
                })
                .create()
                .show();
    }

    private void checkOutDialog(int button, EditText workoutName) {
        String name = workoutName.getText().toString();
        workoutsViewModel.createWorkout(name).observe(this, id -> openEditWorkout(id));
    }



    private void setupViewFragment(WorkoutsViewModel workoutsViewModel){
        FragmentManager fragmentManager = getSupportFragmentManager();
        WorkoutsFragment fragment = (WorkoutsFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        fragment.attachViewModel(workoutsViewModel);
        if (fragment == null) {
            fragment = fragment.newInstance();
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
    public void openEditWorkout(Long id) {
        Toast.makeText(this, "Will be open Workout: "+id+" for edit!", Toast.LENGTH_SHORT).show();
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
