package com.axfex.dorkout.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.axfex.dorkout.R;
import com.axfex.dorkout.data.Exercise;
import com.axfex.dorkout.data.Workout;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ActionWorkoutService extends Service {
    private static final String TAG = "ACTION_WORKOUT_SERVICE";
    private static final int SERVICE_ID = 1937;

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(SERVICE_ID,getNotification());
    }

    private static Intent newIntent(Context context) {
//        initChannels(context);
        return new Intent(context, ActionWorkoutService.class);
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    public static void startActionWorkoutService(Context context, Workout workout, List<Exercise> exercises){
        Intent i = ActionWorkoutService.newIntent(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i);
        } else {
            context.startService(i);
        }

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    public static void stopWorkout(Context context){
        Intent i = ActionWorkoutService.newIntent(context);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(i);
//        } else {
            context.stopService(i);
//        }

    }

    private Notification getNotification() {
        Resources resources = getResources();
//        Intent i = PhotoGalleryActivity.newIntent(this);
//        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

        NotificationCompat.Builder builder =new NotificationCompat.Builder(this,TAG);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    NotificationManager notificationManager =
                (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(TAG,
                "Channel name",
                NotificationManager.IMPORTANCE_NONE);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);


        } else {
        }
        Notification notification= builder
                .setTicker("Current workout name :")
                .setSmallIcon(R.drawable.ic_action_name)
                .setContentTitle("Current Exercise name :")
                .setContentText("Current time maybe..")
//                .setContentIntent(pi)
//                .setAutoCancel(true)
                .build();

        return notification;
    }


}
