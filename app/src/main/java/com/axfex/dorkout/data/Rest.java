package com.axfex.dorkout.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import static com.axfex.dorkout.util.DateUtils.now;

public class Rest {
    private Long restTimePlan;
    private Long restTime;
    private MutableLiveData<Long> restLD = new MutableLiveData<>();
    private long startTime;
    private boolean rest=false;
    private boolean expired=false;

    public Rest(Long restTimePlan) {
        this.restTimePlan = restTimePlan;
    }

    public void start(){
        startTime = now();
        rest=true;
    }

    public void stop(){
        rest=false;
        restTime=0L;
        expired=false;
    }

    public boolean hasExpired(){
        return expired;
    }

    public void updateTime() {
        if (rest && !expired) {
            final long timeSinceStart = now() - startTime;
            restTime = restTimePlan-timeSinceStart;
            if (restTime<0) {
                restTime=0L;
                expired=true;
            }
                restLD.postValue(restTime);
        }
    }


    public LiveData<Long> getRestLD() {
        return restLD;
    }


}
