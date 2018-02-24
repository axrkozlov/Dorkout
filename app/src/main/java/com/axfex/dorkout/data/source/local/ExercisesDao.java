package com.axfex.dorkout.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.axfex.dorkout.data.Exercise;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


/**
 * Created by alexanderkozlov on 2/18/18.
 */
@Dao
public interface ExercisesDao {

    @Query("SELECT * FROM Exercise WHERE id = :id")
    LiveData<Exercise> getExercise(Long id);

    @Query("SELECT * from Exercise where workoutId = :workoutId")
    LiveData<List<Exercise>> getExercises(final int workoutId);

    //Test stuff
    @Query("SELECT * from Exercise")
    LiveData<List<Exercise>> getAllExercises();

    @Insert(onConflict = REPLACE)
    Long insertExercise(Exercise exercise);

    @Delete
    void deleteExercise(Exercise... exercises);
}
