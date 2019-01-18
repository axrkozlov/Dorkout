package com.axfex.dorkout.data;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by alexanderkozlov on 1/2/18.
 */
@Entity
public class Workout {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private String description;
    private Long totalTime;
    private Long lastDate;
    private Integer order;
    private Integer exercisesCount;

    //Remove later, add Reminder instead
    private Long startTime;
    private Integer weekDaysComposed;

    @Ignore
    private transient Boolean mDone = false;
    @Ignore
    private transient Boolean mActive = false;


    @Ignore
    public Workout(String name) {
        this.name = name;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public Long getLastDate() {
        return lastDate;
    }

    public void setLastDate(Long lastDate) {
        this.lastDate = lastDate;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getWeekDaysComposed() {
        return weekDaysComposed;
    }

    public void setWeekDaysComposed(Integer weekDaysComposed) {
        this.weekDaysComposed = weekDaysComposed;
    }

    public Integer getExercisesCount() {
        return exercisesCount;
    }

    public void setExercisesCount(Integer exercisesCount) {
        this.exercisesCount = exercisesCount;
    }

    public Boolean isDone() {
        return mDone;
    }

    public void finish() {
        mActive=false;
        mDone = true;
    }

    public Boolean isActive() {
        return mActive;
    }

    public void start() {
        mActive = true;
    }


}
