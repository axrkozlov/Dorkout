package com.axfex.dorkout.views.workouts;

import android.arch.lifecycle.ViewModel;

import com.axfex.dorkout.data.source.WorkoutsRepository;
public class WorkoutsViewModel extends ViewModel {
    private WorkoutsRepository mWorkoutsRepository;

    public WorkoutsViewModel(WorkoutsRepository workoutsRepository) {
        mWorkoutsRepository = workoutsRepository;
    }



}
