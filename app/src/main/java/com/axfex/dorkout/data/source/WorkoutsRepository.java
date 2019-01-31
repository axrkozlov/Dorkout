package com.axfex.dorkout.data.source;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.Transformations;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.local.ExercisesDao;
import com.axfex.dorkout.data.source.local.WorkoutsDao;
import com.axfex.dorkout.util.AppExecutors;

import java.util.List;

/**
 * Created by alexanderkozlov on 1/2/18.
 */

public class WorkoutsRepository {
    private static final String TAG = "WORKOUTS_REPOSITORY";
    private AppExecutors mAppExecutors;
    private final WorkoutsDao workoutsDao;
    private final ExercisesDao exercisesDao;


    public WorkoutsRepository(WorkoutsDao workoutsDao, ExercisesDao exercisesDao, AppExecutors appExecutors) {
        this.workoutsDao = workoutsDao;
        this.exercisesDao = exercisesDao;
        this.mAppExecutors = appExecutors;
    }

    /**
     * Workouts
     **/

    public LiveData<Long> createWorkoutLD(@NonNull Workout workout) {
        MutableLiveData<Long> newWorkoutId = new MutableLiveData<>();
        mAppExecutors.diskIO().execute(() -> newWorkoutId.postValue(workoutsDao.insertWorkout(workout)));
        return newWorkoutId;
    }

    public LiveData<List<Workout>> getWorkoutsLD() {
        return workoutsDao.getWorkoutsLD();
    }

    public LiveData<Workout> getWorkoutLD(@NonNull Long id) {
        return workoutsDao.getWorkoutLD(id);
    }

//    public LiveData<Workout> getActiveWorkoutLD() {
//        return workoutsDao.getActiveWorkoutLD();
//    }

//    public LiveData<List<Exercise>> getActiveWorkoutExercisesLD() {
//
//        return Transformations.switchMap(getActiveWorkoutLD(), (workout) ->
//                {
//                    if (workout != null) {
//                        return getExercisesLD(workout.getId());
//                    }
//                    return null;
//                }
//        );
//    }

    public void updateWorkout(@NonNull Workout workout) {
        mAppExecutors.diskIO().execute(() -> workoutsDao.updateWorkout(workout));
    }

    public void deleteWorkout(@NonNull Workout workout) {
        mAppExecutors.diskIO().execute(() -> workoutsDao.deleteWorkout(workout));
    }

    /**
     * Exercises
     **/

    public void createExercise(@NonNull Exercise exercise) {
        mAppExecutors.diskIO().execute(() -> exercisesDao.insertExercise(exercise));
    }

    public LiveData<List<Exercise>> getExercisesLD(@NonNull final Long workoutId) {
        return exercisesDao.getExercisesLD(workoutId);
        //return exercisesDao.getAllExercises();
    }

    public LiveData<Exercise> getExerciseLD(@NonNull Long id) {
        return exercisesDao.getExerciseLD(id);
    }

    public LiveData<Integer> getExercisesCountLD(@NonNull final Long workoutId) {
        return exercisesDao.getExercisesCountLD(workoutId);
    }

    public LiveData<List<String>> getAllExerciseNamesLD() {
        return exercisesDao.getAllExerciseNamesLD();
    }

    public void updateExercise(@NonNull Exercise... exercise) {
        mAppExecutors.diskIO().execute(() -> exercisesDao.updateExercise(exercise));
    }

    public void updateExercises(@NonNull List<Exercise> exercises) {
        int i = 0;
        for (Exercise e :
                exercises) {
            e.setOrderNumber(++i);
        }
        Exercise[] exercisesArray = exercises.toArray(new Exercise[exercises.size()]);
        mAppExecutors.diskIO().execute(() -> exercisesDao.updateExercise(exercisesArray));
    }

    public void deleteExercise(@NonNull Exercise... exercise) {
        exercisesDao.deleteExercise(exercise);
    }

    public void deleteExercise(@NonNull Long exerciseId) {
        exercisesDao.deleteExercise(exerciseId);
    }

}
