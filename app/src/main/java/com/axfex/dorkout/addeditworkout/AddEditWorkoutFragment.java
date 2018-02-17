package com.axfex.dorkout.addeditworkout;


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
import android.widget.ImageButton;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.util.ViewModelFactory;
import com.axfex.dorkout.workouts.WorkoutsActivity;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditWorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditWorkoutFragment extends Fragment {
    private EditText mEditName;
    @Inject
    ViewModelFactory viewModelFactory;

    AddEditWorkoutViewModel addEditWorkoutViewModel;


    public static AddEditWorkoutFragment newInstance() {
        AddEditWorkoutFragment fragment = new AddEditWorkoutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((WorkoutApplication) getActivity().getApplication())
                .getAppComponent()
                .inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addEditWorkoutViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddEditWorkoutViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_add_edit_workout, container, false);
        mEditName=v.findViewById(R.id.workout_create_name);
        Button done=v.findViewById(R.id.bt_workout_create);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Workout workout=new Workout(mEditName.getText().toString());
                addEditWorkoutViewModel.addWorkout(workout);
                startWorkoutsActivity();
            }
        });


        return v;
    }
    private void startWorkoutsActivity() {
        startActivity(new Intent(getActivity(), WorkoutsActivity.class));
    }
}
