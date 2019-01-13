package com.axfex.dorkout.views.workouts;

import com.axfex.dorkout.data.Workout;

public interface WorkoutsNavigator {
    void onNewWorkout();
    void onRenameWorkout(Workout workout);
    void onOpenEditWorkout(Workout workout);
    void onDeleteWorkout(Workout workout);
    void onOpenActionWorkout(Workout workout);




}
