package com.axfex.dorkout.data;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import static androidx.room.ForeignKey.CASCADE;
import static com.axfex.dorkout.data.Status.DONE;
import static com.axfex.dorkout.data.Status.RUNNING;
import static com.axfex.dorkout.data.Status.SKIPPED;
import static com.axfex.dorkout.data.Status.STOPPED;
import static com.axfex.dorkout.util.DateUtils.now;

/**
 * Created by alexanderkozlov on 2/18/18.
 */
@TypeConverters({Status.class})

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
    private Integer weightPlan;
    private Integer repeatsPlan;
    private Long timePlan;
    private Long restTimePlan;
    private Long creationDate = now();

    //Results
    private Integer weight;
    private Integer repeats;
    private Integer distance;
    private Long time;
    private Long restTime;

    @Ignore
    private MutableLiveData<Long> timeLD =new MutableLiveData<>();
    @Ignore
    private MutableLiveData<Long> restLD =new MutableLiveData<>();

    @Nullable
    private Status status;

    @Ignore
    private Boolean next;

    @Ignore
    private long startTime;
    @Ignore
    private long stopTime;
    @Ignore
    private long accumulatedTime;
    @Ignore
    private long accumulatedRestTime;


    public Exercise() {
    }

    @Ignore
    public Exercise(String name, final Long workoutId) {
        this.name = name;
        this.workoutId = workoutId;
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

    public Integer getWeightPlan() {
        return weightPlan;
    }

    public void setWeightPlan(Integer weightPlan) {
        this.weightPlan = weightPlan;
    }

    public Integer getRepeatsPlan() {
        return repeatsPlan;
    }

    public void setRepeatsPlan(Integer repeatsPlan) {
        this.repeatsPlan = repeatsPlan;
    }

    public Long getTimePlan() {
        return timePlan;
    }

    public void setTimePlan(Long timePlan) {
        this.timePlan = timePlan;
    }

    public Long getRestTimePlan() {
        return restTimePlan;
    }

    public void setRestTimePlan(Long restTimePlan) {
        this.restTimePlan = restTimePlan;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
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

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getRestTime() {
        return restTime;
    }

    public void setRestTime(Long restTime) {
        this.restTime = restTime;
    }

    @Nullable
    public Status getStatus() {
        return status;
    }

    public void setStatus(@Nullable Status status) {
        this.status = status;
    }

    public void start() {
        startTime = now();
        accumulatedTime = time == null ? 0L : time;
        status = RUNNING;
        next=false;
    }

    public void restart() {
        startTime = now();
        status = RUNNING;
    }

    public void stop() {
        stopTime = now();
        getTime();
        accumulatedRestTime = restTime == null ? 0L : restTime;
        status = STOPPED;
    }

    public void skip() {
        status = SKIPPED;
        weight = 0;
        repeats = 0;
        time = 0L;
        restTime = 0L;
    }

    public void unSkip() {
        if (status==SKIPPED)
        status = null;
    }

    public void resetNext(){
        next=false;
    }

    public void finish() {
        status=DONE;
    }

    public void reset() {
        weight = 0;
        repeats = 0;
        time = 0L;
        restTime = 0L;
        status=null;
        next=false;
    }

    public void updateTime() {
        if (status==RUNNING) {
            final long timeSinceStart = now() - startTime;
            time = accumulatedTime + Math.max(0, timeSinceStart);
            timeLD.postValue(time);
        } else if (status==DONE) {
            final long timeSinceStop = now() - stopTime;
            restTime = accumulatedRestTime + Math.max(0, timeSinceStop);
            restLD.postValue(time);
        }
    }

    public Boolean getNext(){
        return next;
    }

    public void setNext(){
        if (status==null) next=true;
    }

    public LiveData<Long> getTimeLD() {
        return timeLD;
    }
    public LiveData<Long> getRestLD() {
        return restLD;
    }
}
