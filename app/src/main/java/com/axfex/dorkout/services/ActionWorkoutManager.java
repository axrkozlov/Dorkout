package com.axfex.dorkout.services;

import android.os.Handler;
import android.util.Log;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.List;


import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import static com.axfex.dorkout.data.Status.DONE;
import static com.axfex.dorkout.data.Status.RUNNING;
import static com.axfex.dorkout.data.Status.SKIPPED;

public class ActionWorkoutManager {
    private static final String TAG = "ACTION_WORKOUT_MANAGER";


    private Workout mWorkout;
    private List<Exercise> mExercises;
    private Exercise mExercise;
    private int mExercisePosition;
    private boolean mAutoStartNext;
    //    private boolean isStarted = false;
    private WorkoutsRepository mWorkoutsRepository;
    private MutableLiveData<Exercise> mActiveExerciseLD = new MutableLiveData<>();
    private MutableLiveData<Workout> mActiveWorkoutLD = new MutableLiveData<>();
    private final Observer<List<Exercise>> mObserver = this::onExercisesLoaded;

//    private MutableLiveData<Long> mWorkoutTime = new MutableLiveData<>();
//    private MutableLiveData<Long> mExerciseTime = new MutableLiveData<>();
//    private MutableLiveData<Long> mRestTime = new MutableLiveData<>();

    private static final int ONE_SECOND = 1000;
    private static final int _100MS = 100;


    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new TimeUpdate();

    private final class TimeUpdate implements Runnable {

        @Override
        public void run() {

//            mExerciseTime.setValue(mExercise.getCurrentTime());
//            if (mExercise.getRunning()) {
//            timerHandler.postDelayed(this, 100);
//            }
        }

    }

    public ActionWorkoutManager(WorkoutsRepository workoutsRepository) {
        mWorkoutsRepository = workoutsRepository;
    }

    public void startWorkout(@NonNull Workout workout) {
        if (mWorkout == null || mWorkout.getStatus()!=RUNNING) onStartWorkout(workout);
    }

    private void onStartWorkout(Workout workout) {
        mWorkout = workout;
        mWorkout.start();
        storeWorkoutChangesToDB();
        mExercisePosition = -1;
        observeExercises();
    }

    public void stopWorkout() {
        if (mWorkout != null) onStopWorkout();
    }

    private void onStopWorkout() {
        resetWorkoutWithExercises();
    }

    public void startExercise() {
        if (mExercise != null && mExercise.getStatus()!=RUNNING) onStartExercise();
    }

    private void onStartExercise() {
        mExercise.start();
        onExerciseChanges();
    }

    public void stopExercise() {
        if (mExercise != null) onStopExercise();
    }

    private void onStopExercise() {
        mExercise.stop();
        onExerciseChanges();
    }

    public void restartExercise() {
        if (mExercise != null) onRestartExercise();
    }

    private void onRestartExercise() {
        mExercise.restart();
    }

    public void skipExercise() {
        if (mExercise != null) onSkipExercise();
    }

    public void onSkipExercise() {
        mExercise.skip();
        onNextExercise();
    }

    public void finishExercise() {
        if (mExercise != null && mExercise.getStatus()==RUNNING) onFinishExercise();
    }

    private void onFinishExercise() {
        mExercise.finish();
        storeWorkoutChangesToDB();
        storeExerciseChangesToDB();
        onNextExercise();
    }

    public void setExercise(Exercise exercise) {
        if (mExercise == null || mExercise.getStatus()!=RUNNING) onSetExercise(exercise);
    }

    public void onSetExercise(Exercise exercise) {
        mExercise = exercise;
        onExerciseChanges();
    }

    private void onNextExercise() {
        if (mExercises == null) return;
        if (mExercisePosition < mExercises.size() - 1) {
            Exercise exercise = mExercises.get(++mExercisePosition);
            if (exercise.getStatus()!=DONE && exercise.getStatus()!=SKIPPED) {
                mExercise = exercise;
                onExerciseChanges();
                Log.i(TAG, "onNextExercise: " + exercise.getName());
            } else {
                onNextExercise();
            }
        } else {
            mWorkout.stop();
        }
    }

    public LiveData<Workout> getActiveWorkoutLD() {
        return mActiveWorkoutLD;
    }

    public LiveData<Exercise> getActiveExerciseLD() {
        return mActiveExerciseLD;
    }

    public boolean getAutoStartNext() {
        return mAutoStartNext;
    }

    public void setAutoStartNext(boolean autoStartNext) {
        mAutoStartNext = autoStartNext;
    }

    private void resetWorkoutWithExercises() {
        mWorkout.reset();
        mWorkoutsRepository.updateWorkout(mWorkout);
        for (Exercise exercise : mExercises) exercise.reset();
        mWorkoutsRepository.updateExercises(mExercises);
        mWorkoutsRepository.getExercisesLD(mWorkout.getId()).removeObserver(mObserver);
        mWorkout = null;
        mExercises = null;
        mExercise = null;
        mActiveWorkoutLD.postValue(null);
        mActiveExerciseLD.postValue(null);
        timerHandler.removeCallbacks(timerRunnable);
    }

    private void observeExercises() {
        mWorkoutsRepository.getExercisesLD(mWorkout.getId()).observeForever(mObserver);
    }

    private void onExercisesLoaded(List<Exercise> exercises) {
        mExercises = exercises;
        if (mExercise==null) onNextExercise();
        Log.i(TAG, "onExercisesLoaded: " + exercises);
    }


    private void onWorkoutChanges() {
        mActiveWorkoutLD.postValue(mWorkout);
    }

    private void storeWorkoutChangesToDB() {
        mWorkoutsRepository.updateWorkout(mWorkout);
        onWorkoutChanges();
    }

    private void storeExerciseChangesToDB() {
        mWorkoutsRepository.updateExercise(mExercise);
        onExerciseChanges();
    }

    private void onExerciseChanges() {
        mActiveExerciseLD.postValue(mExercise);
    }

}

//
//    public LiveData<Long> getWorkoutTime() {
//        return mWorkoutTime;
//    }
//
//    public LiveData<Long> getExerciseTime() {
//        return mExerciseTime;
//    }
//
//    public LiveData<Long> getRestTimePlan() {
//        return mRestTime;
//    }

//
//    private Timer mWorkoutTimer = new Timer();
//    private Timer mExerciseTimer = new Timer();
//    private Timer mRestTimer = new Timer();
//    private Long lastTime = 0L;
//    private long mTimerSecondCorrection;
//    private void onStartWorkoutTimer() {
//        mWorkoutTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                onTimerWorkoutTick();
//            }
//        }, 1000, 1000);
//    }

//    private void onTimerWorkoutTick() {
//        if (SystemClock.elapsedRealtime() > lastTime) {
//            lastTime = SystemClock.elapsedRealtime() + 1000;
//        }
//        final long workoutTime = (SystemClock.elapsedRealtime() - mWorkoutInitialTime);
//        mWorkoutTime.postValue(workoutTime);
//    }
//private void onTimerExerciseTick() {
//        final long exerciseTime = (SystemClock.elapsedRealtime() - mExerciseInitialTime);
//        mExerciseTime.postValue(exerciseTime);
//        if (exerciseTime > mExercise.getTimePlan() * 1000) {
//            onFinishExercise();
//            mExerciseTimer.cancel();
//        }
//    }
//
//    private void onRestTimerTick() {
//        final long restTime = (SystemClock.elapsedRealtime() - mRestInitialTime);
//        mRestTime.postValue(restTime);
//        if (restTime > mExercise.getRestTimePlan() * 1000) {
//            mRestTimer.cancel();
//            onNextExercise();
//        }
//    }