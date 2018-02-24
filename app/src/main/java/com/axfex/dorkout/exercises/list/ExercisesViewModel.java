package com.axfex.dorkout.exercises.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.List;

/**
 * Created by alexanderkozlov on 2/22/18.
 */

public class ExercisesViewModel extends ViewModel {

    private WorkoutsRepository workoutsRepository;

    public ExercisesViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public LiveData<List<Exercise>> getExercises(@NonNull int workoutId){
        return workoutsRepository.getExercises(workoutId);
    }

}
