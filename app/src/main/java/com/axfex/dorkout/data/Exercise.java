package com.axfex.dorkout.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by alexanderkozlov on 2/18/18.
 */

@Entity(foreignKeys = {@ForeignKey(entity = Workout.class,
        parentColumns = "id",
        childColumns = "workoutId",
        onDelete = CASCADE)},
        indices = {@Index(value = "workoutId")}
        )

    public class Exercise {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name = "workoutId")
    private Long workoutId;
    private String name;
    private String description;
    private Integer orderNumber;
    private Integer normWeight;
    private Integer normRepeats;
    private Integer normTime;
    private Integer restTime;
    private Integer factWeight;
    private Integer factRepeats;
    private Integer factTime;
    @Ignore
    private transient Boolean isChecked = false;
    @Ignore
    private transient Boolean isStarted = false;


    public Exercise(String name, final Long workoutId) {
        this.name = name;
        this.workoutId=workoutId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
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

    public Integer getRestTime() {
        return restTime;
    }

    public void setRestTime(Integer restTime) {
        this.restTime = restTime;
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

    public void setFactRepeats(Integer factRepeats) {
        this.factRepeats = factRepeats;
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
