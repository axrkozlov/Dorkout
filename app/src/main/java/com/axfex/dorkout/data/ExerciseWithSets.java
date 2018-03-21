package com.axfex.dorkout.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by udafk on 21.03.2018.
 */

public class ExerciseWithSets {

    @Embedded
    public Exercise exercise;

    @Relation(parentColumn = "id",
            entityColumn = "exerciseId", entity = Eset.class)
    public List<Eset> esets;

//    public Exercise getExercise() {
//        return exercise;
//    }
//
//    public void setExercise(Exercise exercise) {
//        this.exercise = exercise;
//    }
//
//    public List<Eset> getEsets() {
//        return esets;
//    }
//
//    public void setEsets(List<Eset> esets) {
//        this.esets = esets;
//    }
}
