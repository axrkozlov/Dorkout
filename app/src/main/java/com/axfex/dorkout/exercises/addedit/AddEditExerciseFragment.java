package com.axfex.dorkout.exercises.addedit;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.exercises.list.ExercisesActivity;
import com.axfex.dorkout.util.ViewModelFactory;

import javax.inject.Inject;


public class AddEditExerciseFragment extends Fragment {

    private EditText mEditName;
    int workoutId;
    private static final String WORKOUT_ID = "workout_id";


    @Inject
    ViewModelFactory viewModelFactory;

    AddEditExerciseViewModel addEditExerciseViewModel;

    public static AddEditExerciseFragment newInstance(int workoutId) {
        AddEditExerciseFragment fragment = new AddEditExerciseFragment();
        Bundle args = new Bundle();
        args.putInt(WORKOUT_ID, workoutId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            workoutId = getArguments().getInt(WORKOUT_ID);
        }
        ((WorkoutApplication) getActivity().getApplication())
                .getAppComponent()
                .inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addEditExerciseViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddEditExerciseViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_add_edit_exercise, container, false);
        mEditName=v.findViewById(R.id.et_exercise_name);
        Button done=v.findViewById(R.id.bt_exercise_create);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exercise exercise=new Exercise(mEditName.getText().toString(),workoutId);
                addEditExerciseViewModel.addExercise(exercise);
                startExercisesActivity();
            }
        });


        return v;
    }
    private void startExercisesActivity() {
//        Intent i = new Intent(getActivity(), ExercisesActivity.class);
//        i.putExtra(WORKOUT_ID, workoutId);
//        startActivity(i);
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

}
