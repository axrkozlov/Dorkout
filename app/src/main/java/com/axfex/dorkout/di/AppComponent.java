package com.axfex.dorkout.di;

import android.app.Application;

import com.axfex.dorkout.exercises.addedit.AddEditExerciseFragment;
import com.axfex.dorkout.workouts.addedit.AddEditWorkoutFragment;
import com.axfex.dorkout.exercises.list.ExercisesFragment;
import com.axfex.dorkout.workouts.list.WorkoutsFragment;

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
    void inject (ExercisesFragment exercisesFragment);
    void inject (AddEditExerciseFragment addEditExerciseFragment);


    //void inject (WorkoutsViewModel workoutsViewModel);

    Application application();
}
