package com.axfex.dorkout.di;

import android.app.Application;
import androidx.annotation.NonNull;

import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.util.AppExecutors;

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


}
