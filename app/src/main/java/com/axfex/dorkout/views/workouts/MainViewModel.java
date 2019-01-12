package com.axfex.dorkout.views.workouts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.util.FreshMutableLiveData;
import com.axfex.dorkout.util.Show;

import java.util.List;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class MainViewModel extends ViewModel {
    private static final String TAG = "MAIN_VIEW_MODEL";

    private Workout mCurrentWorkout;
    private Show mShow;

//    private MutableLiveData<Show> mViewStateEvent = new MutableLiveData<>();
    private final MutableLiveData<Show> mShowEvent = new FreshMutableLiveData<>();
    private WorkoutsRepository mWorkoutsRepository;


    public MainViewModel(WorkoutsRepository workoutsRepository) {
        this.mWorkoutsRepository = workoutsRepository;
//        setShow(WorkoutsFragment.TAG);
//        openShow();
    }

    void setShow(String tag){
        mShow=new Show(tag);
    }

    String getShowTag(Show show){
        return show.getTag();
    }

    private void setShow(String string, Workout workout){
        mShow=new Show<>(string,workout);
    }

    private void setShow(String string, Exercise exercise){
        mShow=new Show<>(string,exercise);
    }

    void openShow(){
        mShowEvent.setValue(mShow);
    }

    LiveData<Show> getShow() {
        return mShowEvent;
    }

    void pickWorkout(Workout workout) {
        if (workout == null || workout == mCurrentWorkout) {
            mCurrentWorkout = null;
            setShow(WorkoutsFragment.TAG);
            openShow();
            return;
        }
        mCurrentWorkout = workout;
        setShow(WorkoutsFragment.TAG,workout);
        openShow();
    }

    void editWorkout(Workout workout) {
        mCurrentWorkout = workout;
        setShow(EditWorkoutFragment.TAG,workout);
        openShow();
    }

    void openWorkout(final Workout workout) {
        mCurrentWorkout = workout;
        setShow(ActionWorkoutFragment.TAG,workout);
        openShow();
    }

//    void onShowOpened(java.lang.String showTag) {
//        mCurrentWorkout = null;
//        setShow(WORKOUTS_SHOW);
//    }


    LiveData<List<Workout>> getWorkouts() {
        return mWorkoutsRepository.getWorkouts();
    }

    Workout getCurrentWorkout() {
        return mCurrentWorkout;
    }

    LiveData<Long> createWorkout(Workout workout) {
        return mWorkoutsRepository.createWorkout(workout);
    }

    void updateWorkout(final Workout workout) {
        mWorkoutsRepository.updateWorkout(workout);
    }

    void deleteWorkout(final Workout workout) {
        mWorkoutsRepository.deleteWorkout(workout);
    }

    /*Exercises*/

    LiveData<List<Exercise>> getExercises(@NonNull Long workoutId) {
        return mWorkoutsRepository.getExercises(workoutId);
    }

}
