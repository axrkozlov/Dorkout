package com.axfex.dorkout.views.workouts;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.axfex.dorkout.R;

public class EditWorkoutFragment extends Fragment {
    public static final String TAG = "EDIT_WORKOUT_FRAGMENT";
    private Button mCreate;
    private Button mUpdate;
    private Button mDuplicate;
    private Button mDelete;

    private Long workoutId;
    private static final String WORKOUT_ID = "workout_id";

    private static MainViewModel sMainViewModel;


    public static EditWorkoutFragment newInstance() {
        EditWorkoutFragment fragment = new EditWorkoutFragment();
        return fragment;
    }

    public static void attachViewModel(MainViewModel workoutViewModel) {
        sMainViewModel = workoutViewModel;
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
