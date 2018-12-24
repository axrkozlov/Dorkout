package com.axfex.dorkout;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.axfex.dorkout.views.exercises.list.ExercisesActivity;

import javax.inject.Inject;

public class Navigator {


    WorkoutApplication mWorkoutApplication;

    private static final String WORKOUT_ID = "workout_id";
    private Context mContext;

    @Inject
    public Navigator(WorkoutApplication workoutApplication) {
        mWorkoutApplication = workoutApplication;
        mContext=mWorkoutApplication.getApplicationContext();
//        Log.i("NAVIGATOR", "Navigator: "+mContext);

    }

    public void openWorkout(Long id){
        Log.i(WORKOUT_ID, "onOpenWorkout: ");
        Intent i = new Intent(mWorkoutApplication.getApplicationContext(), ExercisesActivity.class);
        i.putExtra(WORKOUT_ID, id);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }
}
