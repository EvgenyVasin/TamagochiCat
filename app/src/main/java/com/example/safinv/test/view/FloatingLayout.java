package com.example.safinv.test.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by safin.v on 23.03.2016.
 */
public class FloatingLayout extends LinearLayout {
    private static final String TAG = "FloatingView";
    private static final int MOVE_DISTANCE_MIN = 10;

    public float getTouchStartX() {
        return mTouchStartX;
    }

    public void setTouchStartX(float mTouchStartX) {
        this.mTouchStartX = mTouchStartX;
    }

    public float getTouchStartY() {
        return mTouchStartY;
    }

    public void setTouchStartY(float mTouchStartY) {
        this.mTouchStartY = mTouchStartY;
    }

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
    private boolean mIsShowing = false;
//    private HomeReceiver mHomeReceiver;

    private int mScreenHeight;
    private int mStatusBarHeight;

    public Rect rect;

    public boolean isShowing() {
        return mIsShowing;
    }

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

    public FloatingLayout(Context context) {
        super(context);
        init();


    }

    public FloatingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        initScreenWidthHeihgt();
        initWindowLayoutParams();

    }


    private void initScreenWidthHeihgt() {
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager = (WindowManager) getContext().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenHeight = metrics.heightPixels;
        Activity activity = (Activity) getContext();
        rect= new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        mStatusBarHeight = rect.top;

        Log.d(TAG, "initScreenWidthHeihgt mScreenHeight = " + mScreenHeight);
        Log.d(TAG, "initScreenWidthHeihgt mStatusBarHeight = " + mStatusBarHeight);
    }

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

    public void prepareForAddView(View view, int width, int height) {
        mContentView = view;

        addView(mContentView, width,height);
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.x = 0;
        mWindowLayoutParams.y = rect.bottom;
        mRawY = mWindowLayoutParams.y;
        mRawStartY = mRawY;
        mLastWindowHeight = mWindowLayoutParams.height;


        mWindowManager.addView(this, mWindowLayoutParams);

    }

    public void prepareForAddView(View view) {
        mContentView = view;

        addView(mContentView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.x = 0;
        mWindowLayoutParams.y = rect.bottom;
        mRawY = mWindowLayoutParams.y;
        mRawStartY = mRawY;
        mLastWindowHeight = mWindowLayoutParams.height;


        mWindowManager.addView(this, mWindowLayoutParams);

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
        if (null != mContentView &&  mIsShowing) {
            Log.d(TAG, "hide()");
            mWindowLayoutParams.height = 0;
            mWindowManager.updateViewLayout(this, mWindowLayoutParams);
            mContentView.setVisibility(View.GONE);
            mIsShowing = false;
        }
    }

    public void release() {
        if (null != mContentView && null != mWindowManager ) {
            Log.d(TAG, "release()");
            mWindowManager.removeView(this);
            mIsShowing = false;

        }
    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        mRawX = event.getRawX();
//        mRawY = event.getRawY() - mStatusBarHeight; // remove the height of notification bar
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mTouchStartX = event.getX();
//                mTouchStartY = event.getY();
//                mRawStartX = mRawX;
//                mRawStartY = mRawY;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (isNeedUpdateViewPosition()) {
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                if (isNeedUpdateViewPosition()) {
//                    return true;
//                }
//                break;
//        }
//        return super.onInterceptTouchEvent(event);
//    }
//
//    private boolean isNeedUpdateViewPosition() {
//        return Math.abs(mRawX - mRawStartX) > MOVE_DISTANCE_MIN || Math.abs(mRawY - mRawStartY) > MOVE_DISTANCE_MIN;
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        Log.d(TAG, "onTouchEvent() ");
//        mRawX = event.getRawX();
//
//        mRawY = event.getRawY() - mStatusBarHeight;
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//            {
//                break;}
//            case MotionEvent.ACTION_MOVE:
//                updateViewPosition();
//                break;
//            case MotionEvent.ACTION_UP:
//                updateViewPosition();
//                break;
//        }
//        return true;
//    }

    public void updateViewPosition() {
        mWindowLayoutParams.x = (int) (mRawX - mTouchStartX);
        mWindowLayoutParams.y = (int) (mRawY - mTouchStartY);
        Log.d(TAG, "mWindowLayoutParams.y" + mWindowLayoutParams.y);
        mWindowManager.updateViewLayout(this, mWindowLayoutParams);
    }

//    @SuppressWarnings("deprecation")

//    public void onClick(View v) {


    // showPopupMenu(v);
//        mSurfaceView.Anim ++;
//        if( mSurfaceView.Anim > 7)
//            mSurfaceView.Anim = 0;
//        switch (v.getId()) {
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
//        }
//    }





}
