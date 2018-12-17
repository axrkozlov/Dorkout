package com.axfex.dorkout.vm;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.views.workouts.edit.EditWorkoutViewModel;
import com.axfex.dorkout.views.workouts.list.WorkoutsViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by alexanderkozlov on 1/7/18.
 */
@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory {

    private WorkoutsRepository workoutsRepository;

    @Inject
    public ViewModelFactory(WorkoutsRepository workoutsRepository) {
        this.workoutsRepository = workoutsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WorkoutsViewModel.class)) {

            return (T) new WorkoutsViewModel(workoutsRepository);
        }
        else
        if (modelClass.isAssignableFrom(EditWorkoutViewModel.class))
            return (T) new EditWorkoutViewModel(workoutsRepository);

        else
        if (modelClass.isAssignableFrom(ExercisesViewModel.class))
            return (T) new ExercisesViewModel(workoutsRepository);

        else
        if (modelClass.isAssignableFrom(AddEditExerciseViewModel.class))
            return (T) new AddEditExerciseViewModel(workoutsRepository);

        else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }



}
