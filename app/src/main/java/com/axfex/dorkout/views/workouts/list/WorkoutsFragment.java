package com.axfex.dorkout.views.workouts.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axfex.dorkout.R;
import com.axfex.dorkout.util.DateUtils;
import com.axfex.dorkout.data.Workout;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;


public class WorkoutsFragment extends Fragment {

    private static WorkoutsViewModel mWorkoutsViewModel;
    private RecyclerView mRecyclerView;
    private WorkoutsAdapter mAdapter;
    private List<Workout> mWorkouts;
    Workout mPickedWorkout;

    public static WorkoutsFragment newInstance() {
        return new WorkoutsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((WorkoutApplication) getActivity().getApplication())
//                .getAppComponent()
//                .inject(this);

    }

    public static void attachViewModel(WorkoutsViewModel workoutsViewModel) {
        mWorkoutsViewModel = workoutsViewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.workouts_fragment, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView = v.findViewById(R.id.rv_workouts);
        mRecyclerView.setLayoutManager(layoutManager);

        mWorkoutsViewModel.getWorkouts().observe(this, this::onListLoaded);
        mWorkoutsViewModel.getViewType().observe(WorkoutsFragment.this, this::onViewTypeChange);
        return v;
    }

    private void onListLoaded(List<Workout> list) {
        this.mWorkouts = list;
        swapAdapter();
    }

    private void onViewTypeChange(WorkoutsViewModel.ViewType viewType) {
        //if (viewType!=WorkoutsViewModel.ViewType.WORKOUT_SELECTION) return;
        mPickedWorkout = mWorkoutsViewModel.getWorkout();
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
    }

    private void swapAdapter() {
        mAdapter = new WorkoutsAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private boolean isAnyWorkoutPicked(){
        return mPickedWorkout!=null;
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

            if (mWorkout == mPickedWorkout) {
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
            if (!isAnyWorkoutPicked()) {
                mWorkoutsViewModel.openWorkout(getWorkout()); return;
            }
            mWorkoutsViewModel.pickWorkout(getWorkout());
        }

        @Override
        public boolean onLongClick(View v) {
            mWorkoutsViewModel.pickWorkout(getWorkout());
            return true;
        }
    }
}
