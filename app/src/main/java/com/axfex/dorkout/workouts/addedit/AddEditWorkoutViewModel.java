package com.axfex.dorkout.workouts.addedit;

import android.arch.lifecycle.LiveData;
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

    public void addWorkout(@NonNull final Workout workout) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                workoutsRepository.createWorkout(workout);
            }
        };
        Thread custListLoadThread = new Thread(runnable);
        custListLoadThread.start();
    }

    public void updateWorkout(@NonNull final Workout workout) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                workoutsRepository.updateWorkout(workout);
            }
        };
        Thread custListLoadThread = new Thread(runnable);
        custListLoadThread.start();
    }

    public LiveData<Workout> getWorkout(@NonNull final int workoutId) {
        return workoutsRepository.getWorkout(workoutId);

    }
    public void deleteWorkout(final Workout workout){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                workoutsRepository.deleteWorkout(workout);
            }
        };
        Thread custListLoadThread = new Thread(runnable);
        custListLoadThread.start();
    }
}
