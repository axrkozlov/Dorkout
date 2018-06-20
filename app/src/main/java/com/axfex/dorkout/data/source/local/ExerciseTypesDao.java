package com.axfex.dorkout.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.ExerciseType;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ExerciseTypesDao {
    @Insert(onConflict = REPLACE)
    Long insertExerciseType(ExerciseType exerciseType);

    @Query("SELECT * from ExerciseType order by lastUsed desc")
    LiveData<List<ExerciseType>> getExerciseTypes();

    @Delete
    void deleteExerciseType(ExerciseType exerciseType);


}
