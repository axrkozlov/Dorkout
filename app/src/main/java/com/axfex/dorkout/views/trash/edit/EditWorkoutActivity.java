package com.axfex.dorkout.views.trash.edit;


import androidx.lifecycle.ViewModel;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

import com.axfex.dorkout.R;
import com.axfex.dorkout.util.BaseActivity;
import com.axfex.dorkout.views.workouts.EditWorkoutFragment;
import com.axfex.dorkout.vm.ViewModelFactory;

import javax.inject.Inject;

public class EditWorkoutActivity extends BaseActivity {
//    private static final String FRAGMENT_TAG = "EDIT_WORKOUT_FRAGMENT";
//    private static final String WORKOUT_ID = "workout_id";
//
//    private EditWorkoutViewModel editWorkoutViewModel;
//    private ActionBar mActionBar;
//    private Menu menu;
//
//    private Long workoutId;
//
//
//    @Inject
//    ViewModelFactory viewModelFactory;
//
//    @Override
//    public ViewModel getViewModel() {
//        return null;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public ViewModel getViewModel() {
        return null;
    }
}
//        setContentView(R.layout.edit_workout_activity);

//        ((WorkoutApplication) getApplication())
//                .getAppComponent()
//                .inject(this);


//        editWorkoutViewModel=obtainViewModel();
//        EditWorkoutFragment.attachViewModel(editWorkoutViewModel);
//
//        setupViewFragment();
//        workoutId=getWorkoutIdFromExtra();
//        setupToolbar(workoutId!=null);
//
//    }
//
//    private Long getWorkoutIdFromExtra(){
//        Intent i = getIntent();
//        if (i.hasExtra(WORKOUT_ID)) {
//            return i.getLongExtra(WORKOUT_ID, 0L);
//        }
//        return null;
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
//
//    private void setupToolbar(boolean isNewWorkout) {
//        Toolbar toolbar = findViewById(R.id.workouts_toolbar);
//        setSupportActionBar(toolbar);
//
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_cancel_24dp);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        if (isNewWorkout) {
//            actionBar.setTitle(R.string.title_activity_new_workout);
//        } else {
//            actionBar.setTitle(R.string.title_activity_edit_workout);
//        }
//    }
//
//    private void setupViewFragment(){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
//        if (fragment == null) {
//            fragment = EditWorkoutFragment.newInstance();
//        }
//        addFragmentToActivity(fragmentManager,
//                fragment,
//                R.id.contentFrame,
//                FRAGMENT_TAG,
//                true);
//    }

//    private EditWorkoutViewModel obtainViewModel(){
//        return ViewModelProviders.of(this, viewModelFactory).get(EditWorkoutViewModel.class);
//    }
//
//}
