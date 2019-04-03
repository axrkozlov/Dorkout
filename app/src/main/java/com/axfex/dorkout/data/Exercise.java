package com.axfex.dorkout.data;

import android.os.Handler;

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
import static com.axfex.dorkout.data.Status.REST;
import static com.axfex.dorkout.data.Status.RUNNING;
import static com.axfex.dorkout.data.Status.SKIPPED;
import static com.axfex.dorkout.data.Status.PAUSED;
import static com.axfex.dorkout.util.FormatUtils.now;

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
    @Ignore
    private static final String TAG = "EXERCISE";

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

    private long startTime;

    @Ignore
    private long accumulatedTime;

    @Ignore
    private MutableLiveData<Long> timeLD = new MutableLiveData<>();

    @Ignore
    private MutableLiveData<Long> timeProgressLD = new MutableLiveData<>();

    @Ignore
    private MutableLiveData<Status> statusLD = new MutableLiveData<>();

    @Ignore
    private Handler timerHandler;

    @Ignore
    private Long restTime;

    @Ignore
    private MutableLiveData<Long> restProgressLD = new MutableLiveData<>();



    @Ignore
    private static final int UPDATE_PERIOD = 40;

    @Ignore
    public static final Long MAX_TIME_PROGRESS = 10000L;

    @Ignore
    private MutableLiveData<Boolean> activeLD = new MutableLiveData<>();

    @Ignore
    private MutableLiveData<Boolean> finishEventLD = new MutableLiveData<>();

    @Ignore
    private Long startRestTime;
    @Ignore
    private Runnable timerRunnable = new TimeUpdate();
    @Ignore
    private Runnable restRunnable= new RestUpdate();

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
        statusLD.postValue(status);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public MutableLiveData<Long> getTimeLD() {
        return timeLD;
    }

    public MutableLiveData<Long> getTimeProgressLD() {
        return timeProgressLD;
    }

    public MutableLiveData<Long> getRestProgressLD() {
        return restProgressLD;
    }

    public MutableLiveData<Status> getStatusLD() {
        return statusLD;
    }

    public boolean await() {
        if (status == null) {
            setStatus(AWAITING);
            return true;
        }
        return false;
    }

    public boolean start() {
        if (status == AWAITING ) {
            setStatus(status = RUNNING);
            startTimer();
            return true;
        }
        return false;
    }

    public boolean redo() {
        if (status == RUNNING || status == PAUSED || status == DONE || status == SKIPPED) {
            time = null;
            startTime = now();
            updateTime();
            if (status == DONE || status == SKIPPED) setStatus(AWAITING);
            return true;
        }
        return false;
    }

    public boolean pause() {
        if (status == RUNNING) {
            setStatus(PAUSED);
            return true;
        }
        return false;
    }

    public boolean skip() {
        if (status != SKIPPED) {
            setStatus(SKIPPED);
            return true;
        }
        return false;
    }

    public boolean stop() {
        if (status == RUNNING || status == PAUSED ) {
            setStatus(status = REST);
            startRestTimer();
            return true;
        }
        return false;
    }


    public boolean finish() {
        if (status != DONE && status != SKIPPED) {
            setStatus(DONE);
            finishEventLD.postValue(true);
//            finishEventLD.postValue(null);
            return true;
        }
        return false;
    }

    public void reset() {
        setStatus(null);
        weight = 0;
        repeats = 0;
        distance = 0;
        setTime(null);
        accumulatedTime = 0;
    }

    private void startTimer(){
        timerHandler = new Handler();
        startTime = now();
        accumulatedTime = time == null ? 0L : time;
        timerHandler.post(timerRunnable);
    }

    private void startRestTimer(){
        timerHandler = new Handler();
        startRestTime = now();
        timerHandler.post(restRunnable);
    }

    public void updateTime() {
        if (status == RUNNING) {
            final long timeSinceStart = now() - startTime;
            time = accumulatedTime + Math.max(0, timeSinceStart);
        }
        timeLD.postValue(time);
        updateProgress();
    }

    public void updateRestTime() {
        final long timeSinceStart = now() - startRestTime;
        restTime = Math.max(0, timeSinceStart);
        if (restTime > restTimePlan) {
            finish();
        }
        updateRestProgress();
    }

    private void updateProgress() {
        Long progressLong;
        if (time != null && timePlan != null && timePlan > 0) {
            progressLong = time * MAX_TIME_PROGRESS / timePlan;
        } else {
            progressLong=0L;
        }
        timeProgressLD.postValue(progressLong);
    }

    private void updateRestProgress() {
        if (restTime != null && restTimePlan != null && restTimePlan > 0) {
            Long progress = restTime * MAX_TIME_PROGRESS / restTimePlan;
            restProgressLD.postValue(progress);
        } else {
            restProgressLD.postValue(null);
        }
    }

    public boolean getRunning() {
        return status == RUNNING;
    }

    public boolean getPaused() {
        return status == PAUSED;
    }

    public boolean getDone() {
        return status == DONE;
    }

    public boolean getSkipped() {
        return status == SKIPPED;
    }

    public boolean getAwaiting() {
        return status == AWAITING;
    }

    public boolean canStart() {
        return timePlan != null;
    }

    public boolean is(Exercise exercise) {
        if (exercise == null) return false;
        return this.id.equals(exercise.id);
    }


    public MutableLiveData<Boolean> getActiveLD() {
        return activeLD;
    }

    public MutableLiveData<Boolean> getFinishEventLD() {
        return finishEventLD;
    }

    public void setActive(boolean isActive) {
        activeLD.setValue(isActive);
    }

    private final class TimeUpdate implements Runnable {
        @Override
        public void run() {
            if (status==RUNNING) {
                updateTime();
                timerHandler.postDelayed(this, UPDATE_PERIOD);
            }
        }
    }

    private final class RestUpdate implements Runnable {
        @Override
        public void run() {
            if (status==REST) {
                updateRestTime();
                timerHandler.postDelayed(this, UPDATE_PERIOD);
            }
        }
    }

}
