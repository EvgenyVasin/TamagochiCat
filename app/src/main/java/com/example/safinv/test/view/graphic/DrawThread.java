package com.example.safinv.test.view.graphic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;

/**
 * Created by safin.v on 26.02.2016.
 */


public class DrawThread  extends Thread{
    PetSurfaceView surface;
    private boolean runFlag = false;
    private SurfaceHolder surfaceHolder;

    private Matrix matrix;
    private long prevTime;
    private long frameTicker;// время обновления последнего кадра
    private int framePeriod; // сколько миллисекунд должно пройти перед сменой кадра (1000/fps)

    /**Объект класса Sprite*/
    private Sprite sprite;

    public DrawThread(PetSurfaceView sur){
        this.surfaceHolder = sur.getHolder();
        this.surface = sur;

        framePeriod=1000/ 24;
        frameTicker= 0l;


        sprite = new Sprite(sur.getResources(), sur.getWidth(), sur.getHeight());

    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas;
        long gameTime;

        while (runFlag) {
            if(!surfaceHolder.getSurface().isValid())
                continue;
            gameTime = System.currentTimeMillis();
            if (gameTime > frameTicker + framePeriod) {
                frameTicker = gameTime;

                canvas = null;

                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                try {
                    synchronized (surfaceHolder) {

                        canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
                        //                    canvas.drawColor(Color.WHITE);
                        sprite.setAnim(surface.getAnim());
                        sprite.update();
                        sprite.onDraw(canvas);

                    }


                } finally {
                    if (canvas != null) {
                        // отрисовка выполнена. выводим результат на экран
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }

                }
//                if (surface.isStartMoove())
//                    surface.getHandler().sendEmptyMessage(20);
            }
            //break;
        }
        sprite.ClearBtmp();

        return;
    }
}
