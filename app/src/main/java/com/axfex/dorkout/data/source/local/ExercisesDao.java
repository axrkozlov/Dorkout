package com.axfex.dorkout.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Query("SELECT * from Exercise where workoutId = :workoutId order by orderNumber")
    LiveData<List<Exercise>> getExercises(final Long workoutId);


    //@Query("SELECT * FROM colis INNER JOIN step ON colis.idColis= step.idColis ORDER BY date DESC")

//    @Query("SELECT * from Exercise where workoutId = :workoutId order by orderNumber")
//    //@Query("SELECT * from Exercise  INNER JOIN Eset  ON Exercise.id=Eset.exerciseId  where workoutId = :workoutId  ORDER BY Exercise.orderNumber ")
//    LiveData<List<ExerciseWithSets>> getExercisesWithSets(final Long workoutId);

    @Query("SELECT COUNT(*) from Exercise  where workoutId = :workoutId")
    LiveData<Integer> getExercisesCount(final Long workoutId);

    //Test stuff
    @Query("SELECT * from Exercise")
    LiveData<List<Exercise>> getAllExercises();

    @Insert(onConflict = REPLACE)
    Long insertExercise(Exercise exercise);

    @Update
    int updateExercise(Exercise... exercise);

    @Delete
    void deleteExercise(Exercise... exercises);

    @Query("DELETE from Exercise where id = :exerciseId")
    void deleteExercise(final Long exerciseId);

}
