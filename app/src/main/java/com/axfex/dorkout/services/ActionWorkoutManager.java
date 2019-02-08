package com.axfex.dorkout.services;

import android.os.Handler;
import android.util.Log;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Rest;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.List;


import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class ActionWorkoutManager {
    private static final String TAG = "ACTION_WORKOUT_MANAGER";

    private static final int UPDATE_PERIOD = 40;

    private Workout mWorkout;
    private List<Exercise> mExercises;
    private Exercise mExercise;

    private Rest mRest;

    private int mExercisePosition;
    private boolean mAutoMode;
    private boolean prepareExercises = false;
    private WorkoutsRepository mWorkoutsRepository;
    private MutableLiveData<Exercise> mActiveExerciseLD = new MutableLiveData<>();
    private MutableLiveData<Rest> mRestLD = new MutableLiveData<>();
    private MutableLiveData<Workout> mActiveWorkoutLD = new MutableLiveData<>();

    Long mWorkoutId;
    private final Observer<List<Exercise>> mObserver = this::onExercisesLoaded;
    private LiveData<List<Exercise>> mExercisesLD;

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new TimeUpdate();
    private Runnable restRunnable;

    private final class TimeUpdate implements Runnable {
        @Override
        public void run() {
            mExercise.updateTime();
            timerHandler.postDelayed(this, UPDATE_PERIOD);
        }
    }

    private final class RestUpdate implements Runnable {

        Rest mRest;

        public RestUpdate(Rest rest) {
            mRest = rest;
        }

        @Override
        public void run() {
            if (mRest == null) return;
            mRest.updateTime();
            if (mRest.hasExpired()) {
                onFinishRest();
            } else {
                timerHandler.postDelayed(this, UPDATE_PERIOD);
            }
        }
    }

    public ActionWorkoutManager(WorkoutsRepository workoutsRepository) {
        mWorkoutsRepository = workoutsRepository;
    }

    public void startWorkout(@NonNull Workout workout, @NonNull List<Exercise> exercises) {
        if (mWorkout == null || !mWorkout.getRunning()) onStartWorkout(workout, exercises);
    }

    public void stopWorkout() {
        if (mWorkout != null) onStopWorkout();
    }

    public void startExercise() {
        if (mWorkout != null) {
//            if (mExercise.getDone()) checkAndGetNextExercise();
//            else
            onStartExercise();
        }
//        onFinishRest();
    }

    public void pauseExercise() {
        if (mWorkout != null) onPauseExercise();
    }

    public void restartExercise() {
        if (mWorkout != null) onRestartExercise();
    }

    public void skipExercise() {
        if (mWorkout != null) onSkipExercise();
    }

    public void finishExercise() {
        if (mWorkout != null) onFinishExercise();
    }

    private void onStartWorkout(Workout workout, List<Exercise> exercises) {
        mWorkout = workout;
        boolean result = mWorkout.start();
        prepareExercises = result;
        updateWorkoutInDB(result);
        observeExercises();
    }

    private void onStartExercise() {
        if (mExercise.start()) {
            updateExerciseInDB(mExercise);
            timerHandler.post(timerRunnable);
        }
        onFinishRest();
    }

    private void onPauseExercise() {
        if (mExercise.pause()) updateExerciseInDB(mExercise);
    }

    private void onRestartExercise() {
        if (mExercise.restart()) {
            updateExerciseInDB(mExercise);
            timerHandler.post(timerRunnable);
        }

    }

    private void onSkipExercise() {
        if (mExercise.skip()) {
            updateExerciseInDB(mExercise);
            timerHandler.removeCallbacks(timerRunnable);
            onFinishRest();
        }
    }

    private void onFinishExercise() {
        if (mExercise.finish()) {
            updateExerciseInDB(mExercise);
            timerHandler.removeCallbacks(timerRunnable);
            if (!onStartRest()) checkAndGetNextExercise();
        }
    }

    private boolean onStartRest() {
        if (mExercise.getRestTimePlan() != null) {
            mRest = new Rest(mExercise.getRestTimePlan());
            mRestLD.postValue(mRest);
            restRunnable = new RestUpdate(mRest);
            timerHandler.post(restRunnable);
            return true;
        }
        return false;
    }

    private void onFinishRest() {
        timerHandler.removeCallbacks(restRunnable);
        mRest = null;
        mRestLD.postValue(null);
        if (mExercise.getDone() || mExercise.getSkipped()) checkAndGetNextExercise();
    }

    private void onStopWorkout() {
        resetWorkoutWithExercises();
    }

    public void setExercise(@NonNull Exercise exercise) {
//        if (mAutoMode) setNextExercise(exercise);
//        else
        if (mExercise == null || (!mExercise.getRunning() && !mExercise.getPaused())) {
            mExercise = exercise;
            onExerciseChanges();
        }
    }

    private void checkAndGetNextExercise() {
        Exercise anyAwaitingExercise = null;
        Exercise awaitingExercisePastCurrent = null;
        boolean findPastCurrent = false;
        if (mExercise != null)
            Log.i(TAG, "checkAndGetNextExercise: " + mExercise + mExercise.getOrderNumber() + " status: " + mExercise.getStatus());

        int pos = -1;
        for (Exercise e : mExercises) {
            pos++;
            if (e.is(mExercise)) {
                e=mExercise;
                mExercises.set(pos, e);
                findPastCurrent = true;
            }
            if (e.await()) updateExerciseInDB(e);
            if (findPastCurrent && e.getAwaiting() && awaitingExercisePastCurrent == null) {
                awaitingExercisePastCurrent = e;
            } else if (e.getAwaiting() && anyAwaitingExercise == null) {
                anyAwaitingExercise = e;
            }
        }
        if (mExercise != null && mExercise.next()) {
            updateExerciseInDB(mExercise);
        } else if (awaitingExercisePastCurrent != null && awaitingExercisePastCurrent.next()) {
            mExercise = awaitingExercisePastCurrent;
            updateExerciseInDB(mExercise);
        } else if (anyAwaitingExercise != null && anyAwaitingExercise.next()) {
            mExercise = anyAwaitingExercise;
            updateExerciseInDB(mExercise);
        }
    }

    public LiveData<Workout> getActiveWorkoutLD() {
        return mActiveWorkoutLD;
    }

    public LiveData<Exercise> getActiveExerciseLD() {
        return mActiveExerciseLD;
    }

    public LiveData<Rest> getRestLD() {
        return mRestLD;
    }

    public boolean getAutoMode() {
        return mAutoMode;
    }

    public void setAutoMode(boolean autoMode) {
        mAutoMode = autoMode;
    }

    private void resetWorkoutWithExercises() {
        mWorkout.finish();
        mWorkoutsRepository.updateWorkout(mWorkout);
        for (Exercise exercise : mExercises) exercise.reset();
        mWorkoutsRepository.updateExercises(mExercises);
        mExercisesLD.removeObserver(mObserver);
        mWorkout = null;
        mExercises = null;
        mExercise = null;
        mRest = null;
        mActiveWorkoutLD.postValue(null);
        mActiveExerciseLD.postValue(null);
        mRestLD.postValue(null);
        timerHandler.removeCallbacks(timerRunnable);
        timerHandler.removeCallbacks(restRunnable);
    }

    private void observeExercises() {
        mWorkoutId = mWorkout.getId();
        mExercisesLD = mWorkoutsRepository.getExercisesLD(mWorkoutId);
        mExercisesLD.observeForever(mObserver);
    }

    private void onExercisesLoaded(List<Exercise> exercises) {
        mExercises = exercises;
        if (prepareExercises) onPrepareExercises();
        for (Exercise e: exercises
             ) {
            Log.i(TAG, "onExercisesLoaded: " + e.getStatus());
        }
        Log.i(TAG, "onExercisesLoaded: -----------------");
    }

    private void onPrepareExercises() {
        for (Exercise e : mExercises) {
            e.await();
        }
        updateExercisesInDB();
        checkAndGetNextExercise();
        prepareExercises = false;

    }


    private void onWorkoutChanges() {
        mActiveWorkoutLD.postValue(mWorkout);
    }

    private void updateWorkoutInDB(boolean hasChanged) {
        if (!hasChanged) return;
        mWorkoutsRepository.updateWorkout(mWorkout);
        onWorkoutChanges();
    }

    private void updateExerciseInDB(Exercise exercise) {
        mWorkoutsRepository.updateExercise(exercise);
        onExerciseChanges();
    }

    private void updateExercisesInDB() {
        mWorkoutsRepository.updateExercises(mExercises);
    }


    private void onExerciseChanges() {
        mActiveExerciseLD.postValue(mExercise);
    }

}
//        if (mExercisePosition < mExercises.size() - 1) {
////            Exercise exercise = mExercises.get(++mExercisePosition);
////            if (exercise.getStatus() != DONE && exercise.getStatus() != SKIPPED) {
////                mExercise = exercise;
////                onExerciseChanges();
////                Log.i(TAG, "getNextExercise: " + exercise.getName());
////            } else {
////                getNextExercise();
////            }
////        } else {
////            mWorkout.pause();
////        }
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