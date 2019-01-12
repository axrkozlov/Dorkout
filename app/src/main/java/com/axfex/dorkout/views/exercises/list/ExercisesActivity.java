package com.axfex.dorkout.views.exercises.list;

import androidx.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.axfex.dorkout.R;
import com.axfex.dorkout.util.BaseActivity;

public class ExercisesActivity extends BaseActivity {
    private static final String EXERCISES_FRAG = "EXERCISES_FRAG";
    private static final String WORKOUT_ID = "workout_id";
    private static final String WORKOUT_NAME = "workout_name";
    private ExercisesFragment mExercisesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_activity);


        Intent i = getIntent();
//        if (i.hasExtra(WORKOUT_ID)) {
//            Long workoutId = i.getLongExtra(WORKOUT_ID,0L);
//
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            mExercisesFragment  = (ExercisesFragment) fragmentManager.findFragmentByTag(EXERCISES_FRAG);
//
//            if (mExercisesFragment == null) {
//                mExercisesFragment = ExercisesFragment.newInstance(workoutId);
//            }
//            addFragmentToActivity(fragmentManager,
//                    mExercisesFragment,
//                    R.id.contentFrame,
//                    EXERCISES_FRAG);
//
//        } else {
//            Toast.makeText(this, R.string.error_no_extra_found, Toast.LENGTH_LONG).show();
//        }

        setupToolbar();


    }

    @Override
    public ViewModel getViewModel() {
        return null;
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.workouts_toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
    }

}