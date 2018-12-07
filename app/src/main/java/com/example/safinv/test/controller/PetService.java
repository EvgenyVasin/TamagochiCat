package com.example.safinv.test.controller;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.safinv.test.R;


public class PetService extends Service {
    final String LOG_TAG = "myLogs";
    MyBinder binder = new MyBinder();
    Solver solver;

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "MyService onCreate");
        final RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon1);

//        Notification notification;
//        if (Build.VERSION.SDK_INT < 16)
//            notification = builder.getNotification();
//        else
//            notification = builder.build();
//
//        notification.contentView = contentView;
//
//        startForeground(777, notification);



        solver = new Solver(this);
        solver.Start();
        //solver.StartForeground();

    }



    long upInterval(long gap) {
        //interval = interval + gap;
        //schedule();
        return 0;
    }

    long downInterval(long gap) {
//        interval = interval - gap;
//        if (interval < 0) interval = 0;
        //schedule();
        return 0;
    }

    public IBinder onBind(Intent arg0) {
        Log.d(LOG_TAG, "MyService onBind");
        return binder;
    }
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(LOG_TAG, "MyService onRebind");
    }

    public boolean onUnbind(Intent intent) {
        Log.d(LOG_TAG, "MyService onUnbind");
        return true;
    }

    public void onDestroy() {
        solver.Stop();
//        stopForeground(true);

        super.onDestroy();

        Log.d(LOG_TAG, "MyService onDestroy");
    }

    public class MyBinder extends Binder {
        public PetService getService() {
            return PetService.this;
        }
    }
}
