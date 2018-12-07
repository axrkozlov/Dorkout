package com.axfex.dorkout.views.workouts.edit;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.axfex.dorkout.R;
import com.axfex.dorkout.util.BaseActivity;

public class EditWorkoutActivity extends BaseActivity {
    public static final int REQUEST_ADD_TASK = 1;

    private static final String ADD_EDIT_FRAG = "ADD_EDIT_WORKOUT_FRAG";
    private static final String WORKOUT_ID = "workout_id";
    private Long workoutId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_workout);


        Intent i = getIntent();
        boolean isNewWorkout = true;
        if (i.hasExtra(WORKOUT_ID)) {
            workoutId = i.getLongExtra(WORKOUT_ID, 0L);
            isNewWorkout = false;
        }
        setupToolbar(isNewWorkout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        EditWorkoutFragment editWorkoutFragment = (EditWorkoutFragment) fragmentManager.findFragmentByTag(ADD_EDIT_FRAG);

        if (editWorkoutFragment == null) {
            editWorkoutFragment = EditWorkoutFragment.newInstance(workoutId);
        }
        addFragmentToActivity(fragmentManager,
                editWorkoutFragment,
                R.id.contentFrame,
                ADD_EDIT_FRAG);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupToolbar(boolean isNewWorkout) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.ic_cancel_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (isNewWorkout) {
            actionBar.setTitle(R.string.title_activity_new_workout);
        } else {
            actionBar.setTitle(R.string.title_activity_edit_workout);
        }


    }
}
