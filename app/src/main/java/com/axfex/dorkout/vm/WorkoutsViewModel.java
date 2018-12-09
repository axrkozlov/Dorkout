package com.axfex.dorkout.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.axfex.dorkout.data.Workout;
import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.util.LiveEvent;
import java.util.List;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class WorkoutsViewModel extends ViewModel {

    private final MutableLiveData<Boolean> pickEvent = new MutableLiveData<>();
    private final LiveEvent<Long> openWorkoutEvent = new LiveEvent<>();

    private WorkoutsRepository workoutsRepository;
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

    public MutableLiveData<Boolean> getPickEvent(){
        return pickEvent;
    }

    public LiveEvent<Long> getOpenWorkoutEvent(){
        return openWorkoutEvent;
    }



    public Long getPickedId(){
        return pickedId;
    }

    public String getPickedName(){
        return pickedName;
    }

    public void pick(Long id, String name){
        if (id==pickedId) {
            unpick();
            return;
        }
        pickedId =id;
        pickedName =name;
        pickEvent.setValue(true);

    }

    public void unpick(){
        pickedId =null;
        pickedName =null;
        pickEvent.setValue(false);
    }

    public boolean isWorkoutPicked(){
        return pickedId!=null;
    }

    

    public void deleteWorkout(final Workout workout){
        workoutsRepository.deleteWorkout(workout);
    }

    public void openWorkout(Long wodkoutId){
        if (wodkoutId != null) {
            openWorkoutEvent.setValue(wodkoutId);
        }
    }


}
