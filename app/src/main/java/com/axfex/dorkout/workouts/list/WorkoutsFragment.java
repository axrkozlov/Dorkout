package com.axfex.dorkout.workouts.list;

import android.app.Activity;
import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.exercises.list.ExercisesActivity;
import com.axfex.dorkout.workouts.addedit.AddEditWorkoutActivity;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.util.ViewModelFactory;

import java.util.List;

import javax.inject.Inject;

public class WorkoutsFragment extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;

    private WorkoutsViewModel workoutsViewModel;
    private RecyclerView recyclerView;
    private LayoutInflater layoutInflater;
    private RecyclerView.Adapter adapter;
    private List<Workout> workouts;
    private static final String WORKOUT_ID="workout_id";
    public WorkoutsFragment() {
        // Required empty public constructor
    }

    public static WorkoutsFragment newInstance() {
        WorkoutsFragment fragment = new WorkoutsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WorkoutApplication) getActivity().getApplication())
                .getAppComponent()
                .inject(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        workoutsViewModel = ViewModelProviders.of(this, viewModelFactory).get(WorkoutsViewModel.class);
        workoutsViewModel.getWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(@Nullable List<Workout> workouts) {
                if (WorkoutsFragment.this.workouts == null) {
                    setListData(workouts);
                }
            }
        });
    }

    public void setListData(List<Workout> listOfData) {
        this.workouts = listOfData;
        adapter = new WorkoutsAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workouts, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = v.findViewById(R.id.rv_workouts);
        recyclerView.setLayoutManager(layoutManager);
        layoutInflater = getActivity().getLayoutInflater();
        FloatingActionButton fabulous = v.findViewById(R.id.fab_add_workout);
        fabulous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddEditActivity();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    public void startAddEditActivity() {
        startActivity(new Intent(getActivity(), AddEditWorkoutActivity.class));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsViewHolder> {


        @Override
        public WorkoutsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.workout_item, parent, false);
            WorkoutsViewHolder viewHolder = new WorkoutsViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(WorkoutsViewHolder holder, int position) {
            if (workouts.size() == 0) {
                return;
            }
            Workout workout = workouts.get(position);
            if (workout == null) {
                return;
            }
            holder.nameView.setText(workout.getName());
            holder.descView.setText(workout.getDescription());

        }

        @Override
        public int getItemCount() {
            if (workouts.size() > 0) {
                return workouts.size();
            } else {
                return 0;
            }

        }
    }

    private class WorkoutsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameView;
        TextView descView;
        TextView totalTimeView;
        TextView daysView;
        TextView startTimeView;

        public WorkoutsViewHolder(View itemView) {
            super(itemView);
//            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

            nameView = itemView.findViewById(R.id.workout_title);
            descView = itemView.findViewById(R.id.workout_desc);
            totalTimeView = itemView.findViewById(R.id.workout_desc_total_time);
            daysView = itemView.findViewById(R.id.workout_desc_days);
            startTimeView = itemView.findViewById(R.id.workout_desc_start_time);

        }

        @Override
        public void onClick(View v) {
            startExercisesActivity(workouts.get(this.getAdapterPosition()).getId(),v);
        }
    }

    private void startExercisesActivity(int workoutId,View viewRoot){
        Intent i = new Intent(getActivity(), ExercisesActivity.class);
        i.putExtra(WORKOUT_ID, workoutId);
        startActivity(i);
    }


}
