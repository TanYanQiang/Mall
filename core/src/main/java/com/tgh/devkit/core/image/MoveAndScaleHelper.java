package com.tgh.devkit.core.image;

import android.content.Context;
import android.graphics.Matrix;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * 拖动和缩放的帮助类
 * Created by albert on 16/1/5.
 */
public class MoveAndScaleHelper {

    private boolean scale = true;
    private Matrix matrix;
    private GestureDetectorCompat mGestureDetectorCompat;
    private ScaleGestureDetector mScaleGestureDetector;
    private Runnable callback;

    public MoveAndScaleHelper(Context context, Matrix matrix, boolean scale) {
        this.matrix = matrix;
        this.scale = scale;
        mGestureDetectorCompat = new GestureDetectorCompat(context, new ImageGestureListener());
        mScaleGestureDetector = new ScaleGestureDetector(context, new ImageScaleListener());
    }


    public void setScale(boolean scale) {
        this.scale = scale;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    public void onTouchEvent(MotionEvent event) {
        mGestureDetectorCompat.onTouchEvent(event);
        if (scale){
            mScaleGestureDetector.onTouchEvent(event);
        }
    }

    private class ImageGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mScaleGestureDetector.isInProgress()) {
                return false;
            }
            matrix.postTranslate(-distanceX, -distanceY);
            if (null!=callback){
                callback.run();
            }
            return true;
        }
    }

    private class ImageScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
                return false;
            }

            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            if (null!=callback){
                callback.run();
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    }
}
