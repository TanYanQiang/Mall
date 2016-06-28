package com.yalantis.ucrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yalantis.ucrop.util.BitmapLoadUtils;
import com.yalantis.ucrop.util.FastBitmapDrawable;
import com.yalantis.ucrop.util.RectUtils;

public class TransformImageView extends ImageView {
    private static final String TAG = "TransformImageView";
    private static final int RECT_CORNER_POINTS_COORDS = 8;
    private static final int RECT_CENTER_POINT_COORDS = 2;
    private static final int MATRIX_VALUES_COUNT = 9;
    protected final float[] mCurrentImageCorners = new float[8];
    protected final float[] mCurrentImageCenter = new float[2];

    private final float[] mMatrixValues = new float[9];

    protected Matrix mCurrentImageMatrix = new Matrix();
    protected int mThisWidth;
    protected int mThisHeight;
    protected TransformImageListener mTransformImageListener;
    private float[] mInitialImageCorners;
    private float[] mInitialImageCenter;
    private boolean mBitmapWasLoaded = false;

    private int mMaxBitmapSize = 0;
    private Uri mImageUri;

    public TransformImageView(Context context) {
        this(context, null);
    }

    public TransformImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransformImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setTransformImageListener(TransformImageListener transformImageListener) {
        this.mTransformImageListener = transformImageListener;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType == ImageView.ScaleType.MATRIX)
            super.setScaleType(scaleType);
        else
            Log.w("TransformImageView", "Invalid ScaleType. Only ScaleType.MATRIX can be used");
    }

    public void setMaxBitmapSize(int maxBitmapSize) {
        this.mMaxBitmapSize = maxBitmapSize;
    }

    public int getMaxBitmapSize() {
        if (this.mMaxBitmapSize <= 0) {
            this.mMaxBitmapSize = calculateMaxBitmapSize();
        }
        return this.mMaxBitmapSize;
    }

    public void setImageBitmap(Bitmap bitmap) {
        setImageDrawable(new FastBitmapDrawable(bitmap));
    }

    public Uri getImageUri() {
        return this.mImageUri;
    }

    public void setImageUri(Uri imageUri)
            throws Exception {
        this.mImageUri = imageUri;
        int maxBitmapSize = getMaxBitmapSize();

        BitmapLoadUtils.decodeBitmapInBackground(getContext(), imageUri, maxBitmapSize, maxBitmapSize, new BitmapLoadUtils.BitmapLoadCallback() {
            public void onBitmapLoaded(Bitmap bitmap) {
                TransformImageView.this.mBitmapWasLoaded = true;
                TransformImageView.this.setImageBitmap(bitmap);
                TransformImageView.this.invalidate();
            }

            public void onFailure(Exception bitmapWorkerException) {
                Log.e("TransformImageView", "onFailure: setImageUri", bitmapWorkerException);
                if (TransformImageView.this.mTransformImageListener != null)
                    TransformImageView.this.mTransformImageListener.onLoadFailure(bitmapWorkerException);
            }
        });
    }

    public float getCurrentScale() {
        return getMatrixScale(this.mCurrentImageMatrix);
    }

    public float getMatrixScale(Matrix matrix) {
        return (float) Math.sqrt(Math.pow(getMatrixValue(matrix, 0), 2.0D) + Math.pow(getMatrixValue(matrix, 3), 2.0D));
    }

    public float getCurrentAngle() {
        return getMatrixAngle(this.mCurrentImageMatrix);
    }

    public float getMatrixAngle(Matrix matrix) {
        return (float) -(Math.atan2(getMatrixValue(matrix, 1), getMatrixValue(matrix, 0)) * 57.295779513082323D);
    }

    public void setImageMatrix(Matrix matrix) {
        super.setImageMatrix(matrix);
        updateCurrentImagePoints();
    }


    public Bitmap getViewBitmap() {
        if ((getDrawable() == null) || (!(getDrawable() instanceof FastBitmapDrawable))) {
            return null;
        }
        return ((FastBitmapDrawable) getDrawable()).getBitmap();
    }

    public void postTranslate(float deltaX, float deltaY) {
        if ((deltaX != 0.0F) || (deltaY != 0.0F)) {
            this.mCurrentImageMatrix.postTranslate(deltaX, deltaY);
            setImageMatrix(this.mCurrentImageMatrix);
        }
    }

    public void postScale(float deltaScale, float px, float py) {
        if (deltaScale != 0.0F) {
            this.mCurrentImageMatrix.postScale(deltaScale, deltaScale, px, py);
            setImageMatrix(this.mCurrentImageMatrix);
            if (this.mTransformImageListener != null)
                this.mTransformImageListener.onScale(getMatrixScale(this.mCurrentImageMatrix));
        }
    }

    public void postRotate(float deltaAngle, float px, float py) {
        if (deltaAngle != 0.0F) {
            this.mCurrentImageMatrix.postRotate(deltaAngle, px, py);
            setImageMatrix(this.mCurrentImageMatrix);
            if (this.mTransformImageListener != null)
                this.mTransformImageListener.onRotate(getMatrixAngle(this.mCurrentImageMatrix));
        }
    }

    protected void init() {
        setScaleType(ImageView.ScaleType.MATRIX);
    }

    protected int calculateMaxBitmapSize() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        int width;
        int height;
        if (Build.VERSION.SDK_INT >= 13) {
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            width = display.getWidth();
            height = display.getHeight();
        }
        return (int) Math.sqrt(Math.pow(width, 2.0D) + Math.pow(height, 2.0D));
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if ((changed) || (this.mBitmapWasLoaded)) {
            if (this.mBitmapWasLoaded) this.mBitmapWasLoaded = false;

            left = getPaddingLeft();
            top = getPaddingTop();
            right = getWidth() - getPaddingRight();
            bottom = getHeight() - getPaddingBottom();
            this.mThisWidth = (right - left);
            this.mThisHeight = (bottom - top);

            onImageLaidOut();
        }
    }

    protected void onImageLaidOut() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        float w = drawable.getIntrinsicWidth();
        float h = drawable.getIntrinsicHeight();

        Log.d("TransformImageView", String.format("Image size: [%d:%d]", new Object[]{Integer.valueOf((int) w), Integer.valueOf((int) h)}));

        RectF initialImageRect = new RectF(0.0F, 0.0F, w, h);
        this.mInitialImageCorners = RectUtils.getCornersFromRect(initialImageRect);
        this.mInitialImageCenter = RectUtils.getCenterFromRect(initialImageRect);

        if (this.mTransformImageListener != null)
            this.mTransformImageListener.onLoadComplete();
    }

    protected float getMatrixValue(Matrix matrix, int valueIndex) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[valueIndex];
    }

    protected void printMatrix(String logPrefix, Matrix matrix) {
        float x = getMatrixValue(matrix, 2);
        float y = getMatrixValue(matrix, 5);
        float rScale = getMatrixScale(matrix);
        float rAngle = getMatrixAngle(matrix);
        Log.d("TransformImageView", logPrefix + ": matrix: { x: " + x + ", y: " + y + ", scale: " + rScale + ", angle: " + rAngle + " }");
    }

    private void updateCurrentImagePoints() {
        this.mCurrentImageMatrix.mapPoints(this.mCurrentImageCorners, this.mInitialImageCorners);
        this.mCurrentImageMatrix.mapPoints(this.mCurrentImageCenter, this.mInitialImageCenter);
    }

    public interface TransformImageListener {
        void onLoadComplete();

        void onLoadFailure(Exception paramException);

        void onRotate(float paramFloat);

        void onScale(float paramFloat);
    }
}