package com.example.safinv.test.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.safinv.test.R;
import com.example.safinv.test.controller.PetService;
import com.example.safinv.test.view.graphic.PetSurfaceView;


/**
 * Created by safin.v on 25.02.2016.
 */

public class FloatingView extends LinearLayout   implements OnClickListener {
    private static final String TAG = "FloatingView";

    private static final int MOVE_DISTANCE_MIN = 10;

    private float mTouchStartX;
    private float mTouchStartY;
    private float mRawX;
    private float mRawY;

    private float mRawStartX = 0;
    private float mRawStartY = 0;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private int mLastWindowHeight = 0;
    private View mContentView = null;

    private Button mBtnToFullscreen;
    private Button mBtnToMini;
    private Button mBtnToStardard;
    private GLSurfaceView mGLSurfaceView;
    private PetSurfaceView mSurfaceView;
    private RelativeLayout mRelativeLayout;

    private boolean mIsShowing = false;
    private HomeReceiver mHomeReceiver;

    private int mScreenHeight;
    private int mStatusBarHeight;

    SensorManager sensorManager;
    Sensor sensorAccel;

    public float getRawX() {
        return mRawX;
    }

    public void setRawX(float mRawX) {
        this.mRawX = mRawX;
    }

    public float getRawY() {
        return mRawY;
    }

    public void setRawY(float mRawY) {
        this.mRawY = mRawY;
    }

    public PopupWindow popupWindow;







    public FloatingView(Context context) {
        super(context);
        init();


    }

    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        initScreenWidthHeihgt();
        initWindowLayoutParams();
        prepareForAddView();
        prepareForServiceReveiver();







    }

    @SuppressWarnings("deprecation")
    private void prepareForAddView() {
            LayoutInflater inflater = LayoutInflater.from(getContext());
//            mContentView = inflater.inflate(R.layout.test, null);
        mContentView.setOnClickListener(this);



//        mBtnToFullscreen = (Button) mContentView
//                .findViewById(R.id.btn_fullscreen);
//        mBtnToMini = (Button) mContentView.findViewById(R.id.btn_mini);
//        mBtnToFullscreen.setOnClickListener(this);
//        mBtnToMini.setOnClickListener(this);
//        mBtnToStardard = (Button) mContentView.findViewById(R.id.btn_stardard);
//        mBtnToStardard.setOnClickListener(this);

       // mSurfaceView = (MySurfaceView) mContentView.findViewById(R.id.mySurfaceView);
        //mSurfaceView.setStartMoove(false);
//        mSurfaceView.setAnim(Animation.SitLeft);
//        mHandler = new Handler() {
//            public void handleMessage(android.os.Message msg) {
//                // обновляем TextView
//                switch (mSurfaceView.getPosition()) {
//                    case Left:
//                        mRawX = mRawX - msg.what;
//                        break;
//                    case Right:
//                        mRawX = mRawX + msg.what;
//                        break;
//
//                }
//
//                    updateViewPosition();
//                }
//            };
//
//        mSurfaceView.setHandler(mHandler);



        addView(mContentView, GridLayout.LayoutParams.MATCH_PARENT,
                GridLayout.LayoutParams.MATCH_PARENT);
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.x = 0;
        mWindowLayoutParams.y = 5000;
        mRawY = mWindowLayoutParams.y;
        mRawStartY = mRawY;
                mLastWindowHeight = mWindowLayoutParams.height;


        mWindowManager.addView(this, mWindowLayoutParams);
        showCategoriesWindow();

    }

    private void initScreenWidthHeihgt() {
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager = (WindowManager) getContext().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenHeight = metrics.heightPixels;
        Activity activity = (Activity) getContext();
        Rect rect= new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        mStatusBarHeight = rect.top;

        Log.d(TAG, "initScreenWidthHeihgt mScreenHeight = " + mScreenHeight);
        Log.d(TAG, "initScreenWidthHeihgt mStatusBarHeight = " + mStatusBarHeight);
    }

    @SuppressWarnings("deprecation")
    private void initWindowLayoutParams() {
        mWindowLayoutParams =  new WindowManager.LayoutParams();

        mWindowLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mWindowLayoutParams.format = PixelFormat.RGBA_8888;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;

        mWindowLayoutParams.x = 0;
        mWindowLayoutParams.y = 0;

        //Note: the following parameters is the key for width and height of display window
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        mLastWindowHeight = mWindowLayoutParams.height;
    }

    private void prepareForServiceReveiver() {
        getContext().startService(
                new Intent(getContext(), PetService.class));
        mHomeReceiver = new HomeReceiver();
        IntentFilter filter = new IntentFilter();

        getContext().getApplicationContext().registerReceiver(mHomeReceiver,
                filter);
    }

    public void show() {
        if (null != mContentView && !mIsShowing) {
            Log.d(TAG, "show()");
            mWindowLayoutParams.height = mLastWindowHeight;
            mWindowManager.updateViewLayout(this, mWindowLayoutParams);
            mContentView.setVisibility(View.VISIBLE);
            mIsShowing = true;
        }
    }

    public void hide() {
        if (null != mContentView && mIsShowing) {
            Log.d(TAG, "hide()");
            mWindowLayoutParams.height = 0;
            mWindowManager.updateViewLayout(this, mWindowLayoutParams);
            mContentView.setVisibility(View.GONE);
            mIsShowing = false;
        }
    }

    public void release() {
        if (null != mWindowManager && null != mContentView) {
            mSurfaceView.surfaceDestroyed(null);
            Log.d(TAG, "release()");
            mWindowManager.removeView(this);
            mIsShowing = false;
            getContext().getApplicationContext().unregisterReceiver(
                    mHomeReceiver);

        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        mRawX = event.getRawX();
        mRawY = event.getRawY() - mStatusBarHeight; // remove the height of notification bar
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                mRawStartX = mRawX;
                mRawStartY = mRawY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isNeedUpdateViewPosition()) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedUpdateViewPosition()) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    private boolean isNeedUpdateViewPosition() {
        return Math.abs(mRawX - mRawStartX) > MOVE_DISTANCE_MIN || Math.abs(mRawY - mRawStartY) > MOVE_DISTANCE_MIN;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent() ");
        mRawX = event.getRawX();

        mRawY = event.getRawY() - mStatusBarHeight;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            {
                break;}
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                updateViewPosition();
                break;
        }
        return true;
    }

    public void updateViewPosition() {
        mWindowLayoutParams.x = (int) (mRawX - mTouchStartX);
        mWindowLayoutParams.y = (int) (mRawY - mTouchStartY);
        mWindowManager.updateViewLayout(this, mWindowLayoutParams);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        if(popupWindow.isShowing())
            popupWindow.dismiss();
        else
            popupWindow.showAsDropDown(mSurfaceView, 0 + 30, 0 - mSurfaceView.getHeight()- 80);

       // showPopupMenu(v);
//        mSurfaceView.Anim ++;
//        if( mSurfaceView.Anim > 7)
//            mSurfaceView.Anim = 0;
        switch (v.getId()) {
//            case R.id.btn_fullscreen:
//                mWindowLayoutParams.width = WindowManager.LayoutParams.FILL_PARENT;
//                mWindowLayoutParams.height = WindowManager.LayoutParams.FILL_PARENT;
//                mLastWindowHeight = mWindowLayoutParams.height;
//                mWindowManager.updateViewLayout(this, mWindowLayoutParams);
//                break;
//            case R.id.btn_mini:
//                mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//                mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                mWindowLayoutParams.x = 0;
//                mLastWindowHeight = mWindowLayoutParams.height;
//                mWindowManager.updateViewLayout(this, mWindowLayoutParams);
//                break;
//            case R.id.btn_stardard:
//                mWindowLayoutParams.width = WindowManager.LayoutParams.FILL_PARENT;
//                mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                mWindowLayoutParams.x = 0;
//                mLastWindowHeight = mWindowLayoutParams.height;
//                mWindowManager.updateViewLayout(this, mWindowLayoutParams);
//                break;
        }
    }



    private void showCategoriesWindow() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
    /*Поскольку я открывал PopupWindow из Fragment, то инициализировать inflater не пришлось (передаём его из
            onCreateView(final LayoutInflater inflater, ViewGroup viewGroup, Bundle SavedInstanceState)
    Если же вы вызываете PopupWindow из Activity, то инициализируем её так:
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
        //View popupView = inflater.inflate(R.layout.popup_layout, null);
       // popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //popupWindow.showAsDropDown(mSurfaceView, 50, -20);
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.popupmenu); // Для Android 4.0
        // для версии Android 3.0 нужно использовать длинный вариант
        // popupMenu.getMenuInflater().inflate(R.menu.popupmenu,
        // popupMenu.getMenu());
        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Toast.makeText(PopupMenuDemoActivity.this,
                        // item.toString(), Toast.LENGTH_LONG).show();
                        // return true;
                        //item.setIcon(R.mipmap.ic_launcher);
                        switch (item.getItemId()) {

                            case R.id.menu1:
                                Toast.makeText(getContext(),
                                        "Вы выбрали PopupMenu 1",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu2:
                                Toast.makeText(getContext(),
                                        "Вы выбрали PopupMenu 2",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu3:
                                Toast.makeText(getContext(),
                                        "Вы выбрали PopupMenu 3",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {

            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getContext(), "onDismiss",
                        Toast.LENGTH_SHORT).show();
            }
        });
        popupMenu.show();
    }

    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "HomeReceiver onReceive()  action = " + action);

        }

    }
}
