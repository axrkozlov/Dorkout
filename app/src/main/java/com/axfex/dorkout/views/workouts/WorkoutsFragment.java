package com.axfex.dorkout.views.workouts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.vm.ViewModelFactory;

import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;


public class WorkoutsFragment extends Fragment implements WorkoutsNavigator {
    public static final String TAG = "WORKOUTS_FRAGMENT";

    @Inject
    ViewModelFactory<WorkoutsViewModel> mViewModelFactory;
    private WorkoutsViewModel mWorkoutsViewModel;

    private MainViewModel mMainViewModel;
    private RecyclerView mRecyclerView;
    private WorkoutsAdapter mAdapter;
    private List<Workout> mWorkouts;
    private boolean isAnyWorkoutPicked;
    private Workout mPickedWorkout;
    private Menu mMenu;
    private boolean isWorkoutMenuShown;
    private boolean doNotUpdateActionBar;


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
        mMainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
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
        doNotUpdateActionBar = false;
        mWorkoutsViewModel.getWorkouts().observe(this, this::onListLoaded);
        mWorkoutsViewModel.getPickedWorkout().observe(WorkoutsFragment.this, this::onWorkoutPicked);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.workouts_menu, menu);

        mMenu = menu;
        updateActionBar();
    }

    private void updateActionBar() {
        if (doNotUpdateActionBar)  return;

        ActionBar actionBar = ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(isAnyWorkoutPicked);
        String title = mPickedWorkout != null ? mPickedWorkout.getName() : getString(R.string.title_activity_workouts);
        actionBar.setTitle(title);
        mMenu.findItem(R.id.menu_workouts_add).setVisible(!isWorkoutMenuShown);
        mMenu.findItem(R.id.menu_workouts_copy).setVisible(isWorkoutMenuShown);
        mMenu.findItem(R.id.menu_workouts_edit).setVisible(isWorkoutMenuShown);
        mMenu.findItem(R.id.menu_workouts_rename).setVisible(isWorkoutMenuShown);
        mMenu.findItem(R.id.menu_workouts_delete).setVisible(isWorkoutMenuShown);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mWorkoutsViewModel.pickWorkout(null);
                break;
            }
            case R.id.menu_workouts_add: {
                onNewWorkout();
                break;
            }
            case R.id.menu_workouts_edit: {
                onOpenEditWorkout(mPickedWorkout);
                break;
            }
            case R.id.menu_workouts_rename: {
                onRenameWorkout(mPickedWorkout);
                break;
            }
            case R.id.menu_workouts_delete: {
                onDeleteWorkout(mPickedWorkout);
                break;
            }
        }


        return super.onOptionsItemSelected(item);
    }

    private void onListLoaded(List<Workout> workouts) {
        this.mWorkouts = workouts;
        swapAdapter();
    }

    private void onWorkoutPicked(Workout workout) {
        mPickedWorkout = workout;
        notifyAdapter();
        isWorkoutMenuShown = workout != null;
        isAnyWorkoutPicked = workout != null;
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    private void swapAdapter() {
        mAdapter = new WorkoutsAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private void notifyAdapter() {
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onNewWorkout() {
        showNameDialog(null);
    }

    @Override
    public void onRenameWorkout(Workout workout) {
        showNameDialog(workout);
    }

    @Override
    public void onOpenEditWorkout(Workout workout) {
        mMainViewModel.openEditWorkout(workout.getId());
        doNotUpdateActionBar=true;
        mWorkoutsViewModel.pickWorkout(null);
    }

    @Override
    public void onOpenActionWorkout(Workout workout) {
        mMainViewModel.openActionWorkout(workout.getId());
        doNotUpdateActionBar=true;
        mWorkoutsViewModel.pickWorkout(null);
    }

    @Override
    public void onDeleteWorkout(Workout workout) {
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                //TODO:make resource
                .setTitle("Delete " + workout.getName() + "?")
                .setPositiveButton(R.string.bt_ok, (d, i) -> onDeleteDialogOk(workout))
                .setNegativeButton(R.string.bt_cancel, (d, i) -> {
                })
                .create()
                .show();

    }
    private void onDeleteDialogOk(Workout workout) {
        mWorkoutsViewModel.deleteWorkout(workout);
        mWorkoutsViewModel.pickWorkout(null);
    }

    private void showNameDialog(@Nullable Workout workout) {

        EditText workoutEditName = new EditText(getContext());
        String dialogTitle;
        //TODO: create resource
        if (workout == null) {
            dialogTitle = "New Workout";
        } else {
            workoutEditName.setText(workout.getName());
            dialogTitle = "Rename";
        }
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setTitle(dialogTitle)
                .setView(workoutEditName)
                .setPositiveButton(R.string.bt_ok, (d, i) -> onNameDialogOk(workoutEditName.getText().toString(), workout))
                .setNegativeButton(R.string.bt_cancel, (d, i) -> {
                })
                .create()
                .show();
    }

    private void onNameDialogOk(String name, Workout workout) {
        if (workout == null) {
            mWorkoutsViewModel.getWorkouts().observe(this, this::onListLoaded);
            mWorkoutsViewModel.createWorkout(new Workout(name)).observe(this, id -> onOpenEditWorkout(new Workout(id)));
        } else {
            workout.setName(name);
            mWorkoutsViewModel.updateWorkout(workout);
        }
        mWorkoutsViewModel.pickWorkout(null);
    }

    private class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutViewHolder> {

        @NonNull
        @Override
        public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.workout_item, parent, false);
            return new WorkoutViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
            if (mWorkouts.size() == 0) return;
            holder.setWorkout(mWorkouts.get(position));
        }

        @Override
        public int getItemCount() {
            return mWorkouts.size();
        }
    }

    private class WorkoutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private Workout mWorkout;
        private Integer mViewPosition;
        private TextView mName;
        private TextView mDesc;
        private TextView mExercises;
        private TextView mTotalTime;
        private TextView mDays;
        private TextView mStartTime;
        private CardView mCard;
        private ImageView mActiveIcon;

        private WorkoutViewHolder(View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.workout_title);
            mDesc = itemView.findViewById(R.id.workout_desc);
            mExercises = itemView.findViewById(R.id.workout_desc_exercises);
            mTotalTime = itemView.findViewById(R.id.workout_desc_total_time);
            mDays = itemView.findViewById(R.id.workout_desc_days);
            mStartTime = itemView.findViewById(R.id.workout_desc_start_time);
            mCard = itemView.findViewById(R.id.workout_card);
            mActiveIcon = itemView.findViewById(R.id.workout_active_icon);

        }

        private void bindData() {
            mName.setText(mWorkout.getName());
            if (mWorkout.getNote() != null) {
                mDesc.setText(mWorkout.getNote());
            } else mDesc.setVisibility(View.GONE);
            String[] weekDaysText = DateFormatSymbols.getInstance().getShortWeekdays();
//            ArrayList<Boolean> weekDays = FormatUtils.parseWeekDays(mWorkout.getWeekDaysComposed());
//            StringBuilder sb = new StringBuilder();
//
//            for (int i = 0; i < 7; i++) {
//                if (weekDays.get(i)) {
//                    sb.append(weekDaysText[i + 1] + " ");
//                }
//            }
            Integer exercisesCount = mWorkout.getExercisesCount();
            if (exercisesCount != null) {
                mExercises.setText(exercisesCount.toString());
            }

//            mDays.setText(sb.toString());
//            mStartTime.setText(mWorkout.getStartTime().toString());
//            mTotalTime.setText(FormatUtils.getTimeString(mWorkout.getCurrentTime()));
            //holder.mCard.setCardBackgroundColor(workout.isMarked() ? markedCardColor : unmarkedCardColor);
            //holder.mActiveIcon.setVisibility(workout.isMarked() ? View.VISIBLE : View.INVISIBLE);

            itemView.setTag(mWorkout.getId());
            itemView.setSelected(false);
            if (mPickedWorkout != null)
                if (mWorkout.getId().equals(mPickedWorkout.getId())) {
                    itemView.setSelected(true);
                }

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        public void setWorkout(Workout workout) {
            mWorkout = workout;
            bindData();
        }

        @Override
        public void onClick(View v) {
            if (!isAnyWorkoutPicked) {
                mMainViewModel.openActionWorkout(mWorkout.getId());
                return;
            }
            mWorkoutsViewModel.pickWorkout(mWorkout);
            //For Tests
//            onOpenEditWorkout(mWorkout);
        }

        @Override
        public boolean onLongClick(View v) {
//            mWorkoutsViewModel.pickWorkout(mWorkout);
            onOpenEditWorkout(mWorkout);
            return true;
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }


}
