package com.axfex.dorkout.di;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.axfex.dorkout.WorkoutApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alexanderkozlov on 1/7/18.
 */
@Module
public class AppModule {

    private final Application application;

    public AppModule(@NonNull WorkoutApplication application) {
        this.application = application;
    }


    @Provides
    @Singleton
    Context provideApplication(){
        return application;
    }

}
