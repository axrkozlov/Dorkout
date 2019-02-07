package com.axfex.dorkout.data;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.room.TypeConverter;

public class LiveDataConverter {

    @TypeConverter
    public static MutableLiveData<Long> getLiveData(Long field) {
        MutableLiveData<Long> mutableLiveData=new MutableLiveData<>();
        mutableLiveData.postValue(field);
        return mutableLiveData;
    }

    @Nullable
    @TypeConverter
    public static Long setLiveData(MutableLiveData<Long> mutableLiveData) {
        if (mutableLiveData==null) return null;
        return mutableLiveData.getValue();
    }

}
