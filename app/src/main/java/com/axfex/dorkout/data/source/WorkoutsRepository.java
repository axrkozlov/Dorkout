package com.axfex.dorkout.data.source;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.local.ExercisesDao;
import com.axfex.dorkout.data.source.local.WorkoutsDao;

import java.util.List;

/**
 * Created by alexanderkozlov on 1/2/18.
 */

public class WorkoutsRepository  {

    private final WorkoutsDao workoutsDao;
    private final ExercisesDao ExercisesDao;

    public WorkoutsRepository(WorkoutsDao workoutsDao,ExercisesDao ExercisesDao){
        this.workoutsDao = workoutsDao;
        this.ExercisesDao = ExercisesDao;
    }

    public LiveData<List<Workout>> getWorkouts() {
        return workoutsDao.getWorkouts();
    }

    public LiveData<Workout> getWorkout(@NonNull Long id) {
        return workoutsDao.getWorkout(id);
    }

    public Long createWorkout(@NonNull Workout workout) {
        return workoutsDao.insertWorkout(workout);
    }

    public void deleteWorkout(@NonNull Workout... workout) {
        workoutsDao.deleteWorkout(workout);
    }


    public LiveData<List<Exercise>> getExercises(@NonNull final int workoutId) {
        return ExercisesDao.getExercises(workoutId);
        //return ExercisesDao.getAllExercises();
    }

    public LiveData<Exercise> getExercise(@NonNull Long id) {
        return ExercisesDao.getExercise(id);
    }

    public Long createExercise(@NonNull Exercise exercise) {
        return ExercisesDao.insertExercise(exercise);
    }

    public void deleteExercise(@NonNull Exercise... exercise) {
        ExercisesDao.deleteExercise(exercise);
    }


}
