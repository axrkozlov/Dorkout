package com.axfex.dorkout.util;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class provides a new data delivery only
 * WrapperObserver class prevent onChange when LiveData have an old data
 */

public class FreshMutableLiveData<T> extends MutableLiveData<T> {

    private int version = -1;
    private HashMap<Observer<T>, WrapperObserver> mWrappers = new HashMap<>();

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        WrapperObserver wrapper = new WrapperObserver(owner, observer);
        mWrappers.put(observer, wrapper);
        super.observe(owner, wrapper);
    }

    @Override
    public void observeForever(@NonNull Observer<T> observer) {
        WrapperObserver wrapper = new WrapperObserver(observer);
        mWrappers.put(observer, wrapper);
        super.observeForever(observer);
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
        for (Map.Entry<Observer<T>, WrapperObserver> entry : mWrappers.entrySet()) {
            if (entry.getValue().isAttachedTo(owner))
                removeObserver(entry.getKey());
        }
    }

    @Override
    public void removeObserver(@NonNull final Observer<T> observer) {
        WrapperObserver wrapper = mWrappers.get(observer);
        if (wrapper == null) return;
        mWrappers.remove(observer);
        super.removeObserver(wrapper);
    }

    private class WrapperObserver implements Observer<T> {
        private Observer<T> mObserver;
        WeakReference<LifecycleOwner> mLifecycleOwnerWeakReference;
        private int thisVersion;

        WrapperObserver(LifecycleOwner lifecycleOwner, Observer<T> observer) {
            this.mObserver = observer;
            mLifecycleOwnerWeakReference = new WeakReference<>(lifecycleOwner);
            thisVersion = version;
        }


        WrapperObserver(Observer<T> observer) {
            this.mObserver = observer;
            thisVersion = version;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (thisVersion != version) {
                mObserver.onChanged(t);
            }
            thisVersion = version;
        }

        boolean isAttachedTo(LifecycleOwner lifecycleOwner) {
            return mLifecycleOwnerWeakReference.get().equals(lifecycleOwner);
        }
    }
}