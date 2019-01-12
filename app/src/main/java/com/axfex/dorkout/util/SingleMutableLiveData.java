package com.axfex.dorkout.util;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class provides a new data delivery only once per subscription
 * WrapperObserver class prevent onChange when fresh data has been delivered, then observer will be removed
 */

public class SingleMutableLiveData<T> extends MutableLiveData<T> {
    private int version = -1;
    private HashMap<Observer<? super T>, WrapperObserver> mWrappers = new HashMap<>();

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        WrapperObserver wrapper = new WrapperObserver(owner, observer);
        mWrappers.put(observer, wrapper);
        super.observe(owner, wrapper);
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
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
        for (Map.Entry<Observer<? super T>, WrapperObserver> entry : mWrappers.entrySet()) {
            if (entry.getValue().isAttachedTo(owner))
                removeObserver(entry.getKey());
        }
    }

    @Override
    public void removeObserver(@NonNull final Observer<? super T> observer) {
        WrapperObserver wrapper = mWrappers.get(observer);
        if (wrapper == null) return;
        mWrappers.remove(observer);
        super.removeObserver(wrapper);
    }

    private class WrapperObserver implements Observer<T> {
        private Observer<? super T> mObserver;
        WeakReference<LifecycleOwner> mLifecycleOwnerWeakReference;
        private int thisVersion;

        WrapperObserver(LifecycleOwner lifecycleOwner, Observer<? super T> observer) {
            this.mObserver = observer;
            mLifecycleOwnerWeakReference = new WeakReference<>(lifecycleOwner);
            thisVersion = version;
        }


        WrapperObserver(Observer<? super T> observer) {
            this.mObserver = observer;
            thisVersion = version;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (thisVersion != version) {
                mObserver.onChanged(t);
                removeObserver(mObserver);
            }
        }

        boolean isAttachedTo(LifecycleOwner lifecycleOwner) {
            return mLifecycleOwnerWeakReference.get().equals(lifecycleOwner);
        }
    }
}