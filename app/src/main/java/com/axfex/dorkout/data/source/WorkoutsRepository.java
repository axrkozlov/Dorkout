package com.axfex.dorkout.data.source;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.local.WorkoutsDao;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by alexanderkozlov on 1/2/18.
 */

public class WorkoutsRepository  {

    private final WorkoutsDao workoutsDao;

    @Inject
    public WorkoutsRepository(WorkoutsDao workoutsDao){
        this.workoutsDao = workoutsDao;
    }

    public LiveData<List<Workout>> getWorkouts() {
        return workoutsDao.getWorkouts();
    }

    public LiveData<Workout> getWorkout(@NonNull String id) {
        return workoutsDao.getWorkout(id);
    }


    public Long createWorkout(@NonNull Workout workout) {
        return workoutsDao.insertWorkout(workout);
    }

    public void deleteWorkout(@NonNull Workout... workout) {
        workoutsDao.deleteWorkout(workout);
    }
}
