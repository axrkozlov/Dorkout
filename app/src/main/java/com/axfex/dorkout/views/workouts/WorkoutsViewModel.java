package com.axfex.dorkout.views.workouts;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.List;

public class WorkoutsViewModel extends ViewModel {
    public static final String TAG = "WORKOUTS_VIEW_MODEL";
    private WorkoutsRepository mWorkoutsRepository;
    public WorkoutsViewModel(WorkoutsRepository workoutsRepository) {
        mWorkoutsRepository = workoutsRepository;
    }

    private Workout mPickedWorkout;

    private final MutableLiveData<Workout> mPickEventSource = new MutableLiveData<>();

    LiveData<List<Workout>> getWorkouts() {
        return mWorkoutsRepository.getWorkoutsLD();
    }

    LiveData<Workout> getPickedWorkout() {
        return mPickEventSource;
    }

    LiveData<Long> createWorkout(Workout workout) {
        return mWorkoutsRepository.createWorkoutLD(workout);
    }

    void updateWorkout(final Workout workout) {
        mWorkoutsRepository.updateWorkout(workout);
    }

    void deleteWorkout(final Workout workout) {
        mWorkoutsRepository.deleteWorkout(workout);
    }

    void pickWorkout(Workout workout) {
        if (workout == null || mPickedWorkout!=null&&workout.getId().equals(mPickedWorkout.getId())) {
            mPickedWorkout=null;
        } else {
            mPickedWorkout = workout;
        }
        mPickEventSource.setValue(mPickedWorkout);
    }

    void resetPicked(){
        mPickedWorkout=null;
    }

    @Override
    protected void onCleared() {
        Log.i(TAG, "onCleared: ");
        super.onCleared();
    }

}
