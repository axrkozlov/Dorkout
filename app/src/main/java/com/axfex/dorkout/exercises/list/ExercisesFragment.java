package com.axfex.dorkout.exercises.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.exercises.addedit.AddEditExerciseActivity;
import com.axfex.dorkout.util.ViewModelFactory;
import com.axfex.dorkout.workouts.addedit.AddEditWorkoutActivity;
import com.axfex.dorkout.workouts.list.WorkoutsFragment;

import java.util.List;

import javax.inject.Inject;

public class ExercisesFragment extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;

    private ExercisesViewModel exercisesViewModel;
    private RecyclerView recyclerView;
    private LayoutInflater layoutInflater;
    private RecyclerView.Adapter adapter;
    private List<Exercise> exercises;

    private static final String WORKOUT_ID = "workout_id";

    private int workoutId;
    private String mParam2;

    public ExercisesFragment() {
        // Required empty public constructor
    }

    public static ExercisesFragment newInstance(int workoutId) {
        ExercisesFragment fragment = new ExercisesFragment();
        Bundle args = new Bundle();
        args.putInt(WORKOUT_ID, workoutId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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



        exercisesViewModel = ViewModelProviders.of(this, viewModelFactory).get(ExercisesViewModel.class);
        exercisesViewModel.getExercises(workoutId).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exercises) {
                if (ExercisesFragment.this.exercises == null) {
                    setExercises(exercises);
                }
            }
        });
    }

    private void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        adapter = new ExercisesAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exercises, container,false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = v.findViewById(R.id.rv_exercises);
        recyclerView.setLayoutManager(layoutManager);
        layoutInflater = getActivity().getLayoutInflater();
        FloatingActionButton fabulous = v.findViewById(R.id.fab_add_exercise);
        fabulous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddEditExercise();
            }
        });
        return v;
    }

    private void startAddEditExercise() {
        Intent i = new Intent(getActivity(), AddEditExerciseActivity.class);
        i.putExtra(WORKOUT_ID, workoutId);
        startActivity(i);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class ExercisesAdapter extends RecyclerView.Adapter<ExercisesViewHolder> {

        @Override
        public ExercisesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.exercise_item, parent, false);
            ExercisesViewHolder viewHolder = new ExercisesViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ExercisesViewHolder holder, int position) {
            if (exercises.size() == 0) {
                return;
            }
            Exercise exercise = exercises.get(position);
            if (exercise == null) {
                return;
            }
            holder.nameView.setText(exercise.getName());
            holder.descView.setText(exercise.getDescription());

        }

        @Override
        public int getItemCount() {
            if (exercises.size() > 0) {
                return exercises.size();
            } else {
                return 0;
            }

        }
    }


    private class ExercisesViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView descView;
        public ExercisesViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.exercise_title);
            descView = itemView.findViewById(R.id.exercise_desc);

        }
    }

}
