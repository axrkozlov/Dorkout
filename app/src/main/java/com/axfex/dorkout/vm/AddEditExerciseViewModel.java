package com.axfex.dorkout.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.concurrent.Executor;

/**
 * Created by alexanderkozlov on 2/22/18.
 */

public class AddEditExerciseViewModel extends ViewModel {

    private WorkoutsRepository workoutsRepository;

    public AddEditExerciseViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public void addExercise(@NonNull final Exercise exercise) {

        new Thread(() -> workoutsRepository.createExercise(exercise)).start();

//        final Handler handler = new Handler();
//        final Long[] res=new Long[1];
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                final Long createdId= workoutsRepository.createExercise(exercise);
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        res[0]=createdId;
//                        //res[0] = createdId;
//                    }
//                });
//
//            }
//
//        };
//        new Thread(runnable).start();
//        LiveData<Long> longLiveData = new LiveData>();
//        return new LiveData<>;

    }

    public LiveData<Exercise> getExercise(@NonNull final Long exerciseId) {
        return workoutsRepository.getExercise(exerciseId);
    }

    public LiveData<Integer> getExercisesCount(@NonNull final Long workoutId){
        return workoutsRepository.getExercisesCount(workoutId);
    }


    public void deleteExercise(final Exercise  exercise){
        new Thread(() -> workoutsRepository.deleteExercise(exercise)).start();
    }




}
