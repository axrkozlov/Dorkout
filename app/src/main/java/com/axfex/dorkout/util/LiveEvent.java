package com.axfex.dorkout.util;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

//The Class with a condition, that prevent onChange method on observer starts

public class LiveEvent<T> extends MutableLiveData<T> {

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {

        super.observe(owner, new Observer<T>() {
            private Boolean isNewObserver=true;
            @Override
            public void onChanged(@Nullable T t) {
                if (!isNewObserver) observer.onChanged(t);
                isNewObserver=false;
            }
        });

    }

    @Override
    public void observeForever(@NonNull Observer<T> observer) {
        super.observeForever(new Observer<T>() {
            private Boolean isNewObserver=true;
            @Override
            public void onChanged(@Nullable T t) {
                if (!isNewObserver) observer.onChanged(t);
                isNewObserver=false;
            }
        });

    }
}
