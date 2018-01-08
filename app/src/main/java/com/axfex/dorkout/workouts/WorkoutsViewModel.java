package com.axfex.dorkout.workouts;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class WorkoutsViewModel extends ViewModel {

    private WorkoutsRepository workoutsRepository;

    //@Inject
    public WorkoutsViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public LiveData<List<Workout>> getWorkouts(){
        return workoutsRepository.getWorkouts();
    }

    public void deleteWorkout(final Workout workout){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                workoutsRepository.deleteWorkout(workout);
            }
        };
    }


}
