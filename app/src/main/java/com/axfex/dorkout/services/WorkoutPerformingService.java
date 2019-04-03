package com.axfex.dorkout.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.axfex.dorkout.R;
import com.axfex.dorkout.WorkoutApplication;
import com.axfex.dorkout.util.FormatUtils;
import com.axfex.dorkout.views.workouts.MainActivity;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;

public class WorkoutPerformingService extends LifecycleService {

    @Inject
    WorkoutPerformingManager mWorkoutPerformingManager;

    private static final String TAG = "ACTION_WORKOUT_SERVICE";
    private static final String WORKOUT_ID = "WORKOUT_ID";
    private static final int SERVICE_ID = 1937;
    private IBinder mBinder= new ActionBinder();

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (intent.hasExtra(WORKOUT_ID)) {
            Long id =intent.getLongExtra(WORKOUT_ID,0);
            mWorkoutPerformingManager.startWorkout(id);
        } else stopSelf();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((WorkoutApplication) getApplication())
                .getAppComponent()
                .inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        startForeground(SERVICE_ID,getNotification(""));

    }

    private static Intent newIntent(Context context) {
//        initChannels(context);
        return new Intent(context, WorkoutPerformingService.class);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        mWorkoutPerformingManager.getExerciseTime().observe(this,this::onTimerChanged);

        super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }

    private void onTimerChanged(Long time) {
        ((NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE)).notify(SERVICE_ID,getNotification(FormatUtils.getTimeString(time)));
    }

    public static void startActionWorkoutService(Context context,Long workoutId){
        Intent i = WorkoutPerformingService.newIntent(context);
        i.putExtra(WORKOUT_ID,workoutId);
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

    public static void stopActionWorkoutService(Context context){
        Intent i = WorkoutPerformingService.newIntent(context);
            context.stopService(i);
    }

    private Notification getNotification(String timer) {
        Resources resources = getResources();
        Intent i = newIntent(this);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        NotificationCompat.Builder builder =new NotificationCompat.Builder(this,TAG);
        Notification notification= builder
                .setTicker("Current workout name :")
                .setSmallIcon(R.drawable.progressbar_secondary)
                .setContentTitle("Current Exercise name :"+timer)
                .setContentText("Current time maybe..")
                .setContentIntent(pi)
//                .setAutoCancel(true)
                .build();

        return notification;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(){
        NotificationManager notificationManager =
                (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(TAG,
                "Channel name",
                NotificationManager.IMPORTANCE_NONE);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }

    public class ActionBinder extends Binder{
        public WorkoutPerformingManager getActionWorkoutManager(){
            return mWorkoutPerformingManager;
        }
    }




}
