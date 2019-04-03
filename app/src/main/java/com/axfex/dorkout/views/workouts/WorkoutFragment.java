package com.axfex.dorkout.views.workouts;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;

import com.axfex.dorkout.databinding.ActionWorkoutExerciseItemBinding;
import com.axfex.dorkout.databinding.ActionWorkoutFragmentBinding;
import com.axfex.dorkout.services.WorkoutPerformingManager;
import com.axfex.dorkout.services.WorkoutPerformingService;
import com.axfex.dorkout.util.CenterLinearLayoutManager;
import com.axfex.dorkout.vm.ViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static android.content.Context.MODE_PRIVATE;

public class WorkoutFragment extends Fragment {
    public static final String TAG = "WORKOUT_FRAGMENT";
    private static final String WORKOUT_ID = "WORKOUT_ID";
    private static final String PERFORMING_WORKOUT_ID = "PERFORMING_WORKOUT_ID";

    @Inject
    ViewModelFactory<WorkoutViewModel> mViewModelFactory;

    private Menu mMenu;
    private RecyclerView mRecyclerView;

    private ExercisesAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;


    private MainViewModel mMainViewModel;
    private WorkoutViewModel mWorkoutViewModel;
    private Long mWorkoutId;
    private Long mPerformingWorkoutId;
    private Workout mWorkout;

    private LiveData<List<Exercise>> mExercisesLD;
    private LiveData<Exercise> mActiveExerciseLD;
    private LiveData<Workout> mWorkoutLD;

    private List<Exercise> mExercises;
    private FloatingActionButton mStartWorkout;
    private FloatingActionButton mStopWorkout;


    ActionWorkoutFragmentBinding mBinding;
    private boolean isRecyclerViewLocked = false;
    private boolean notifyAdapterPending;
    private boolean scrollAfterOpenedPending;

    private View lockView;

    WorkoutPerformingManager mWorkoutPerformingManager;


    public static WorkoutFragment newInstance() {
        return new WorkoutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle savedParameters = getArguments();

        if (mWorkoutId == null) {
            if (savedParameters != null)
                mWorkoutId = savedParameters.getLong(WORKOUT_ID);
        }

        ((WorkoutApplication) Objects.requireNonNull(getActivity()).getApplication())
                .getAppComponent()
                .inject(this);

        mPerformingWorkoutId = requireActivity().getPreferences(MODE_PRIVATE).getLong(PERFORMING_WORKOUT_ID, 0);
    }

    public void setWorkoutId(Long id) {
        mWorkoutId = id;
        Bundle args = getArguments() != null ? getArguments() : new Bundle();
        args.putLong(WORKOUT_ID, id);
        setArguments(args);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.action_workout_fragment, container, false);

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.action_workout_fragment, container, false);
        mBinding.setViewModel(mWorkoutViewModel);

        mBinding.setLifecycleOwner(this);

        View v = mBinding.getRoot();

        setupActionWidgets(v);
        return v;
    }

    private void setupActionWidgets(View v) {
        mLayoutManager = new CenterLinearLayoutManager(getActivity());
        mRecyclerView = v.findViewById(R.id.rv_exercises);
//        SnapHelper snapHelper = new StartSnapHelper();
//        snapHelper.attachToRecyclerView(mRecyclerView);
//        ViewGroup.LayoutParams lp = mRecyclerView.getLayoutParams();
//        lp.height = (int) (lp.height * 1.5);
//        mRecyclerView.setLayoutParams(lp);
        scrollAfterOpenedPending =true;
        mAdapter = new ExercisesAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mStartWorkout = v.findViewById(R.id.fab_start_workout);
        mStartWorkout.setOnClickListener(this::startWorkout);
        mStopWorkout = v.findViewById(R.id.fab_stop_workout);
        mStopWorkout.setOnClickListener(this::stopWorkout);
        lockView = v.findViewById(R.id.lock_view);
    }


    @Override
    public void onStart() {
        super.onStart();
        mMainViewModel = ((MainActivity) requireActivity()).getViewModel();
        if (mPerformingWorkoutId != 0) {
            bindPerformingService();
        } else {
            observeWorkout();
        }
        Log.i(TAG, "onStart: mPerformingWorkoutId  " + mPerformingWorkoutId);
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindPerformingService();
    }


    private void observeWorkout() {
        mWorkoutViewModel = ViewModelProviders.of(this, mViewModelFactory).get(WorkoutViewModel.class);
        if (mExercisesLD != null) mExercisesLD.removeObservers(this);
        if (mWorkoutLD != null) mWorkoutLD.removeObservers(this);
        mWorkoutLD = mWorkoutViewModel.getWorkout(mWorkoutId);
        mExercisesLD = mWorkoutViewModel.getExercises(mWorkoutId);
        mWorkoutLD.observe(this, this::onWorkoutLoaded);
        mExercisesLD.observe(this, this::onExerciseListLoaded);
    }

    private void observePerformingWorkout() {
        if (mActiveExerciseLD != null) mActiveExerciseLD.removeObservers(this);
        if (mExercisesLD != null) mExercisesLD.removeObservers(this);
        if (mWorkoutLD != null) mWorkoutLD.removeObservers(this);

        mWorkoutLD = mWorkoutPerformingManager.getActiveWorkoutLD();
        mExercisesLD = mWorkoutPerformingManager.getActiveExercisesLD();
        mActiveExerciseLD = mWorkoutPerformingManager.getActiveExerciseLD();
        mWorkoutLD.observe(this, this::onWorkoutLoaded);
        mExercisesLD.observe(this, this::onExerciseListLoaded);
        mActiveExerciseLD.observe(this, this::onActiveExerciseLoaded);
    }


    private void startWorkout(View v) {
//        setPerformingWorkoutId(mWorkoutId);
//        mMainViewModel.startWorkout(mWorkoutId);
//        memorizePerformingWorkoutId();

        mMainViewModel.startWorkout(mWorkoutId);

        Log.i(TAG, "startWorkout: ");
//        startPerformingService();
        bindPerformingService();

    }

    private void stopWorkout(View v) {
        mMainViewModel.stopWorkout(0L);
    }

    private void bindPerformingService() {
        Context context = requireContext();
        Intent intent = new Intent(context, WorkoutPerformingService.class);
        context.bindService(intent, mServiceConnection, 0);
        mServiceBound = true;
    }

    private void unbindPerformingService() {
        if (mServiceBound) {
            requireContext().unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    boolean mServiceBound = false;
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mWorkoutPerformingManager = ((WorkoutPerformingService.ActionBinder) service).getActionWorkoutManager();
            observePerformingWorkout();
            Log.i(TAG, "onServiceConnected: " + "Manager" + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mWorkoutPerformingManager = null;
            mServiceBound = false;
            Log.i(TAG, "onServiceDisconnected: ");
        }
    };

    private void onActiveExerciseLoaded(Exercise exercise) {
        Transition transition = new ChangeBounds();
        transition.setInterpolator(new LinearInterpolator());
        transition.setDuration(750L);
        transition.addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {
                setRecyclerViewLocked();
            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                setRecyclerViewUnlocked();
                scrollToActiveExercise();
                if (notifyAdapterPending) {
                    mAdapter.notifyDataSetChanged();
                    scrollToActiveExercise();
                }
            }
        });
        TransitionManager.beginDelayedTransition(mRecyclerView, transition);

    }

    private void setRecyclerViewLocked() {
        lockView.setVisibility(View.VISIBLE);
        isRecyclerViewLocked = true;
    }

    private void setRecyclerViewUnlocked() {
        lockView.setVisibility(View.GONE);
        isRecyclerViewLocked = false;
    }

    private void scrollToActiveExercise() {
        if (scrollAfterOpenedPending) {
            mRecyclerView.scrollToPosition(findRunningPosition());
            scrollAfterOpenedPending=false;
            return;
        }
        mRecyclerView.smoothScrollToPosition(findRunningPosition());
    }

    private int findRunningPosition() {
        if (mExercises == null) return 0;
        int i = 0;
        for (Exercise e : mExercises) {
            if (Boolean.TRUE.equals(e.getActiveLD().getValue())) {
                return i;
            }
            i++;
        }
        return 0;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        requireActivity().getMenuInflater().inflate(R.menu.action_menu, menu);
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
        }
        requireActivity().invalidateOptionsMenu();
    }

    private void onExerciseListLoaded(List<Exercise> exercises) {
        this.mExercises = exercises;
        notifyAdapter();
    }


    private void notifyAdapter() {
        if (!isRecyclerViewLocked) {
            mAdapter.notifyDataSetChanged();
            scrollToActiveExercise();
        }
        else notifyAdapterPending = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
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
            binding.setLifecycleOwner(WorkoutFragment.this);
            mHolder = new ExerciseViewHolder(binding);

            return mHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
            Exercise mExercise = mExercises.get(position);
            holder.bind(mExercise, position == getItemCount() - 1);
        }


        @Override
        public int getItemCount() {
            if (mExercises == null) return 0;
            return mExercises.size();
        }

        @Override
        public long getItemId(int position) {
            return mExercises.get(position).getId();
        }

    }

    private class ExerciseViewHolder extends RecyclerView.ViewHolder {

        private final ActionWorkoutExerciseItemBinding binding;

        public ExerciseViewHolder(ActionWorkoutExerciseItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Exercise exercise, boolean isLast) {
            if (exercise == null) return;
            binding.setClicklistener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mWorkoutPerformingManager != null)
                        mWorkoutPerformingManager.setExercise(exercise);
                }
            });
            binding.setExercise(exercise);
            binding.setViewModel(mWorkoutViewModel);
            binding.actionPanel.setExercise(exercise);
            binding.actionPanel.setViewModel(mWorkoutViewModel);
            binding.setIsLastExercise(isLast);
            binding.executePendingBindings();
//            Log.i(TAG, "bind: " + exercise.getOrderNumber());
        }
    }
}

//    private void memorizePerformingWorkoutId(){
//        Activity activity = requireActivity();
//        activity.getPreferences(Context.MODE_PRIVATE).edit().putLong(PERFORMING_WORKOUT_ID,mWorkoutId).apply();
//    }
//
//    private void clearPerformingWorkoutId(){
//        Activity activity = requireActivity();
//        activity.getPreferences(Context.MODE_PRIVATE).edit().remove(PERFORMING_WORKOUT_ID).apply();
//    }
//
//    private void startPerformingService() {
//        Context context = requireContext();
//        Intent intent = new Intent(context, WorkoutPerformingService.class);
//        WorkoutPerformingService.startActionWorkoutService(context, mWorkoutId);
//        context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
//    }
//

//
//
//    private void stopService() {
//        Context context = requireContext();
//        WorkoutPerformingService.stopActionWorkoutService(context);
//    }


//
//
//        TextView mName;
//        TextView mDesc;
//
//        TextView mNormTime;
//        TextView mRestTime;
//        TextView mOrderNumber;
//        ImageButton mOrderButton;
//        Exercise mExercise;
//        View exerciseView = itemView.findViewById(R.id.exercise_view);
//        ViewGroup infoBar = itemView.findViewById(R.id.info_panel);
//        View actionPanel = itemView.findViewById(R.id.action_panel);
//        View infoPanel = itemView.findViewById(R.id.info_panel);


//        int mPosition;
//        TextView setsView;
//

//        private void updateTime(Long aLong) {
//            if (aLong==null) return;
//            ((TextView)itemView.findViewById(R.id.time)).setText(aLong.toString());
//        }

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
//
//        private void changeActive(){
//            mExpandPosition = mPosition;
//            collapse();
//            expand();
//        }
//
//        private void animateRecyclerView(RecyclerView mRecyclerView){
//
//            Transition transition = new ChangeBounds();
//            transition.setInterpolator(new LinearInterpolator());
//            transition.setDuration(500L);
//            transition.addListener(new TransitionListenerAdapter() {
//                @Override
//                public void onTransitionStart(@NonNull Transition transition) {
//                    lockRecyclerView();
//                }
//
//                @Override
//                public void onTransitionEnd(@NonNull Transition transition) {
//                    unlockRecyclerView();
//                    mRecyclerView.smoothScrollToPosition(mPosition);
//                }
//            });
//            TransitionManager.beginDelayedTransition(mRecyclerView, transition);
//
//
//        }
//
//        private void expand() {
//
//            Transition transition = new Fade();
//            transition.setDuration(750L);
//            TransitionManager.beginDelayedTransition((ViewGroup) itemView, transition);
//            actionPanel.setVisibility(View.VISIBLE);
//            infoPanel.setVisibility(View.GONE);
//            exerciseView.setEnabled(false);
//            viewToCollapse = itemView;
//
//
//
//            animateRecyclerView((RecyclerView) itemView.getParent());
//        }
//
//        private void collapse() {
//            if (viewToCollapse != null) {
//                View collapseActionPanel = viewToCollapse.findViewById(R.id.action_panel);
//                View collapseInfoPanel = viewToCollapse.findViewById(R.id.info_panel);
//                View collapseExerciseView = viewToCollapse.findViewById(R.id.exercise_view);
//
//                Transition transition = new Fade();
//                transition.setDuration(750L);
//                TransitionManager.beginDelayedTransition((ViewGroup) viewToCollapse, transition);
//
//                collapseActionPanel.setVisibility(View.GONE);
//                collapseInfoPanel.setVisibility(View.VISIBLE);
//                collapseExerciseView.setEnabled(true);
//            }
//
//        }
//


//        public void setExercise(Exercise exercise, int position) {
//            mExercise = exercise;
//            mPosition = position;
//            bindData();
//        }


//        void bindData() {
//
//            exerciseView.setEnabled(mExpandPosition != mPosition);
//            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
//            if (mPosition == mAdapter.getItemCount() - 1) {
//                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                itemView.findViewById(R.id.finish_widget).setVisibility(View.VISIBLE);
//            } else {
//                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                itemView.findViewById(R.id.finish_widget).setVisibility(View.GONE);
//            }
//            itemView.setLayoutParams(lp);
//
//
//
//            if (mPosition == mExpandPosition) {
//                actionPanel.setVisibility(View.VISIBLE);
//                viewToCollapse = itemView;
//                infoBar.setVisibility(View.GONE);
//            } else {
//                actionPanel.setVisibility(View.GONE);
//                infoBar.setVisibility(View.VISIBLE);
//            }
//
//
//            mName.setText(mExercise.getName());
//            if (mExercise.getNote() != null) mDesc.setText(mExercise.getNote());
//            else mDesc.setVisibility(View.GONE);
//
//            if (mExercise.getTimePlan() != null) {
////                mNormTime.setText(String.format(Locale.getDefault(), "%d", mExercise.getTimePlan()));
//            }
//
////            mOrderNumber.setText(String.format(Locale.getDefault(), "%d", mPosition + 1));
////            mOrderNumber.setVisibility(View.GONE);
//            itemView.setTag(mExercise.getId());
//        }

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
