package com.axfex.dorkout.views.workouts;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;

import com.axfex.dorkout.databinding.ActionWorkoutFragmentBinding;
import com.axfex.dorkout.databinding.ActionWorkoutPanelBinding;
import com.axfex.dorkout.services.ActionWorkoutService;
import com.axfex.dorkout.util.StartSnapHelper;
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
    LinearLayoutManager mLayoutManager;
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
    ScrollView mScrollView;
    ScrollView mScrollViewPanel;


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

        mBinding.setLifecycleOwner(this);

        View v = mBinding.getRoot();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = v.findViewById(R.id.rv_exercises);
        SnapHelper snapHelper = new StartSnapHelper();


//        SnapOnScrollListener mSnapOnScrollListener = new SnapOnScrollListener(
//                snapHelper, SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE, new OnSnapPositionChangeListener() {
//            @Override
//            public void onSnapPositionChange(int position) {
//                Toast.makeText(getContext(), Integer.toString(position), Toast.LENGTH_SHORT).show();
//            }
//        });

        //mRecyclerView.addOnScrollListener(mSnapOnScrollListener);
//
//        mViewSwitcher = v.findViewById(R.id.view_switcher);
//        mScrollViewPanel = v.findViewById(R.id.scroll_view_panel);
//        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 0);
//        animation.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
//        animation.setFillAfter(true);


        snapHelper.attachToRecyclerView(mRecyclerView);


        mRecyclerView.setLayoutManager(mLayoutManager);
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
        ConstraintLayout mConstraintLayout = v.findViewById(R.id.constraint);
        mViewSwitcher = v.findViewById(R.id.view_switcher);
        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top); // load an animation

        mViewSwitcher.setInAnimation(in); // set in Animation for ViewSwitcher

        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_bottom); // load an animation
        mViewSwitcher.setOutAnimation(out); // set out Animation for ViewSwitcher

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

    private class ExercisesAdapter extends RecyclerView.Adapter {

        private static final int EXERCISE_VIEW_TYPE = 1;
        private static final int FINAL_VIEW_TYPE = 2;

        private ExercisesAdapter() {
//            setHasStableIds(true);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View mView;
            final RecyclerView.ViewHolder mHolder;
            if (viewType == EXERCISE_VIEW_TYPE) {
                mView = LayoutInflater.from(getContext()).inflate(R.layout.action_workout_exercise_item, parent, false);
                mHolder = new ExerciseViewHolder(mView);
            } else {
                mView = LayoutInflater.from(getContext()).inflate(R.layout.action_workout_exercise_item_finish, parent, false);
                mHolder = new FinishViewHolder(mView);
            }
            return mHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (position < mExercises.size())
                ((ExerciseViewHolder) holder).setExercise(mExercises.get(position), position);
        }


        @Override
        public int getItemCount() {
            return mExercises.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position < mExercises.size()) return EXERCISE_VIEW_TYPE;
            else if (position == mExercises.size()) return FINAL_VIEW_TYPE;
            else return 0;
        }

        @Override
        public long getItemId(int position) {
            return mExercises.get(position).getId();
        }
    }

    private class FinishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public FinishViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }

    int mExpandedPosition;

    private class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView mName;
        TextView mDesc;

        View mInfoBar;
        TextView mNormTime;
        TextView mRestTime;
        TextView mOrderNumber;
        ImageButton mOrderButton;
        Exercise mExercise;
        ViewGroup mPanel = itemView.findViewById(R.id.panel);
        int mPosition;
        TextView setsView;
        boolean vis = false;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mDesc = itemView.findViewById(R.id.desc);
            mInfoBar = itemView.findViewById(R.id.exercise_info_bar);
            mNormTime = mInfoBar.findViewById(R.id.time);
            mOrderNumber = itemView.findViewById(R.id.order);
            mOrderButton = itemView.findViewById(R.id.change_order);
            setsView = itemView.findViewById(R.id.set);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.findViewById(R.id.show_comment).setOnClickListener((v) -> showContent());


        }

        private void showContent() {
//            mPanel.animate().scaleY(1.0F);
//            vis=!vis;
//            boolean isExpanded;
            mExpandedPosition = mPosition;

            ChangeBounds transition = new ChangeBounds();
            transition.setInterpolator(new OvershootInterpolator());
            transition.setDuration(2000L);
            TransitionManager.beginDelayedTransition(mRecyclerView, transition);
//                TransitionManager.beginDelayedTransition(mRecyclerView);
//            mLayoutManager.scrollToPositionWithOffset(5,0);
            mAdapter.notifyDataSetChanged();

            Log.i(TAG, "showContent: " + mPosition);
        }


        public void setExercise(Exercise exercise, int position) {
            mExercise = exercise;
            mPosition = position;
            bindData();

        }

        void bindData() {

            vis = mPosition == mExpandedPosition;
//            if (vis) {
//                mPanel.animate().scaleY(1.0F);
//
//            } else {
//                mPanel.animate().scaleY(0.0F);
//                mPanel.setScaleY(0);
//            }
            ConstraintLayout layout = itemView.findViewById(R.id.panel);
//            ChangeBounds transition = new ChangeBounds();
//            transition.setInterpolator(new OvershootInterpolator());
//            transition.setDuration(1000L);
//            TransitionManager.beginDelayedTransition(layout, transition);
//            TransitionManager.beginDelayedTransition(layout);
            if (vis) {


                Log.i(TAG, "bindData: " + mPanel.getHeight());
//                ConstraintSet cs = new ConstraintSet();
//                ConstraintLayout layout = itemView.findViewById(R.id.panel);
//                cs.clone(layout);
//                cs.constrainHeight(R.id.panel, 500);
//                cs.applyTo((ConstraintLayout) mPanel);

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout.getLayoutParams();
                lp.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                layout.setLayoutParams(lp);

//                ValueAnimator anim = ValueAnimator.ofInt(0, 200);
//                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                        int val = (Integer) valueAnimator.getAnimatedValue();
//                        ViewGroup.LayoutParams layoutParams = mPanel.getLayoutParams();
//                        layoutParams.height = val;
//                        mPanel.setLayoutParams(layoutParams);
//                    }
//                });
//                anim.setDuration(500);
//                anim.start();
            } else {
//                ViewGroup.LayoutParams layoutParams = mPanel.getLayoutParams();
//                layoutParams.height = 0;
//                mPanel.setLayoutParams(layoutParams);
//                ConstraintSet cs = new ConstraintSet();
//                ConstraintLayout layout = itemView.findViewById(R.id.panel);
//                cs.clone(layout);
//                cs.constrainHeight(R.id.panel,0);
//                cs.applyTo((ConstraintLayout) mPanel);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout.getLayoutParams();
                lp.height=0;
                layout.setLayoutParams(lp);
//                ChangeBounds transition = new ChangeBounds();
//                transition.setInterpolator(new OvershootInterpolator());
//                TransitionManager.beginDelayedTransition(layout, transition);
            }

//            mPanel.setActivated(vis);
//            mPanel.setVisibility(vis?View.VISIBLE:View.GONE);
            mInfoBar.setVisibility(vis ? View.GONE : View.VISIBLE);
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
//                mNormTime.setText(String.format(Locale.getDefault(), "%d", mExercise.getTimePlan()));
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
