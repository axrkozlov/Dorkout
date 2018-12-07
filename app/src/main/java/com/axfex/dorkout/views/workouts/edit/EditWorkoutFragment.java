package com.axfex.dorkout.views.workouts.edit;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.util.DateUtils;
import com.axfex.dorkout.vm.AddEditWorkoutViewModel;
import com.axfex.dorkout.vm.ViewModelFactory;

import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

public class EditWorkoutFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener{
    private Button mCreate;
    private Button mUpdate;
    private Button mDuplicate;
    private Button mDelete;
    private Button mStartTime;
    private EditText mName;
    private EditText mDesc;
    private ToggleButton mDay1;
    private ToggleButton mDay2;
    private ToggleButton mDay3;
    private ToggleButton mDay4;
    private ToggleButton mDay5;
    private ToggleButton mDay6;
    private ToggleButton mDay7;
    private Long workoutId;
    private static final String WORKOUT_ID = "workout_id";

    @Inject
    ViewModelFactory viewModelFactory;
    AddEditWorkoutViewModel addEditWorkoutViewModel;


    public static EditWorkoutFragment newInstance(@Nullable Long workoutId) {
        EditWorkoutFragment fragment = new EditWorkoutFragment();
        Bundle args = new Bundle();
        if (workoutId != null) {
            args.putLong(WORKOUT_ID, workoutId);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            workoutId = getArguments().getLong(WORKOUT_ID);
        }
        ((WorkoutApplication) getActivity().getApplication())
                .getAppComponent()
                .inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        addEditWorkoutViewModel= ViewModelProviders.of(this,viewModelFactory).get(AddEditWorkoutViewModel.class);
        addEditWorkoutViewModel.getWorkout(workoutId).observe(this, new Observer<Workout>() {
            @Override
            public void onChanged(@Nullable Workout workout) {
                if (workout != null) {
                    bindWorkoutToEdit(workout);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_add_edit_workout, container, false);

        mName =v.findViewById(R.id.et_exercise_name);
//        mCreate = v.findViewById(R.id.bt_workout_create);
//        mUpdate = v.findViewById(R.id.bt_workout_update);
//        mDuplicate = v.findViewById(R.id.bt_workout_duplicate);
//        mDelete = v.findViewById(R.id.bt_workout_delete);
        mStartTime = v.findViewById(R.id.bt_workout_start_time);
        mName = v.findViewById(R.id.et_workout_name);
        mDesc = v.findViewById(R.id.et_workout_desc);
        mDay1 = v.findViewById(R.id.tb_workout_day1);
        mDay2 = v.findViewById(R.id.tb_workout_day2);
        mDay3 = v.findViewById(R.id.tb_workout_day3);
        mDay4 = v.findViewById(R.id.tb_workout_day4);
        mDay5 = v.findViewById(R.id.tb_workout_day5);
        mDay6 = v.findViewById(R.id.tb_workout_day6);
        mDay7 = v.findViewById(R.id.tb_workout_day7);

//        mCreate.setOnClickListener(this);
        mStartTime.setOnClickListener(this);
//        mUpdate.setOnClickListener(this);
//        mDuplicate.setOnClickListener(this);
//        mDelete.setOnClickListener(this);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_addeditworkout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_addeditworkout_save: {
                saveWorkout();
                break;
            }
            case R.id.homeAsUp: {
                close();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_workout_start_time:
                showTimePickerDialog();
                break;
//            case R.id.bt_workout_delete:
//                deleteWorkout();
//                break;
        }
    }

    private boolean checkNameField(){
        if (mName.getText().length() == 0) {
            Toast.makeText(getContext(), "Please, type Name of workout", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Workout buildWorkout(){

        Workout workout=new Workout(mName.getText().toString());
        if (workoutId != 0) {
            workout.setId(workoutId);
        } else {
            workout.setLastDate(0L);
            workout.setTotalTime(0L);
            workout.setExercisesCount(0);
        }

        workout.setDescription(mDesc.getText().toString());
        ArrayList<Boolean> weekDays = new ArrayList<>();
        weekDays.add(mDay1.isChecked());
        weekDays.add(mDay2.isChecked());
        weekDays.add(mDay3.isChecked());
        weekDays.add(mDay4.isChecked());
        weekDays.add(mDay5.isChecked());
        weekDays.add(mDay6.isChecked());
        weekDays.add(mDay7.isChecked());

        workout.setWeekDaysComposed(DateUtils.composeWeekDays(weekDays));

        workout.setLastDate(Calendar.getInstance().getTimeInMillis());

        String startTimeText = mStartTime.getText().toString();
        if (!startTimeText.equals(getResources().getString(R.string.workout_id_tag))) {
            long startTimeLong = DateUtils.getTimeMillis(startTimeText);
            workout.setStartTime(startTimeLong);
        }

        return workout;
    }

    private void bindWorkoutToEdit(Workout workout) {

        if (workout == null) {
            return;
        }

//        mCreate.setVisibility(View.GONE);
//        mUpdate.setVisibility(View.VISIBLE);
//        mDuplicate.setVisibility(View.VISIBLE);
//        mDelete.setVisibility(View.VISIBLE);

        mName.setText(workout.getName());
        mDesc.setText(workout.getDescription());
        ArrayList<Boolean> weekDays = DateUtils.parseWeekDays(workout.getWeekDaysComposed());
        mDay1.setChecked(weekDays.get(0));
        mDay2.setChecked(weekDays.get(1));
        mDay3.setChecked(weekDays.get(2));
        mDay4.setChecked(weekDays.get(3));
        mDay5.setChecked(weekDays.get(4));
        mDay6.setChecked(weekDays.get(5));
        mDay7.setChecked(weekDays.get(6));
        mStartTime.setText(DateUtils.getTimeString(workout.getStartTime()));

    }

    public void saveWorkout(){
        if (!checkNameField()){
            return;
        }
        Workout newWorkout = buildWorkout();
        if (newWorkout == null) {
            return;
        }
        if (workoutId == 0) {
            addEditWorkoutViewModel.addWorkout(newWorkout);
        } else {
            addEditWorkoutViewModel.updateWorkout(newWorkout);
        }
        close();
    }


    public void deleteWorkout(){
        addEditWorkoutViewModel.deleteWorkout(new Workout(workoutId));
        close();
    }

    public void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR);
        int currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), this
                , currentHour, currentMinute, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mStartTime.setText(String.format("%02d:%02d", hourOfDay, minute));
    }

    private void close() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();

    }



}
