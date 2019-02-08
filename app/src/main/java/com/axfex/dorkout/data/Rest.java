package com.axfex.dorkout.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import static com.axfex.dorkout.util.FormatUtils.now;

public class Rest {
    public static final Long MAX_PROGRESS = 10000L;
    private Long restTimePlan;
    private Long restTime;

    private MutableLiveData<Long> restTimeLD = new MutableLiveData<>();
    private MutableLiveData<Long> progressLD = new MutableLiveData<>();

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
            Long progress = restTime * MAX_PROGRESS / restTimePlan;
            progressLD.postValue(progress);
        } else {
            progressLD.postValue(null);
        }
    }

    public Long getRestTime() {
        return restTime;
    }

    public LiveData<Long> getRestTimeLD() {
        return restTimeLD;
    }

    public LiveData<Long> getProgressLD() {
        return progressLD;
    }
}
