package com.axfex.dorkout.util;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class LiveEvent<T> extends MutableLiveData<T> {

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {

        super.observe(owner, new Observer<T>() {
            private Boolean init=true;
            @Override
            public void onChanged(@Nullable T t) {
                if (!init) observer.onChanged(t);
                init=false;
            }
        });

    }





}
