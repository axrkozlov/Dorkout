package com.axfex.dorkout.services;

import android.util.Log;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ActionWorkoutManager {
    private static final String TAG ="ACTION_WORKOUT_MANAGER";
    private Workout mWorkout;
    private List<Exercise> mExercises;
    private Exercise mExercise;
    private int mExercisePosition;
    private boolean mAutoStartNext;
    //    private boolean isStarted = false;
    private WorkoutsRepository mWorkoutsRepository;
    private MutableLiveData<Exercise> mActiveExercise=new MutableLiveData<>();

    public ActionWorkoutManager(WorkoutsRepository workoutsRepository) {
        mWorkoutsRepository = workoutsRepository;
    }

    public void startWorkout(@NonNull Workout workout, @NonNull List<Exercise> exercises) {
        if (mWorkout == null||!mWorkout.getActive()) {
            mWorkout = workout;
            mExercises = exercises;
            onStartWorkout();
        }
    }

    public void stopWorkout() {
        if (mWorkout!=null) onStopWorkout();
    }

    public void startExercise() {
        if (mExercise!=null) onStartExercise(mExercise);
    }

    public void finishExercise() {
        if (mExercise!=null) onFinishExercise(mExercise);
        onNextExercise();
    }

    private void onStartWorkout(){
        mWorkout.setActive(true);
        updateWorkout();
        mExercisePosition = -1;
        onNextExercise();
    }

    private void onStopWorkout(){
        mWorkout.setActive(false);
        updateWorkout();
        mWorkout=null;
        for (Exercise exercise:mExercises
        ) {
            exercise.setDone(false);
            exercise.setPaused(false);
            exercise.setSkipped(false);
            exercise.setActive(false);
        }
        updateExercises();
        mExercises=null;
        mExercise=null;
        notifyExerciseChanged();
    }

    private void onStartExercise(Exercise exercise){
        exercise.setDone(false);
        exercise.setPaused(false);
        exercise.setSkipped(false);
        exercise.setActive(true);
        updateExercise();
        notifyExerciseChanged();
    }

    private void onFinishExercise(Exercise exercise){
        exercise.setDone(true);
        exercise.setPaused(false);
        exercise.setSkipped(false);
        exercise.setActive(false);
        updateExercise();
        notifyExerciseChanged();
    }

    public void setExercise(Exercise exercise) {
        if (mExercise!=null&&mExercise.getActive()) return;
        mExercise=exercise;
        notifyExerciseChanged();
    }

    public void onNextExercise() {
        if (mExercises==null) return;
        if (mExercisePosition < mExercises.size()-1) {
            if (!mExercises.get(++mExercisePosition).getDone()) {
                Exercise exercise=mExercises.get(mExercisePosition);
                setExercise(exercise);
                onStartExercise(exercise);
                Log.i(TAG, "onNextExercise: "+exercise.getName());
            } else {
                onNextExercise();
            }
        } else {
            onStopWorkout();
        }
    }


    public void pause() {
//        if (mExercise==null)return;
//        mExercise.setPaused(true);
    }



    public void restartExercise() {
        //about timers
    }

    public void skipExercise() {
//        if (mExercise==null)return;
//        mExercise.setPaused(false);
//        mExercise.setSkipped(true);
//        mExercise.setActive(false);
    }





    public Workout getWorkout() {
        return mWorkout;
    }

    public LiveData<Exercise> getActiveExercise() {
        return mActiveExercise;
    }

    public List<Exercise> getExercises() {
        return mExercises;
    }

    public boolean isAutoStartNext() {
        return mAutoStartNext;
    }

    public void setAutoStartNext(boolean autoStartNext) {
        mAutoStartNext = autoStartNext;
    }

    public Workout getActiveWorkout() {
        if (mWorkout != null && mWorkout.getActive()) return mWorkout;
        return null;
    }

    private void updateWorkout(){
        mWorkoutsRepository.updateWorkout(mWorkout);
    }

    private void updateExercise(){
        mWorkoutsRepository.updateExercise(mExercise);
    }

    private void updateExercises(){
        mWorkoutsRepository.updateExercises(mExercises);
    }

    private void notifyExerciseChanged(){
        mActiveExercise.setValue(mExercise);
    }

}
