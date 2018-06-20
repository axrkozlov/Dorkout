package com.axfex.dorkout.views.exercises.addedit;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
    private EditText mDesc;
    private Spinner mType;
    private AutoCompleteTextView mType1;
    private FloatingActionButton mDone;
    private Integer mExercisesCount;

    private Long workoutId;
    private Long exerciseId;
    private static final String WORKOUT_ID = "workout_id";
    private static final String EXERCISE_ID = "exercise_id";

    @Inject
    ViewModelFactory viewModelFactory;
    AddEditExerciseViewModel addEditExerciseViewModel;

    public static AddEditExerciseFragment newInstance(Long workoutId, Long exerciseId) {
        AddEditExerciseFragment fragment = new AddEditExerciseFragment();
        Bundle args = new Bundle();
        if (workoutId != null) {
            args.putLong(WORKOUT_ID, workoutId);
        }
        if (exerciseId != null) {
            args.putLong(EXERCISE_ID, exerciseId);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            workoutId = getArguments().getLong(WORKOUT_ID);
            exerciseId = getArguments().getLong(EXERCISE_ID);
        }
        ((WorkoutApplication) getActivity().getApplication())
                .getAppComponent()
                .inject(this);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addEditExerciseViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEditExerciseViewModel.class);

        if (exerciseId != 0) {
            addEditExerciseViewModel.getExercise(exerciseId).observe(this, exercise -> bindExercise(exercise));
        }

    }

    private void bindExercise(Exercise exercise) {
        mName.setText(exercise.getName());
        mDesc.setText(exercise.getDescription());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_edit_exercise, container, false);
        //mName = v.findViewById(R.id.et_exercise_name);
        mDesc = v.findViewById(R.id.et_exercise_desc);

//        mType=v.findViewById(R.id.sp_exercise_type);
        mType1=v.findViewById(R.id.at_exercise_type);

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(getContext(), R.array.exercise_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        mType.setPrompt("Select type");
        mType1.setAdapter(adapter);
        mType1.setOnTouchListener((v1, event) -> {
            mType1.showDropDown();
            return false;
        });
//        mType.setAdapter(adapter);
        v.findViewById(R.id.bt_exercise_create).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_exercise_create:
                addExercise();
                break;
            case R.id.bt_workout_update:
                updateExercise();
                break;
            case R.id.bt_workout_delete:
                deleteExercise();
                break;
        }
    }

    private boolean checkNameField() {
        if (mName.getText().length() == 0) {
            Toast.makeText(getContext(), "Please, type Name of workout", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Exercise buildExercise() {
        Exercise newExercise = new Exercise(mName.getText().toString(), workoutId);
        return newExercise;
    }


    private void addExercise() {
        if (!checkNameField()) return;
        addEditExerciseViewModel.addExercise(buildExercise());

        close();
    }

    private void updateExercise() {
    }

    private void deleteExercise() {
    }


    private void close() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}