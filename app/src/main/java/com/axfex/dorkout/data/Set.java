package com.axfex.dorkout.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by udafk on 11.03.2018.
 */

@Entity(foreignKeys = {@ForeignKey(entity = Exercise.class,
        parentColumns = "id",
        childColumns = "exerciseId",
        onDelete = CASCADE)},
        indices = {@Index(value = "exerciseId")}
)
public class Set {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "exerciseId")
    private final int exerciseId;
    private Integer order;
    private Integer normWeight;
    private Integer normRepeats;
    private Integer normTime;
    private Integer factWeight;
    private Integer factRepeats;
    private Integer factTime;
    @Ignore
    private transient Boolean isChecked = false;
    @Ignore
    private transient Boolean isStarted = false;

    public Set(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getNormWeight() {
        return normWeight;
    }

    public void setNormWeight(Integer normWeight) {
        this.normWeight = normWeight;
    }

    public Integer getNormRepeats() {
        return normRepeats;
    }

    public void setNormRepeats(Integer normRepeats) {
        this.normRepeats = normRepeats;
    }

    public Integer getNormTime() {
        return normTime;
    }

    public void setNormTime(Integer normTime) {
        this.normTime = normTime;
    }

    public Integer getFactWeight() {
        return factWeight;
    }

    public void setFactWeight(Integer factWeight) {
        this.factWeight = factWeight;
    }

    public Integer getFactRepeats() {
        return factRepeats;
    }

    public void setFactRepeats(Integer fectRepeats) {
        this.factRepeats = fectRepeats;
    }

    public Integer getFactTime() {
        return factTime;
    }

    public void setFactTime(Integer factTime) {
        this.factTime = factTime;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void check(Boolean checked) {
        isChecked = true;
    }
    public void uncheck(Boolean checked) {
        isChecked = false;
    }

    public Boolean getStarted() {
        return isStarted;
    }

    public void start() {
        isStarted = true;
    }
    public void stop() {
        isStarted = false;
    }
}
