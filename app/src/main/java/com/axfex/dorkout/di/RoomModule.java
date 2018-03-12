package com.axfex.dorkout.di;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.data.source.local.ExercisesDao;
import com.axfex.dorkout.data.source.local.SetsDao;
import com.axfex.dorkout.data.source.local.WorkoutsDao;
import com.axfex.dorkout.data.source.local.WorkoutsDatabase;
import com.axfex.dorkout.vm.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

@Module
public class RoomModule {

    private final WorkoutsDatabase workoutsDatabase;


    public RoomModule(Application application) {
        this.workoutsDatabase = Room.databaseBuilder(application,WorkoutsDatabase.class,"workouts.db").build();
    }

    @Provides
    @Singleton
    WorkoutsRepository providesWorkoutsRepository(WorkoutsDao workoutsDao, ExercisesDao exercisesDao, SetsDao setsDao){
        return new WorkoutsRepository(workoutsDao,exercisesDao,setsDao);
    }

    @Provides
    @Singleton
    WorkoutsDao providesWorkoutsDao(WorkoutsDatabase workoutsDatabase){
        return workoutsDatabase.workoutsDao();
    }

    @Provides
    @Singleton
    ExercisesDao providesExercisesDao(WorkoutsDatabase workoutsDatabase){
        return workoutsDatabase.exercisesDao();
    }

    @Provides
    @Singleton
    SetsDao providesSetsDao(WorkoutsDatabase workoutsDatabase){
        return workoutsDatabase.setsDao();
    }

    @Provides
    @Singleton
    WorkoutsDatabase providesWorkoutsDatabase(){
        return workoutsDatabase;
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(WorkoutsRepository workoutsRepository){
        return new ViewModelFactory(workoutsRepository);
    }

}
