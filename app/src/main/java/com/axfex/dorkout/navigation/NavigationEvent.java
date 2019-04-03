package com.axfex.dorkout.navigation;

import android.util.Log;

public class NavigationEvent {
    public enum EventType {
        WORKOUTS,
        EDIT_WORKOUT,
        WORKOUT,
        WORKOUT_PERFORMING
    }

    Long id;
    EventType mEventType;

    public NavigationEvent(EventType eventType, Long id) {
        mEventType = eventType;
        this.id = id;
    }

    public NavigationEvent(EventType eventType) {
        mEventType = eventType;
    }

    public EventType getEventType() {
        return mEventType;
    }

    public Long getId() {
        return id;
    }
}
