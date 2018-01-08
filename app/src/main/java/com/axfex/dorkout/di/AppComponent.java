package com.axfex.dorkout.di;

import com.axfex.dorkout.addeditworkout.AddEditWorkoutFragment;
import com.axfex.dorkout.workouts.WorkoutsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by alexanderkozlov on 1/7/18.
 */
@Singleton
@Component(modules = {AppModule.class,RoomModule.class})
public interface AppComponent {

    void inject (WorkoutsFragment workoutsFragment);
    void inject (AddEditWorkoutFragment addEditWorkoutFragment);

    //Application application();
}
