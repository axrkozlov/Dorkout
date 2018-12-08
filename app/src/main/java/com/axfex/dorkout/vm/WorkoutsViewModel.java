package com.axfex.dorkout.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.views.workouts.list.WorkoutsNavigator;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class WorkoutsViewModel extends ViewModel {

    private final MutableLiveData<Boolean> pickedHolder = new MutableLiveData<>();
    private WorkoutsRepository workoutsRepository;
    private WeakReference<WorkoutsNavigator> mNavigator;
    private Long pickedId;
    private String pickedName;

    public WorkoutsViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public LiveData<List<Workout>> getWorkouts() {
        return workoutsRepository.getWorkouts();
    }

    public LiveData<Long> createWorkout(String name) {
        return workoutsRepository.createWorkout(new Workout(name));
    }

    public MutableLiveData<Boolean> onPick(){
        return pickedHolder;
    }


    public Long getPickedId(){
        return pickedId;
    }

    public String getPickedName(){
        return pickedName;
    }

    public void pick(Long id, String name){
        pickedId =id;
        pickedName =name;
        pickedHolder.postValue(true);

    }

    public void unpick(){
        pickedId =null;
        pickedName =null;
        pickedHolder.postValue(false);
    }

    public void deleteWorkout(final Workout workout){
        workoutsRepository.deleteWorkout(workout);
    }


}
