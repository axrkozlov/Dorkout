package com.axfex.dorkout.views.workouts;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.transition.AutoTransition;
import androidx.transition.ChangeBounds;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;

import com.axfex.dorkout.databinding.ActionWorkoutExerciseItemBinding;
import com.axfex.dorkout.databinding.ActionWorkoutFragmentBinding;
import com.axfex.dorkout.services.ActionWorkoutService;
import com.axfex.dorkout.util.CenterLinearLayoutManager;
import com.axfex.dorkout.vm.ViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
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
    private LinearLayoutManager mLayoutManager;


    private MainViewModel mMainViewModel;
    private ActionWorkoutViewModel mActionWorkoutViewModel;
    private Long mWorkoutId;
    private Workout mWorkout;
    private List<Exercise> mExercises;
    View emptyView;
    private FloatingActionButton mStartWorkout;
    private FloatingActionButton mStopWorkout;

    private ImageView mGreenLamp;

    private Button mButton;
    private ViewSwitcher mViewSwitcher;

    ActionWorkoutFragmentBinding mBinding;
    private View lockView;

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
            }
        ((WorkoutApplication) Objects.requireNonNull(getActivity()).getApplication())
                .getAppComponent()
                .inject(this);

        emptyView = new View(getActivity());
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

        mLayoutManager = new CenterLinearLayoutManager(getActivity());
        mRecyclerView = v.findViewById(R.id.rv_exercises);
//        SnapHelper snapHelper = new StartSnapHelper();
//        snapHelper.attachToRecyclerView(mRecyclerView);


//        ViewGroup.LayoutParams lp = mRecyclerView.getLayoutParams();
//        lp.height = (int) (lp.height * 1.5);
//        mRecyclerView.setLayoutParams(lp);
        mRecyclerView.setLayoutManager(mLayoutManager);

        setupActionWidgets(v);
        return v;
    }

    private void setupActionWidgets(View v) {
        mStartWorkout = v.findViewById(R.id.fab_start_workout);
        mStartWorkout.setOnClickListener(view -> mActionWorkoutViewModel.startWorkout(mWorkout, mExercises));
        mStopWorkout = v.findViewById(R.id.fab_stop_workout);
        mStopWorkout.setOnClickListener(view -> {
            mActionWorkoutViewModel.finishWorkout();
            ActionWorkoutService.stopWorkout(getContext());
        });

        lockView = v.findViewById(R.id.lock_view);
    }


    @Override
    public void onStart() {
        super.onStart();
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
        mAdapter.notifyDataSetChanged();
    }


    private void lockRecyclerView() {
        lockView.setVisibility(View.VISIBLE);
    }


    private void unlockRecyclerView() {
        lockView.setVisibility(View.GONE);
    }


    private class ExercisesAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {


        private ExercisesAdapter() {
            setHasStableIds(true);
        }

        @NonNull
        @Override
        public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View mView;
            final ExerciseViewHolder mHolder;
//            mView = LayoutInflater.from(getContext()).inflate(R.layout.action_workout_exercise_item, parent, false);
//            mHolder = new ExerciseViewHolder(mView);

            ActionWorkoutExerciseItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.action_workout_exercise_item, parent, false);
            binding.setLifecycleOwner(ActionWorkoutFragment.this);
            mHolder = new ExerciseViewHolder(binding);

            return mHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
            Exercise mExercise = mExercises.get(position);
            holder.bind(mExercise,position);
//            if (position < mExercises.size())
//                holder.setExercise(mExercises.get(position), position);
        }


        @Override
        public int getItemCount() {
            return mExercises.size();
        }

        @Override
        public long getItemId(int position) {
            return mExercises.get(position).getId();
        }
    }


    private int mExpandPosition = -1;
    private View viewToCollapse;
    Exercise prevExercise;
    private class ExerciseViewHolder extends RecyclerView.ViewHolder {


        TextView mName;
        TextView mDesc;

        TextView mNormTime;
        TextView mRestTime;
        TextView mOrderNumber;
        ImageButton mOrderButton;
        Exercise mExercise;
        View exerciseView = itemView.findViewById(R.id.exercise_view);
        ViewGroup infoBar = itemView.findViewById(R.id.info_panel);
        View actionPanel = itemView.findViewById(R.id.action_panel);
        View infoPanel = itemView.findViewById(R.id.info_panel);


        int mPosition;
        TextView setsView;
//
private final ActionWorkoutExerciseItemBinding binding;

        public ExerciseViewHolder(ActionWorkoutExerciseItemBinding binding)

        {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Exercise exercise, int position){
            if (exercise==null) return;
            mPosition=position;
//            if (exercise.getOrderNumber()==10) exercise.active=true;
            binding.setClicklistener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    animateRecyclerView(mRecyclerView);
//                    if (prevExercise!=null) prevExercise.setActiveLD(false);
                    mActionWorkoutViewModel.setActiveExercise(exercise);

                    //Exercise exercise=binding.getActiveExercise();
//                    exercise.setActive(true);
//                    if (exercise.getTime()==null) exercise.setTime(0L);
//                    exercise.setTime(exercise.getTime()+1000);
//                    prevExercise=exercise;
//                    mAdapter.notifyItemChanged(mPosition);
                }
            });
            binding.setExercise(exercise);
            binding.actionPanel.setExercise(exercise);
            binding.setIsLast(position==mAdapter.getItemCount()-1);
            binding.executePendingBindings();

//            exercise.getTimeLD().observe(ActionWorkoutFragment.this,this::updateTime);

        }

        private void updateTime(Long aLong) {
            if (aLong==null) return;
            ((TextView)itemView.findViewById(R.id.time)).setText(aLong.toString());
        }

//        public ExerciseViewHolder(@NonNull View itemView) {
//            super(itemView);
//            mName = itemView.findViewById(R.id.name);
//            mDesc = itemView.findViewById(R.id.desc);
//
//            mNormTime = infoBar.findViewById(R.id.time);
//            mOrderNumber = itemView.findViewById(R.id.order);
//            mOrderButton = itemView.findViewById(R.id.change_order);
//            setsView = itemView.findViewById(R.id.set);
//            exerciseView.setOnClickListener(v -> changeActive());
//        }

        private void changeActive(){
            mExpandPosition = mPosition;
            collapse();
            expand();
        }

        private void animateRecyclerView(RecyclerView mRecyclerView){

            Transition transition = new ChangeBounds();
            transition.setInterpolator(new LinearInterpolator());
            transition.setDuration(500L);
            transition.addListener(new TransitionListenerAdapter() {
                @Override
                public void onTransitionStart(@NonNull Transition transition) {
                    lockRecyclerView();
                }

                @Override
                public void onTransitionEnd(@NonNull Transition transition) {
                    unlockRecyclerView();
                    mRecyclerView.smoothScrollToPosition(mPosition);
                }
            });
            TransitionManager.beginDelayedTransition(mRecyclerView, transition);


        }

        private void expand() {

            Transition transition = new Fade();
            transition.setDuration(750L);
            TransitionManager.beginDelayedTransition((ViewGroup) itemView, transition);
            actionPanel.setVisibility(View.VISIBLE);
            infoPanel.setVisibility(View.GONE);
            exerciseView.setEnabled(false);
            viewToCollapse = itemView;



            animateRecyclerView((RecyclerView) itemView.getParent());
        }

        private void collapse() {
            if (viewToCollapse != null) {
                View collapseActionPanel = viewToCollapse.findViewById(R.id.action_panel);
                View collapseInfoPanel = viewToCollapse.findViewById(R.id.info_panel);
                View collapseExerciseView = viewToCollapse.findViewById(R.id.exercise_view);

                Transition transition = new Fade();
                transition.setDuration(750L);
                TransitionManager.beginDelayedTransition((ViewGroup) viewToCollapse, transition);

                collapseActionPanel.setVisibility(View.GONE);
                collapseInfoPanel.setVisibility(View.VISIBLE);
                collapseExerciseView.setEnabled(true);
            }

        }



        public void setExercise(Exercise exercise, int position) {
            mExercise = exercise;
            mPosition = position;
            bindData();
        }



        void bindData() {

            exerciseView.setEnabled(mExpandPosition != mPosition);
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            if (mPosition == mAdapter.getItemCount() - 1) {
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                itemView.findViewById(R.id.finish_widget).setVisibility(View.VISIBLE);
            } else {
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                itemView.findViewById(R.id.finish_widget).setVisibility(View.GONE);
            }
            itemView.setLayoutParams(lp);



            if (mPosition == mExpandPosition) {
                actionPanel.setVisibility(View.VISIBLE);
                viewToCollapse = itemView;
                infoBar.setVisibility(View.GONE);
            } else {
                actionPanel.setVisibility(View.GONE);
                infoBar.setVisibility(View.VISIBLE);
            }


            mName.setText(mExercise.getName());
            if (mExercise.getNote() != null) mDesc.setText(mExercise.getNote());
            else mDesc.setVisibility(View.GONE);

            if (mExercise.getTimePlan() != null) {
//                mNormTime.setText(String.format(Locale.getDefault(), "%d", mExercise.getTimePlan()));
            }

//            mOrderNumber.setText(String.format(Locale.getDefault(), "%d", mPosition + 1));
//            mOrderNumber.setVisibility(View.GONE);
            itemView.setTag(mExercise.getId());
        }


    }

}
//    ValueAnimator anim = ValueAnimator.ofInt(0, 200);
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
//                ConstraintSet cs = new ConstraintSet();
//                ConstraintLayout layout = itemView.findViewById(R.id.panel);
//                cs.clone(layout);
//                cs.constrainHeight(R.id.panel, 500);
//                cs.applyTo((ConstraintLayout) mPanel);

//                panel.animate()
//                        .translationY(panel.getHeight())
//                        .alpha(0.0f)
//                        .setDuration(300)
//                        .setListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                super.onAnimationEnd(animation);
//                                panel.setVisibility(View.GONE);
//                            }
//                        });


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
//        mViewSwitcher = v.findViewById(R.id.view_switcher);
//        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top); // load an animation
//
//        mViewSwitcher.setInAnimation(in); // set in Animation for ViewSwitcher
//
//        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_bottom); // load an animation
//        mViewSwitcher.setOutAnimation(out); // set out Animation for ViewSwitcher
//        mScrollViewPanel = v.findViewById(R.id.scroll_view_panel);
//        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 0);
//        animation.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
//        animation.setFillAfter(true);


//            ConstraintLayout layout = itemView.findViewById(R.id.panel);
//            if (vis) {
//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout.getLayoutParams();
//                lp.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
//                layout.setLayoutParams(lp);
//            } else {
//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout.getLayoutParams();
//                lp.height = 0;
//                layout.setLayoutParams(lp);
//            }

//    RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
//                    @Override
//                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                        super.onScrollStateChanged(recyclerView, newState);
//                        if (newState == RecyclerView.SCROLL_STATE_IDLE &&
//                                mLayoutManager.findFirstVisibleItemPosition() <= mPosition &&
//                                mLayoutManager.findLastVisibleItemPosition() >= mPosition) {
////                            collapse();
////                            expand();
//                        }
//                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                            recyclerView.removeOnScrollListener(this);
//                            unlockRecyclerView();
//                        }
//                    }
//                };
//                mRecyclerView.addOnScrollListener(listener);



//            if (mPosition==mAdapter.getItemCount()-1) {
//                LinearLayout item=itemView.findViewById(R.id.finish_widget);
//                item.getLayoutParams();
//                ViewGroup.LayoutParams lp=item.getLayoutParams();
//                lp.height= ViewGroup.LayoutParams.MATCH_PARENT;
//                item.setLayoutParams(lp);
//                View view = new View(getContext());
//                ViewGroup.LayoutParams lp1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//                Log.i(TAG, "bind: ");
//                ((ViewGroup) itemView).addView(view,lp1);
//            } else {
//                LinearLayout item=itemView.findViewById(R.id.finish_widget);
//                item.getLayoutParams();
//                ViewGroup.LayoutParams lp=item.getLayoutParams();
//                lp.height= ViewGroup.LayoutParams.WRAP_CONTENT;
//                item.setLayoutParams(lp);
//            }


//        @Override
//        public int getItemViewType(int position) {
//            if (position < mExercises.size() - 1) return EXERCISE_VIEW_TYPE;
//            else if (position == mExercises.size() - 1) return FINAL_VIEW_TYPE;
//            else return 0;
//        }
//            if (viewType == EXERCISE_VIEW_TYPE) {
//                View finishWidget=

//            } else {
//                mView = LayoutInflater.from(getContext()).inflate(R.layout.z_action_workout_exercise_item_finish, parent, false);
//
//            }


//        private static final int EXERCISE_VIEW_TYPE = 1;
//        private static final int FINAL_VIEW_TYPE = 2;

//        ViewGroup.LayoutParams lp = mRecyclerView.getLayoutParams();
//        lp.height = (int) (lp.height * 1.5);
//        mRecyclerView.setLayoutParams(lp);
