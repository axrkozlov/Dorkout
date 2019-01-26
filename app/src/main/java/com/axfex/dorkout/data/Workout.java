package com.axfex.dorkout.data;


import com.axfex.dorkout.data.source.local.TimestampConverter;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

/**
 * Created by alexanderkozlov on 1/2/18.
 */
@Entity
public class Workout {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private String note;
    private Long totalTime;
    private Long lastStartTime;
    private Integer order;
    private Integer exercisesCount;

    @TypeConverters({TimestampConverter.class})
    private Date startTime;
    private Boolean active = false;

    //Add Reminder instead


    public Workout() {
    }
    @Ignore
    public Workout(String name) {
        this.name = name;
    }
    @Ignore
    public Workout(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public Long getLastStartTime() {
        return lastStartTime;
    }

    public void setLastStartTime(Long lastStartTime) {
        this.lastStartTime = lastStartTime;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getExercisesCount() {
        return exercisesCount;
    }

    public void setExercisesCount(Integer exercisesCount) {
        this.exercisesCount = exercisesCount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}



//
//    public Integer getWeekDaysComposed() {
//        return weekDaysComposed;
//    }
//
//    public void setWeekDaysComposed(Integer weekDaysComposed) {
//        this.weekDaysComposed = weekDaysComposed;
//    }
