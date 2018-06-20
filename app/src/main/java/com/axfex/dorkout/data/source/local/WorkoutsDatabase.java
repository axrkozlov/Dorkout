package com.axfex.dorkout.data.source.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.ExerciseType;
import com.axfex.dorkout.data.Workout;

/**
 * Created by alexanderkozlov on 1/2/18.
 */

@Database(entities = {Workout.class, Exercise.class, ExerciseType.class}, version = 1)
public abstract class WorkoutsDatabase extends RoomDatabase {

    public abstract WorkoutsDao workoutsDao();
    public abstract ExercisesDao exercisesDao();
    public abstract ExerciseTypesDao exerciseTypesDao();


    private class Callback extends RoomDatabase.Callback {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

        }
    }

}
