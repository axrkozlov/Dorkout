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
import java.util.Objects;


public class WorkoutsFragment extends Fragment {

    private static WorkoutsViewModel sWorkoutsViewModel;
    private RecyclerView recyclerView;
    private WorkoutsAdapter mAdapter;
    private List<Workout> mWorkouts;
    Workout mPickedWorkout;

    public static WorkoutsFragment newInstance() {
        WorkoutsFragment fragment = new WorkoutsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void attachViewModel(WorkoutsViewModel workoutsViewModel) {
        sWorkoutsViewModel = workoutsViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.workouts_fragment, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = v.findViewById(R.id.rv_workouts);
        recyclerView.setLayoutManager(layoutManager);

        sWorkoutsViewModel.getWorkouts().observe(this, workouts -> setListData(workouts));
        sWorkoutsViewModel.getPickedWorkout().observe(WorkoutsFragment.this, workout -> onWorkoutPicked(workout));

        return v;
    }


    public void setListData(List<Workout> listOfData) {
        this.mWorkouts = listOfData;
        swapAdapter();
    }

    private void onWorkoutPicked(Workout pickedWorkout) {
        mPickedWorkout = pickedWorkout;
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
//
//            if (pickedView != null) {
//                pickedView.setSelected(false);
//            }
//            mPickedWorkout=pickedWorkout;
////            if (mPickedPosition != null) {
////                Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(mPickedPosition)).itemView.setSelected(false);
////            }
////            if (position != null) {
////                mPickedPosition = position;
////                Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(mPickedPosition)).itemView.setSelected(true);
////            }

    }

    public void swapAdapter() {
        mAdapter = new WorkoutsAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    private class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsViewHolder> implements View.OnClickListener, View.OnLongClickListener {

        @Override
        public WorkoutsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.workout_item, parent, false);

            return  new WorkoutsViewHolder(view);
        }


        @Override
        public void onBindViewHolder(WorkoutsViewHolder holder, int position) {

            if (mWorkouts.size() == 0 || mWorkouts.get(position) == null) {
                return;
            }


            Workout workout = mWorkouts.get(position);
            holder.setWorkout(workout);
            holder.mName.setText(workout.getName());
            if (workout.getDescription() != null) {
                holder.mDesc.setText(workout.getDescription());
            } else holder.mDesc.setVisibility(View.GONE);
            String[] weekDaysText = DateFormatSymbols.getInstance().getShortWeekdays();
            ArrayList<Boolean> weekDays = DateUtils.parseWeekDays(workout.getWeekDaysComposed());
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < 7; i++) {
                if (weekDays.get(i)) {
                    sb.append(weekDaysText[i + 1] + " ");
                }
            }
            Integer exercisesCount = workout.getExercisesCount();
            if (exercisesCount != null) {
                holder.mExercises.setText(exercisesCount.toString());
            }

            holder.mDays.setText(sb.toString());
            holder.mStartTime.setText(DateUtils.getTimeString(workout.getStartTime()));
            holder.mTotalTime.setText(DateUtils.getTimeString(workout.getTotalTime()));
            //holder.mCard.setCardBackgroundColor(workout.isMarked() ? markedCardColor : unmarkedCardColor);
            //holder.mMarkIcon.setVisibility(workout.isMarked() ? View.VISIBLE : View.INVISIBLE);

            holder.itemView.setTag(position);

            if (workout == mPickedWorkout) {
                holder.itemView.setSelected(true);
            } else {
                holder.itemView.setSelected(false);
            }

            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);

        }

        @Override
        public int getItemCount() {
            return mWorkouts.size();
        }

        @Override
        public void onClick(View v) {

            WorkoutsViewHolder viewHolder = (WorkoutsViewHolder) recyclerView.findViewHolderForAdapterPosition((Integer) v.getTag());
            if (mPickedWorkout == null) {
                sWorkoutsViewModel.openWorkout(viewHolder.getWorkout());
                return;
            }
            sWorkoutsViewModel.pickWorkout(viewHolder.getWorkout());
        }

        @Override
        public boolean onLongClick(View v) {
            WorkoutsViewHolder viewHolder = (WorkoutsViewHolder) recyclerView.findViewHolderForAdapterPosition((Integer) v.getTag());
            if (viewHolder != null) {
                sWorkoutsViewModel.pickWorkout(viewHolder.getWorkout());
            }
            return true;
        }
    }

    private class WorkoutsViewHolder extends RecyclerView.ViewHolder {


        private Workout mWorkout;
        private TextView mName;
        private TextView mDesc;
        private TextView mExercises;
        private TextView mTotalTime;
        private TextView mDays;
        private TextView mStartTime;
        private CardView mCard;
        private ImageView mMarkIcon;

        public WorkoutsViewHolder(View itemView) {
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

        public Workout getWorkout() {
            return mWorkout;
        }

        public void setWorkout(Workout workout) {
            mWorkout = workout;
        }

    }
}
