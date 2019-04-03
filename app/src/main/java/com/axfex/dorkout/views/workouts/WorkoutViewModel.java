package com.axfex.dorkout.views.workouts;

import android.util.Log;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Rest;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.services.WorkoutPerformingManager;

import java.util.List;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class WorkoutViewModel extends ViewModel {
    public static final String TAG = "ACTION_WORKOUT_VM";
    private WorkoutsRepository mWorkoutsRepository;
    private WorkoutPerformingManager mWorkoutPerformingManager;


    public WorkoutViewModel(WorkoutsRepository workoutsRepository, WorkoutPerformingManager workoutPerformingManager) {
        this.mWorkoutsRepository = workoutsRepository;
        this.mWorkoutPerformingManager = workoutPerformingManager;

    }

    public LiveData<Workout> getWorkout(@NonNull final Long workoutId) {
        return mWorkoutsRepository.getWorkoutLD(workoutId);
    }

    public LiveData<List<Exercise>> getExercises(@NonNull Long workoutId) {
        return mWorkoutsRepository.getExercisesLD(workoutId);
    }


    public LiveData<Exercise> getActiveExercise() {
        return mWorkoutPerformingManager.getActiveExerciseLD();
    }

    public LiveData<List<Exercise>> getActiveExercises() {
        return mWorkoutPerformingManager.getActiveExercisesLD();
    }

    public LiveData<Rest> getRest() {
        return mWorkoutPerformingManager.getRestLD();
    }

    public void setActiveExercise(Exercise exercise) {
        mWorkoutPerformingManager.setExercise(exercise);
    }

    public void startWorkout(Workout workout, List<Exercise> exercises) {
//        mWorkoutPerformingManager.startWorkout(workout, exercises);
    }

    public void finishWorkout() {
        mWorkoutPerformingManager.stopWorkout();
    }

    public void startExercise() {
        mWorkoutPerformingManager.startExercise();
    }

    public void pauseExercise() {
        mWorkoutPerformingManager.pauseExercise();
    }

    public void restartExercise() {
        mWorkoutPerformingManager.restartExercise();
    }

    public void skipExercise() {
        mWorkoutPerformingManager.skipExercise();

    }

    public void finishExercise() {
        mWorkoutPerformingManager.finishExercise();
    }

    public void onMasterClick(Exercise exercise) {
        if (!exercise.getRunning()) exercise.start();
        else  exercise.finish();
//        Exercise exercise = getActiveExercise().getValue();
//        Rest rest = getRest().getValue();
//        if (rest != null) {
//            mWorkoutPerformingManager.finishRest();
//        } else if (exercise != null) {
//            if (exercise.canStart() && !exercise.getRunning()) {
//                mWorkoutPerformingManager.startExercise();
//            } else {
//                mWorkoutPerformingManager.finishExercise();
//            }
//
//
//        }
    }

//    public Workout getActiveWorkout() {
//        return mWorkoutPerformingManager.getActiveWorkoutLD();
//    }

//    public LiveData<Long> getWorkoutTime() {
//        return mWorkoutPerformingManager.getWorkoutTime();
//    }
//
//    public LiveData<Long> getExerciseTime() {
//        return mWorkoutPerformingManager.getExerciseTime();
//    }
//    public LiveData<Long> getRestTimePlan() {
//        return mWorkoutPerformingManager.getRestTimePlan();
//    }
//    public List<Exercise> getActiveExercises() {
//        return mWorkoutPerformingManager.getExercises();
//    }

}
