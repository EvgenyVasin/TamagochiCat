package com.example.safinv.test.view.graphic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.safinv.test.R;

import java.lang.ref.WeakReference;

/**
 * Created by safin.v on 01.03.2016.
 */
public class Sprite {
    final String LOG_TAG = "myLogs";

    private int Anim;
    private static final int BMP_ROWS = 12;
    /**Колонок в спрайте = 3*/
    private static final int BMP_COLUMNS = 1;

    /**Картинка*/
    private Bitmap picture;

    /* Матрица для отрисовки спрайта на канве*/
    private Matrix matrix;

    /**Позиция по Х=0*/
    private int x = 0;
    private int y = 0;

    /**Скорость по Х=5*/
    private int xSpeed = 5;

    private int ySpeed = 5;

    /**Текущий кадр = 0*/
    private int currentFrame = 0;

    private int CountFrames = 12;

    /**Ширина*/
    private int FrameWidth ;

    /**Ввыоста*/
    private int FrameHeight;

    /**Ширина*/
    private int OutWidth;

    /**Ввыоста*/
    private int OutHeight;



    /**Конструктор*/
    public Sprite(Resources resources, int W, int H)
    {
        // загружаем картинку, которую будем отрисовывать
        if(picture != null) {
            picture.recycle();
            picture = null;
        }

        picture = decodeSampledBitmapFromResource(resources,
        W, H);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        picture = BitmapFactory.decodeResource( resources, R.drawable.tuna_sprite2, options);

        FrameWidth = this.picture.getWidth()/8;
        FrameHeight = this.picture.getHeight()/13;
        Log.d(LOG_TAG,this.picture.getWidth() + "-" + this.picture.getHeight());
        OutWidth = W;
        OutHeight = H;
    }
    public void setAnim(int anim)
    {
        Anim = anim;
    }
    /**Перемещение объекта, его направление*/
    public void update()
    {

    // увеличиваем номер текущего кадра
        currentFrame++;

        switch (Anim){
            case 0:
                if(currentFrame>= 11)
                    currentFrame=0;
            break;

            case 1:
                if(currentFrame>= 5)
                    currentFrame=5;
            break;


            case 6:
                if(currentFrame>= 5)
                    currentFrame=5;
            break;

            case 7:
                if(currentFrame>= 11)
                    currentFrame=0;
            break;


        }


    }

    public static Bitmap decodeSampledBitmapFromResource(Resources resources,
                                                         int reqWidth, int reqHeight) {
        Bitmap bmp;
        // Читаем с inJustDecodeBounds=true для определения размеров
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDensity = DisplayMetrics.DENSITY_XHIGH;

        BitmapFactory.decodeResource(resources,R.drawable.tuna_sprite2, options);

        // Вычисляем inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth,
//                reqHeight);

        options.inSampleSize = 1;

        // Читаем с использованием inSampleSize коэффициента
        options.inJustDecodeBounds = false;

        bmp = BitmapFactory.decodeResource(resources,R.drawable.tuna_sprite2, options);
        //bmp.setDensity(DisplayMetrics.DENSITY_XHIGH);
        return bmp;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Реальные размеры изображения
        final int height = options.outHeight / 13;
        final int width = options.outWidth / 8;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Вычисляем наибольший inSampleSize, который будет кратным двум
            // и оставит полученные размеры больше, чем требуемые
            while ((halfHeight / inSampleSize)/ 13 > reqHeight
                    && (halfWidth / inSampleSize)/ 8 > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**Рисуем наши спрайты*/
    public void onDraw(Canvas canvas)
    {


        int srcX = FrameWidth * Anim;
        int srcY = currentFrame * FrameHeight;
        Rect src = new Rect(srcX, srcY, srcX + FrameWidth, srcY + FrameHeight);
        Rect dst = new Rect(0, 0, OutWidth , OutHeight);

        canvas.drawBitmap(picture, src, dst, null);
    }

    public void ClearBtmp(){
        if(picture != null) {
            picture.recycle();
            picture = null;
        }
    }


}
