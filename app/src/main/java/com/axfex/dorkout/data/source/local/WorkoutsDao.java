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
    LiveData<Workout> getWorkoutLD(Long id);

//    @Query("SELECT * FROM Workout WHERE running")
//    LiveData<Workout> getActiveWorkout();

    @Query("SELECT * FROM Workout ORDER BY startTime DESC")
    LiveData<List<Workout>> getWorkoutsLD();

    @Insert(onConflict = REPLACE)
    Long insertWorkout(Workout workout);

    @Update
    int updateWorkout(Workout workout);

    @Delete
    void deleteWorkout(Workout workout);

}
