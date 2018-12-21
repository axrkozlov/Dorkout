package com.axfex.dorkout.views.workouts.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.util.LiveEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class WorkoutsViewModel extends ViewModel {

    private final MutableLiveData<Workout> pickedWorkout = new MutableLiveData<>();

    private final LiveEvent<Workout> openWorkoutEvent = new LiveEvent<>();

    private WorkoutsRepository workoutsRepository;

    public WorkoutsViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    LiveData<List<Workout>> getWorkouts() {
        return workoutsRepository.getWorkouts();
    }

    LiveData<Long> createWorkout(String name) {
        return workoutsRepository.createWorkout(new Workout(name));
    }

    LiveEvent<Workout> getOpenWorkoutEvent() {
        return openWorkoutEvent;
    }

    void pickWorkout(Workout workout) {
        if (workout == null || workout == pickedWorkout.getValue()) {
            pickedWorkout.postValue(null);
            return;
        }
            pickedWorkout.postValue(workout);
    }

    MutableLiveData<Workout> getPickedWorkout() {
        return pickedWorkout;
    }

    void deleteWorkout(final Workout workout) {
        workoutsRepository.deleteWorkout(workout);

    }

    void openWorkout(final Workout workout) {
        openWorkoutEvent.setValue(workout);
    }

}
