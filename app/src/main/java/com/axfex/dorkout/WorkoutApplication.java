package com.axfex.dorkout;

import android.app.Application;
import android.content.Context;

import com.axfex.dorkout.di.AppComponent;
import com.axfex.dorkout.di.AppModule;
import com.axfex.dorkout.di.DaggerAppComponent;
import com.axfex.dorkout.di.RoomModule;

import java.lang.ref.WeakReference;

import androidx.core.content.ContextCompat;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class WorkoutApplication extends Application {
    private AppComponent appComponent;
    private static WeakReference<Context> mContext;
    @Override
    public void onCreate() {
        super.onCreate();

        appComponent= DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .roomModule(new RoomModule(this))
                .build();
        mContext=new WeakReference<>(this);

    }
    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static Context getContext(){
        return mContext.get();
    }

}
