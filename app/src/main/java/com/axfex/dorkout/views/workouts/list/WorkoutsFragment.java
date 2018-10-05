package com.axfex.dorkout.views.workouts.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.views.exercises.list.ExercisesActivity;
import com.axfex.dorkout.util.DateUtils;
import com.axfex.dorkout.vm.WorkoutsViewModel;
import com.axfex.dorkout.views.workouts.addedit.AddEditWorkoutActivity;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.vm.ViewModelFactory;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class WorkoutsFragment extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;

    private WorkoutsViewModel workoutsViewModel;
    private RecyclerView recyclerView;
    private WorkoutsAdapter mAdapter;
    private List<Workout> mWorkouts;
    private Workout markedWorkout;
    private Long selectedWorkoutId;


    private Integer markedCardColor;
    private Integer unmarkedCardColor;

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
        ((WorkoutApplication) getActivity().getApplication())
                .getAppComponent()
                .inject(this);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        Objects.requireNonNull(
                ((AppCompatActivity) Objects.requireNonNull(getActivity()))
                        .getSupportActionBar()
        )
                .setDisplayHomeAsUpEnabled(true);

        markedCardColor = getResources().getColor(R.color.workout_item_marked);
        unmarkedCardColor = getResources().getColor(R.color.workout_item);

        workoutsViewModel = ViewModelProviders.of(this, viewModelFactory).get(WorkoutsViewModel.class);
        workoutsViewModel.getWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(@Nullable List<Workout> workouts) {
                setListData(workouts);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //TODO:show message ok
    }

    public void setListData(List<Workout> listOfData) {
        this.mWorkouts = listOfData;
        swapAdapter();
    }

    public void swapAdapter() {
        mAdapter = new WorkoutsAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_workouts, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = v.findViewById(R.id.rv_workouts);
        recyclerView.setLayoutManager(layoutManager);
//        FloatingActionButton fabulous = v.findViewById(R.id.fab_add_workout);
//        fabulous.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startAddEditActivity();
//            }
//        });

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_workouts, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_workouts_add: {
                startAddEditActivity();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsViewHolder> {
        private View selectedView;

        @Override
        public WorkoutsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.item_workout, parent, false);
            WorkoutsViewHolder viewHolder = new WorkoutsViewHolder(view);

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
            if (workout.getDescription().length() > 0) {
                holder.mDesc.setText(workout.getDescription());
            } else holder.mDesc.setVisibility(View.GONE);
            String[] weekDaysText = DateFormatSymbols.getInstance().getShortWeekdays();
            ArrayList<Boolean> weekDays = DateUtils.parseWeekDays(workout.getWeekDaysComposed());
            StringBuffer sb = new StringBuffer("");

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

        private void onOneItemSelected(View newSelectedView) {
            newSelectedView.setSelected(true);
            if (selectedView != null) {
                selectedView.setSelected(false);
            }
            selectedView = newSelectedView;

        }

        public View getSelectedView() {
            return selectedView;
        }

        public void setSelectedView(View selectedView) {
            this.selectedView = selectedView;
            selectedView.setSelected(true);
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
            mAdapter.getSelectedView().setSelected(false);
            mAdapter.setSelectedView(v);
            if (mAdapter.selectedView!=null){
                mAdapter.onOneItemSelected(v);
            }
//
//            if  (markedWorkout != null) {
//               // mark(mWorkouts.get(getAdapterPosition()));
////                mark(null);
////                swapAdapter();
//            } else {
////                startExercisesActivity(mWorkouts.get(this.getAdapterPosition()).getId());
//            }

        }

        @Override
        public boolean onLongClick(View v) {

            mAdapter.onOneItemSelected(v);

//            oldSelectedPosition = getAdapterPosition();
//            mAdapter.onOneItemSelected(selectedPosition,oldSelectedPosition);
//            oldSelectedPosition=selectedPosition;

//            mark(mWorkouts.get(getAdapterPosition()));
//            mWorkouts.get(getAdapterPosition());
//            mark(markedWorkout);
//            swapAdapter();
            return true;
        }

    }

    private void startExercisesActivity(Long workoutId) {
        Intent i = new Intent(getActivity(), ExercisesActivity.class);
        i.putExtra(WORKOUT_ID, workoutId);
        startActivity(i);
    }

    public void startAddEditActivity() {
        startActivityForResult(
                new Intent(getActivity(), AddEditWorkoutActivity.class), AddEditWorkoutActivity.REQUEST_ADD_TASK);
    }

    private void startAddEditActivity(Long workoutId) {
        Intent i = new Intent(getActivity(), AddEditWorkoutActivity.class);
        i.putExtra(WORKOUT_ID, workoutId);
        startActivityForResult(i, AddEditWorkoutActivity.REQUEST_ADD_TASK);
    }

}
