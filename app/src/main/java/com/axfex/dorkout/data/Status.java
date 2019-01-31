package com.axfex.dorkout.data;

import androidx.room.TypeConverter;

public enum Status {
    RUNNING(0),
    STOPPED(1),
    DONE(2),
    SKIPPED(3);
    private int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @TypeConverter
    public static Status getStatus(Integer numeral) {
        if (numeral==null) return null;
        for (Status ds : values()) {
            if (ds.code == numeral) {
                return ds;
            }
        }
        return null;
    }

    @TypeConverter
    public static Integer getStatusInt(Status status) {

        if (status != null)
            return status.code;

        return null;
    }
}
