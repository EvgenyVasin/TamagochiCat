package com.example.safinv.test.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.app.Service;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by safin.v on 18.03.2016.
 */
public class Solver {
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;

    NotificationsHelper nh;

    private Service cntxt;
    NotificationManager myNotificationManager;
    private Notification notification;
    private RemoteViews contentView;
    private NotificationCompat.Builder builder;

    private ProgressBar ntf_progress_hunger, ntf_progress_energy,ntf_progress_happiness, ntf_progress_health;

    final String LOG_TAG = "myLogs";
    private String Name;
    private int Hunger;
    private int Energy;
    private int Happiness;
    private int Health;
    Date Birthday;

    private  long  IntervalHunger = 1080000;
    private  long  IntervalEnergy = 720000;

    public synchronized  long  getIntervalHunger() {
        return IntervalHunger;
    }

    public synchronized void setIntervalHunger(int intervalHunger) {
        IntervalHunger = intervalHunger;
        scheduleHunger();
    }

    public synchronized  long  getIntervalEnergy() {
        return IntervalEnergy;
    }

    public synchronized void setIntervalEnergy(int intervalEnergy) {
        IntervalEnergy = intervalEnergy;
        scheduleEnergy();
    }

    public synchronized String getName() {
        return Name;

    }

    public synchronized void setName(String name) {
        Name = name;
        mSqLiteDatabase.execSQL("update " + DatabaseHelper.DATABASE_TABLE + "set " + DatabaseHelper.NAME_COLUMN + "= '" + name + "'");
    }

    public synchronized int getHunger() {
        return Hunger;
    }

    public synchronized void setHunger(int hunger) {
        Hunger = hunger;
        if(Hunger < 0)
            Hunger = 0;

        mSqLiteDatabase.execSQL("update pets set hunger = '" + Hunger + "'");
        UpdateForeground();
    }

    public synchronized int getEnergy() {
        return Energy;
    }

    public synchronized void setEnergy(int energy) {
        Energy = energy;
        mSqLiteDatabase.execSQL("update pets set energy = '" + energy + "'");
        UpdateForeground();
    }

    public synchronized int getHappiness() {
        return Happiness;
    }

    public synchronized void setHappiness(int happiness) {
        Happiness = happiness;
        mSqLiteDatabase.execSQL("update pets set happiness = '" + happiness + "'");
        UpdateForeground();
    }

    public synchronized int getHealth() {
        return Health;
    }

    public synchronized void setHealth(int health) {
        Health = health;
        mSqLiteDatabase.execSQL("update pets set health = '" + health + "'");
        UpdateForeground();
    }

    Timer timerEnergy, timerHunger;
    TimerTask tTaskEnergy, tTaskHunger;

    public Solver(Context context) {
        cntxt = (Service) context;
        mDatabaseHelper = new DatabaseHelper(context, "petsdatabase.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
//        myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
//        builder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.mipmap.icon1);
//
//        if (Build.VERSION.SDK_INT < 16)
//            notification = builder.getNotification();
//        else
//            notification = builder.build();
//
//        notification.contentView = contentView;
        //cntxt.startForeground(777, notification);

        nh = new NotificationsHelper(context);

        Cursor cursor = mSqLiteDatabase.query(DatabaseHelper.DATABASE_TABLE, new String[]{DatabaseHelper.NAME_COLUMN,
                        DatabaseHelper.HUNGER_COLUMN, DatabaseHelper.ENERGY_COLUMN, DatabaseHelper.HAPPINESS_COLUMN, DatabaseHelper.HEALTH_COLUMN, DatabaseHelper.BIRTHDAY_COLUMN},
                null, null,
                null, null, null);

        cursor.moveToFirst();


        Name =cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME_COLUMN));
        Hunger=cursor.getInt(cursor.getColumnIndex(DatabaseHelper.HUNGER_COLUMN));

        Energy=cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ENERGY_COLUMN));
        Happiness=cursor.getInt(cursor.getColumnIndex(DatabaseHelper.HAPPINESS_COLUMN));
        Health=cursor.getInt(cursor.getColumnIndex(DatabaseHelper.HEALTH_COLUMN));
        Birthday = new Date(cursor.getString(cursor.getColumnIndex(DatabaseHelper.BIRTHDAY_COLUMN)));
        cursor.close();

//        UpdateForeground();
        nh.createForegroundNotification(Hunger, Energy, Happiness, Health);
        setHunger(100);
        setEnergy(100);
        timerHunger = new Timer();
        timerEnergy = new Timer();

    }

    public void UpdateForeground() {

       // cntxt.stopForeground(true);


        nh.UpdateForeground(Hunger, Energy, Happiness, Health);
//        notification.contentView.getLayoutId()
//        notification.contentView.setProgressBar(R.id.progress_hunger, 100, Hunger, false);
//        notification.contentView.setProgressBar(R.id.progress_energy, 100, Energy, false);
//        notification.contentView.setProgressBar(R.id.progress_happiness, 100, Happiness, false);
//        notification.contentView.setProgressBar(R.id.progress_health, 100, Health, false);
//
//        myNotificationManager.notify(777, notification);

    }

    void scheduleHunger() {

        if (tTaskHunger != null) tTaskHunger.cancel();
        if (IntervalHunger > 0) {
            tTaskHunger = new TimerTask() {
                public void run() {
                    int hunger = getHunger();
                    setHunger(hunger - 1);

                   // Log.d(LOG_TAG, "scheduleHunger" + " " + Name + " " + Hunger + " " + Energy + " " + Happiness + " " + Health + " " + Birthday);
                }
            };
            timerHunger.schedule(tTaskHunger, 1000, IntervalHunger);
        }
    }

    void scheduleEnergy() {
        if (tTaskEnergy != null) tTaskEnergy.cancel();
        if (IntervalEnergy > 0) {
            tTaskEnergy = new TimerTask() {
                public void run() {
                    int energy = getEnergy();
                    setEnergy(energy - 1);
                    //Log.d(LOG_TAG, "scheduleEnergy" + " " + Name + " " + Hunger + " " + Energy + " " + Happiness + " " + Health + " " + Birthday );
                }
            };
            timerEnergy.schedule(tTaskEnergy, 1000, IntervalEnergy);
        }
    }

    public void Start(){
        scheduleHunger();
        scheduleEnergy();
    }

    public void Stop(){
        if (tTaskHunger != null) tTaskHunger.cancel();
        if (tTaskEnergy != null) tTaskEnergy.cancel();
    }
}
