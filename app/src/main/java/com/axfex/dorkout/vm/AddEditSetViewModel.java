package com.axfex.dorkout.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.axfex.dorkout.data.Eset;
import com.axfex.dorkout.data.source.WorkoutsRepository;

/**
 * Created by udafk on 11.03.2018.
 */

public class AddEditSetViewModel extends ViewModel {
    private WorkoutsRepository workoutsRepository;

    public AddEditSetViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public void addSet(@NonNull final Eset... esets){
        new Thread(() -> workoutsRepository.createSets(esets)).start();
    }

    public LiveData<Eset> getSet(@NonNull final Long setId) {
        return workoutsRepository.getSet(setId);
    }

    public LiveData<Integer> getSetsCount(@NonNull final Long exerciseId){
        return workoutsRepository.getSetsCount(exerciseId);
    }

    public void updateSet(@NonNull final Eset eset) {
        new Thread(()->workoutsRepository.updateSet(eset)).start();
    }

    public void deleteSet(final Eset eset){
        new Thread(() -> workoutsRepository.deleteSet(eset)).start();
    }

}
