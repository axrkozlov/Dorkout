package com.axfex.dorkout.di;

import android.app.Application;

import com.axfex.dorkout.views.workouts.EditWorkoutFragment;
import com.axfex.dorkout.views.workouts.MainActivity;
import com.axfex.dorkout.views.workouts.WorkoutsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by alexanderkozlov on 1/7/18.
 */
@Singleton
@Component(modules = {AppModule.class,RoomModule.class,ViewModelsModule.class})
public interface AppComponent {

    void inject (MainActivity mainActivity);
    void inject (WorkoutsFragment workoutsFragment);
    void inject (EditWorkoutFragment editWorkoutFragment);
//    void inject (EditWorkoutActivity editWorkoutActivity);
//    void inject (ExercisesFragment exercisesFragment);
//    void inject (AddEditExerciseFragment addEditExerciseFragment);
//    void inject (WorkoutsFragment workoutsFragment);



    //void inject (MainViewModel mMainViewModel);

    Application application();
}
