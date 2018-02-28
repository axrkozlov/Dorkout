package com.axfex.dorkout.exercises.addedit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.axfex.dorkout.R;
import com.axfex.dorkout.util.BaseActivity;

public class AddEditExerciseActivity extends BaseActivity {

    private static final String ADD_EDIT_FRAG = "ADD_EDIT_EXERCISE_FRAG";
    private static final String WORKOUT_ID = "workout_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_exercise);

        Intent i = getIntent();
        if (i.hasExtra(WORKOUT_ID)) {

            int workoutId = i.getIntExtra(WORKOUT_ID, 0);

            FragmentManager fragmentManager = getSupportFragmentManager();
            AddEditExerciseFragment addEditExerciseFragment = (AddEditExerciseFragment) fragmentManager.findFragmentByTag(ADD_EDIT_FRAG);

            if (addEditExerciseFragment == null) {
                addEditExerciseFragment = AddEditExerciseFragment.newInstance(workoutId);
            }
            addFragmentToActivity(fragmentManager,
                    addEditExerciseFragment,
                    R.id.activity_add_edit_exercise,
                    ADD_EDIT_FRAG);
        }
        else{
            Toast.makeText(this, R.string.error_no_extra_found, Toast.LENGTH_LONG).show();
        }
    }

}
