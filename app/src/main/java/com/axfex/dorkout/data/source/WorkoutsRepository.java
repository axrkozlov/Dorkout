package com.axfex.dorkout.data.source;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Set;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.local.ExercisesDao;
import com.axfex.dorkout.data.source.local.SetsDao;
import com.axfex.dorkout.data.source.local.WorkoutsDao;

import java.util.List;

/**
 * Created by alexanderkozlov on 1/2/18.
 */

public class WorkoutsRepository  {

    private final WorkoutsDao workoutsDao;
    private final ExercisesDao exercisesDao;
    private final SetsDao setsDao;


    public WorkoutsRepository(WorkoutsDao workoutsDao, ExercisesDao exercisesDao, SetsDao setsDao){
        this.workoutsDao = workoutsDao;
        this.exercisesDao = exercisesDao;
        this.setsDao=setsDao;
    }

    /**Workouts**/

    public Long createWorkout(@NonNull Workout workout) {
        return workoutsDao.insertWorkout(workout);
    }

    public LiveData<List<Workout>> getWorkouts() {
        return workoutsDao.getWorkouts();
    }

    public LiveData<Workout> getWorkout(@NonNull int id) {
        return workoutsDao.getWorkout(id);
    }

    public int updateWorkout(@NonNull Workout workout) {
        return workoutsDao.updateWorkout(workout);
    }

    public void deleteWorkout(@NonNull Workout... workout) {
        workoutsDao.deleteWorkout(workout);
    }

    /**Exercises**/

    public Long createExercise(@NonNull Exercise exercise) {
        return exercisesDao.insertExercise(exercise);
    }

    public LiveData<List<Exercise>> getExercises(@NonNull final int workoutId) {
        return exercisesDao.getExercises(workoutId);
        //return exercisesDao.getAllExercises();
    }

    public LiveData<Exercise> getExercise(@NonNull int id) {
        return exercisesDao.getExercise(id);
    }

    public LiveData<Integer> getExercisesCount(@NonNull final int workoutId) {
        return exercisesDao.getExercisesCount(workoutId);
    }

    public int updateExercise(@NonNull Exercise exercise) {
        return exercisesDao.updateExercise(exercise);
    }

    public void deleteExercise(@NonNull Exercise... exercise) {
        exercisesDao.deleteExercise(exercise);
    }

    /**Sets**/

    public Long createSet(@NonNull Set set) {
        return setsDao.insertSet(set);
    }

    public LiveData<List<Set>> getSets(@NonNull final int exerciseId) {
        return setsDao.getSets(exerciseId);
        //return exercisesDao.getAllExercises();
    }

    public LiveData<Set> getSet(@NonNull int id) {
        return setsDao.getSet(id);
    }

    public LiveData<Integer> getSetsCount(@NonNull final int exerciseId) {
        return setsDao.getSetsCount(exerciseId);
    }

    public int updateSet(@NonNull Set set) {
        return setsDao.updateSet(set);
    }

    public void deleteSet(@NonNull Set... set) {
        setsDao.deleteSet(set);
    }
}
