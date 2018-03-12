package com.axfex.dorkout.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.concurrent.Executor;

/**
 * Created by alexanderkozlov on 2/22/18.
 */

public class AddEditExerciseViewModel extends ViewModel {

    private WorkoutsRepository workoutsRepository;

    public AddEditExerciseViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public void addExercise(@NonNull final Exercise exercise){
        new Thread(() -> workoutsRepository.createExercise(exercise)).start();
    }

    public LiveData<Exercise> getExercise(@NonNull final int exerciseId) {
        return workoutsRepository.getExercise(exerciseId);
    }

    public LiveData<Integer> getExercisesCount(@NonNull final int workoutId){
        return workoutsRepository.getExercisesCount(workoutId);
    }

    public void updateExercise(@NonNull final Exercise exercise) {
        new Thread(()->workoutsRepository.updateExercise(exercise)).start();
    }

    public void deleteExercise(final Exercise  exercise){
        new Thread(() -> workoutsRepository.deleteExercise(exercise)).start();
    }


}
