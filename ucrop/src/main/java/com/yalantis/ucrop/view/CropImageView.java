package com.yalantis.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.yalantis.ucrop.R;
import com.yalantis.ucrop.util.CubicEasing;
import com.yalantis.ucrop.util.RectUtils;

import java.lang.ref.WeakReference;
import java.util.Arrays;

public class CropImageView extends TransformImageView {
    public static final int DEFAULT_MAX_BITMAP_SIZE = 0;
    public static final int DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION = 500;
    public static final float DEFAULT_MAX_SCALE_MULTIPLIER = 10.0F;
    public static final float SOURCE_IMAGE_ASPECT_RATIO = 0.0F;
    public static final float DEFAULT_ASPECT_RATIO = 0.0F;
    private final RectF mCropRect = new RectF();

    private final Matrix mTempMatrix = new Matrix();
    private float mTargetAspectRatio;
    private float mMaxScaleMultiplier = 10.0F;
    private CropBoundsChangeListener mCropBoundsChangeListener;
    private Runnable mWrapCropBoundsRunnable;
    private Runnable mZoomImageToPositionRunnable = null;
    private float mMaxScale;
    private float mMinScale;
    private int mMaxResultImageSizeX = 0;
    private int mMaxResultImageSizeY = 0;
    private long mImageToWrapCropBoundsAnimDuration = 500L;

    public CropImageView(Context context) {
        this(context, null);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Bitmap cropImage() {
        Bitmap viewBitmap = getViewBitmap();
        if ((viewBitmap == null) || (viewBitmap.isRecycled())) {
            return null;
        }

        cancelAllAnimations();
        setImageToWrapCropBounds(false);

        RectF currentImageRect = RectUtils.trapToRect(this.mCurrentImageCorners);
        if (currentImageRect.isEmpty()) {
            return null;
        }

        float currentScale = getCurrentScale();
        float currentAngle = getCurrentAngle();

        if ((this.mMaxResultImageSizeX > 0) && (this.mMaxResultImageSizeY > 0)) {
            float cropWidth = this.mCropRect.width() / currentScale;
            float cropHeight = this.mCropRect.height() / currentScale;

            if ((cropWidth > this.mMaxResultImageSizeX) || (cropHeight > this.mMaxResultImageSizeY)) {
                float scaleX = this.mMaxResultImageSizeX / cropWidth;
                float scaleY = this.mMaxResultImageSizeY / cropHeight;
                float resizeScale = Math.min(scaleX, scaleY);

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(viewBitmap, (int) (viewBitmap.getWidth() * resizeScale), (int) (viewBitmap.getHeight() * resizeScale), false);

                if (viewBitmap != resizedBitmap) {
                    viewBitmap.recycle();
                }
                viewBitmap = resizedBitmap;

                currentScale /= resizeScale;
            }
        }

        if (currentAngle != 0.0F) {
            this.mTempMatrix.reset();
            this.mTempMatrix.setRotate(currentAngle, viewBitmap.getWidth() / 2, viewBitmap.getHeight() / 2);

            Bitmap rotatedBitmap = Bitmap.createBitmap(viewBitmap, 0, 0, viewBitmap.getWidth(), viewBitmap.getHeight(), this.mTempMatrix, true);

            if (viewBitmap != rotatedBitmap) {
                viewBitmap.recycle();
            }
            viewBitmap = rotatedBitmap;
        }

        int top = (int) ((this.mCropRect.top - currentImageRect.top) / currentScale);
        int left = (int) ((this.mCropRect.left - currentImageRect.left) / currentScale);
        int width = (int) (this.mCropRect.width() / currentScale);
        int height = (int) (this.mCropRect.height() / currentScale);

        return Bitmap.createBitmap(viewBitmap, left, top, width, height);
    }

    public float getMaxScale() {
        return this.mMaxScale;
    }

    public float getMinScale() {
        return this.mMinScale;
    }

    public float getTargetAspectRatio() {
        return this.mTargetAspectRatio;
    }

    public void setTargetAspectRatio(float targetAspectRatio) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            this.mTargetAspectRatio = targetAspectRatio;
            return;
        }

        if (targetAspectRatio == 0.0F)
            this.mTargetAspectRatio = (drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight());
        else {
            this.mTargetAspectRatio = targetAspectRatio;
        }

        setupCropBounds();
        postInvalidate();
    }

    public CropBoundsChangeListener getCropBoundsChangeListener() {
        return this.mCropBoundsChangeListener;
    }

    public void setCropBoundsChangeListener(CropBoundsChangeListener cropBoundsChangeListener) {
        this.mCropBoundsChangeListener = cropBoundsChangeListener;
    }

    public void setMaxResultImageSizeX(int maxResultImageSizeX) {
        this.mMaxResultImageSizeX = maxResultImageSizeX;
    }

    public void setMaxResultImageSizeY(int maxResultImageSizeY) {
        this.mMaxResultImageSizeY = maxResultImageSizeY;
    }

    public void setImageToWrapCropBoundsAnimDuration(long imageToWrapCropBoundsAnimDuration) {
        if (imageToWrapCropBoundsAnimDuration > 0L)
            this.mImageToWrapCropBoundsAnimDuration = imageToWrapCropBoundsAnimDuration;
        else
            throw new IllegalArgumentException("Animation duration cannot be negative value.");
    }

    public void setMaxScaleMultiplier(float maxScaleMultiplier) {
        this.mMaxScaleMultiplier = maxScaleMultiplier;
    }

    public void zoomOutImage(float deltaScale) {
        zoomOutImage(deltaScale, this.mCropRect.centerX(), this.mCropRect.centerY());
    }

    public void zoomOutImage(float scale, float centerX, float centerY) {
        if (scale >= getMinScale())
            postScale(scale / getCurrentScale(), centerX, centerY);
    }

    public void zoomInImage(float deltaScale) {
        zoomInImage(deltaScale, this.mCropRect.centerX(), this.mCropRect.centerY());
    }

    public void zoomInImage(float scale, float centerX, float centerY) {
        if (scale <= getMaxScale())
            postScale(scale / getCurrentScale(), centerX, centerY);
    }

    public void postScale(float deltaScale, float px, float py) {
        if ((deltaScale > 1.0F) && (getCurrentScale() * deltaScale <= getMaxScale()))
            super.postScale(deltaScale, px, py);
        else if ((deltaScale < 1.0F) && (getCurrentScale() * deltaScale >= getMinScale()))
            super.postScale(deltaScale, px, py);
    }

    public void postRotate(float deltaAngle) {
        postRotate(deltaAngle, this.mCropRect.centerX(), this.mCropRect.centerY());
    }

    public void cancelAllAnimations() {
        removeCallbacks(this.mWrapCropBoundsRunnable);
        removeCallbacks(this.mZoomImageToPositionRunnable);
    }

    public void setImageToWrapCropBounds() {
        setImageToWrapCropBounds(true);
    }

    public void setImageToWrapCropBounds(boolean animate) {
        if (!isImageWrapCropBounds()) {
            float currentX = this.mCurrentImageCenter[0];
            float currentY = this.mCurrentImageCenter[1];
            float currentScale = getCurrentScale();

            float deltaX = this.mCropRect.centerX() - currentX;
            float deltaY = this.mCropRect.centerY() - currentY;
            float deltaScale = 0.0F;

            this.mTempMatrix.reset();
            this.mTempMatrix.setTranslate(deltaX, deltaY);

            float[] tempCurrentImageCorners = Arrays.copyOf(this.mCurrentImageCorners, this.mCurrentImageCorners.length);
            this.mTempMatrix.mapPoints(tempCurrentImageCorners);

            boolean willImageWrapCropBoundsAfterTranslate = isImageWrapCropBounds(tempCurrentImageCorners);

            if (willImageWrapCropBoundsAfterTranslate) {
                float[] imageIndents = calculateImageIndents();
                deltaX = -(imageIndents[0] + imageIndents[2]);
                deltaY = -(imageIndents[1] + imageIndents[3]);
            } else {
                RectF tempCropRect = new RectF(this.mCropRect);
                this.mTempMatrix.reset();
                this.mTempMatrix.setRotate(getCurrentAngle());
                this.mTempMatrix.mapRect(tempCropRect);

                float[] currentImageSides = RectUtils.getRectSidesFromCorners(this.mCurrentImageCorners);

                deltaScale = Math.max(tempCropRect.width() / currentImageSides[0], tempCropRect.height() / currentImageSides[1]);

                deltaScale = (float) (deltaScale * 1.01D);
                deltaScale = deltaScale * currentScale - currentScale;
            }

            if (animate) {
                post(this.mWrapCropBoundsRunnable = new WrapCropBoundsRunnable(this, this.mImageToWrapCropBoundsAnimDuration, currentX, currentY, deltaX, deltaY, currentScale, deltaScale, willImageWrapCropBoundsAfterTranslate));
            } else {
                postTranslate(deltaX, deltaY);
                if (!willImageWrapCropBoundsAfterTranslate)
                    zoomInImage(currentScale + deltaScale, this.mCropRect.centerX(), this.mCropRect.centerY());
            }
        }
    }

    private float[] calculateImageIndents() {
        this.mTempMatrix.reset();
        this.mTempMatrix.setRotate(-getCurrentAngle());

        float[] unrotatedImageCorners = Arrays.copyOf(this.mCurrentImageCorners, this.mCurrentImageCorners.length);
        float[] unrotatedCropBoundsCorners = RectUtils.getCornersFromRect(this.mCropRect);

        this.mTempMatrix.mapPoints(unrotatedImageCorners);
        this.mTempMatrix.mapPoints(unrotatedCropBoundsCorners);

        RectF unrotatedImageRect = RectUtils.trapToRect(unrotatedImageCorners);
        RectF unrotatedCropRect = RectUtils.trapToRect(unrotatedCropBoundsCorners);

        float deltaLeft = unrotatedImageRect.left - unrotatedCropRect.left;
        float deltaTop = unrotatedImageRect.top - unrotatedCropRect.top;
        float deltaRight = unrotatedImageRect.right - unrotatedCropRect.right;
        float deltaBottom = unrotatedImageRect.bottom - unrotatedCropRect.bottom;

        float[] indents = new float[4];
        indents[0] = (deltaLeft > 0.0F ? deltaLeft : 0.0F);
        indents[1] = (deltaTop > 0.0F ? deltaTop : 0.0F);
        indents[2] = (deltaRight < 0.0F ? deltaRight : 0.0F);
        indents[3] = (deltaBottom < 0.0F ? deltaBottom : 0.0F);

        this.mTempMatrix.reset();
        this.mTempMatrix.setRotate(getCurrentAngle());
        this.mTempMatrix.mapPoints(indents);

        return indents;
    }

    protected void onImageLaidOut() {
        super.onImageLaidOut();
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        float drawableWidth = drawable.getIntrinsicWidth();
        float drawableHeight = drawable.getIntrinsicHeight();

        if (this.mTargetAspectRatio == 0.0F) {
            this.mTargetAspectRatio = (drawableWidth / drawableHeight);
        }

        setupCropBounds();
        setupInitialImagePosition(drawableWidth, drawableHeight);
        setImageMatrix(this.mCurrentImageMatrix);

        if (this.mTransformImageListener != null) {
            this.mTransformImageListener.onScale(getCurrentScale());
            this.mTransformImageListener.onRotate(getCurrentAngle());
        }
    }

    protected boolean isImageWrapCropBounds() {
        return isImageWrapCropBounds(this.mCurrentImageCorners);
    }

    protected boolean isImageWrapCropBounds(float[] imageCorners) {
        this.mTempMatrix.reset();
        this.mTempMatrix.setRotate(-getCurrentAngle());

        float[] unrotatedImageCorners = Arrays.copyOf(imageCorners, imageCorners.length);
        this.mTempMatrix.mapPoints(unrotatedImageCorners);

        float[] unrotatedCropBoundsCorners = RectUtils.getCornersFromRect(this.mCropRect);
        this.mTempMatrix.mapPoints(unrotatedCropBoundsCorners);

        return RectUtils.trapToRect(unrotatedImageCorners).contains(RectUtils.trapToRect(unrotatedCropBoundsCorners));
    }

    protected void zoomImageToPosition(float scale, float centerX, float centerY, long durationMs) {
        if (scale > getMaxScale()) {
            scale = getMaxScale();
        }

        float oldScale = getCurrentScale();
        float deltaScale = scale - oldScale;

        post(this.mZoomImageToPositionRunnable = new ZoomImageToPosition(this, durationMs, oldScale, deltaScale, centerX, centerY));
    }

    private void setupInitialImagePosition(float drawableWidth, float drawableHeight) {
        float cropRectWidth = this.mCropRect.width();
        float cropRectHeight = this.mCropRect.height();

        float widthScale = cropRectWidth / drawableWidth;
        float heightScale = cropRectHeight / drawableHeight;

        this.mMinScale = Math.max(widthScale, heightScale);
        this.mMaxScale = (this.mMinScale * this.mMaxScaleMultiplier);

        float tw = (cropRectWidth - drawableWidth * this.mMinScale) / 2.0F + this.mCropRect.left;
        float th = (cropRectHeight - drawableHeight * this.mMinScale) / 2.0F + this.mCropRect.top;

        this.mCurrentImageMatrix.reset();
        this.mCurrentImageMatrix.postScale(this.mMinScale, this.mMinScale);
        this.mCurrentImageMatrix.postTranslate(tw, th);
    }

    protected void processStyledAttributes(TypedArray a) {
        float targetAspectRatioX = Math.abs(a.getFloat(R.styleable.ucrop_UCropView_ucrop_aspect_ratio_x, 0.0F));
        float targetAspectRatioY = Math.abs(a.getFloat(R.styleable.ucrop_UCropView_ucrop_aspect_ratio_y, 0.0F));

        if ((targetAspectRatioX == 0.0F) || (targetAspectRatioY == 0.0F))
            this.mTargetAspectRatio = 0.0F;
        else
            this.mTargetAspectRatio = (targetAspectRatioX / targetAspectRatioY);
    }

    private void setupCropBounds() {
        int height = (int) (this.mThisWidth / this.mTargetAspectRatio);
        if (height > this.mThisHeight) {
            int width = (int) (this.mThisHeight * this.mTargetAspectRatio);
            int halfDiff = (this.mThisWidth - width) / 2;
            this.mCropRect.set(halfDiff, 0.0F, width + halfDiff, this.mThisHeight);
        } else {
            int halfDiff = (this.mThisHeight - height) / 2;
            this.mCropRect.set(0.0F, halfDiff, this.mThisWidth, height + halfDiff);
        }

        if (this.mCropBoundsChangeListener != null)
            this.mCropBoundsChangeListener.onCropBoundsChangedRotate(this.mTargetAspectRatio);
    }

    private static class ZoomImageToPosition
            implements Runnable {
        private final WeakReference<CropImageView> mCropImageView;
        private final long mDurationMs;
        private final long mStartTime;
        private final float mOldScale;
        private final float mDeltaScale;
        private final float mDestX;
        private final float mDestY;

        public ZoomImageToPosition(CropImageView cropImageView, long durationMs, float oldScale, float deltaScale, float destX, float destY) {
            this.mCropImageView = new WeakReference(cropImageView);

            this.mStartTime = System.currentTimeMillis();
            this.mDurationMs = durationMs;
            this.mOldScale = oldScale;
            this.mDeltaScale = deltaScale;
            this.mDestX = destX;
            this.mDestY = destY;
        }

        public void run() {
            CropImageView cropImageView = (CropImageView) this.mCropImageView.get();
            if (cropImageView == null) {
                return;
            }

            long now = System.currentTimeMillis();
            float currentMs = (float) Math.min(this.mDurationMs, now - this.mStartTime);
            float newScale = CubicEasing.easeInOut(currentMs, 0.0F, this.mDeltaScale, (float) this.mDurationMs);

            if (currentMs < (float) this.mDurationMs) {
                cropImageView.zoomInImage(this.mOldScale + newScale, this.mDestX, this.mDestY);
                cropImageView.post(this);
            } else {
                cropImageView.setImageToWrapCropBounds();
            }
        }
    }

    private static class WrapCropBoundsRunnable
            implements Runnable {
        private final WeakReference<CropImageView> mCropImageView;
        private final long mDurationMs;
        private final long mStartTime;
        private final float mOldX;
        private final float mOldY;
        private final float mCenterDiffX;
        private final float mCenterDiffY;
        private final float mOldScale;
        private final float mDeltaScale;
        private final boolean mWillBeImageInBoundsAfterTranslate;

        public WrapCropBoundsRunnable(CropImageView cropImageView, long durationMs, float oldX, float oldY, float centerDiffX, float centerDiffY, float oldScale, float deltaScale, boolean willBeImageInBoundsAfterTranslate) {
            this.mCropImageView = new WeakReference(cropImageView);

            this.mDurationMs = durationMs;
            this.mStartTime = System.currentTimeMillis();
            this.mOldX = oldX;
            this.mOldY = oldY;
            this.mCenterDiffX = centerDiffX;
            this.mCenterDiffY = centerDiffY;
            this.mOldScale = oldScale;
            this.mDeltaScale = deltaScale;
            this.mWillBeImageInBoundsAfterTranslate = willBeImageInBoundsAfterTranslate;
        }

        public void run() {
            CropImageView cropImageView = (CropImageView) this.mCropImageView.get();
            if (cropImageView == null) {
                return;
            }

            long now = System.currentTimeMillis();
            float currentMs = (float) Math.min(this.mDurationMs, now - this.mStartTime);

            float newX = CubicEasing.easeOut(currentMs, 0.0F, this.mCenterDiffX, (float) this.mDurationMs);
            float newY = CubicEasing.easeOut(currentMs, 0.0F, this.mCenterDiffY, (float) this.mDurationMs);
            float newScale = CubicEasing.easeInOut(currentMs, 0.0F, this.mDeltaScale, (float) this.mDurationMs);

            if (currentMs < (float) this.mDurationMs) {
                cropImageView.postTranslate(newX - (cropImageView.mCurrentImageCenter[0] - this.mOldX), newY - (cropImageView.mCurrentImageCenter[1] - this.mOldY));
                if (!this.mWillBeImageInBoundsAfterTranslate) {
                    cropImageView.zoomInImage(this.mOldScale + newScale, cropImageView.mCropRect.centerX(), cropImageView.mCropRect.centerY());
                }
                if (!cropImageView.isImageWrapCropBounds())
                    cropImageView.post(this);
            }
        }
    }

    public static abstract interface CropBoundsChangeListener {
        public abstract void onCropBoundsChangedRotate(float paramFloat);
    }
}