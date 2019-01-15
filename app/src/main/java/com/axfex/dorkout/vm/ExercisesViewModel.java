package com.axfex.dorkout.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.List;

/**
 * Created by alexanderkozlov on 2/22/18.
 */

public class ExercisesViewModel extends ViewModel {

    private WorkoutsRepository workoutsRepository;

    public ExercisesViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public LiveData<List<Exercise>> getExercises(@NonNull Long workoutId){
        return workoutsRepository.getExercisesLD(workoutId);
    }

    public LiveData<Workout> getWorkout(@NonNull final Long workoutId) {
        return workoutsRepository.getWorkoutLD(workoutId);
    }

    public void deleteExercise(final Long  exerciseId){
        new Thread(() -> workoutsRepository.deleteExercise(exerciseId)).start();
    }

    public void updateExercise(@NonNull final Exercise exercise) {
        new Thread(()->workoutsRepository.updateExercise(exercise)).start();
    }


    public void updateExercises(@NonNull final List<Exercise> exercises) {
        new Thread(()->workoutsRepository.updateExercises(exercises)).start();
    }

}
