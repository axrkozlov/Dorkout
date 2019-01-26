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
    private String note;
    private Integer orderNumber;
    private Integer weight;
    private Integer repeats;
    private Integer time;
    private Integer restTime;
    @TypeConverters({TimestampConverter.class})
    private Date creationDate = new Date(System.currentTimeMillis());

    private Integer weightFact;
    private Integer repeatsFact;
    private Integer timeFact;
    private Integer restTimeFact;

    private Boolean mDone = false;
    private Boolean mActive = false;
    private Boolean mSkipped = false;
    private Boolean mPaused = false;


    public Exercise() {
    }

    @Ignore
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Integer getWeightFact() {
        return weightFact;
    }

    public void setWeightFact(Integer weightFact) {
        this.weightFact = weightFact;
    }

    public Integer getRepeatsFact() {
        return repeatsFact;
    }

    public void setRepeatsFact(Integer repeatsFact) {
        this.repeatsFact = repeatsFact;
    }

    public Integer getTimeFact() {
        return timeFact;
    }

    public void setTimeFact(Integer timeFact) {
        this.timeFact = timeFact;
    }

    public Integer getRestTimeFact() {
        return restTimeFact;
    }

    public void setRestTimeFact(Integer restTimeFact) {
        this.restTimeFact = restTimeFact;
    }

    public Boolean getDone() {
        return mDone;
    }

    public void setDone(Boolean done) {
        mDone = done;
    }

    public Boolean getActive() {
        return mActive;
    }

    public void setActive(Boolean active) {
        mActive = active;
    }

    public Boolean getSkipped() {
        return mSkipped;
    }

    public void setSkipped(Boolean skipped) {
        mSkipped = skipped;
    }

    public Boolean getPaused() {
        return mPaused;
    }

    public void setPaused(Boolean paused) {
        mPaused = paused;
    }

}
