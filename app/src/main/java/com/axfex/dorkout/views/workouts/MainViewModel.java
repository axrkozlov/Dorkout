package com.axfex.dorkout.views.workouts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.axfex.dorkout.navigation.NavigationEvent;
import com.axfex.dorkout.util.FreshMutableLiveData;

import static com.axfex.dorkout.navigation.NavigationEvent.EventType.EDIT_WORKOUT;
import static com.axfex.dorkout.navigation.NavigationEvent.EventType.WORKOUT;
import static com.axfex.dorkout.navigation.NavigationEvent.EventType.WORKOUTS;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class MainViewModel extends ViewModel {
    private static final String TAG = "MAIN_VIEW_MODEL";

    private final MutableLiveData<NavigationEvent> mNavigationEvent = new FreshMutableLiveData<>();
    private final MutableLiveData<Long> mStartingWorkoutEvent = new FreshMutableLiveData<>();
    private final MutableLiveData<Long> mStoppingWorkoutEvent = new FreshMutableLiveData<>();


    LiveData<NavigationEvent> getNavigationEvent() {
        return mNavigationEvent;
    }

    private void setShow(NavigationEvent.EventType eventType, Long id){
        NavigationEvent navigationEvent=new NavigationEvent(eventType,id);
        mNavigationEvent.setValue(navigationEvent);
    }

    void openEditWorkout(Long id) {
        setShow(EDIT_WORKOUT,id);
    }

    void openWorkout(Long id) {
        setShow(WORKOUT,id);
    }

    MutableLiveData<Long> getStartingWorkoutEvent() {
        return mStartingWorkoutEvent;
    }

    MutableLiveData<Long> getStoppingWorkoutEvent() {
        return mStoppingWorkoutEvent;
    }

    void startWorkout(Long id){
        mStartingWorkoutEvent.setValue(id);
    }

    void openWorkouts(){
        setShow(WORKOUTS,null);
    }

    public void stopWorkout(Long id) {
        mStoppingWorkoutEvent.setValue(id);
    }
}
