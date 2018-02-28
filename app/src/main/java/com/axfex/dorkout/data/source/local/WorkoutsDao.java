package com.axfex.dorkout.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.axfex.dorkout.data.Workout;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by alexanderkozlov on 1/2/18.
 */

@Dao
public interface WorkoutsDao {

    @Query("SELECT * FROM Workout WHERE id = :id")
    LiveData<Workout> getWorkout(int id);

    @Query("SELECT * FROM Workout")
    LiveData<List<Workout>> getWorkouts();

    @Insert(onConflict = REPLACE)
    Long insertWorkout(Workout workout);

    @Update
    int updateWorkout(Workout workout);

    @Delete
    void deleteWorkout(Workout... workout);

}
