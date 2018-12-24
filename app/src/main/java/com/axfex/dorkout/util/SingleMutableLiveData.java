package com.axfex.dorkout.util;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * The Class provides a new data delivery only once per subscription
 * WrapperObserver class prevent onChange when fresh data has been delivered
 */

public class SingleMutableLiveData<T> extends MutableLiveData<T> {

    private int version = -1;
    private HashMap<Observer, WrapperObserver> mWrappers = new HashMap<>();

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        WrapperObserver wrapper = new WrapperObserver(observer);
        mWrappers.put(observer, wrapper);
        super.observe(owner, wrapper);
    }

    @Override
    public void postValue(T value) {
        version++;
        super.postValue(value);
    }

    @Override
    public void setValue(T value) {
        version++;
        super.setValue(value);
    }

    @Override
    public void removeObservers(@NonNull final LifecycleOwner owner) {
        mWrappers.clear();
        super.removeObservers(owner);
    }

    @Override
    public void removeObserver(@NonNull final Observer<T> observer) {
        WrapperObserver wrapper = mWrappers.get(observer);
        if (wrapper == null) return;
        mWrappers.remove(observer);
        super.removeObserver(wrapper);
    }


    private class WrapperObserver implements Observer<T> {
        private boolean isDelivered = false;
        private Observer<T> innerObserver;
        private int thisVersion;

        WrapperObserver(Observer<T> innerObserver) {
            this.innerObserver = innerObserver;
            thisVersion = version;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (thisVersion != version && !isDelivered) {
                innerObserver.onChanged(t);
                isDelivered = true;
            }
            thisVersion = version;
        }
    }
}