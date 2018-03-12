package com.axfex.dorkout.views.exercises.addedit;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.vm.AddEditExerciseViewModel;
import com.axfex.dorkout.vm.ViewModelFactory;
import com.axfex.dorkout.vm.AddEditWorkoutViewModel;

import javax.inject.Inject;




public class AddEditExerciseFragment extends Fragment implements View.OnClickListener {

    private EditText mName;
    int workoutId;
    int exerciseId;
    private Workout mWorkout;
    private Integer mExercisesCount;

    private static final String WORKOUT_ID = "workout_id";


    @Inject
    ViewModelFactory viewModelFactory;

    AddEditExerciseViewModel addEditExerciseViewModel;
    AddEditWorkoutViewModel addEditWorkoutViewModel;

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
        addEditWorkoutViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddEditWorkoutViewModel.class);
        addEditExerciseViewModel.getExercisesCount(workoutId).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer exercisesCount) {
                if (exercisesCount != null) {
                    mExercisesCount=exercisesCount;
                }
            }
        });
        addEditExerciseViewModel.getExercise(exerciseId).observe(this, new Observer<Exercise>() {
            @Override
            public void onChanged(@Nullable Exercise exercise) {
                if (exercise != null) {
                     bindExerciseToEdit(exercise);
                }
            }
        });
        addEditWorkoutViewModel.getWorkout(workoutId).observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(@Nullable Workout workout) {
                if (workout != null) {
                    mWorkout=workout;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_add_edit_exercise, container, false);
        mName =v.findViewById(R.id.et_exercise_name);
        Button done=v.findViewById(R.id.bt_exercise_create);
        done.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_exercise_create:
                addExercise();
                break;
            case R.id.bt_workout_start_time:
                //showTimePickerDialog();
                break;
            case R.id.bt_workout_update:
                updateExercise();
                break;
            case R.id.bt_workout_delete:
                deleteExercise();
                break;
        }
    }

    private boolean checkNameField(){
        if (mName.getText().length() == 0) {
            Toast.makeText(getContext(), "Please, type Name of workout", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Exercise buildExercise(){
        return new Exercise(mName.getText().toString(),workoutId);
    }

    private void bindExerciseToEdit(Exercise exercise) {

        if (exercise == null) {
            return;
        }
    }

    private void addExercise(){
        if (!checkNameField()){
            return;
        }

        Exercise newExercise= buildExercise();
        if (newExercise == null) {
            return;
        }
        addEditExerciseViewModel.addExercise(newExercise);
        updateWorkout();
        close();
    }

    private void updateExercise(){

    }

    private void updateWorkout(){
        if (mWorkout == null) {
            return;
        }
        if (mExercisesCount == null) {
            mExercisesCount=0;
        }
        mWorkout.setExercisesCount(++mExercisesCount);
        addEditWorkoutViewModel.updateWorkout(mWorkout);
    }

    private void deleteExercise(){

    }


    private void close() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
