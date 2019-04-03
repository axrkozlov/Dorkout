package com.axfex.dorkout.di;


import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.services.WorkoutPerformingManager;
import com.axfex.dorkout.views.workouts.WorkoutViewModel;
import com.axfex.dorkout.views.workouts.EditWorkoutViewModel;
import com.axfex.dorkout.views.workouts.MainViewModel;
import com.axfex.dorkout.views.workouts.WorkoutsViewModel;
import com.axfex.dorkout.vm.ViewModelFactory;


import dagger.Module;
import dagger.Provides;

@Module
class ViewModelsModule {

    @Provides
    ViewModelFactory<MainViewModel> provideMainViewModelFactory(MainViewModel mainViewModel){
        return new ViewModelFactory<>(mainViewModel);
    }

    @Provides
    MainViewModel providesMainViewModel(){
        return new MainViewModel();
    }

    @Provides
    ViewModelFactory<WorkoutsViewModel> provideWorkoutsViewModelFactory(WorkoutsViewModel workoutsViewModel){
        return new ViewModelFactory<>(workoutsViewModel);
    }

    @Provides
    WorkoutsViewModel providesWorkoutsViewModel(WorkoutsRepository workoutsRepository){
        return new WorkoutsViewModel(workoutsRepository);
    }

    @Provides
    ViewModelFactory<EditWorkoutViewModel> provideEditWorkoutViewModelFactory(EditWorkoutViewModel editWorkoutViewModel){
        return new ViewModelFactory<>(editWorkoutViewModel);
    }

    @Provides
    EditWorkoutViewModel providesEditWorkoutViewModel(WorkoutsRepository workoutsRepository){
        return new EditWorkoutViewModel(workoutsRepository);
    }

    @Provides
    ViewModelFactory<WorkoutViewModel> provideActionWorkoutViewModelFactory(WorkoutViewModel workoutViewModel){
        return new ViewModelFactory<>(workoutViewModel);
    }

    @Provides
    WorkoutPerformingManager providesActionWorkoutManager(WorkoutsRepository workoutsRepository){
        return new WorkoutPerformingManager(workoutsRepository);
    }

    @Provides
    WorkoutViewModel providesActionWorkoutViewModel(WorkoutsRepository workoutsRepository, WorkoutPerformingManager workoutPerformingManager){
        return new WorkoutViewModel(workoutsRepository, workoutPerformingManager);
    }


}
