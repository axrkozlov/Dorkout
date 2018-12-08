package com.axfex.dorkout.views.workouts.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.views.exercises.list.ExercisesActivity;
import com.axfex.dorkout.util.DateUtils;
import com.axfex.dorkout.vm.ViewModelFactory;
import com.axfex.dorkout.vm.WorkoutsViewModel;
import com.axfex.dorkout.views.workouts.edit.EditWorkoutActivity;
import com.axfex.dorkout.data.Workout;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class WorkoutsFragment extends Fragment {

    private static WorkoutsViewModel sWorkoutsViewModel;
    private RecyclerView recyclerView;
    private WorkoutsAdapter mAdapter;
    private List<Workout> mWorkouts;
    private Long pickedId;


    private static final String WORKOUT_ID = "workout_id";


    public WorkoutsFragment() {
        // Required empty public constructor
    }


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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //TODO:show message ok
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workouts, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = v.findViewById(R.id.rv_workouts);
        recyclerView.setLayoutManager(layoutManager);

        sWorkoutsViewModel.getWorkouts().observe(this, workouts -> setListData(workouts));
        pickedId=sWorkoutsViewModel.getPickedId();


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setListData(List<Workout> listOfData) {
        this.mWorkouts = listOfData;
        swapAdapter();
    }

    public void swapAdapter() {
        mAdapter = new WorkoutsAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    private void updateWorkout(Long id) {
        //Toast.makeText(getContext(), id.toString(), Toast.LENGTH_SHORT).show();
        //swapAdapter();
    }



    private class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsViewHolder> {
        private View picked;

        @Override
        public WorkoutsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.item_workout, parent, false);
            WorkoutsViewHolder viewHolder = new WorkoutsViewHolder(view);
            sWorkoutsViewModel.onPick().observe(WorkoutsFragment.this, isPicked-> onPick(isPicked));
            return viewHolder;
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

            if (pickedId==workout.getId()){pick(holder.itemView);}
//            if (holder.selectedPosition == position) {
//                holder.itemView.setSelected(true);
//            } else {
//                holder.itemView.setSelected(false);
//            }
        }

        @Override
        public int getItemCount() {
            return mWorkouts.size();
        }



        private void onPick(Boolean isPicked) {
            pickedId=sWorkoutsViewModel.getPickedId();
            if (!isPicked) {unpick();}
        }

        private void onViewPicked(View view) {
            unpick();
            pick(view);
        }

        private void pick(View view) {
            if (view != picked) {
                view.setSelected(true);
                picked = view;
                TextView text = view.findViewById(R.id.workout_title);
                sWorkoutsViewModel.pick((Long)view.getTag(),text.getText().toString());
            } else {
                sWorkoutsViewModel.unpick();
                picked = null;
            }
        }

        private void unpick() {
            if (picked != null) {
                picked.setSelected(false);
            }
        }


    }

    private class WorkoutsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

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
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

            mName = itemView.findViewById(R.id.workout_title);
            mDesc = itemView.findViewById(R.id.workout_desc);
            mExercises = itemView.findViewById(R.id.workout_desc_exercises);
            mTotalTime = itemView.findViewById(R.id.workout_desc_total_time);
            mDays = itemView.findViewById(R.id.workout_desc_days);
            mStartTime = itemView.findViewById(R.id.workout_desc_start_time);
            mCard = itemView.findViewById(R.id.workout_card);
            mMarkIcon = itemView.findViewById(R.id.workout_mark_icon);


        }

        @Override
        public void onClick(View v) {

            if (mAdapter.picked != null) {
                mAdapter.onViewPicked(v);
            } else {
                startExercisesActivity(mWorkouts.get(this.getAdapterPosition()).getId());
            }

        }

        @Override
        public boolean onLongClick(View v) {

            mAdapter.onViewPicked(v);

//            oldSelectedPosition = getAdapterPosition();
//            mAdapter.onViewPicked(selectedPosition,oldSelectedPosition);
//            oldSelectedPosition=selectedPosition;

//            mark(mWorkouts.get(getAdapterPosition()));
//            mWorkouts.get(getAdapterPosition());
//            mark(markedWorkout);
//            swapAdapter();
            //showAddEditWorkoutActivity(mWorkouts.get(this.getAdapterPosition()).getId());
            return true;
        }

    }

    private void startExercisesActivity(Long workoutId) {
        Intent i = new Intent(getActivity(), ExercisesActivity.class);
        i.putExtra(WORKOUT_ID, workoutId);
        startActivity(i);
    }

    public void showAddEditWorkoutActivity() {
        startActivityForResult(
                new Intent(getActivity(), EditWorkoutActivity.class), EditWorkoutActivity.REQUEST_ADD_TASK);
    }

    private void showAddEditWorkoutActivity(Long workoutId) {
        Intent i = new Intent(getActivity(), EditWorkoutActivity.class);
        i.putExtra(WORKOUT_ID, workoutId);
        startActivityForResult(i, EditWorkoutActivity.REQUEST_ADD_TASK);
    }



}
