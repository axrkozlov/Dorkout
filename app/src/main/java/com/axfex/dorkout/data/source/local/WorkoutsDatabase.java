package com.axfex.dorkout.data.source.local;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Created by alexanderkozlov on 1/2/18.
 */

@Database(entities = {Workout.class, Exercise.class}, version = 1,exportSchema = false)
public abstract class WorkoutsDatabase extends RoomDatabase {

    public abstract WorkoutsDao workoutsDao();
    public abstract ExercisesDao exercisesDao();
}
