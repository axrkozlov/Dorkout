package com.axfex.dorkout.data.source;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.local.ExercisesDao;
import com.axfex.dorkout.data.source.local.WorkoutsDao;
import com.axfex.dorkout.util.AppExecutors;

import java.util.List;

/**
 * Created by alexanderkozlov on 1/2/18.
 */

public class WorkoutsRepository  {
    private static final String TAG = "WORKOUTS_REPOSITORY";
    private AppExecutors mAppExecutors;
    private final WorkoutsDao workoutsDao;
    private final ExercisesDao exercisesDao;


    public WorkoutsRepository(WorkoutsDao workoutsDao, ExercisesDao exercisesDao,AppExecutors appExecutors){
        this.workoutsDao = workoutsDao;
        this.exercisesDao = exercisesDao;
        this.mAppExecutors=appExecutors;

    }

    /**Workouts**/

    public LiveData<Long> createWorkoutLD(@NonNull Workout workout) {
        MutableLiveData<Long> newWorkoutId=new MutableLiveData<>();
        mAppExecutors.diskIO().execute(()->newWorkoutId.postValue(workoutsDao.insertWorkout(workout)));
        return newWorkoutId;
    }

    public LiveData<List<Workout>> getWorkoutsLD() {
        return workoutsDao.getWorkoutsLD();
    }

    public LiveData<Workout> getWorkoutLD(@NonNull Long id) {
        return workoutsDao.getWorkoutLD(id);
    }

    public void updateWorkout(@NonNull Workout workout) {
        mAppExecutors.diskIO().execute(()->workoutsDao.updateWorkout(workout));
    }

    public void deleteWorkout(@NonNull Workout workout) {
        mAppExecutors.diskIO().execute(()->workoutsDao.deleteWorkout(workout));
    }

    /**Exercises**/

    public void createExercise(@NonNull Exercise exercise) {

        Long exerciseId=
                exercisesDao.insertExercise(exercise);
//
//        List<Eset> esets =exercise.getEsets();
//        for (Eset eset : esets) {
//            eset.setExerciseId(exerciseId);
//        }
//        Eset[] setsArray= esets.toArray(new Eset[esets.size()]);
//        createSets(setsArray);

    }

    public LiveData<List<Exercise>> getExercises(@NonNull final Long workoutId) {
        return exercisesDao.getExercisesLD(workoutId);
        //return exercisesDao.getAllExercises();
    }
//
//    public LiveData<List<ExerciseWithSets>> getExercisesWithSets(@NonNull final Long workoutId) {
//        return exercisesDao.getExercisesWithSets(workoutId);
//        //return exercisesDao.getAllExercises();
//    }

    public LiveData<Exercise> getExerciseLD(@NonNull Long id) {
        return exercisesDao.getExerciseLD(id);
    }

    public LiveData<Integer> getExercisesCountLD(@NonNull final Long workoutId) {
        return exercisesDao.getExercisesCountLD(workoutId);
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

    public void deleteExercise(@NonNull Exercise... exercise) {
        exercisesDao.deleteExercise(exercise);
    }

    public void deleteExercise(@NonNull Long exerciseId) {
        exercisesDao.deleteExercise(exerciseId);
    }

//    /**Sets**/
//
//    public Long[] createSets(@NonNull Eset... esets) {
//        return setsDao.insertSets(esets);
//    }
//
//    public LiveData<List<Eset>> getSets(@NonNull final Long exerciseId) {
//        return setsDao.getSets(exerciseId);
//        //return exercisesDao.getAllExercises();
//    }
//
//    public LiveData<Eset> getSet(@NonNull Long id) {
//        return setsDao.getSet(id);
//    }
//
//    public LiveData<Integer> getSetsCount(@NonNull final Long exerciseId) {
//        return setsDao.getSetsCount(exerciseId);
//    }
//
//    public int updateSet(@NonNull Eset eset) {
//        return setsDao.updateSet(eset);
//    }
//
//    public void deleteSet(@NonNull Eset... eset) {
//        setsDao.deleteSet(eset);
//    }
}
