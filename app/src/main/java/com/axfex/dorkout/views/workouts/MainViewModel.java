package com.axfex.dorkout.views.workouts;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.List;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class MainViewModel extends ViewModel {

    private Workout mCurrentWorkout;

    public enum ViewState {
        WORKOUTS,
        WORKOUT_SELECTION,
        WORKOUT_MODIFICATION,
        EXECUTION,
        OPEN_WORKOUTS,
        OPEN_WORKOUT_MODIFICATION,
        OPEN_EXECUTION
    }

    private MutableLiveData<ViewState> mViewStateEvent = new MutableLiveData<>();
    private WorkoutsRepository mWorkoutsRepository;


    public MainViewModel(WorkoutsRepository workoutsRepository) {
        this.mWorkoutsRepository = workoutsRepository;
        setViewType(ViewState.OPEN_WORKOUTS);
    }

    LiveData<List<Workout>> getWorkouts() {
        return mWorkoutsRepository.getWorkouts();
    }

    Workout getCurrentWorkout() {
        return mCurrentWorkout;
    }

    LiveData<Long> createWorkout(Workout workout) {
        return mWorkoutsRepository.createWorkout(workout);
    }

    void updateWorkout(final Workout workout) {
        mWorkoutsRepository.updateWorkout(workout);
    }

    void deleteWorkout(final Workout workout) {
        mWorkoutsRepository.deleteWorkout(workout);
    }

    LiveData<ViewState> getViewType() {
        return mViewStateEvent;
    }

    private void setViewType(ViewState viewState) {
        mViewStateEvent.setValue(viewState);
    }

    void pickWorkout(Workout workout) {
        if (workout == null || workout == mCurrentWorkout) {
            mCurrentWorkout = null;
            setViewType(ViewState.WORKOUTS);
            return;
        }
        mCurrentWorkout = workout;
        setViewType(ViewState.WORKOUT_SELECTION);
    }

    void editWorkout(Workout workout) {
        mCurrentWorkout = workout;
        setViewType(ViewState.OPEN_WORKOUT_MODIFICATION);
    }

    void openWorkout(final Workout workout) {
        mCurrentWorkout = workout;
        setViewType(ViewState.OPEN_EXECUTION);
    }

    void onWorkoutsViewOpened() {
        mCurrentWorkout = null;
        setViewType(ViewState.WORKOUTS);
    }

    /*Exercises*/

    LiveData<List<Exercise>> getExercises(@NonNull Long workoutId) {
        return mWorkoutsRepository.getExercises(workoutId);
    }







}
