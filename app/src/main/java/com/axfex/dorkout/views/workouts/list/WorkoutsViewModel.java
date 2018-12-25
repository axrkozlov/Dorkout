package com.axfex.dorkout.views.workouts.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.axfex.dorkout.Navigator;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.util.FreshMutableLiveData;
import com.axfex.dorkout.util.SingleMutableLiveData;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class WorkoutsViewModel extends ViewModel {

    private final MutableLiveData<Workout> pickedWorkout = new MutableLiveData<>();

    private MutableLiveData<Workout> openWorkoutEvent=new SingleMutableLiveData<>();
    private WorkoutsRepository mWorkoutsRepository;
    private Navigator mNavigator;

    @Inject
    public WorkoutsViewModel(WorkoutsRepository workoutsRepository, Navigator navigator) {
        this.mWorkoutsRepository = workoutsRepository;
        this.mNavigator=navigator;
    }

    LiveData<List<Workout>> getWorkouts() {
        return mWorkoutsRepository.getWorkouts();
    }

    LiveData<Long> createWorkout(String name) {
        return mWorkoutsRepository.createWorkout(new Workout(name));
    }

    LiveData<Workout> getOpenWorkoutEvent() {
        return openWorkoutEvent;
    }

    void pickWorkout(Workout workout) {
        if (workout == null || workout == pickedWorkout.getValue()) {
            pickedWorkout.postValue(null);
            return;
        }
            pickedWorkout.postValue(workout);
    }

    MutableLiveData<Workout> getPickWorkoutEvent() {
        return pickedWorkout;
    }

    void deleteWorkout(final Workout workout) {
        mWorkoutsRepository.deleteWorkout(workout);
    }

    void openWorkout(final Workout workout) {
        //mNavigator.openWorkout(workout.getId());
        openWorkoutEvent.setValue(workout);
    }

}
