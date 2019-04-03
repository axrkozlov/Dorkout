package com.axfex.dorkout.services;

import android.util.Log;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Rest;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.util.FreshMutableLiveData;

import java.util.List;


import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class WorkoutPerformingManager {
    private static final String TAG = "ACTION_WORKOUT_MANAGER";

    private static final int UPDATE_PERIOD = 40;

    private Workout mWorkout;
    private List<Exercise> mExercises;
    private Exercise mExercise;

    private Rest mRest;

    private boolean mAutoMode;
    private boolean isWorkoutStarted = false;
    private boolean isWorkoutResumed = false;
    private WorkoutsRepository mWorkoutsRepository;
    private MutableLiveData<Exercise> mActiveExerciseLD = new FreshMutableLiveData<>();
    private MutableLiveData<Rest> mRestLD = new MutableLiveData<>();
    private MutableLiveData<Workout> mActiveWorkoutLD = new MutableLiveData<>();
    private MutableLiveData<List<Exercise>> mActiveExercisesLD = new MutableLiveData<>();

    Long mWorkoutId;
    private final Observer<Workout> mWorkoutObserver = this::onWorkoutLoaded;
    private final Observer<List<Exercise>> mExercisesObserver = this::onExercisesLoaded;
    private LiveData<List<Exercise>> mExercisesLD;
    private LiveData<Workout> mWorkoutLD;

//    private Handler timerHandler = new Handler();
//    private Runnable timerRunnable = new TimeUpdate();
//    private Runnable restRunnable;
//
//    private final class TimeUpdate implements Runnable {
//        @Override
//        public void run() {
//            mExercise.updateTime();
//            timerHandler.postDelayed(this, UPDATE_PERIOD);
//        }
//    }
//
//    private final class RestUpdate implements Runnable {
//
//        Rest mRest;
//
//        public RestUpdate(Rest rest) {
//            mRest = rest;
//        }
//
//        @Override
//        public void run() {
//            if (mRest == null) return;
//            mRest.updateTime();
//            if (mRest.hasExpired()) {
//                onFinishRest();
//            } else {
//                timerHandler.postDelayed(this, UPDATE_PERIOD);
//            }
//        }
//    }

    public WorkoutPerformingManager(WorkoutsRepository workoutsRepository) {
        mWorkoutsRepository = workoutsRepository;
    }

    public void startWorkout(@NonNull Long workoutId) {
        Log.i(TAG, "startWorkout: " + workoutId);
        mWorkoutsRepository.setActiveWorkoutId(workoutId);
        if (mWorkout == null) init(workoutId);
    }

    public void stopWorkout() {
        if (mWorkout != null) onFinishWorkout();
    }

    public void startExercise() {
        if (mWorkout != null) onStartExercise();
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

//    public void finishRest() {
//        if (mWorkout != null) onFinishRest();
//    }

    public void setExercise(@NonNull Exercise exercise) {
        onSetExercise(exercise);
    }


    private void init(Long workoutId) {
        mWorkoutId = workoutId;
//        boolean result = mWorkout.start();
//        if (result) isWorkoutStarted = true;
//        else isWorkoutResumed = true;
//        updateWorkoutInDB(result);
        observeExercises();
    }

    private void onStartExercise() {
        if (mExercise.start()) {
            updateExerciseInDB(mExercise);
//            timerHandler.post(timerRunnable);
        }
        Log.i(TAG, "onStartExercise: ");
    }

    private void onPauseExercise() {
        if (mExercise.pause()) updateExerciseInDB(mExercise);
    }

    private void onRestartExercise() {
        if (mExercise.redo()) {
            updateExerciseInDB(mExercise);
        }
    }

    private void onSkipExercise() {
        if (mExercise.skip()) {
            updateExerciseInDB(mExercise);
//            timerHandler.removeCallbacks(timerRunnable);
//            onFinishRest();
        }
    }

    private void onFinishExercise() {
        if (mExercise.finish()) {
            updateExerciseInDB(mExercise);
//            timerHandler.removeCallbacks(timerRunnable);
//            if (!onStartRest()) getNextExercise();
        }
    }

//
//    private boolean onStartRest() {
//        if (mExercise.getRestTimePlan() != null) {
//            mRest = new Rest(mExercise.getRestTimePlan());
//            mRestLD.postValue(mRest);
//            restRunnable = new RestUpdate(mRest);
//            timerHandler.post(restRunnable);
//            return true;
//        }
//        return false;
//    }
//
//    private void onFinishRest() {
//        timerHandler.removeCallbacks(restRunnable);
//        mRest = null;
//        mRestLD.postValue(null);
//        Log.i(TAG, "onFinishRest: ");
//        getNextExercise();
//    }

    private void onFinishWorkout() {
//        resetWorkoutWithExercises();
    }

    private void onSetExercise(Exercise exercise) {
        if (exercise.is(mExercise)) return;
        if (mExercise != null && (mExercise.getRunning() || mExercise.getPaused())) {
            mExercise.reset();
            mExercise.await();
        }
        if (mExercise != null) {
            mExercise.setActive(false);
        }
        mExercise = exercise;
        mExercise.setActive(true);
        onExerciseChanges();
    }


    private void getNextExercise() {
        Exercise anyAwaitingExercise = null;
        Exercise awaitingExercisePastCurrent = null;
        boolean findPastCurrent = false;

        if (mExercise != null && (mExercise.getAwaiting() || mExercise.getRunning() || mExercise.getPaused()))
            return;
        for (Exercise e : mExercises) {
            if (e.is(mExercise)) {
                findPastCurrent = true;
            }
            if (findPastCurrent && e.getAwaiting() && awaitingExercisePastCurrent == null) {
                awaitingExercisePastCurrent = e;
            } else if (e.getAwaiting() && anyAwaitingExercise == null) {
                anyAwaitingExercise = e;
            }
        }

        if (awaitingExercisePastCurrent != null) {
            onSetExercise(awaitingExercisePastCurrent);
        } else if (anyAwaitingExercise != null) {
            onSetExercise(anyAwaitingExercise);
        }

    }

    public LiveData<Workout> getActiveWorkoutLD() {
        return mActiveWorkoutLD;
    }

    public LiveData<Exercise> getActiveExerciseLD() {
        return mActiveExerciseLD;
    }

    public LiveData<List<Exercise>> getActiveExercisesLD() {
        return mActiveExercisesLD;
    }

    public LiveData<Rest> getRestLD() {
        return mRestLD;
    }

//    private void resetWorkoutWithExercises() {
//        mWorkout.finish();
//        mWorkoutsRepository.updateWorkout(mWorkout);
//        for (Exercise exercise : mExercises) exercise.reset();
//        mWorkoutsRepository.updateExercises(mExercises);
//        mExercisesLD.removeObserver(mExercisesObserver);
//        mWorkout = null;
//        mExercise = null;
//        mExercises = null;
//        mRest = null;
//        mActiveWorkoutLD.postValue(null);
//        mRestLD.postValue(null);
//    }

    private void observeExercises() {

        mWorkoutLD = mWorkoutsRepository.getWorkoutLD(mWorkoutId);
        mWorkoutLD.observeForever(mWorkoutObserver);
        mExercisesLD = mWorkoutsRepository.getExercisesLD(mWorkoutId);
        mExercisesLD.observeForever(mExercisesObserver);
    }

    private void onWorkoutLoaded(Workout workout) {
        mWorkout = workout;
    }

    private void onExercisesLoaded(List<Exercise> exercises) {
        mExercises = exercises;
        prepareExercises();
        onExercisesChanges();
        for (Exercise e : exercises
        ) {
            e.getFinishEventLD().observeForever(this::finished);
            Log.i(TAG, "onExercisesLoaded: " + e);
        }
        Log.i(TAG, "onExercisesLoaded: -----------------");
    }

    private void finished(Boolean finished) {
        if (Boolean.TRUE.equals(finished)) getNextExercise();
    }

    private void prepareExercises() {
        boolean hasChanges = false;
        int pos = -1;
        for (Exercise e : mExercises) {
            pos++;
            if (e.is(mExercise)) {
//                e = mExercise;
//                mExercises.set(pos, e);
//                mExercise=e;
            }
            if (isWorkoutStarted || (isWorkoutResumed && (e.getRunning() || e.getPaused()))) {
                e.reset();
            }
            if (e.await()) hasChanges = true;
        }
        if (hasChanges) updateExercisesInDB();
        if (isWorkoutStarted || isWorkoutResumed) {
            getNextExercise();
            Log.i(TAG, "prepareExercises: ");
        }

        isWorkoutStarted = false;
        isWorkoutResumed = false;
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
    }

    private void updateExercisesInDB() {
        mWorkoutsRepository.updateExercises(mExercises);
    }


    private void onExerciseChanges() {
        mActiveExerciseLD.setValue(mExercise);
        Log.i(TAG, "onExerciseChanges: " + mExercise);
    }

    private void onExercisesChanges() {
        mActiveExercisesLD.setValue(mExercises);
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