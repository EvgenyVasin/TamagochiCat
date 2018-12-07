package com.example.safinv.test.view;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.safinv.test.R;
import com.example.safinv.test.view.dialogs.DialogWindow;
import com.example.safinv.test.view.graphic.PetSurfaceView;

/**
 * Created by safin.v on 24.03.2016.
 */
public class PetView extends FloatingLayout{
    /**
     * constants of animation
     */
    public static final int ANIM_WALK_LEFT = 0;
    public static final int ANIM_SIT_LEFT = 1;
    public static final int ANIM_X1 = 2;
    public static final int ANIM_RUN_LEFT = 3;
    public static final int ANIM_RUN_RIGHT = 4;
    public static final int ANIM_X2 = 5;
    public static final int ANIM_SIT_RIGHT = 6;
    public static final int ANIM_WALK_RIGHT = 7;

    /**
     * constants of position
     */
    public final int MOVE_POSITION_LEFT = 0;
    public final int MOVE_POSITION_RIGHT = 1;

    final String ATTRIBUTE_NAME_TEXT = "text";
    final String ATTRIBUTE_NAME_IMAGE = "image";
    final String LOG_TAG = "myLogs";
    PetSurfaceView petSurfaceView;
    //FloatingLayout petLayout;

    DialogWindow dlgMenu;
    FloatingLayout popupMenu;

    SensorManager sensorManager;
    Sensor sensorAccel;

    Handler mHandler;
    int petWidth, petHeight;
    float density;
    SensorEventListener sensorEventListener;
    View.OnClickListener onClickListener;
    LayoutInflater inflater;

    float[] valuesAccel = new float[3];

    public void initSize(Context context){
        density = context.getResources().getDisplayMetrics().density;
        Display display = ((WindowManager) context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int  height = display.getHeight();
        petWidth = (int) Math.sqrt(height*width/12);
        petHeight = petWidth/2;
    }

    public PetView(Context context) {
        super(context);

        popupMenu = null;
        //petLayout = new FloatingLayout(context);

        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pet_layaut, null);
        petSurfaceView = (PetSurfaceView)view.findViewById(R.id.surfaceView);
        petWidth =  petSurfaceView.getWidth();
        petHeight = petSurfaceView.getHeight();

        initSize(context);


        initSensors();
        //Log.d(LOG_TAG, " mHandler-getRawX()" + this.getRawX());
        mHandler = new Handler() {

            public void handleMessage(android.os.Message msg) {
                float mRawX = getRawX();
                switch (petSurfaceView.getPosition()) {
                    case MOVE_POSITION_LEFT:
                        mRawX = mRawX - msg.what;
                        break;
                    case MOVE_POSITION_RIGHT:
                        mRawX = mRawX + msg.what;
                        break;

                }
                    setRawX(mRawX);
                    updateViewPosition();
                    if (popupMenu!=null)
                        UpdatePopupPosition();
            }
        };

        petSurfaceView.setHandler(mHandler);
        this.prepareForAddView(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        onClickListener = new View.OnClickListener(){
            public void onClick(View v) {
               // Log.d(LOG_TAG, "Click-getRawX()1-" + getRawX());
               if (popupMenu!=null)
                   popupMenuClose();
               else
                   ShowPopupMenu();

            }
        };

        setOnClickListener(onClickListener);
        setRawY(getRawY() - petHeight);
        updateViewPosition();
    }
    private void popupMenuClose(){
        popupMenu.release();
        popupMenu = null;
    }
    private void ShowPopupMenu(){
        if(popupMenu != null){
            popupMenu.release();
            popupMenu = null;
        }

        popupMenu = new FloatingLayout(getContext());
        View popupView = inflater.inflate(R.layout.popup_layout, null);
        popupMenu.prepareForAddView(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        UpdatePopupPosition();

        popupMenu.show();
    }

    private void UpdatePopupPosition(){
        popupMenu.setRawX(getRawX());
        popupMenu.setRawY((getRawY() - petHeight));
        popupMenu.updateViewPosition();
    }



    //инициализация сенсоров
    public void initSensors() {

        sensorEventListener = new SensorEventListener() {

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            @Override
            public void onSensorChanged(SensorEvent event) {

                switch (event.sensor.getType()) {

                    case Sensor.TYPE_ACCELEROMETER:
                        for (int i = 0; i < 3; i++) {
                            valuesAccel[i] = event.values[i];

                        }

                        if((valuesAccel[1]<1)&&(valuesAccel[1]>-1)) {
                            petSurfaceView.setStartMoove(false);
                            if(petSurfaceView.getPosition() == MOVE_POSITION_RIGHT)
                                petSurfaceView.setAnim(ANIM_SIT_RIGHT);
                            else
                                petSurfaceView.setAnim(ANIM_SIT_LEFT);
                        }

                        if((valuesAccel[1]>1)) {
                            petSurfaceView.setPosition(MOVE_POSITION_RIGHT);
                            petSurfaceView.setAnim(ANIM_WALK_RIGHT);
                            //petSurfaceView.setStartMoove(true);

                        }
                        if((valuesAccel[1]<-1)) {
                            petSurfaceView.setPosition(MOVE_POSITION_LEFT);
                            petSurfaceView.setAnim(ANIM_WALK_LEFT);
                           // petSurfaceView.setStartMoove(true);


                        }
                        break;
                }
                petSurfaceView.setStartMoove(true);

            }


        };

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void release() {
        Log.d(LOG_TAG, "PetView release()");
        petSurfaceView.surfaceDestroyed(null);
        sensorManager.unregisterListener(sensorEventListener);
        if(popupMenu != null){
            popupMenu.release();
            popupMenu = null;
        }
        if(dlgMenu != null){
            dlgMenu.release();
            dlgMenu = null;
        }

        super.release();
        petSurfaceView = null;
    }


    public void onClickpopupWindow(View v) {
        // по id определеяем кнопку, вызвавшую этот обработчик
        popupMenu.hide();
        switch (v.getId()) {
            case R.id.btn_food:
                dlgMenuShow(DialogWindow.DLG_FOOD);
                break;
            case R.id.btn_home:
                // кнопка ОК
                //tvOut.setText("Нажата кнопка food");
                //setIntent(new Intent(getApplicationContext(), MainActivity.class));
                //startActivity(getIntent());
                break;
            case R.id.btn_close:
                // кнопка close
                ((Activity)this.getContext()).finish();

                break;
        }

    }

    private void dlgMenuShow(int dlg_id) {

        if(dlgMenu != null){
            dlgMenu.release();
            dlgMenu = null;
        }

        dlgMenu = new DialogWindow(this.getContext());
        ImageButton mBtnClose = (ImageButton)dlgMenu.findViewById(R.id.dlg_btn_close);
        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgMenu.release();
                dlgMenu = null;
            }
        });
        dlgMenu.dlgShow(dlg_id, (int)this.getRawX(), (int)(this.getRawY() - petHeight));

    }
}



