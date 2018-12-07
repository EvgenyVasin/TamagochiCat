package com.example.safinv.test;

import android.app.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.safinv.test.view.PetView;
import com.example.safinv.test.controller.PetService;

public class MainActivity extends Activity {
    final int MENU_COLOR_RED = 1;
    final int MENU_COLOR_GREEN = 2;
    final int MENU_COLOR_BLUE = 3;

    final int MENU_SIZE_22 = 4;
    final int MENU_SIZE_26 = 5;
    final int MENU_SIZE_30 = 6;

    final String ATTRIBUTE_NAME_TEXT = "text";
    final String ATTRIBUTE_NAME_IMAGE = "image";

    private PetView pet = null;
    final String LOG_TAG = "myLogs";

    /////////////////////
    boolean bound = false;
    ServiceConnection sConn;
    Intent intent;

    PetService myService;
    TextView tvInterval;
    long interval;
    ///////////////////////

    @Override
    protected void onResume() {
        super.onResume();
    //    bindService(intent, sConn, 0);
        Log.d(LOG_TAG, "MainActivity onResume");
    }

    @Override
    protected void onPause() {
//        if (null != pet)
//            pet.release();
//        unbindService(sConn);
        super.onPause();

        Log.d(LOG_TAG, "MainActivity onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "MainActivity onRestart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating);
        //if (pet !=null)
            pet = new PetView(this);

        ///////////////////////
        //tvInterval = (TextView) findViewById(R.id.tvInterval);
        intent = new Intent(this, PetService.class);
        Log.d(LOG_TAG, "MainActivity onCreate");
        sConn = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected");
                myService = ((PetService.MyBinder) binder).getService();
                bound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
                bound = false;
            }
        };
        ///////////////////////

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != pet) {
            pet.release();

        }
    }

    public void onClickpopupWindow(View v) {
        pet.onClickpopupWindow(v);
    }



    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        // TODO Auto-generated method stub
        // super.onBackPressed();

    }

    //////////////////////////////////
    @Override
    protected void onStart() {
        super.onStart();
        bindService(intent, sConn, 0);
        Log.d(LOG_TAG, "MainActivity onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!bound) return;
        unbindService(sConn);
        bound = false;
        Log.d(LOG_TAG, "MainActivity onStop");
    }

    public void onClickStart(View v) {
        startService(intent);

    }

    public void onClickStop(View v) {
        stopService(intent);
    }

    public void onClickUp(View v) {
    moveTaskToBack(true);
//        if (!bound) return;
//        interval = myService.upInterval(500);
//        tvInterval.setText("interval = " + interval);
    }

    public void onClickDown(View v) {

//        if (!bound) return;
//        interval = myService.downInterval(500);
//        tvInterval.setText("interval = " + interval);
    }

    public void okFoodClicked() {
    }

    public void cancelFoodClicked() {
    }
    /////////////////////////////
}
