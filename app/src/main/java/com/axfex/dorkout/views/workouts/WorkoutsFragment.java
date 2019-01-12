package com.axfex.dorkout.views.workouts;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.util.DateUtils;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.util.Show;
import com.axfex.dorkout.vm.ViewModelFactory;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;


public class WorkoutsFragment extends Fragment {
    public static final String TAG = "WORKOUTS_FRAGMENT";

    @Inject
    public ViewModelFactory<WorkoutsViewModel> mViewModelFactory;
    public WorkoutsViewModel mWorkoutsViewModel;

    private MainViewModel mMainViewModel;
    private RecyclerView mRecyclerView;
    private WorkoutsAdapter mAdapter;
    private List<Workout> mWorkouts;
    private boolean isAnyWorkoutPicked;
    Workout mPickedWorkout;
    private Menu mMenu;
    private boolean isWorkoutMenuShown;

    public static WorkoutsFragment newInstance() {
        return new WorkoutsFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        ((WorkoutApplication) Objects.requireNonNull(getActivity()).getApplication())
                .getAppComponent()
                .inject(this);
        mWorkoutsViewModel = ViewModelProviders.of(this, mViewModelFactory).get(WorkoutsViewModel.class);
       // mMainViewModel=ViewModelProviders.of(getActivity()).get(MainViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.workouts_fragment, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = v.findViewById(R.id.rv_workouts);
        mRecyclerView.setLayoutManager(layoutManager);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mMainViewModel.getWorkouts().observe(this, this::onListLoaded);
        mMainViewModel.getShow().observe(WorkoutsFragment.this, this::onViewTypeChange);
//        mMainViewModel.onShowOpened(TAG);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_workouts, menu);
        mMenu=menu;
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void updateMenu() {
//        ActionBar actionBar=((AppCompatActivity)Objects.requireNonNull(getActivity())).getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(isAnyWorkoutPicked);
//        String title=mPickedWorkout!=null? mPickedWorkout.getName():getString(R.string.title_activity_workouts);
//        actionBar.setTitle(title);
//        mMenu.findItem(R.id.menu_workouts_add).setVisible(!isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_copy).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_edit).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_rename).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_workouts_delete).setVisible(isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_settings).setVisible(!isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_about).setVisible(!isWorkoutMenuShown);
//        mMenu.findItem(R.id.menu_donate).setVisible(!isWorkoutMenuShown);
    }

    private void onListLoaded(List<Workout> list) {
        this.mWorkouts = list;
        swapAdapter();
    }

    private void onViewTypeChange(Show show) {
//        isAnyWorkoutPicked=false;
//        isWorkoutMenuShown=false;
//        if (show.is(TAG))
//        {
//            mPickedWorkout = (Workout) show.getItem();
//            Log.i(TAG, "onViewTypeChange: "+show.getTag() + " and workout : " + mPickedWorkout);
//            notifyAdapter();
//            isAnyWorkoutPicked=true;
//            isWorkoutMenuShown=true;
//            updateMenu();
//        } else if (mPickedWorkout!=null){
//            mPickedWorkout = null;
//            notifyAdapter();
//            updateMenu();
//        }

    }

    private void swapAdapter() {
        mAdapter = new WorkoutsAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void notifyAdapter(){
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
    }








    private class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsViewHolder> {

        @NonNull
        @Override
        public WorkoutsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.workout_item, parent, false);
            return new WorkoutsViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull WorkoutsViewHolder holder, int position) {
            if (mWorkouts.size() == 0) return;
            holder.setWorkout(mWorkouts.get(position));
        }

        @Override
        public int getItemCount() {
            return mWorkouts.size();
        }

    }

    private class WorkoutsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private Workout mWorkout;
        private Integer mViewPosition;
        private TextView mName;
        private TextView mDesc;
        private TextView mExercises;
        private TextView mTotalTime;
        private TextView mDays;
        private TextView mStartTime;
        private CardView mCard;
        private ImageView mMarkIcon;

        private WorkoutsViewHolder(View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.workout_title);
            mDesc = itemView.findViewById(R.id.workout_desc);
            mExercises = itemView.findViewById(R.id.workout_desc_exercises);
            mTotalTime = itemView.findViewById(R.id.workout_desc_total_time);
            mDays = itemView.findViewById(R.id.workout_desc_days);
            mStartTime = itemView.findViewById(R.id.workout_desc_start_time);
            mCard = itemView.findViewById(R.id.workout_card);
            mMarkIcon = itemView.findViewById(R.id.workout_mark_icon);

        }

        private void bindData() {
            mName.setText(mWorkout.getName());
            if (mWorkout.getDescription() != null) {
                mDesc.setText(mWorkout.getDescription());
            } else mDesc.setVisibility(View.GONE);
            String[] weekDaysText = DateFormatSymbols.getInstance().getShortWeekdays();
            ArrayList<Boolean> weekDays = DateUtils.parseWeekDays(mWorkout.getWeekDaysComposed());
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < 7; i++) {
                if (weekDays.get(i)) {
                    sb.append(weekDaysText[i + 1] + " ");
                }
            }
            Integer exercisesCount = mWorkout.getExercisesCount();
            if (exercisesCount != null) {
                mExercises.setText(exercisesCount.toString());
            }

            mDays.setText(sb.toString());
            mStartTime.setText(DateUtils.getTimeString(mWorkout.getStartTime()));
            mTotalTime.setText(DateUtils.getTimeString(mWorkout.getTotalTime()));
            //holder.mCard.setCardBackgroundColor(workout.isMarked() ? markedCardColor : unmarkedCardColor);
            //holder.mMarkIcon.setVisibility(workout.isMarked() ? View.VISIBLE : View.INVISIBLE);

            itemView.setTag(mWorkout.getId());

            if (mWorkout == WorkoutsFragment.this.mPickedWorkout) {
                itemView.setSelected(true);
            } else {
                itemView.setSelected(false);
            }

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        public Workout getWorkout() {
            return mWorkout;
        }

        public void setWorkout(Workout workout) {
            mWorkout = workout;
            bindData();
        }

        @Override
        public void onClick(View v) {
            if (!isAnyWorkoutPicked) {
                mMainViewModel.openWorkout(getWorkout()); return;
            }
            mMainViewModel.pickWorkout(getWorkout());
        }

        @Override
        public boolean onLongClick(View v) {
            mMainViewModel.pickWorkout(getWorkout());
            Log.i(TAG, "Pick workout : " + getWorkout());
            return true;
        }
    }
}
