package com.axfex.dorkout.data;


import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import static com.axfex.dorkout.data.Status.RUNNING;
import static com.axfex.dorkout.util.FormatUtils.now;

/**
 * Created by alexanderkozlov on 1/2/18.
 */

@TypeConverters({Status.class})

@Entity
public class Workout {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private String note;
    private Integer order;
    private Integer color;
    private Long totalExercisesTime;
    private Integer exercisesCount;
    private Long startTime;
    @TypeConverters(LiveDataConverter.class)
    private MutableLiveData<Long> time;
    @Nullable
    private Status status;
    @Ignore
    private long accumulatedTime;

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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Long getTotalExercisesTime() {
        return totalExercisesTime;
    }

    public void setTotalExercisesTime(Long totalExercisesTime) {
        this.totalExercisesTime = totalExercisesTime;
    }

    public Integer getExercisesCount() {
        return exercisesCount;
    }

    public void setExercisesCount(Integer exercisesCount) {
        this.exercisesCount = exercisesCount;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public MutableLiveData<Long> getTime() {
        return time;
    }

    public void setTime(MutableLiveData<Long> time) {
        this.time = time;
    }

    @Nullable
    public Status getStatus() {
        return status;
    }

    public void setStatus(@Nullable Status status) {
        this.status = status;
    }

    public boolean start() {
        if (status != RUNNING) {
            startTime = now();
            accumulatedTime = time.getValue()==null?0L:time.getValue();
            status = RUNNING;
            return true;
        }
        return false;
    }

    public void finish() {
        status = null;
        startTime = 0L;
        time.postValue(null);
    }

    public void updateTime() {
        if (status == RUNNING) {
            final long timeSinceStart = now() - startTime;
            time.postValue(accumulatedTime + Math.max(0, timeSinceStart));
        }
    }

    public  boolean getRunning(){
        return status==RUNNING;
    }

    public boolean is(Workout workout) {
        if (workout == null) return false;
        return this.id.equals(workout.id);
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
