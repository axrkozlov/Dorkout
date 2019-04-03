package com.axfex.dorkout.data.source.local;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * Created by alexanderkozlov on 2/18/18.
 */
@Dao
public interface ExercisesDao {

    @Query("SELECT * FROM Exercise WHERE id = :id")
    LiveData<Exercise> getExerciseLD(Long id);

    @Query("SELECT * from Exercise where workoutId = :workoutId order by orderNumber")
    LiveData<List<Exercise>> getExercisesLD(final Long workoutId);

    @Query("SELECT DISTINCT name from Exercise order by creationDate DESC")
    LiveData<List<String>> getAllExerciseNamesLD();


    //@Query("SELECT * FROM colis INNER JOIN step ON colis.idColis= step.idColis ORDER BY date DESC")

//    @Query("SELECT * from Exercise where workoutId = :workoutId order by orderNumber")
//    //@Query("SELECT * from Exercise  INNER JOIN Eset  ON Exercise.id=Eset.exerciseId  where workoutId = :workoutId  ORDER BY Exercise.orderNumber ")
//    LiveData<List<ExerciseWithSets>> getExercisesWithSets(final Long workoutId);

    @Query("SELECT COUNT(*) from Exercise  where workoutId = :workoutId")
    LiveData<Integer> getExercisesCountLD(final Long workoutId);

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
