package com.axfex.dorkout.views.workouts.edit;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.List;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class EditWorkoutViewModel extends ViewModel {

    private WorkoutsRepository workoutsRepository;

    public EditWorkoutViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public void addWorkout(@NonNull final Workout workout) {
        new Thread(() -> workoutsRepository.createWorkout(workout)).start();
    }

    public LiveData<Workout> getWorkout(@NonNull final Long workoutId) {
        return workoutsRepository.getWorkout(workoutId);
    }

    public void updateWorkout(@NonNull final Workout workout) {
        new Thread(() -> workoutsRepository.updateWorkout(workout)).start();
    }

    public void deleteWorkout(@NonNull final Workout workout) {
        new Thread(() -> workoutsRepository.deleteWorkout(workout)).start();
    }

    public void addExercise(@NonNull final Exercise exercises) {
        new Thread(() -> workoutsRepository.createExercise(exercises)).start();
    }

    public LiveData<List<Exercise>> getExercises(@NonNull final Long workoutId) {
        return workoutsRepository.getExercises(workoutId);
    }

    public void updateExercise(@NonNull final Exercise exercise) {
        new Thread(() -> workoutsRepository.updateExercise(exercise)).start();
    }

    public void deleteExercise(@NonNull final Exercise exercise) {
        new Thread(() -> workoutsRepository.deleteExercise(exercise)).start();

    }


}
