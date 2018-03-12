package com.axfex.dorkout.views.workouts.addedit;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.axfex.dorkout.R;
import com.axfex.dorkout.util.BaseActivity;

public class AddEditWorkoutActivity extends BaseActivity {
    public static final int REQUEST_ADD_TASK = 1;

    private static final String ADD_EDIT_FRAG = "ADD_EDIT_WORKOUT_FRAG";
    private static final String WORKOUT_ID = "workout_id";
    private int workoutId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_workout);

        Intent i = getIntent();
        if (i.hasExtra(WORKOUT_ID)) {

            workoutId = i.getIntExtra(WORKOUT_ID, 0);

            }

        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditWorkoutFragment addEditWorkoutFragment = (AddEditWorkoutFragment) fragmentManager.findFragmentByTag(ADD_EDIT_FRAG);

        if (addEditWorkoutFragment == null) {
            addEditWorkoutFragment = AddEditWorkoutFragment.newInstance(workoutId);
        }
        addFragmentToActivity(fragmentManager,
                addEditWorkoutFragment,
                R.id.activity_add_edit_workout,
                ADD_EDIT_FRAG);
    }
}
