package com.axfex.dorkout.views.trash.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

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
        new Thread(() -> workoutsRepository.createWorkoutLD(workout)).start();
    }

    public LiveData<Workout> getWorkout(@NonNull final Long workoutId) {
        return workoutsRepository.getWorkoutLD(workoutId);
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
        return workoutsRepository.getExercisesLD(workoutId);
    }

    public void updateExercise(@NonNull final Exercise exercise) {
        new Thread(() -> workoutsRepository.updateExercise(exercise)).start();
    }

    public void deleteExercise(@NonNull final Exercise exercise) {
        new Thread(() -> workoutsRepository.deleteExercise(exercise)).start();

    }


}
