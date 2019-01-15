package com.axfex.dorkout.views.trash.edit.exercises.addedit;

import androidx.lifecycle.ViewModel;

import com.axfex.dorkout.util.BaseActivity;

public class AddEditExerciseActivity extends BaseActivity {
    public static final int REQUEST_ADD_TASK = 2;

    private static final String ADD_EDIT_FRAG = "ADD_EDIT_EXERCISE_FRAG";
    private static final String WORKOUT_ID = "workout_id";
    private static final String EXERCISE_ID = "exercise_id";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.edit_exercise_activity);
//
//        Intent i = getIntent();
//        if (i.hasExtra(WORKOUT_ID)) {
//
//            Long workoutId = i.getLongExtra(WORKOUT_ID, 0L);
//            Long exerciseId = i.getLongExtra(EXERCISE_ID, 0L);
//
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            AddEditExerciseFragment addEditExerciseFragment = (AddEditExerciseFragment) fragmentManager.findFragmentByTag(ADD_EDIT_FRAG);
//
//            if (addEditExerciseFragment == null) {
//                addEditExerciseFragment = AddEditExerciseFragment.newInstance(workoutId,exerciseId);
//            }
//            addFragmentToActivity(fragmentManager,
//                    addEditExerciseFragment,
//                    R.id.activity_add_edit_exercise,
//                    ADD_EDIT_FRAG,
//                    true);
//        }
//        else{
//            Toast.makeText(this, R.string.error_no_extra_found, Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    public ViewModel getViewModel() {
        return null;
    }
}
