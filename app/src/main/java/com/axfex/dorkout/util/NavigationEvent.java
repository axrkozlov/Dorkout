package com.axfex.dorkout.util;

public class NavigationEvent<T> {
    private boolean hasBeenHandled=false;

    public T getEventContent(T t){
        if (hasBeenHandled){
            return null;
        }
        hasBeenHandled=true;
        return t;
    }


}
