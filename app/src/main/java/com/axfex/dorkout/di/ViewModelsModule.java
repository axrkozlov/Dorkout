package com.axfex.dorkout.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.MessageQueue;


import com.axfex.dorkout.data.source.WorkoutsRepository;
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
    MainViewModel providesMainViewModel(WorkoutsRepository workoutsRepository){
        return new MainViewModel(workoutsRepository);
    }
    @Provides
    ViewModelFactory<WorkoutsViewModel> provideWorkoutsViewModelFactory(WorkoutsViewModel workoutsViewModel){
        return new ViewModelFactory<>(workoutsViewModel);
    }

    @Provides
    WorkoutsViewModel providesWorkoutsViewModel(WorkoutsRepository workoutsRepository){
        return new WorkoutsViewModel(workoutsRepository);
    }

}
