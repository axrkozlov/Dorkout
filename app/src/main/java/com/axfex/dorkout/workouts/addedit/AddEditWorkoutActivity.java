package com.axfex.dorkout.workouts.addedit;


import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.axfex.dorkout.R;
import com.axfex.dorkout.util.BaseActivity;

public class AddEditWorkoutActivity extends BaseActivity {
    private static final String ADD_EDIT_FRAG = "ADD_EDIT_WORKOUT_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_workout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEditWorkoutFragment addEditWorkoutFragment =(AddEditWorkoutFragment) fragmentManager.findFragmentByTag(ADD_EDIT_FRAG);

        if (addEditWorkoutFragment == null) {
            addEditWorkoutFragment = AddEditWorkoutFragment.newInstance();
        }
        addFragmentToActivity(fragmentManager,
                addEditWorkoutFragment,
                R.id.root_activity_add_edit_workout,
                ADD_EDIT_FRAG);
    }
}
