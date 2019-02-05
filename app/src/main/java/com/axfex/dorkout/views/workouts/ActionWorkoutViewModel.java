package com.axfex.dorkout.views.workouts;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.services.ActionWorkoutManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ActionWorkoutViewModel extends ViewModel {
    public static final String TAG = "ACTION_WORKOUT_VIEW_MODEL";
    private WorkoutsRepository mWorkoutsRepository;
    private ActionWorkoutManager mActionWorkoutManager;
    private LiveData<Exercise> exercise;

    public ActionWorkoutViewModel(WorkoutsRepository workoutsRepository, ActionWorkoutManager actionWorkoutManager) {
        this.mWorkoutsRepository = workoutsRepository;
        this.mActionWorkoutManager = actionWorkoutManager;
        exercise=actionWorkoutManager.getActiveExerciseLD();
    }

    public LiveData<Workout> getWorkout(@NonNull final Long workoutId) {
        return mWorkoutsRepository.getWorkoutLD(workoutId);
    }

    public LiveData<List<Exercise>> getExercises(@NonNull Long workoutId) {
        return mWorkoutsRepository.getExercisesLD(workoutId);
    }

    public LiveData<Workout> getActiveWorkoutLD() {
        return mActionWorkoutManager.getActiveWorkoutLD();
    }

    public LiveData<Exercise> getExercise() {
        return mActionWorkoutManager.getActiveExerciseLD();
    }

    public void setActiveExercise(Exercise exercise) {
        mActionWorkoutManager.setExercise(exercise);
    }
    public void startWorkout(Workout workout,List<Exercise> exercises) {
        mActionWorkoutManager.startWorkout(workout,exercises);
    }

    public void finishWorkout() {
        mActionWorkoutManager.stopWorkout();
    }

    public void startExercise() {
        mActionWorkoutManager.startExercise();
    }

    public void pauseExercise() {
        mActionWorkoutManager.pauseExercise();
    }

    public void restartExercise() {
        mActionWorkoutManager.restartExercise();
    }

    public void skipExercise() {
        mActionWorkoutManager.skipExercise();
    }

    public void finishExercise() {
        mActionWorkoutManager.finishExercise();
    }

//    public LiveData<Long> getWorkoutTime() {
//        return mActionWorkoutManager.getWorkoutTime();
//    }
//
//    public LiveData<Long> getExerciseTime() {
//        return mActionWorkoutManager.getExerciseTime();
//    }
//    public LiveData<Long> getRestTimePlan() {
//        return mActionWorkoutManager.getRestTimePlan();
//    }
//    public List<Exercise> getActiveExercises() {
//        return mActionWorkoutManager.getExercises();
//    }



}
