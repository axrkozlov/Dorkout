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
    private Integer weight;
    private Integer repeats;
    private Integer time;
    private Integer restTime;


//    private Integer factWeight;
//    private Integer factRepeats;
//    private Integer factTime;
    @TypeConverters({TimestampConverter.class})
    private Date creationDate = new Date(System.currentTimeMillis());

    @Ignore
    private transient Boolean mDone = false;
    @Ignore
    private transient Boolean mActive = false;
    @Ignore
    private transient Boolean mSkipped = false;
    @Ignore
    private transient Boolean mPaused = false;


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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getRepeats() {
        return repeats;
    }

    public void setRepeats(Integer repeats) {
        this.repeats = repeats;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getRestTime() {
        return restTime;
    }

    public void setRestTime(Integer restTime) {
        this.restTime = restTime;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean isDone() {
        return mDone;
    }

    public void finish() {
        mActive=false;
        mDone = true;
    }

    public Boolean isSkipped() {
        return mSkipped;
    }

    public void skip() {
        mActive=false;
        mSkipped = true;
    }

    public Boolean isActive() {
        return mActive;
    }

    public void start() {
        if (mPaused) mPaused=false;
        if (mSkipped) mSkipped=false;
        else mActive = true;
        mSkipped=false;
    }

    public Boolean isPaused() {
        return mPaused;
    }

    public void pause() {
        mPaused = true;
    }
}
