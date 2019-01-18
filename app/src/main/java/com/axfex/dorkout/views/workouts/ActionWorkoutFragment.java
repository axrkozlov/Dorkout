package com.axfex.dorkout.views.workouts;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.services.ActionWorkoutService;
import com.axfex.dorkout.vm.ViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

public class ActionWorkoutFragment extends Fragment {
    public static final String TAG = "ACTION_WORKOUT_FRAGMENT";
    private static final String WORKOUT_ID = "WORKOUT_ID";
    @Inject
    ViewModelFactory<ActionWorkoutViewModel> mViewModelFactory;
    private Menu mMenu;
    private RecyclerView mRecyclerView;
    private ExercisesAdapter mAdapter;
    private MainViewModel mMainViewModel;
    private ActionWorkoutViewModel mActionWorkoutViewModel;
    private Long mWorkoutId;
    private Workout mWorkout;
    private List<Exercise> mExercises;
    private FloatingActionButton mStartWorkout;
    private FloatingActionButton mStopWorkout;

    public static ActionWorkoutFragment newInstance() {
        ActionWorkoutFragment fragment = new ActionWorkoutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (mWorkoutId == null)
            if (getArguments() != null) {
                mWorkoutId = getArguments().getLong(WORKOUT_ID);
            } else {
                throw new IllegalArgumentException("Not have a workout id for edit");
            }
        ((WorkoutApplication) Objects.requireNonNull(getActivity()).getApplication())
                .getAppComponent()
                .inject(this);
    }

    public void setWorkoutId(Long id) {
        Log.i(TAG, "setWorkoutId: " + id);
        mWorkoutId = id;
        Bundle args = new Bundle();
        args.putLong(WORKOUT_ID, id);
        setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.action_workout_fragment, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = v.findViewById(R.id.rv_exercises);
        mRecyclerView.setLayoutManager(layoutManager);
        mStartWorkout=v.findViewById(R.id.fab_start_workout);
        mStartWorkout.setOnClickListener( view -> ActionWorkoutService.startActionWorkoutService(getContext(),mWorkout,mExercises));
        mStopWorkout=v.findViewById(R.id.fab_stop_workout);
        mStopWorkout.setOnClickListener(view -> ActionWorkoutService.stopWorkout(getContext()));
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionWorkoutViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ActionWorkoutViewModel.class);
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mActionWorkoutViewModel.getWorkout(mWorkoutId).observe(this, this::onWorkoutLoaded);
        mActionWorkoutViewModel.getExercises(mWorkoutId).observe(this, this::onExerciseListLoaded);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.action_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeAsUp: {
            }
        }
        return super.onOptionsItemSelected(item);

    }

    private void onWorkoutLoaded(Workout workout) {
        mWorkout = workout;
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    private void onExerciseListLoaded(List<Exercise> exercises) {
        this.mExercises = exercises;
        if (mAdapter == null) setupAdapter();
        else notifyAdapter();
    }

    private void setupAdapter() {
        mAdapter = new ExercisesAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void notifyAdapter() {
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
    }

    private class ExercisesAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {

        @NonNull
        @Override
        public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.edit_workout_exercise_item, parent, false);
            return new ExerciseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
            if (mExercises.size() == 0) {
                return;
            }
            holder.setExercise(mExercises.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mExercises.size();
        }

    }

    private class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView mName;
        TextView mDesc;

        View mInfoBar;
        TextView mNormTime;
        TextView mRestTime;
        TextView mOrderNumber;
        ImageButton mOrderButton;
        Exercise mExercise;
        int mPosition;
        TextView setsView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.exercise_name);
            mDesc = itemView.findViewById(R.id.exercise_desc);
            mInfoBar = itemView.findViewById(R.id.exercise_info_bar);
            mNormTime = mInfoBar.findViewById(R.id.norm_time);
            mRestTime = mInfoBar.findViewById(R.id.rest_time);
            mOrderNumber = itemView.findViewById(R.id.exercise_order);
            mOrderButton = itemView.findViewById(R.id.exercise_collapse);
            setsView = itemView.findViewById(R.id.exercise_sets);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setExercise(Exercise exercise, int position) {
            mExercise = exercise;
            mPosition = position;
            bindData();
        }

        void bindData() {
//            final boolean isExpanded = mPosition == mExpandedPosition ;
//            mOrderButton.setImageResource(isExpanded ? R.drawable.ic_collapse_24dp : R.drawable.ic_expand_24dp);
//            itemView.setActivated(isExpanded);
//            setsView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(view -> {
//                mExpandedPosition = isExpanded ? -1 : mPosition;
//                TransitionManager.beginDelayedTransition(rvExercises);
//                notifyDataSetChanged();
            });


            mName.setText(mExercise.getName());
            if (mExercise.getDescription() != null) mDesc.setText(mExercise.getDescription());
            else mDesc.setVisibility(View.GONE);

            if (mExercise.getTime() != null) {
                mNormTime.setText(String.format(Locale.getDefault(), "%d", mExercise.getTime()));
            }
            if (mExercise.getRestTime() != null) {
                mRestTime.setText(String.format(Locale.getDefault(), "%d", mExercise.getRestTime()));
            }
            mOrderNumber.setText(String.format(Locale.getDefault(), "%d", mPosition + 1));
//            mOrderNumber.setVisibility(View.GONE);
            itemView.setTag(mExercise.getId());
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }


    }
}