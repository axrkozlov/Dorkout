package com.axfex.dorkout.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.views.workouts.list.WorkoutsNavigator;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class WorkoutsViewModel extends ViewModel {


    private WorkoutsRepository workoutsRepository;
    private WeakReference<WorkoutsNavigator> mNavigator;

    public WorkoutsViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public LiveData<List<Workout>> getWorkouts() {
        return workoutsRepository.getWorkouts();
    }

    public LiveData<Long> createWorkout(String name) {
        return workoutsRepository.createWorkout(new Workout(name));
    }

    public void deleteWorkout(final Workout workout){
        workoutsRepository.deleteWorkout(workout);
    }


}
