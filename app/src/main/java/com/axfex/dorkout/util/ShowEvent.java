package com.axfex.dorkout.util;

public class ShowEvent {


    private String mTag;
    private Long mId;

    public ShowEvent(String tag) {
        mTag = tag;
    }

    public ShowEvent(String tag, Long id) {
        mTag = tag;
        this.mId=id;
    }



    public boolean is(String tag){
        return mTag.equals(tag);
    }

    public String getTag() {
        return mTag;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        this.mId = id;
    }
}
