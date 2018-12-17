package com.axfex.dorkout.views.workouts.edit;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class EditWorkoutViewModel extends ViewModel {

    private WorkoutsRepository workoutsRepository;

    public EditWorkoutViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public void addWorkout(@NonNull final Workout workout) {
        new Thread(()->workoutsRepository.createWorkout(workout)).start();
    }

    public LiveData<Workout> getWorkout(@NonNull final Long workoutId) {
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
