package com.axfex.dorkout.data;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import static androidx.room.ForeignKey.CASCADE;
import static com.axfex.dorkout.data.Status.AWAITING;
import static com.axfex.dorkout.data.Status.DONE;
import static com.axfex.dorkout.data.Status.READY;
import static com.axfex.dorkout.data.Status.RUNNING;
import static com.axfex.dorkout.data.Status.SKIPPED;
import static com.axfex.dorkout.data.Status.PAUSED;
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
    private Integer distancePlan;
    private Long restTimePlan;
    private Long timePlan;
    private Long creationDate = now();

    //Results
    private Integer weight;
    private Integer repeats;
    private Integer distance;
    private Long time;

    @Nullable
    private Status status;

    @Ignore
    private long startTime;

    @Ignore
    private long accumulatedTime;


    @Ignore
    private MutableLiveData<Long> timeLD = new MutableLiveData<>();

    @Ignore
    private MutableLiveData<Integer> progressLD = new MutableLiveData<>();

    @Ignore
    public static final int MAX_PROGRESS = 10000;

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

    public Integer getDistancePlan() {
        return distancePlan;
    }

    public void setDistancePlan(Integer distancePlan) {
        this.distancePlan = distancePlan;
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
        updateTime();
    }

    @Nullable
    public Status getStatus() {
        return status;
    }

    public void setStatus(@Nullable Status status) {
        this.status = status;
    }

    public MutableLiveData<Long> getTimeLD() {
        return timeLD;
    }

    public MutableLiveData<Integer> getProgressLD() {
        return progressLD;
    }


    public void await() {
        if (status == null) status = AWAITING;
    }

    public void ready() {
        if (status == AWAITING) status = READY;
    }


    public boolean start() {
        if (status != RUNNING && status != DONE) {
            startTime = now();
            accumulatedTime = timeLD.getValue() == null ? 0L : timeLD.getValue();
            status = RUNNING;
            return true;
        }
        return false;
    }

    public boolean restart() {
        if (status == RUNNING || status == PAUSED || status == DONE) {
            startTime = now();
            accumulatedTime = 0L;
            status = RUNNING;
            return true;
        }
        return false;
    }

    public boolean pause() {
        if (status == RUNNING) {
            status = PAUSED;
            return true;
        }
        return false;
    }

    public boolean skip() {
        status = SKIPPED;
        return true;
    }

    public boolean finish() {
        status = DONE;
        return true;
    }

    public void reset() {
        status = null;
        weight = 0;
        repeats = 0;
        distance = 0;
        setTime(null);
        accumulatedTime = 0;
    }

    public void updateTime() {
        if (status == RUNNING) {
            final long timeSinceStart = now() - startTime;
            time = accumulatedTime + Math.max(0, timeSinceStart);
        }
        timeLD.postValue(time);
        updateProgress();
    }

    private void updateProgress() {
        if (time != null && timePlan != null && timePlan > 0) {
            Long progressLong = time * MAX_PROGRESS / timePlan;
            progressLD.postValue(progressLong.intValue());
        }
    }

    public Boolean getRunning() {
        return status == RUNNING;
    }

    public Boolean getPaused() {
        return status == PAUSED;
    }

    public Boolean getDone() {
        return status == DONE;
    }

    public Boolean getSkipped() {
        return status == SKIPPED;
    }

    public Boolean getAwaiting() {
        return status == AWAITING;
    }

    public Boolean getReady() {
        return status == AWAITING;
    }

    public boolean is(Exercise exercise) {
        if (exercise == null) return false;
        return this.id.equals(exercise.id);
    }
}
