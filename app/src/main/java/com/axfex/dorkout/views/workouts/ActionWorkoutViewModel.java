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

    public ActionWorkoutViewModel(WorkoutsRepository workoutsRepository, ActionWorkoutManager actionWorkoutManager) {
        this.mWorkoutsRepository = workoutsRepository;
        this.mActionWorkoutManager=actionWorkoutManager;
    }

    public LiveData<Workout> getWorkout(@NonNull final Long workoutId) {
        return mWorkoutsRepository.getWorkoutLD(workoutId);
    }

    public LiveData<List<Exercise>> getExercises(@NonNull Long workoutId) {
        return mWorkoutsRepository.getExercisesLD(workoutId);
    }

    public void setWorkout(Workout workout,List<Exercise> exercises){
        mActionWorkoutManager.setWorkout(workout,exercises);
    }

    public Workout getActiveWorkout() {
        return mActionWorkoutManager.getWorkout();
    }

    public Exercise getActiveExercise() {
        return mActionWorkoutManager.getExercise();
    }

    public List<Exercise> getActiveExercises() {
        return mActionWorkoutManager.getExercises();
    }

}
