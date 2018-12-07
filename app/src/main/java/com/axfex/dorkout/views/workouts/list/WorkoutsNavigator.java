package com.axfex.dorkout.views.workouts.list;

public interface WorkoutsNavigator {
    void openEditWorkout(Long id);
    void startWorkout(Long id);
    void openAbout();
    void openSettings();
    void openDonate();

}
