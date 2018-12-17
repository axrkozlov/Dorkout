package com.axfex.dorkout.views.workouts.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    private static WorkoutsViewModel sWorkoutsViewModel;
    private RecyclerView recyclerView;
    private WorkoutsAdapter mAdapter;
    private List<Workout> mWorkouts;

    public static WorkoutsFragment newInstance() {
        WorkoutsFragment fragment = new WorkoutsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void attachViewModel (WorkoutsViewModel workoutsViewModel){
        sWorkoutsViewModel=workoutsViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.workouts_fragment, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = v.findViewById(R.id.rv_workouts);
        recyclerView.setLayoutManager(layoutManager);

        sWorkoutsViewModel.getWorkouts().observe(this, workouts -> setListData(workouts));

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setListData(List<Workout> listOfData) {
        this.mWorkouts = listOfData;
        swapAdapter();
    }

    public void swapAdapter() {
        mAdapter = new WorkoutsAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    private class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsViewHolder> implements View.OnClickListener, View.OnLongClickListener {
        private View pickedView;
        private View newPickedView;
        private Long pickedId;

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            sWorkoutsViewModel.getPickEvent().observe(WorkoutsFragment.this, isPicked-> onWorkoutPicked(isPicked));
        }

        @Override
        public WorkoutsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.workout_item, parent, false);
            WorkoutsViewHolder viewHolder = new WorkoutsViewHolder(view);

            return viewHolder;
        }

        private void onWorkoutPicked(Boolean isPicked) {
            unpickView(pickedView);
            if (isPicked) {
                pickedId = sWorkoutsViewModel.getPickedId();
                pickView(newPickedView);
            }
        }

        private void unpickView(View view){
            if (view != null) {
                view.setSelected(false);
                pickedView=null;
            }
        }

        private void pickView(View view){
            if (view != null) {
                view.setSelected(true);
                pickedView= view;
            }
        }

        private void setNewPickedView(View view){
            newPickedView =view;
        }

        @Override
        public void onBindViewHolder(WorkoutsViewHolder holder, int position) {

            if (mWorkouts.size() == 0) {
                return;
            }
            Workout workout = mWorkouts.get(position);
            if (workout == null) {
                return;
            }

            holder.mName.setText(workout.getName());
            if (workout.getDescription() != null) {
                holder.mDesc.setText(workout.getDescription());
            } else holder.mDesc.setVisibility(View.GONE);
            String[] weekDaysText = DateFormatSymbols.getInstance().getShortWeekdays();
            ArrayList<Boolean> weekDays = DateUtils.parseWeekDays(workout.getWeekDaysComposed());
            StringBuffer sb = new StringBuffer();

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

            holder.itemView.setTag(workout.getId());

            if (pickedId==workout.getId()){
                pickView(holder.itemView);
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

            if (sWorkoutsViewModel.isWorkoutPicked()){
                setNewPickedView(v);
                TextView name = v.findViewById(R.id.workout_title);
                sWorkoutsViewModel.pick((Long)v.getTag(),name.getText().toString());


            } else {
                sWorkoutsViewModel.openWorkout( (Long) v.getTag());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            setNewPickedView(v);
            TextView name = v.findViewById(R.id.workout_title);
            sWorkoutsViewModel.pick((Long)v.getTag(),name.getText().toString());
            return true;
        }
    }

    private class WorkoutsViewHolder extends RecyclerView.ViewHolder  {

        TextView mName;
        TextView mDesc;
        TextView mExercises;
        TextView mTotalTime;
        TextView mDays;
        TextView mStartTime;
        CardView mCard;
        ImageView mMarkIcon;

        public WorkoutsViewHolder(View itemView) {
            super(itemView);
//            itemView.setOnLongClickListener(this);
//            itemView.setOnClickListener(this);

            mName = itemView.findViewById(R.id.workout_title);
            mDesc = itemView.findViewById(R.id.workout_desc);
            mExercises = itemView.findViewById(R.id.workout_desc_exercises);
            mTotalTime = itemView.findViewById(R.id.workout_desc_total_time);
            mDays = itemView.findViewById(R.id.workout_desc_days);
            mStartTime = itemView.findViewById(R.id.workout_desc_start_time);
            mCard = itemView.findViewById(R.id.workout_card);
            mMarkIcon = itemView.findViewById(R.id.workout_mark_icon);


        }

//        @Override
//        public void onClick(View v) {
//
//
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//
//            mAdapter.onViewPicked(v);
//
////            oldSelectedPosition = getAdapterPosition();
////            mAdapter.onViewPicked(selectedPosition,oldSelectedPosition);
////            oldSelectedPosition=selectedPosition;
//
////            mark(mWorkouts.get(getAdapterPosition()));
////            mWorkouts.get(getAdapterPosition());
////            mark(markedWorkout);
////            swapAdapter();
//            //showAddEditWorkoutActivity(mWorkouts.get(this.getAdapterPosition()).getId());
//            return true;
//        }

    }



//    public void showAddEditWorkoutActivity() {
//        startActivityForResult(
//                new Intent(getActivity(), EditWorkoutActivity.class), EditWorkoutActivity.REQUEST_ADD_TASK);
//    }
//
//    private void showAddEditWorkoutActivity(Long workoutId) {
//        Intent i = new Intent(getActivity(), EditWorkoutActivity.class);
//        i.putExtra(WORKOUT_ID, workoutId);
//        startActivityForResult(i, EditWorkoutActivity.REQUEST_ADD_TASK);
//    }



}
