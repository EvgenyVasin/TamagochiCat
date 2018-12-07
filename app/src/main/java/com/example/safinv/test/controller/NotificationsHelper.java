package com.example.safinv.test.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.safinv.test.MainActivity;
import com.example.safinv.test.R;

import java.util.HashMap;

/**
 * Created by safin.v on 18.03.2016.
 */
public class NotificationsHelper {
    private static final String TAG = NotificationsHelper.class.getSimpleName();

    private Service service;
    NotificationManager myNotificationManager;
//    private Notification notification;
//    private RemoteViews contentView;
//    private NotificationCompat.Builder builder;
    private Context cntxt;
    private int lastId = 0; //постоянно увеличивающееся поле, уникальный номер каждого уведомления
    private HashMap<Integer, Notification> notifications; //массив ключ-значение на все отображаемые пользователю уведомления



    public NotificationsHelper(Context context){
        cntxt = context;
        service = (Service)context;
        myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifications = new HashMap<Integer, Notification>();
    }



    /**
     * генерация уведомления с ProgressBar, иконкой и заголовком
     *
     * @param text заголовок уведомления
     * @param message сообщение, уотображаемое в закрытом статус-баре при появлении уведомления
     * @return View уведомления.
     */
    public int createInfoNotification(String text, String message){
        Intent notificationIntent = new Intent(cntxt, MainActivity.class); // по клику на уведомлении откроется HomeActivity
        NotificationCompat.Builder nb = new NotificationCompat.Builder(cntxt)
//NotificationCompat.Builder nb = new NotificationBuilder(context) //для версии Android > 3.0
                .setSmallIcon(R.mipmap.icon1) //иконка уведомления
                .setAutoCancel(true) //уведомление закроется по клику на него
                .setTicker(message) //текст, который отобразится вверху статус-бара при создании уведомления
                .setContentText(message) // Основной текст уведомления
                .setContentIntent(PendingIntent.getActivity(cntxt, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                .setWhen(System.currentTimeMillis()) //отображаемое время уведомления
                .setContentTitle("AppName") //заголовок уведомления
                .setDefaults(Notification.DEFAULT_ALL); // звук, вибро и диодный индикатор выставляются по умолчанию

        Notification notification; //генерируем уведомление
        if (Build.VERSION.SDK_INT < 16)
            notification = nb.getNotification();
        else
            notification = nb.build();

        myNotificationManager.notify(lastId, notification); // отображаем его пользователю.
        notifications.put(lastId, notification); //теперь мы можем обращаться к нему по id
        return lastId++;
    }






    /**
     * генерация уведомления с ProgressBar, иконкой и заголовком
     *
     * @return View уведомления.
     */
    public void createForegroundNotification(int hunger, int energy, int happiness, int health) {

        RemoteViews contentView = new RemoteViews(cntxt.getPackageName(), R.layout.notification_layout);
        contentView.setProgressBar(R.id.progress_hunger, 100, hunger, false);
        contentView.setProgressBar(R.id.progress_energy, 100, energy, false);
        contentView.setProgressBar(R.id.progress_happiness, 100, happiness, false);
        contentView.setProgressBar(R.id.progress_health, 100, health, false);

        NotificationCompat.Builder nb = new NotificationCompat.Builder(cntxt)
                .setSmallIcon(R.mipmap.icon1);

        Notification notification; //генерируем уведомление
        if (Build.VERSION.SDK_INT < 16)
            notification = nb.getNotification();
        else
            notification = nb.build();

        notification.contentView = contentView;
       // notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT | Notification.FLAG_ONLY_ALERT_ONCE;

        Intent notificationIntent = new Intent(cntxt, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(cntxt, 0, notificationIntent, 0);
        notification.contentIntent = contentIntent;

        service.startForeground(777, notification);
        notifications.put(777,notification);


//        return contentView;
    }
    public void UpdateForeground(int hunger, int energy, int happiness, int health){

        Notification notification = notifications.get(777);
        notification.contentView.setProgressBar(R.id.progress_hunger, 100, hunger, false);
        notification.contentView.setProgressBar(R.id.progress_energy, 100, energy, false);
        notification.contentView.setProgressBar(R.id.progress_happiness, 100, happiness, false);
        notification.contentView.setProgressBar(R.id.progress_health, 100, health, false);

        myNotificationManager.notify(777, notification);
    }
}
