package com.axfex.dorkout.views.workouts.list;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.axfex.dorkout.R;
import com.axfex.dorkout.views.workouts.edit.EditWorkoutViewModel;

public class EditWorkoutFragment extends Fragment {
    private Button mCreate;
    private Button mUpdate;
    private Button mDuplicate;
    private Button mDelete;

    private Long workoutId;
    private static final String WORKOUT_ID = "workout_id";

    private static EditWorkoutViewModel sEditWorkoutViewModel;


    public static EditWorkoutFragment newInstance() {
        EditWorkoutFragment fragment = new EditWorkoutFragment();
        return fragment;
    }

    public static void attachViewModel(EditWorkoutViewModel editWorkoutViewModel) {
        sEditWorkoutViewModel = editWorkoutViewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.edit_workout_fragment, container, false);

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
                break;
            }
            case R.id.homeAsUp: {
            }
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.bt_workout_start_time:
//                break;
////            case R.id.bt_workout_delete:
////                deleteWorkout();
////                break;
//        }
//    }




}
