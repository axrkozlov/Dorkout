package com.axfex.dorkout.vm;

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
        new Thread(()->workoutsRepository.createWorkout(workout)).start();
    }

    public LiveData<Workout> getWorkout(@NonNull final int workoutId) {
        return workoutsRepository.getWorkout(workoutId);
    }

    public void updateWorkout(@NonNull final Workout workout) {
        new Thread(()->workoutsRepository.updateWorkout(workout)).start();
    }

    public void deleteWorkout(final Workout workout){
        new Thread(() -> workoutsRepository.deleteWorkout(workout)).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                workoutsRepository.deleteWorkout(workout);
//            }
//        }).start();
    }
}
