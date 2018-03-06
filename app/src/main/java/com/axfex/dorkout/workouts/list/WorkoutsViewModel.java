package com.axfex.dorkout.workouts.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.List;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class WorkoutsViewModel extends ViewModel {


    private WorkoutsRepository workoutsRepository;


    public WorkoutsViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public LiveData<List<Workout>> getWorkouts(){
        return workoutsRepository.getWorkouts();
    }



}
