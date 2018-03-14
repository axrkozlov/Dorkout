package com.axfex.dorkout.data.source;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Set;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.local.ExercisesDao;
import com.axfex.dorkout.data.source.local.SetsDao;
import com.axfex.dorkout.data.source.local.WorkoutsDao;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    public LiveData<Workout> getWorkout(@NonNull Long id) {
        return workoutsDao.getWorkout(id);
    }

    public int updateWorkout(@NonNull Workout workout) {
        return workoutsDao.updateWorkout(workout);
    }

    public void deleteWorkout(@NonNull Workout... workout) {
        workoutsDao.deleteWorkout(workout);
    }

    /**Exercises**/

    public void createExercise(@NonNull Exercise exercise) {

        Long exerciseId=
                exercisesDao.insertExercise(exercise);
        List<Set> sets=exercise.getSets();

        for (Set set:sets) {
            set.setExerciseId(exerciseId);
        }
        Set[] setsArray=sets.toArray(new Set[sets.size()]);
        createSets(setsArray);

    }

    public LiveData<List<Exercise>> getExercises(@NonNull final Long workoutId) {
        return exercisesDao.getExercises(workoutId);
        //return exercisesDao.getAllExercises();
    }

    public LiveData<Exercise> getExercise(@NonNull Long id) {
        return exercisesDao.getExercise(id);
    }

    public LiveData<Integer> getExercisesCount(@NonNull final Long workoutId) {
        return exercisesDao.getExercisesCount(workoutId);
    }

    public int updateExercise(@NonNull Exercise exercise) {
        return exercisesDao.updateExercise(exercise);
    }

    public void deleteExercise(@NonNull Exercise... exercise) {
        exercisesDao.deleteExercise(exercise);
    }

    /**Sets**/

    public Long[] createSets(@NonNull Set... sets) {
        return setsDao.insertSets(sets);
    }

    public LiveData<List<Set>> getSets(@NonNull final Long exerciseId) {
        return setsDao.getSets(exerciseId);
        //return exercisesDao.getAllExercises();
    }

    public LiveData<Set> getSet(@NonNull Long id) {
        return setsDao.getSet(id);
    }

    public LiveData<Integer> getSetsCount(@NonNull final Long exerciseId) {
        return setsDao.getSetsCount(exerciseId);
    }

    public int updateSet(@NonNull Set set) {
        return setsDao.updateSet(set);
    }

    public void deleteSet(@NonNull Set... set) {
        setsDao.deleteSet(set);
    }
}
