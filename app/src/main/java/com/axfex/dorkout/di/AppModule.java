package com.axfex.dorkout.di;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.axfex.dorkout.Navigator;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.data.source.WorkoutsRepository;
import com.axfex.dorkout.util.AppExecutors;
import com.axfex.dorkout.views.workouts.list.WorkoutsViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alexanderkozlov on 1/7/18.
 */
@Module
public class AppModule {

    private final WorkoutApplication application;

    public AppModule(@NonNull WorkoutApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication(){
        return application;
    }

    @Provides
    @Singleton
    WorkoutApplication provideWorkoutApplication(){
        return application;
    }

    @Provides
    @Singleton
    AppExecutors providesAppExecutors(){
        return new AppExecutors();
    }

    @Provides
    @Singleton
    Navigator providesNavigator(WorkoutApplication workoutApplication){return new Navigator(workoutApplication);}

    @Provides
    @Singleton
    WorkoutsViewModel providesWorkoutsViewModel(WorkoutsRepository workoutsRepository,Navigator navigator){return new WorkoutsViewModel(workoutsRepository,navigator);}


}
