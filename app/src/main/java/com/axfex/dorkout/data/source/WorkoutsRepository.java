package com.axfex.dorkout.data.source;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.ExerciseType;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.local.ExerciseTypesDao;
import com.axfex.dorkout.data.source.local.ExercisesDao;
import com.axfex.dorkout.data.source.local.WorkoutsDao;

import java.util.List;

/**
 * Created by alexanderkozlov on 1/2/18.
 */

public class WorkoutsRepository {

    private final WorkoutsDao workoutsDao;
    private final ExercisesDao exercisesDao;
    private final ExerciseTypesDao exerciseTypesDao;


    public WorkoutsRepository(WorkoutsDao workoutsDao, ExercisesDao exercisesDao,ExerciseTypesDao exerciseTypesDao){
        this.workoutsDao = workoutsDao;
        this.exercisesDao = exercisesDao;
        this.exerciseTypesDao=exerciseTypesDao;
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


    /**ExerciseTypes**/

    public void createExerciseType(@NonNull ExerciseType exerciseType) {
        Long exerciseId=exerciseTypesDao.insertExerciseType(exerciseType);
    }

    public LiveData<List<ExerciseType>> getExerciseTypes() {
        return exerciseTypesDao.getExerciseTypes();
    }


    public void deleteExerciseType(@NonNull ExerciseType exerciseType) {
        exerciseTypesDao.deleteExerciseType(exerciseType);
    }

}

//
//    public int updateExercises(List<ExerciseWithSets> exercisesWithSets){
//        int exercisesArraySize=exercisesWithSets.size();
//        Exercise[] exercisesArray=new Exercise[exercisesArraySize];
//        for (int i = 0; i < exercisesArraySize; i++) {
//            Exercise exercise=exercisesWithSets.get(i).exercise;
//            exercise.setOrderNumber(i);
//            exercisesArray[i]=exercise;
//        }
//        return exercisesDao.updateExercise(exercisesArray);
//    }
