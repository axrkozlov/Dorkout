package com.axfex.dorkout.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.axfex.dorkout.data.Set;
import com.axfex.dorkout.data.source.WorkoutsRepository;

import java.util.List;

/**
 * Created by udafk on 11.03.2018.
 */

public class SetsViewModel extends ViewModel {
    private WorkoutsRepository workoutsRepository;

    public SetsViewModel(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    public LiveData<List<Set>> getSets(@NonNull int exerciseId){
        return workoutsRepository.getSets(exerciseId);
    }
}
