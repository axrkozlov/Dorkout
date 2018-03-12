package com.axfex.dorkout.di;

import android.app.Application;

import com.axfex.dorkout.views.exercises.addedit.AddEditExerciseFragment;
import com.axfex.dorkout.views.workouts.addedit.AddEditWorkoutFragment;
import com.axfex.dorkout.views.exercises.list.ExercisesFragment;
import com.axfex.dorkout.views.workouts.list.WorkoutsFragment;

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
