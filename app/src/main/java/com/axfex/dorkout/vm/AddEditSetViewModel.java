package com.axfex.dorkout.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import com.axfex.dorkout.data.Set;
import com.axfex.dorkout.data.source.WorkoutsRepository;

/**
 * Created by udafk on 11.03.2018.
 */

public class AddEditSetViewModel extends ViewModel {
    private WorkoutsRepository workoutsRepository;

    public AddEditSetViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public void addSet(@NonNull final Set... sets){
        new Thread(() -> workoutsRepository.createSets(sets)).start();
    }

    public LiveData<Set> getSet(@NonNull final Long setId) {
        return workoutsRepository.getSet(setId);
    }

    public LiveData<Integer> getSetsCount(@NonNull final Long exerciseId){
        return workoutsRepository.getSetsCount(exerciseId);
    }

    public void updateSet(@NonNull final Set set) {
        new Thread(()->workoutsRepository.updateSet(set)).start();
    }

    public void deleteSet(final Set  set){
        new Thread(() -> workoutsRepository.deleteSet(set)).start();
    }

}
