package com.axfex.dorkout.views.workouts.list;

public interface WorkoutsNavigator {
    void newWorkout();
    void deleteWorkout(String name, Long id);
    void openEditWorkout(Long id);
    void openWorkout(Long id);
    void openAbout();
    void openSettings();
    void openDonate();

}
