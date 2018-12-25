package com.axfex.dorkout.views.workouts.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.ViewGroup;

import com.axfex.dorkout.Navigator;
import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.util.SingleMutableLiveData;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class WorkoutsViewModel extends ViewModel {

    //private final MutableLiveData<Workout> mWorkout = new MutableLiveData<>();
    private Workout mWorkout;
    public enum ViewType {
        WORKOUTS,
        WORKOUT_SELECTION,
        WORKOUT_MODIFICATION,
        WORKOUT_EXECUTION,
    }


    private ViewType mViewType = ViewType.WORKOUTS;

    private MutableLiveData<Workout> mOpenWorkoutEvent = new SingleMutableLiveData<>();
    private MutableLiveData<ViewType> mViewTypeEvent = new MutableLiveData<>();
    private WorkoutsRepository mWorkoutsRepository;
    private Navigator mNavigator;

    @Inject
    public WorkoutsViewModel(WorkoutsRepository workoutsRepository, Navigator navigator) {
        this.mWorkoutsRepository = workoutsRepository;
        this.mNavigator=navigator;
        setViewType(ViewType.WORKOUTS);
    }

    LiveData<List<Workout>> getWorkouts() {
        return mWorkoutsRepository.getWorkouts();
    }

    Workout getWorkout(){
        return mWorkout;
    }

    LiveData<Long> createWorkout(Workout workout) { return mWorkoutsRepository.createWorkout(workout); }

    void updateWorkout(final Workout workout){mWorkoutsRepository.updateWorkout(workout);}

    void deleteWorkout(final Workout workout) {
        mWorkoutsRepository.deleteWorkout(workout);
    }

    LiveData<Workout> getOpenWorkoutEvent() {
        return mOpenWorkoutEvent;
    }

    LiveData<ViewType> getViewType() {
        return mViewTypeEvent;
    }

    private void setViewType(ViewType viewType){
        mViewTypeEvent.setValue(viewType);
    }

//    MutableLiveData<Workout> getPickWorkoutEvent() {
//        return mWorkout;
//    }

    void pickWorkout(Workout workout) {
        if (workout == null || workout == mWorkout) {
            mWorkout=null;
            setViewType(ViewType.WORKOUTS);
            return;
        }
        mWorkout=workout;
        setViewType(ViewType.WORKOUT_SELECTION);
    }

    void editWorkout(Workout workout){
        mWorkout=workout;
        setViewType(ViewType.WORKOUT_MODIFICATION);
    }

    ViewType getCurrentState(){
        return mViewType;
    }

    void openWorkout(final Workout workout) {
        //mOpenWorkoutEvent.setValue(workout);
        setViewType(ViewType.WORKOUT_EXECUTION);
    }


}
