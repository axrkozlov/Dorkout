package com.axfex.dorkout.data.source.local;

import com.axfex.dorkout.data.Workout;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * Created by alexanderkozlov on 1/2/18.
 */

@Dao
public interface WorkoutsDao {

    @Query("SELECT * FROM Workout WHERE id = :id")
    LiveData<Workout> getWorkout(Long id);

    @Query("SELECT * FROM Workout ORDER BY lastDate DESC")
    LiveData<List<Workout>> getWorkouts();

    @Insert(onConflict = REPLACE)
    Long insertWorkout(Workout workout);

    @Update
    int updateWorkout(Workout workout);

    @Delete
    void deleteWorkout(Workout workout);

}
