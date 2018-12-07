package com.example.safinv.test.view.graphic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Matrix;
import android.util.DisplayMetrics;

import com.example.safinv.test.R;

/**
 * Created by safin.v on 15.04.2016.
 */
public class SpritePet{

    class PartOfBody{
        private Bitmap picture;
        private Matrix matrix;


        PartOfBody(Resources resources, int bmpWidth, int bmpHeight) {
            picture = decodeSampledBitmapFromResource(resources, bmpWidth, bmpHeight);
        }

        private void relise(){
            if(picture != null) {
                picture.recycle();
                picture = null;
            }
        }
    }
    private Bitmap picBody, picHead, picShoulder, picForearm, picThigh, picShin;

    public static Bitmap decodeSampledBitmapFromResource(Resources resources,
                                                         int reqWidth, int reqHeight) {
        Bitmap bmp;
        // Читаем с inJustDecodeBounds=true для определения размеров
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDensity = DisplayMetrics.DENSITY_XHIGH;

        BitmapFactory.decodeResource(resources, R.drawable.tuna_sprite2, options);

        // Вычисляем inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Читаем с использованием inSampleSize коэффициента
        options.inJustDecodeBounds = false;

        bmp = BitmapFactory.decodeResource(resources,R.drawable.tuna_sprite2, options);

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
}
