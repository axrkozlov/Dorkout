package com.axfex.dorkout.views.workouts.list;

import com.axfex.dorkout.data.Workout;

public interface WorkoutsNavigator {
    void newWorkout();
    void deleteWorkout(Workout workout);
    void openEditWorkout(Workout workout);
    void onOpenWorkout(Workout workout);
    void renameWorkout(Workout workout);
    void openAbout();
    void openSettings();
    void openDonate();

}
