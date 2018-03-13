package com.axfex.dorkout.views.workouts.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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
import com.axfex.dorkout.views.exercises.list.ExercisesActivity;
import com.axfex.dorkout.util.DateUtils;
import com.axfex.dorkout.vm.WorkoutsViewModel;
import com.axfex.dorkout.views.workouts.addedit.AddEditWorkoutActivity;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.vm.ViewModelFactory;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
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
                    setListData(workouts);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //TODO:show message ok
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

        return v;
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
            View view = inflater.inflate(R.layout.item_workout, parent, false);
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
            holder.mName.setText(workout.getName());
            holder.mDesc.setText(workout.getDescription());
            String[] weekDaysText = DateFormatSymbols.getInstance().getShortWeekdays();
            ArrayList<Boolean> weekDays = DateUtils.parseWeekDays(workout.getWeekDaysComposed());
            StringBuffer sb = new StringBuffer("");

            for (int i = 0; i < 7; i++) {
                if (weekDays.get(i)) {
                    sb.append(weekDaysText[i + 1] + " ");
                }
            }
            Integer exercisesCount=workout.getExercisesCount();
            if (exercisesCount != null) {
                holder.mExercises.setText(exercisesCount.toString());
            }

            holder.mDays.setText(sb.toString());
            holder.mStartTime.setText(DateUtils.getTimeString(workout.getStartTime()));
            holder.mTotalTime.setText(DateUtils.getTimeString(workout.getTotalTime()));
            holder.itemView.setTag(workout.getId());

        }

        @Override
        public int getItemCount() {
            return workouts.size();

        }
    }

    private class WorkoutsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView mName;
        TextView mDesc;
        TextView mExercises;
        TextView mTotalTime;
        TextView mDays;
        TextView mStartTime;

        public WorkoutsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

            mName = itemView.findViewById(R.id.workout_title);
            mDesc = itemView.findViewById(R.id.workout_desc);
            mExercises = itemView.findViewById(R.id.workout_desc_exercises);
            mTotalTime = itemView.findViewById(R.id.workout_desc_total_time);
            mDays = itemView.findViewById(R.id.workout_desc_days);
            mStartTime = itemView.findViewById(R.id.workout_desc_start_time);

        }

        @Override
        public void onClick(View v) {
            startExercisesActivity(workouts.get(this.getAdapterPosition()).getId());
        }

        @Override
        public boolean onLongClick(View v) {
            startAddEditActivity(workouts.get(this.getAdapterPosition()).getId());
            return true;
        }
    }

    private void startExercisesActivity(int workoutId){
        Intent i = new Intent(getActivity(), ExercisesActivity.class);
        i.putExtra(WORKOUT_ID, workoutId);
        startActivity(i);
    }

    public void startAddEditActivity() {
        startActivityForResult(
                new Intent(getActivity(), AddEditWorkoutActivity.class),AddEditWorkoutActivity.REQUEST_ADD_TASK);
    }

    private void startAddEditActivity(int workoutId){
        Intent i = new Intent(getActivity(), AddEditWorkoutActivity.class);
        i.putExtra(WORKOUT_ID, workoutId);
        startActivityForResult(i,AddEditWorkoutActivity.REQUEST_ADD_TASK);
    }

}
