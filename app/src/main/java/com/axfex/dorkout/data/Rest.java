package com.axfex.dorkout.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import static com.axfex.dorkout.data.Exercise.MAX_PROGRESS;
import static com.axfex.dorkout.util.DateUtils.now;

public class Rest {
    private Long restTimePlan;
    private Long restTime;

    private MutableLiveData<Long> restTimeLD = new MutableLiveData<>();
    private MutableLiveData<Integer> progressLD = new MutableLiveData<>();

    private long startTime;
    private boolean expired = false;

    public Rest(Long restTimePlan) {
        this.restTimePlan = restTimePlan;
        startTime = now();
    }

    public boolean hasExpired() {
        return expired;
    }

    public void updateTime() {
        final long timeSinceStart = now() - startTime;
        restTime = Math.max(0, timeSinceStart);
        if (restTime > restTimePlan) {
            expired = true;
        }
        updateProgress();
    }

    private void updateProgress() {
        if (restTime != null && restTimePlan != null && restTimePlan > 0) {
            Long progressLong = restTime * MAX_PROGRESS / restTimePlan;
            progressLD.postValue(progressLong.intValue());
        } else {
            progressLD.postValue(0);
        }
    }

    public Long getRestTime() {
        return restTime;
    }

    public LiveData<Long> getRestTimeLD() {
        return restTimeLD;
    }

    public LiveData<Integer> getProgressLD() {
        return progressLD;
    }
}
