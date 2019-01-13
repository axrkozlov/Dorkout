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
    private WorkoutsRepository workoutsRepository;

    public EditWorkoutViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public LiveData<List<Exercise>> getExercises(@NonNull Long workoutId){
        return workoutsRepository.getExercises(workoutId);
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

    @Override
    protected void onCleared() {
        Log.i(TAG, "onCleared: ");
        super.onCleared();
    }
}
