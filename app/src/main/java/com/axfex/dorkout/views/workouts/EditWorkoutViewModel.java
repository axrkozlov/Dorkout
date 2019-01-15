package com.axfex.dorkout.views.workouts;

import android.util.Log;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class EditWorkoutViewModel extends ViewModel {
    public static final String TAG = "EDIT_WORKOUT_VIEW_MODEL";
    private WorkoutsRepository mWorkoutsRepository;

    public EditWorkoutViewModel(WorkoutsRepository workoutsRepository) {
        this.mWorkoutsRepository = workoutsRepository;
    }

    public LiveData<Workout> getWorkout(@NonNull final Long workoutId) {
        return mWorkoutsRepository.getWorkoutLD(workoutId);
    }

    public LiveData<List<Exercise>> getExercises(@NonNull Long workoutId) {
        return mWorkoutsRepository.getExercisesLD(workoutId);
    }

    public LiveData<List<String>> getAllExerciseNames() {
        return mWorkoutsRepository.getAllExerciseNamesLD();
    }

    public void createExercise(@NonNull Exercise exercise) {
         mWorkoutsRepository.createExercise(exercise);
    }

    public void deleteExercise(final Long exerciseId) {
        new Thread(() -> mWorkoutsRepository.deleteExercise(exerciseId)).start();
    }

    public void deleteExercise(final Exercise exercise) {
        new Thread(() -> mWorkoutsRepository.deleteExercise(exercise)).start();
    }

    public void updateExercise(@NonNull final Exercise exercise) {
        new Thread(() -> mWorkoutsRepository.updateExercise(exercise)).start();
    }


    public void updateExercises(@NonNull final List<Exercise> exercises) {
        new Thread(() -> mWorkoutsRepository.updateExercises(exercises)).start();
    }

    @Override
    protected void onCleared() {
        Log.i(TAG, "onCleared: ");
        super.onCleared();
    }
}
