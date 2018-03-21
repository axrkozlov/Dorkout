package com.axfex.dorkout.data.source;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.axfex.dorkout.data.Eset;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.ExerciseWithSets;
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

        List<Eset> esets =exercise.getEsets();
        for (Eset eset : esets) {
            eset.setExerciseId(exerciseId);
        }
        Eset[] setsArray= esets.toArray(new Eset[esets.size()]);
        createSets(setsArray);

    }

    public LiveData<List<Exercise>> getExercises(@NonNull final Long workoutId) {
        return exercisesDao.getExercises(workoutId);
        //return exercisesDao.getAllExercises();
    }

    public LiveData<List<ExerciseWithSets>> getExercisesWithSets(@NonNull final Long workoutId) {
        return exercisesDao.getExercisesWithSets(workoutId);
        //return exercisesDao.getAllExercises();
    }

    public LiveData<Exercise> getExercise(@NonNull Long id) {
        return exercisesDao.getExercise(id);
    }

    public LiveData<Integer> getExercisesCount(@NonNull final Long workoutId) {
        return exercisesDao.getExercisesCount(workoutId);
    }

    public int updateExercise(@NonNull Exercise... exercise) {
        return exercisesDao.updateExercise(exercise);
    }

    public int updateExercises(@NonNull List<Exercise> exercises) {
        int i=0;
        for (Exercise e :
                exercises) {
            e.setOrderNumber(++i);
        }
        Exercise[] exercisesArray=exercises.toArray(new Exercise[exercises.size()]);
        return exercisesDao.updateExercise(exercisesArray);
    }

    public void deleteExercise(@NonNull Exercise... exercise) {
        exercisesDao.deleteExercise(exercise);
    }

    public void deleteExercise(@NonNull Long exerciseId) {
        exercisesDao.deleteExercise(exerciseId);
    }

    /**Sets**/

    public Long[] createSets(@NonNull Eset... esets) {
        return setsDao.insertSets(esets);
    }

    public LiveData<List<Eset>> getSets(@NonNull final Long exerciseId) {
        return setsDao.getSets(exerciseId);
        //return exercisesDao.getAllExercises();
    }

    public LiveData<Eset> getSet(@NonNull Long id) {
        return setsDao.getSet(id);
    }

    public LiveData<Integer> getSetsCount(@NonNull final Long exerciseId) {
        return setsDao.getSetsCount(exerciseId);
    }

    public int updateSet(@NonNull Eset eset) {
        return setsDao.updateSet(eset);
    }

    public void deleteSet(@NonNull Eset... eset) {
        setsDao.deleteSet(eset);
    }
}
