package com.example.safinv.test.view.graphic;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.safinv.test.view.PetView;

/**
 * Created by safin.v on 24.03.2016.
 */

public class PetSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    class Sync {
        boolean runFlag = true;
        private int Anim = PetView.ANIM_SIT_LEFT;
        private int Position;
        private boolean StartMoove = true;
        private Handler mHandler;
    }
    final String LOG_TAG = "myLogs";

    private Thread drawThread;




    private Sync sync;

    public synchronized int getAnim() {
        synchronized (sync) {
            return sync.Anim;
        }
    }

    public synchronized void setAnim(int anim) {
        synchronized (sync){
            sync.Anim = anim;
        }
    }

    public synchronized Handler getHandler() {
        synchronized (sync) {
            return sync.mHandler;
        }
    }

    public synchronized void setHandler(Handler mHandler) {
        synchronized (sync) {
            sync.mHandler = mHandler;
        }
    }

    public synchronized boolean isStartMoove() {
        synchronized (sync) {
            return sync.StartMoove;
        }
    }

    public synchronized void setStartMoove(boolean startMoove) {
        synchronized (sync) {
            sync.StartMoove = startMoove;
        }
    }

    public synchronized int getPosition() {
        synchronized (sync) {
            return sync.Position;
        }
    }

    public synchronized void setPosition(int position) {
        synchronized (sync) {
            sync.Position = position;
        }
    }



    public PetSurfaceView(Context context) {
        super(context);
        sync = new Sync();
        setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        getHolder().addCallback(this);

    }

    public PetSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sync = new Sync();
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        getHolder().addCallback(this);
    }

    public PetSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        sync = new Sync();
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(LOG_TAG, "PetSurfaceView surfaceCreated");

        drawThread = null;
        drawThread = new Thread(()->{
            Sprite1 sprite = new Sprite1(getResources(), getWidth(), getHeight());

            while (sync.runFlag) {
                synchronized (sync) {
                    if(!holder.getSurface().isValid())
                        continue;

                   if(!sync.StartMoove){
                        try {
                            sync.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }



                    }
                    sync.mHandler.sendEmptyMessage(20);
                    sprite.onDraw(holder, sync.Anim);



                }

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            sprite.ClearBtmp();

        });
//        drawThread = new DrawThread(this);
//        drawThread = new DrawThread1(getHolder(), getResources());
        //drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        // завершаем работу потока

        //drawThread.setRunning(false);
        sync.runFlag = false;
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.d(LOG_TAG, "PetSurfaceView не получилось разрушить поток"); // если не получилось, то будем пытаться еще и еще
            }
        }
        Log.d(LOG_TAG, "PetSurfaceView surfaceDestroyed");

    }


}
