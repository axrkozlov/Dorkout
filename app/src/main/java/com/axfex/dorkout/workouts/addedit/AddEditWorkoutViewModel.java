package com.axfex.dorkout.workouts.addedit;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class AddEditWorkoutViewModel extends ViewModel {

    private WorkoutsRepository workoutsRepository;

    public AddEditWorkoutViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public void addWorkout(@NonNull final Workout workout){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                workoutsRepository.createWorkout(workout);
                Log.e("AddworkoutVM", workout.getName());
            }
        };
        Thread custListLoadThread = new Thread(runnable );
        custListLoadThread.start();
    }

}
