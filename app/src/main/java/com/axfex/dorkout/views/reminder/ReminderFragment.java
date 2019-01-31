package com.axfex.dorkout.views.reminder;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.axfex.dorkout.R;


public class ReminderFragment extends Fragment {
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

    private ReminderViewModel mViewModel;

    public static ReminderFragment newInstance() {
        return new ReminderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.reminder_fragment, container, false);


        //mName =v.findViewById(R.id.et_exercise_name);
//        mCreate = v.findViewById(R.id.bt_workout_create);
//        mUpdate = v.findViewById(R.id.bt_workout_update);
//        mDuplicate = v.findViewById(R.id.bt_workout_duplicate);
//        mDelete = v.findViewById(R.id.bt_workout_delete);
        mStartTime = v.findViewById(R.id.bt_workout_start_time);
        //mName = v.findViewById(R.id.et_workout_name);
        //mDesc = v.findViewById(R.id.et_workout_desc);
        mDay1 = v.findViewById(R.id.tb_workout_day1);
        mDay2 = v.findViewById(R.id.tb_workout_day2);
        mDay3 = v.findViewById(R.id.tb_workout_day3);
        mDay4 = v.findViewById(R.id.tb_workout_day4);
        mDay5 = v.findViewById(R.id.tb_workout_day5);
        mDay6 = v.findViewById(R.id.tb_workout_day6);
        mDay7 = v.findViewById(R.id.tb_workout_day7);

//        mCreate.setOnClickListener(this);
        //mStartTime.setOnClickListener(this);
//        mUpdate.setOnClickListener(this);
//        mDuplicate.setOnClickListener(this);
//        mDelete.setOnClickListener(this);


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ReminderViewModel.class);
        // TODO: Use the ViewModel
    }

    private boolean checkNameField(){
        if (mName.getText().length() == 0) {
            Toast.makeText(getContext(), "Please, type Name of workout", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

//    private Workout buildWorkout(){
//
//        Workout workout=new Workout(mName.getText().toString());
//        if (workoutId != 0) {
//            workout.setId(workoutId);
//        } else {
//            workout.setLastStartTime(0L);
//            workout.setTotalExercisesTime(0L);
//            workout.setExercisesCount(0);
//        }
//
//        workout.setNote(mDesc.getText().toString());
//        ArrayList<Boolean> weekDays = new ArrayList<>();
//        weekDays.add(mDay1.isChecked());
//        weekDays.add(mDay2.isChecked());
//        weekDays.add(mDay3.isChecked());
//        weekDays.add(mDay4.isChecked());
//        weekDays.add(mDay5.isChecked());
//        weekDays.add(mDay6.isChecked());
//        weekDays.add(mDay7.isChecked());
//
//        workout.setWeekDaysComposed(DateUtils.composeWeekDays(weekDays));
//
//        workout.setLastStartTime(Calendar.getInstance().getTimeInMillis());
//
//        String startTimeText = mStartTime.getText().toString();
//        if (!startTimeText.equals(getResources().getShowTag(R.string.workout_id_tag))) {
//            long startTimeLong = DateUtils.getTimeMillis(startTimeText);
//            workout.setStartTime(startTimeLong);
//        }
//
//        return workout;
//    }
//
//    private void bindWorkoutToEdit(Workout workout) {
//
//        if (workout == null) {
//            return;
//        }
//
////        mCreate.setVisibility(View.GONE);
////        mUpdate.setVisibility(View.VISIBLE);
////        mDuplicate.setVisibility(View.VISIBLE);
////        mDelete.setVisibility(View.VISIBLE);
//
//        mName.setText(workout.getName());
//        mDesc.setText(workout.getNote());
//        ArrayList<Boolean> weekDays = DateUtils.parseWeekDays(workout.getWeekDaysComposed());
//        mDay1.setChecked(weekDays.get(0));
//        mDay2.setChecked(weekDays.get(1));
//        mDay3.setChecked(weekDays.get(2));
//        mDay4.setChecked(weekDays.get(3));
//        mDay5.setChecked(weekDays.get(4));
//        mDay6.setChecked(weekDays.get(5));
//        mDay7.setChecked(weekDays.get(6));
//        mStartTime.setText(DateUtils.getTimeString(workout.getStartTime()));
//
//    }

//    public void saveWorkout(){
//        if (!checkNameField()){
//            return;
//        }
//        Workout onNewWorkout = buildWorkout();
//        if (onNewWorkout == null) {
//            return;
//        }
//        if (workoutId == 0) {
//            sEditWorkoutViewModel.addWorkout(onNewWorkout);
//        } else {
//            sEditWorkoutViewModel.updateWorkout(onNewWorkout);
//        }
//        close();
//    }
//
//
//    public void onDeleteWorkout(){
//        sEditWorkoutViewModel.onDeleteWorkout(new Workout(workoutId));
//        close();
//    }
//
//    public void showTimePickerDialog() {
//        Calendar calendar = Calendar.getInstance();
//        int currentHour = calendar.get(Calendar.HOUR);
//        int currentMinute = calendar.get(Calendar.MINUTE);
//
//        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), this
//                , currentHour, currentMinute, true);
//        timePickerDialog.show();
//    }
//
//    @Override
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        mStartTime.setText(String.format("%02d:%02d", hourOfDay, minute));
//    }
//
//    private void close() {
//        getActivity().setResult(Activity.RESULT_OK);
//        getActivity().finish();
//
//    }

}
