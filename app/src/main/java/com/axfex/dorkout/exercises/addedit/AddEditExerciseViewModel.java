package com.axfex.dorkout.exercises.addedit;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

/**
 * Created by alexanderkozlov on 2/22/18.
 */

public class AddEditExerciseViewModel extends ViewModel {

    private WorkoutsRepository workoutsRepository;

    public AddEditExerciseViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }
    public void addExercise(@NonNull final Exercise exercise){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                workoutsRepository.createExercise(exercise);
                Log.e("AddworkoutVM", exercise.getName());
            }
        };
        Thread custListLoadThread = new Thread(runnable );
        custListLoadThread.start();
    }
}
