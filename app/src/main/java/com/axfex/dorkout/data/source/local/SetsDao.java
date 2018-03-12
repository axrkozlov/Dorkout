package com.axfex.dorkout.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.axfex.dorkout.data.Set;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by udafk on 11.03.2018.
 */
@Dao
public interface SetsDao {
    @Query("SELECT * FROM 'Set' WHERE id = :id")
    LiveData<Set> getSet(int id);

    @Query("SELECT * from 'Set' where exerciseId = :exerciseId")
    LiveData<List<Set>> getSets(final int exerciseId);

    @Query("SELECT COUNT(*) from 'Set'  where exerciseId = :exerciseId")
    LiveData<Integer> getSetsCount(final int exerciseId);

    @Insert(onConflict = REPLACE)
    Long insertSet(Set Set);

    @Update
    int updateSet(Set Set);

    @Delete
    void deleteSet(Set... Sets);
    
}
