package com.axfex.dorkout.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ExerciseType {
    @PrimaryKey(autoGenerate = true)
    private Long type_id;
    private String type_name;
    private Integer lastUsed;

    public Long getType_id() {
        return type_id;
    }

    public void setType_id(Long type_id) {
        this.type_id = type_id;
    }

    public ExerciseType(String type_name) {
        this.type_name = type_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public Integer getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Integer lastUsed) {
        this.lastUsed = lastUsed;
    }
}
