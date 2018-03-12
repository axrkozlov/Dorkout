package com.axfex.dorkout.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

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
    private int id;
    @ColumnInfo(name = "workoutId")
    private final int workoutId;
    private String name;
    private String description;
    private Integer order;
    private Integer setsCount;
    @Ignore
    private transient Boolean isChecked = false;
    @Ignore
    private transient Boolean isStarted = false;

    public Exercise(String name, final int workoutId) {
        this.name = name;
        this.workoutId=workoutId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkoutId() {
        return workoutId;
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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getSetsCount() {
        return setsCount;
    }

    public void setSetsCount(Integer setsCount) {
        this.setsCount = setsCount;
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
