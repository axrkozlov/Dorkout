package com.axfex.dorkout.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.axfex.dorkout.data.Workout;

/**
 * Created by alexanderkozlov on 1/2/18.
 */
@Database(entities = {Workout.class}, version = 1)
public abstract class WorkoutsDatabase extends RoomDatabase {

    public abstract WorkoutsDao workoutsDao();
//    private static WorkoutsDatabase INSTANCE;
//
//    private static final Object sLock = new Object();
//
//    public static WorkoutsDatabase getInstance(Context context) {
//        synchronized (sLock) {
//            if (INSTANCE == null) {
//                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                        WorkoutsDatabase.class, "Workouts.db")
//                        .build();
//            }
//            return INSTANCE;
//        }
//    }
}
