package com.axfex.dorkout.views.workouts;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;

import com.axfex.dorkout.databinding.ActionWorkoutFragmentBinding;
import com.axfex.dorkout.databinding.ActionWorkoutPanelBinding;
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
    private ImageView mGreenLamp;

    private Button mButton;
    private ViewSwitcher mViewSwitcher;
    ActionWorkoutPanelBinding panel1;
    ActionWorkoutPanelBinding panel2;

    ActionWorkoutFragmentBinding mBinding;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.action_workout_fragment, container, false);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mActionWorkoutViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ActionWorkoutViewModel.class);

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.action_workout_fragment, container, false);
        mBinding.setViewModel(mActionWorkoutViewModel);



//        mBinding.included1.setViewModel(mActionWorkoutViewModel);
//        mBinding.included2.setViewModel(mActionWorkoutViewModel);
        mBinding.setLifecycleOwner(this);

        View v = mBinding.getRoot();
//        panel1 = ActionWorkoutPanelBinding.inflate(
//                inflater, container, false);
//        panel1.setLifecycleOwner(this);
//
//        panel2 = ActionWorkoutPanelBinding.inflate(
//                inflater, container, false);
//        panel2.setLifecycleOwner(this);
//        mBinding.viewSwitcher.addView(panel1.getRoot());
//        mBinding.viewSwitcher.addView(panel2.getRoot());


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = v.findViewById(R.id.rv_exercises);
        mRecyclerView.setLayoutManager(layoutManager);
        setupActionWidgets(v);
        return v;
    }

    private void setupActionWidgets(View v) {
        mStartWorkout = v.findViewById(R.id.fab_start_workout);
        mStartWorkout.setOnClickListener(view -> {
                    mActionWorkoutViewModel.startWorkout(mWorkout, mExercises);
                }
        );
        mStopWorkout = v.findViewById(R.id.fab_stop_workout);
        mStopWorkout.setOnClickListener(view -> {
            mActionWorkoutViewModel.finishWorkout();
            ActionWorkoutService.stopWorkout(getContext());
        });


//        panel= LayoutInflater.from(getContext()).inflate(R.layout.action_workout_panel, null);

        mViewSwitcher=v.findViewById(R.id.view_switcher);
        Animation in = AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_top); // load an animation
        mViewSwitcher.setInAnimation(in); // set in Animation for ViewSwitcher
        Animation out = AnimationUtils.loadAnimation(getContext(),R.anim.slide_out_bottom); // load an animation
        mViewSwitcher.setOutAnimation(out); // set out Animation for ViewSwitcher
        mButton=v.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewSwitcher.showNext();
                Log.i(TAG, "onClick: ");
            }
        });
    }




    @Override
    public void onStart() {
        super.onStart();
        mActionWorkoutViewModel.getWorkout(mWorkoutId).observe(this, this::onWorkoutLoaded);
        mActionWorkoutViewModel.getExercises(mWorkoutId).observe(this, this::onExerciseListLoaded);
//        mActionWorkoutViewModel.getActiveWorkoutLD().observe(this, this::onUpdateWorkout);
//        mActionWorkoutViewModel.getExercise().observe(this, this::onUpdateExercise);
//        mActionWorkoutViewModel.getWorkoutTime().observe(this,this::onWorkoutTimeUpdate);
//        mActionWorkoutViewModel.getExerciseTime().observe(this,this::onExerciseTimeUpdate);
//        mActionWorkoutViewModel.getRestTime().observe(this,this::onRestTimeUpdate);
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
        if (workout != null) {
            mWorkout = workout;
//            if (mWorkout.getStatus() == RUNNING) mWorkoutTime.post(mWorkoutTimeUpdateAction);
//            else mWorkoutTime.removeCallbacks(mWorkoutTimeUpdateAction);
        }
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    private void onExerciseListLoaded(List<Exercise> exercises) {
        this.mExercises = exercises;
        if (mAdapter == null) setupAdapter();
        else notifyAdapter();
        Log.i(TAG, "onExerciseListLoaded: ");
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
            View view = LayoutInflater.from(getContext()).inflate(R.layout.action_workout_exercise_item, parent, false);
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
            mName = itemView.findViewById(R.id.name);
            mDesc = itemView.findViewById(R.id.desc);
            mInfoBar = itemView.findViewById(R.id.exercise_info_bar);
            mNormTime = mInfoBar.findViewById(R.id.time);
            mRestTime = mInfoBar.findViewById(R.id.rest);
            mOrderNumber = itemView.findViewById(R.id.order);
            mOrderButton = itemView.findViewById(R.id.change_order);
            setsView = itemView.findViewById(R.id.set);
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

//            itemView.setOnClickListener(view -> {
////                mExpandedPosition = isExpanded ? -1 : mPosition;
////                TransitionManager.beginDelayedTransition(rvExercises);
////                notifyDataSetChanged();
//            });

            itemView.setOnClickListener(this);
            mName.setText(mExercise.getName());
            if (mExercise.getNote() != null) mDesc.setText(mExercise.getNote());
            else mDesc.setVisibility(View.GONE);

            if (mExercise.getTimePlan() != null) {
                mNormTime.setText(String.format(Locale.getDefault(), "%d", mExercise.getTimePlan()));
            }
            if (mExercise.getRestTimePlan() != null) {
                mRestTime.setText(String.format(Locale.getDefault(), "%d", mExercise.getRestTimePlan()));
            }
            mOrderNumber.setText(String.format(Locale.getDefault(), "%d", mPosition + 1));
//            mOrderNumber.setVisibility(View.GONE);
            itemView.setTag(mExercise.getId());
        }

        @Override
        public void onClick(View v) {
            mActionWorkoutViewModel.setActiveExercise(mExercise);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }


    }
}
//    @BindingAdapter({"exercise", "rest"})
//    public static void setProgress(ProgressBar progressBar, Exercise exercise, Rest rest) {
//       if (exercise == null) {
//            progressBar.setProgress(0);
//            progressBar.setSecondaryProgress(0);
//        } else if (exercise.getRunning() || exercise.getPaused()) {
//            {
//                Long time = exercise.getTimeLD().observe(this,);
//                Long timePlan = exercise.getTimePlan();
//                int progress = time != null ? time.intValue() : 0;
//                int MAX_PROGRESS = timePlan != null ? timePlan.intValue() : 0;
//                progressBar.setProgress(progress);
//                progressBar.setMax(MAX_PROGRESS);
//                progressBar.setSecondaryProgress(0);
//
//            }
//        } else if (rest != null) {
//            Long restTime = rest.getRestTime().getValue();
//            Long restTimePlan = exercise.getTimePlan();
//            int secondaryProgress = restTime != null ? restTime.intValue() : 0;
//            int MAX_PROGRESS = restTimePlan != null ? restTimePlan.intValue() : 0;
//            progressBar.setProgress(0);
//            progressBar.setSecondaryProgress(secondaryProgress);
//            progressBar.setMax(MAX_PROGRESS);
//        } else {
//            progressBar.setProgress(0);
//            progressBar.setSecondaryProgress(0);
//        }
//    }
//Place bottom
//    private void onUpdateExercise(Exercise exercise) {
//        mBinding.setExercise(exercise);
//        mExercise = exercise;
//        mExerciseTime.post(mExerciseTimeUpdateAction);
//        if (mExercise != null) {
//            mExerciseName.setText(exercise.getName());
//            mGreenLamp.setEnabled(exercise.getStatus() == RUNNING);
//            mRedLamp.setEnabled(exercise.getStatus() == PAUSED);
//            mPBRestTime.setMax(sec(exercise.getRestTimePlan()));
//            if (mExercise.getStatus() == RUNNING) {
//                mExerciseTime.post(mExerciseTimeUpdateAction);
//            } else {
//                mExerciseTime.removeCallbacks(mExerciseTimeUpdateAction);
//            }
//            if (mExercise.getStatus() == PAUSED) {
//                mRestTime.post(mRestTimeUpdateAction);
//            } else {
//                mRestTime.removeCallbacks(mRestTimeUpdateAction);
//            }
////
//        } else {
//            mExerciseTime.removeCallbacks(mExerciseTimeUpdateAction);
//            mRestTime.removeCallbacks(mRestTimeUpdateAction);
//            mExerciseName.setText("");
//            mRedLamp.setEnabled(false);
//            mGreenLamp.setEnabled(false);
//            mYellowLamp.setEnabled(false);
//        }
//    }

//    private void onUpdateWorkout(Workout workout) {
//        if (workout != null) {
//            mWorkout = workout;
//            if (mWorkout.getRunning()) mWorkoutTime.post(mWorkoutTimeUpdateAction);
//        }
//    }
//
//    private void onWorkoutTimeUpdate() {
//        String timeString = FormatUtils.getTimeString(mWorkout.getTimeLD());
//        mWorkoutTime.setText(timeString);
//        mWorkoutTime.postDelayed(mWorkoutTimeUpdateAction, 1000);
//        Log.i(TAG, "onWorkoutTimeUpdate: " + mWorkout.getName() + ", running:" + mWorkout.getStatus() + ", time:" + mWorkout.getTimeLD());
//    }
//
//    private void onExerciseTimeUpdate() {
//        String timeString = FormatUtils.getTimeString(mExercise.getTimeLD());
//
////        mExerciseTime.setText(timeString);
//        mExerciseTime.postDelayed(mExerciseTimeUpdateAction, 1000);
//        Log.i(TAG, "onExerciseTimeUpdate: " + mExercise.getName() + ", time:" + mExercise.getTimeLD());
//    }
//
//    private void onRestTimeUpdate() {
//        final long time = mExercise.getRestTime();
//        String timeString = FormatUtils.getTimeString(time);
//        mRestTime.setText(timeString);
//        mRestTime.postDelayed(mRestTimeUpdateAction, 1000);
//        Log.i(TAG, "onRestTimeUpdate: " + mExercise.getName() + ", time:" + time);
//        mPBRestTime.setProgress(sec(time));
//    }