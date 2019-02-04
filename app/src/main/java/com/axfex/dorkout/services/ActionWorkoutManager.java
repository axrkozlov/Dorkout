package com.axfex.dorkout.services;

import android.os.Handler;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.List;


import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import static com.axfex.dorkout.data.Status.RUNNING;

public class ActionWorkoutManager {
    private static final String TAG = "ACTION_WORKOUT_MANAGER";

    private boolean isRunning;

    private Workout mWorkout;
    private List<Exercise> mExercises;
    private Exercise mExercise;
    private Exercise mNextExercise;

    private int mExercisePosition;
    private boolean mAutoMode;
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
            mExercise.updateTime();
            timerHandler.postDelayed(this, 40);

        }

    }

    public ActionWorkoutManager(WorkoutsRepository workoutsRepository) {
        mWorkoutsRepository = workoutsRepository;
    }

    public void startWorkout(@NonNull Workout workout,@NonNull List<Exercise> exercises) {
        if (mWorkout == null || mWorkout.getStatus() != RUNNING) onStartWorkout(workout, exercises);
    }

    private void onStartWorkout(Workout workout,List<Exercise> exercises) {
        isRunning = true;
        mWorkout = workout;
        mWorkout.start();
        storeWorkoutChangesToDB();
        mExercises=exercises;
        for (Exercise exercise : mExercises) exercise.await();
        mWorkoutsRepository.updateExercises(mExercises);
        mExercisePosition = -1;
        observeExercises();
    }

    public void stopWorkout() {
        if (mWorkout != null) onStopWorkout();
    }

    private void onStopWorkout() {
        isRunning = false;
        resetWorkoutWithExercises();
    }

    public void startExercise() {
        if (mExercise != null && mExercise.getStatus() != RUNNING) onStartExercise();
    }

    private void onStartExercise() {
        mExercise.start();
        timerHandler.post(timerRunnable);
        onExerciseChanges();
    }

    public void stopExercise() {
        if (mExercise != null) onStopExercise();
    }

    private void onStopExercise() {
        mExercise.pause();
        timerHandler.removeCallbacks(timerRunnable);
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
        findNextExercise();
    }

    public void finishExercise() {
        if (mExercise != null &&(mExercise.getRunning()||mExercise.getPaused())) onFinishExercise();
    }

    private void onFinishExercise() {
        mExercise.finish();
        storeWorkoutChangesToDB();
        storeExerciseChangesToDB();
        findNextExercise();
    }

    public void setExercise(@NonNull Exercise exercise) {
        if (mAutoMode) setNextExercise(exercise);
        else if (mExercise == null || !mExercise.getRunning()) {
            mExercise = exercise;
            onExerciseChanges();
        }
    }

    private void setNextExercise(Exercise exercise) {
        if (exercise.getNext()) {
            exercise.await();
            storeExercisesChangesToDB();
        } else {
            for (Exercise e : mExercises) e.await();
            exercise.next();
            storeExercisesChangesToDB();
        }
    }

    private void findNextExercise() {
        Exercise nextExercise = null;
        Exercise anyAwaitingExercise = null;
        Exercise awaitingExercisePastCurrent = null;
        boolean findPastCurrent = false;
        for (Exercise e : mExercises) {
            if (e.getNext()) {
                nextExercise = e;
                break;
            }
            if (e.is(mExercise)) findPastCurrent = true;
            if (findPastCurrent && e.getAwaiting()) awaitingExercisePastCurrent = e;
            if (e.getAwaiting()) anyAwaitingExercise = e;
        }
        if (nextExercise != null) mExercise = nextExercise;
        else if (awaitingExercisePastCurrent != null) mExercise = awaitingExercisePastCurrent;
        else if (anyAwaitingExercise != null) mExercise = anyAwaitingExercise;
//        if (mExercisePosition < mExercises.size() - 1) {
////            Exercise exercise = mExercises.get(++mExercisePosition);
////            if (exercise.getStatus() != DONE && exercise.getStatus() != SKIPPED) {
////                mExercise = exercise;
////                onExerciseChanges();
////                Log.i(TAG, "findNextExercise: " + exercise.getName());
////            } else {
////                findNextExercise();
////            }
////        } else {
////            mWorkout.pause();
////        }
    }

    public LiveData<Workout> getActiveWorkoutLD() {
        return mActiveWorkoutLD;
    }

    public LiveData<Exercise> getActiveExerciseLD() {
        return mActiveExerciseLD;
    }

    public boolean getAutoMode() {
        return mAutoMode;
    }

    public void setAutoMode(boolean autoMode) {
        mAutoMode = autoMode;
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
        if (mExercise == null) findNextExercise();
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

    private void storeExercisesChangesToDB() {
        mWorkoutsRepository.updateExercises(mExercises);
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