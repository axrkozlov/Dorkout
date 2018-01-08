package com.axfex.dorkout;

import android.app.Application;

import com.axfex.dorkout.di.AppComponent;
import com.axfex.dorkout.di.AppModule;
import com.axfex.dorkout.di.DaggerAppComponent;
import com.axfex.dorkout.di.RoomModule;

/**
 * Created by alexanderkozlov on 1/7/18.
 */

public class WorkoutApplication extends Application {
    private AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();

        appComponent= DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .roomModule(new RoomModule(this))
                .build();

    }
    public AppComponent getAppComponent() {
        return appComponent;
    }

}
