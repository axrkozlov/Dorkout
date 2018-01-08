package com.axfex.dorkout.util;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.axfex.dorkout.addeditworkout.AddEditWorkoutViewModel;
import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.workouts.WorkoutsViewModel;

import javax.inject.Inject;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {
    private WorkoutsRepository workoutsRepository;

    @Inject
    public ViewModelFactory(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WorkoutsViewModel.class))
            return (T) new WorkoutsViewModel(workoutsRepository);

        else if (modelClass.isAssignableFrom(AddEditWorkoutViewModel.class))
            return (T) new AddEditWorkoutViewModel(workoutsRepository);

        else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }



}
