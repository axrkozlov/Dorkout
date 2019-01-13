package com.axfex.dorkout.views.workouts;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.views.exercises.list.ExercisesFragment;
import com.axfex.dorkout.vm.ViewModelFactory;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class EditWorkoutFragment extends Fragment {
    @Inject
    ViewModelFactory<EditWorkoutViewModel> mViewModelFactory;

    public static final String TAG = "EDIT_WORKOUT_FRAGMENT";
    private static final String WORKOUT_ID = "WORKOUT_ID";

    private Menu mMenu;
    private RecyclerView mRecyclerView;
    private ExercisesAdapter mAdapter;
    private MainViewModel mMainViewModel;
    private EditWorkoutViewModel mEditWorkoutViewModel;
    private Long mWorkoutId;
    private Workout mWorkout;
    private List<Exercise> mExercises;


    public static EditWorkoutFragment newInstance() {
        return new EditWorkoutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.i(TAG, "onCreate: " + mWorkoutId);
        if (mWorkoutId == null)
            if (getArguments() != null) {
                mWorkoutId = getArguments().getLong(WORKOUT_ID);
            } else {
                throw new IllegalArgumentException("Not have a workout id for edit");
            }
        ((WorkoutApplication) Objects.requireNonNull(getActivity()).getApplication())
                .getAppComponent()
                .inject(this);
        mEditWorkoutViewModel = ViewModelProviders.of(this, mViewModelFactory).get(EditWorkoutViewModel.class);
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
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
        View v = inflater.inflate(R.layout.edit_workout_fragment, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = v.findViewById(R.id.rv_exercises);
        mRecyclerView.setLayoutManager(layoutManager);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        mEditWorkoutViewModel.getWorkout(mWorkoutId).observe(this, this::onWorkoutLoaded);
        mEditWorkoutViewModel.getExercises(mWorkoutId).observe(this, this::onListLoaded);
        //mWorkoutsViewModel.getPickedWorkout().observe(WorkoutsFragment.this, this::onWorkoutPicked);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        Log.i(TAG, "onCreateOptionsMenu: ");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.addeditworkout_menu, menu);
        mMenu = menu;
        updateActionBar();
    }

    private void updateActionBar() {
//        if (mWorkout != null)
//            Log.i(TAG, "updateActionBar: " + mWorkout.getName());
        ActionBar actionBar = ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (mWorkout != null) actionBar.setTitle(mWorkout.getName());


//        mMenu.findItem(R.id.menu_workouts_add).setVisible(!isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_copy).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_edit).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_rename).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_delete).setVisible(isWorkoutMenuShown);
    }

    private void onWorkoutLoaded(Workout workout) {
        Log.i(TAG, "onWorkoutLoaded: " +workout+ workout.getName());
        mWorkout = workout;
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    private void onListLoaded(List<Exercise> exercises) {
        this.mExercises = exercises;
        swapAdapter();
    }

    private void swapAdapter() {
        mAdapter = new ExercisesAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void notifyAdapter() {
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeAsUp: {
                getActivity().onBackPressed();
            }
            case R.id.menu_workout_edit_add:{
                
            }
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    private class ExercisesAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {

        @NonNull
        @Override
        public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.exercise_item, parent, false);
            return new ExerciseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
            if (mExercises.size() == 0) {
                return;
            }
            holder.setExercise(mExercises.get(position),position);
        }

        @Override
        public int getItemCount() {
            return mExercises.size();
        }
        
        
        
    }

    private class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener{
        TextView nameView;
        TextView descView;
        TextView numberView;
        ImageButton collapseButton;
        TextView setsView;
        Exercise mExercise;
        Integer mExpandedPosition = -1;
        int mPosition;
        private boolean mEditMode;


        public ExerciseViewHolder(@NonNull View itemView) {
                super(itemView);
                nameView = itemView.findViewById(R.id.exercise_title);
                descView = itemView.findViewById(R.id.exercise_desc);
                numberView = itemView.findViewById(R.id.exercise_number);
                collapseButton = itemView.findViewById(R.id.exercise_collapse);
                setsView = itemView.findViewById(R.id.exercise_sets);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            
            
        }
        public void setExercise(Exercise exercise,int position) {
            mExercise=exercise;
            mPosition=position;
            bindData();
        }
        
        void bindData(){

//            List<Eset> esets = mExercisesWithSets.get(position).esets;
//            if (exercise == null) {
//                return;
//            }
            collapseButton.setOnTouchListener(this);
            final boolean isExpanded = mPosition == mExpandedPosition && !mEditMode;
            collapseButton.setImageResource(isExpanded ? R.drawable.ic_collapse_24dp : R.drawable.ic_expand_24dp);
            itemView.setActivated(isExpanded);
            setsView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(view -> {
//                mExpandedPosition = isExpanded ? -1 : mPosition;
//                TransitionManager.beginDelayedTransition(rvExercises);
//                notifyDataSetChanged();
            });

            nameView.setText(mExercise.getName());
            descView.setText(mExercise.getDescription());
            numberView.setText(Integer.toString(mPosition + 1));
            itemView.setTag(mExercise.getId());
        }

        @Override
        public void onClick(View v) {
            
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
        
        
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.bt_workout_start_time:
//                break;
////            case R.id.bt_workout_delete:
////                onDeleteWorkout();
////                break;
//        }
//    }


}
