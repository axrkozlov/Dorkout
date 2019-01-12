package com.axfex.dorkout.util;

public class Show<T> {
//    public enum String {
//        WORKOUTS_SHOW,
//        WORKOUT_PICK_SHOW,
//        WORKOUT_EDIT_SHOW,
//        WORKOUT_ACTION_SHOW
//    }

    private String mTag;
    private T t;

    public Show(String tag) {
        mTag = tag;
    }

    public Show(String tag, T t) {
        mTag = tag;
        this.t=t;
    }

    public boolean is(String tag){
        return mTag == tag;
    }

    public String getTag() {
        return mTag;
    }

    public T getItem() {
        return t;
    }

    public void setItem(T t) {
        this.t = t;
    }
}
