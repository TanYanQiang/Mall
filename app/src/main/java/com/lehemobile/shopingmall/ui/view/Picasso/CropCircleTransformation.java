package com.lehemobile.shopingmall.ui.view.Picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Transformation;

/**
 * Created by tanyq on 2/8/16.
 */
public class CropCircleTransformation implements Transformation {
    private float borderWidth = 0;
    private int borderColor;

    public CropCircleTransformation() {
    }

    public CropCircleTransformation(float borderWidth, int borderColor) {
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int width = (source.getWidth() - size) / 2;
        int height = (source.getHeight() - size) / 2;

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);


        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader =
                new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        if (width != 0 || height != 0) {
            // source isn't square, move viewport to center
            Matrix matrix = new Matrix();
            matrix.setTranslate(-width, -height);
            shader.setLocalMatrix(matrix);
        }
        paint.setShader(shader);
        paint.setAntiAlias(true);
        float r = size / 2f;

        if (borderWidth > 0) {
            Paint boardPaint = new Paint();
            boardPaint.setColor(borderColor);
            boardPaint.setStyle(Paint.Style.FILL);
            boardPaint.setAntiAlias(true);
            boardPaint.setStrokeWidth(borderWidth);
            canvas.drawCircle(r, r, r, boardPaint);
            Logger.i("--->border Width:"+borderWidth);
        }

        canvas.drawCircle(r, r, r - borderWidth, paint);
        if (bitmap != source) {
            source.recycle();
        }
        return bitmap;
    }

    @Override
    public String key() {
        return "CropCircleTransformation()";
    }
}
