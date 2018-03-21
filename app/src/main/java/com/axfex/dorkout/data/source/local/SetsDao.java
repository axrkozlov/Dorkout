package com.axfex.dorkout.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.axfex.dorkout.data.Eset;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by udafk on 11.03.2018.
 */
@Dao
public interface SetsDao {
    @Query("SELECT * FROM Eset WHERE id = :id")
    LiveData<Eset> getSet(Long id);

    @Query("SELECT * from Eset where exerciseId = :exerciseId")
    LiveData<List<Eset>> getSets(final Long exerciseId);

    @Query("SELECT COUNT(*) from Eset  where exerciseId = :exerciseId")
    LiveData<Integer> getSetsCount(final Long exerciseId);

    @Insert(onConflict = REPLACE)
    Long[] insertSets(Eset... Eset);

    @Update
    int updateSet(Eset Eset);

    @Delete
    void deleteSet(Eset... esets);
    
}
