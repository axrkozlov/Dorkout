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

public class WorkoutsRepository {
    private static final String TAG = "WORKOUTS_REPOSITORY";
    private AppExecutors mAppExecutors;
    private final WorkoutsDao mWorkoutsDao;
    private final ExercisesDao mExercisesDao;
    private Long mActiveWorkoutId;


    public WorkoutsRepository(WorkoutsDao workoutsDao, ExercisesDao exercisesDao, AppExecutors appExecutors) {
        this.mWorkoutsDao = workoutsDao;
        this.mExercisesDao = exercisesDao;
        this.mAppExecutors = appExecutors;
    }

    /**
     * Workouts
     **/

    public LiveData<Long> createWorkoutLD(@NonNull Workout workout) {
        MutableLiveData<Long> newWorkoutId = new MutableLiveData<>();
        mAppExecutors.diskIO().execute(() -> newWorkoutId.postValue(mWorkoutsDao.insertWorkout(workout)));
        return newWorkoutId;
    }

    public LiveData<List<Workout>> getWorkoutsLD() {
        return mWorkoutsDao.getWorkoutsLD();
    }

    public LiveData<Workout> getWorkoutLD(@NonNull Long id) {
        return mWorkoutsDao.getWorkoutLD(id);
    }

//    public LiveData<Workout> getActiveWorkoutLD() {
//        return mWorkoutsDao.getActiveWorkoutLD();
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
        mAppExecutors.diskIO().execute(() -> mWorkoutsDao.updateWorkout(workout));
    }

    public void deleteWorkout(@NonNull Workout workout) {
        mAppExecutors.diskIO().execute(() -> mWorkoutsDao.deleteWorkout(workout));
    }

    public Long getActiveWorkoutId(){
        return mActiveWorkoutId;
    }

    public void setActiveWorkoutId(Long activeWorkoutId) {
        mActiveWorkoutId = activeWorkoutId;
    }

    /**
     * Exercises
     **/

    public void createExercise(@NonNull Exercise exercise) {
        mAppExecutors.diskIO().execute(() -> mExercisesDao.insertExercise(exercise));
    }

    public LiveData<List<Exercise>> getExercisesLD(@NonNull final Long workoutId) {
        return mExercisesDao.getExercisesLD(workoutId);
        //return mExercisesDao.getAllExercises();
    }

    public LiveData<Exercise> getExerciseLD(@NonNull Long id) {
        return mExercisesDao.getExerciseLD(id);
    }

    public LiveData<Integer> getExercisesCountLD(@NonNull final Long workoutId) {
        return mExercisesDao.getExercisesCountLD(workoutId);
    }

    public LiveData<List<String>> getAllExerciseNamesLD() {
        return mExercisesDao.getAllExerciseNamesLD();
    }

    public void updateExercise(@NonNull Exercise... exercise) {
        mAppExecutors.diskIO().execute(() -> {
            mExercisesDao.updateExercise(exercise);
            Log.i(TAG, "updateExercise: " +exercise[0].getStatus());
        });
    }

    public void updateExercises(@NonNull List<Exercise> exercises) {
        int i = 0;
        for (Exercise e :
                exercises) {
            e.setOrderNumber(++i);
        }
        Exercise[] exercisesArray = exercises.toArray(new Exercise[exercises.size()]);
        mAppExecutors.diskIO().execute(() -> mExercisesDao.updateExercise(exercisesArray));
    }

    public void deleteExercise(@NonNull Exercise... exercise) {
        mExercisesDao.deleteExercise(exercise);
    }

    public void deleteExercise(@NonNull Long exerciseId) {
        mExercisesDao.deleteExercise(exerciseId);
    }

}
