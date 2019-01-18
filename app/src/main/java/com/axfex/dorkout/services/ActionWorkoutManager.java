package com.axfex.dorkout.services;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;

import java.util.List;

import androidx.annotation.NonNull;

public class ActionWorkoutManager {
    private Workout mWorkout;
    private List<Exercise> mExercises;
    private Exercise mExercise;
    private int mExercisePosition=0;
    private boolean mAutoStartNext;
//    private boolean isOn =false;
    private boolean isWorkoutPaused=false;
    private boolean isWorkoitDone=false;

    public ActionWorkoutManager() {
    }

    public void setWorkout(@NonNull  Workout workout, @NonNull List<Exercise> exercises){
        mWorkout = workout;
        mExercises = exercises;
    }

    public void start(){

        mWorkout.start();
        if (mExercises.size()!=0) {
            goToNextExercise();
        } else {
            mExercise=new Exercise("Exercise",mWorkout.getId());
        }
    }

    public Workout getWorkout() {
        return mWorkout;
    }
    public Exercise getExercise() {
        return mExercise;
    }

    public List<Exercise> getExercises() {
        return mExercises;
    }

    public void pause(){
        mExercise.pause();
    }

    public void finish(){
        mWorkout.finish();
        mExercise.finish();
    }

    public void setExercise(int exercisePosition){
        if (mExercise.isActive()) return;
        mExercise= mExercises.get(exercisePosition);
        mExercisePosition=exercisePosition;
        if (mAutoStartNext) startExercise();
    }

    public void goToNextExercise(){
        if (mExercisePosition<mExercises.size()){
            //check if next is done
            if (!mExercises.get(mExercisePosition+1).isDone()) {
                setExercise(mExercisePosition+1);
            } else {
                goToNextExercise();
            }
        } else {
            int i=0;
            boolean hasAnyUndone=false;
            for (Exercise exercise:mExercises
                    ) {
                i++;
                if (!exercise.isDone()){
                    setExercise(i);
                    hasAnyUndone=true;
                }
            }
            if (!hasAnyUndone) {mWorkout.finish();}
        }
    }

    public void startExercise(){
        mExercise.start();
    }

    public void restartExercise(){
        //about timers
    }

    public void skipExercise(){

    }

    public void finishExercise(){
        if (mWorkout.isActive()) {
            mExercise.finish();
            goToNextExercise();
        }
    }

    public boolean isAutoStartNext() {
        return mAutoStartNext;
    }

    public void setAutoStartNext(boolean autoStartNext) {
        mAutoStartNext = autoStartNext;
    }
}
