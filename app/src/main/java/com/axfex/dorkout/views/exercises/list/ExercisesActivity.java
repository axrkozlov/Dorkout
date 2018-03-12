package com.axfex.dorkout.views.exercises.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.axfex.dorkout.R;
import com.axfex.dorkout.util.BaseActivity;

public class ExercisesActivity extends BaseActivity {
    private static final String EXERCISES_FRAG = "EXERCISES_FRAG";
    private static final String WORKOUT_ID = "workout_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        Intent i = getIntent();

        if (i.hasExtra(WORKOUT_ID)) {
            int workoutId = i.getIntExtra(WORKOUT_ID,0);

            FragmentManager fragmentManager = getSupportFragmentManager();
            ExercisesFragment exercisesFragment = (ExercisesFragment) fragmentManager.findFragmentByTag(EXERCISES_FRAG);


            if (exercisesFragment == null) {
                exercisesFragment = ExercisesFragment.newInstance(workoutId);
            }
            addFragmentToActivity(fragmentManager,
                    exercisesFragment,
                    R.id.root_activity_exercises,
                    EXERCISES_FRAG);


        } else {
            Toast.makeText(this, R.string.error_no_extra_found, Toast.LENGTH_LONG).show();
        }

    }
}