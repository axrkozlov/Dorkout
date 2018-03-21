package com.axfex.dorkout.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.axfex.dorkout.data.Eset;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;

/**
 * Created by alexanderkozlov on 1/2/18.
 */

@Database(entities = {Workout.class, Exercise.class, Eset.class}, version = 1)
public abstract class WorkoutsDatabase extends RoomDatabase {

    public abstract WorkoutsDao workoutsDao();
    public abstract ExercisesDao exercisesDao();
    public abstract  SetsDao setsDao();
}
