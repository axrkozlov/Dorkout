package com.axfex.dorkout.data;


import com.axfex.dorkout.data.source.local.TimestampConverter;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import static androidx.room.ForeignKey.CASCADE;

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
    @TypeConverters({TimestampConverter.class})
//    private Date creationDate;
//    @ColumnInfo(name = "creation_date")
    private Date creationDate = new Date(System.currentTimeMillis());

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean isStarted() {
        return isStarted;
    }

    public void start() {
        isStarted = true;
    }
    public void stop() {
        isStarted = false;
    }
}
