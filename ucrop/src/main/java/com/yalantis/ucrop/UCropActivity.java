package com.yalantis.ucrop;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalantis.ucrop.util.BitmapLoadUtils;
import com.yalantis.ucrop.util.SelectedStateListDrawable;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;
import com.yalantis.ucrop.view.widget.AspectRatioTextView;
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView;

import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class UCropActivity extends Activity {
    public static final int DEFAULT_COMPRESS_QUALITY = 90;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    public static final int NONE = 0;
    public static final int SCALE = 1;
    public static final int ROTATE = 2;
    public static final int ALL = 3;
    private static final String TAG = "UCropActivity";
    private static final int TABS_COUNT = 3;
    private static final int SCALE_WIDGET_SENSITIVITY_COEFFICIENT = 15000;
    private static final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;
    private String mToolbarTitle;
    private int mActiveWidgetColor;
    private int mToolbarTextColor;
    private int mLogoColor;
    private UCropView mUCropView;
    private GestureCropImageView mGestureCropImageView;
    private OverlayView mOverlayView;
    private ViewGroup mWrapperStateAspectRatio;
    private ViewGroup mWrapperStateRotate;
    private ViewGroup mWrapperStateScale;
    private ViewGroup mLayoutAspectRatio;
    private ViewGroup mLayoutRotate;
    private ViewGroup mLayoutScale;
    private List<ViewGroup> mCropAspectRatioViews = new ArrayList();
    private TextView mTextViewRotateAngle;
    private TextView mTextViewScalePercent;
    private Uri mOutputUri;
    private Bitmap.CompressFormat mCompressFormat = DEFAULT_COMPRESS_FORMAT;
    private int mCompressQuality = 90;
    private int[] mAllowedGestures = {1, 2, 3};

    private TransformImageView.TransformImageListener mImageListener = new TransformImageView.TransformImageListener() {
        public void onRotate(float currentAngle) {
            UCropActivity.this.setAngleText(currentAngle);
        }

        public void onScale(float currentScale) {
            UCropActivity.this.setScaleText(currentScale);
        }

        public void onLoadComplete() {
            View ucropView = UCropActivity.this.findViewById(R.id.ucrop);
            Animation fadeInAnimation = AnimationUtils.loadAnimation(UCropActivity.this.getApplicationContext(), R.anim.ucrop_fade_in);
            fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    UCropActivity.this.mUCropView.setVisibility(View.VISIBLE);
                }

                public void onAnimationEnd(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }
            });
            ucropView.startAnimation(fadeInAnimation);
        }

        public void onLoadFailure(Exception e) {
            UCropActivity.this.setResultException(e);
            UCropActivity.this.finish();
        }
    };

    private final View.OnClickListener mStateClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!v.isSelected())
                UCropActivity.this.setWidgetState(v.getId());
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ucrop_activity_photobox);

        Intent intent = getIntent();

        setupViews(intent);
        setImageData(intent);
        setInitialState();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ucrop_menu_activity, menu);

        MenuItem next = menu.findItem(R.id.menu_crop);

        Drawable defaultIcon = next.getIcon();
        if (defaultIcon != null) {
            defaultIcon.mutate();
            defaultIcon.setColorFilter(this.mToolbarTextColor, PorterDuff.Mode.SRC_ATOP);
            next.setIcon(defaultIcon);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_crop)
            cropAndSaveImage();
        else if (item.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onStop() {
        super.onStop();
        if (this.mGestureCropImageView != null)
            this.mGestureCropImageView.cancelAllAnimations();
    }

    private void setImageData(Intent intent) {
        Uri inputUri = (Uri) intent.getParcelableExtra("com.yalantis.ucrop.InputUri");
        this.mOutputUri = ((Uri) intent.getParcelableExtra("com.yalantis.ucrop.OutputUri"));
        processOptions(intent);

        if ((inputUri != null) && (this.mOutputUri != null)) {
            try {
                this.mGestureCropImageView.setImageUri(inputUri);
            } catch (Exception e) {
                setResultException(e);
                finish();
            }
        } else {
            setResultException(new NullPointerException(getString(R.string.ucrop_error_input_data_is_absent)));
            finish();
        }

        if (intent.getBooleanExtra("com.yalantis.ucrop.AspectRatioSet", false)) {
            this.mWrapperStateAspectRatio.setVisibility(View.GONE);

            float aspectRatioX = intent.getFloatExtra("com.yalantis.ucrop.AspectRatioX", 0.0F);
            float aspectRatioY = intent.getFloatExtra("com.yalantis.ucrop.AspectRatioY", 0.0F);

            if ((aspectRatioX > 0.0F) && (aspectRatioY > 0.0F))
                this.mGestureCropImageView.setTargetAspectRatio(aspectRatioX / aspectRatioY);
            else {
                this.mGestureCropImageView.setTargetAspectRatio(0.0F);
            }
        }

        if (intent.getBooleanExtra("com.yalantis.ucrop.MaxSizeSet", false)) {
            int maxSizeX = intent.getIntExtra("com.yalantis.ucrop.MaxSizeX", 0);
            int maxSizeY = intent.getIntExtra("com.yalantis.ucrop.MaxSizeY", 0);

            if ((maxSizeX > 0) && (maxSizeY > 0)) {
                this.mGestureCropImageView.setMaxResultImageSizeX(maxSizeX);
                this.mGestureCropImageView.setMaxResultImageSizeY(maxSizeY);
            } else {
                Log.w("UCropActivity", "EXTRA_MAX_SIZE_X and EXTRA_MAX_SIZE_Y must be greater than 0");
            }
        }
    }

    private void processOptions(Intent intent) {
        String compressionFormatName = intent.getStringExtra("com.yalantis.ucrop.CompressionFormatName");
        Bitmap.CompressFormat compressFormat = null;
        if (!TextUtils.isEmpty(compressionFormatName)) {
            compressFormat = Bitmap.CompressFormat.valueOf(compressionFormatName);
        }
        this.mCompressFormat = (compressFormat == null ? DEFAULT_COMPRESS_FORMAT : compressFormat);

        this.mCompressQuality = intent.getIntExtra("com.yalantis.ucrop.CompressionQuality", 90);

        int[] allowedGestures = intent.getIntArrayExtra("com.yalantis.ucrop.AllowedGestures");
        if ((allowedGestures != null) && (allowedGestures.length == 3)) {
            this.mAllowedGestures = allowedGestures;
        }

        this.mGestureCropImageView.setMaxBitmapSize(intent.getIntExtra("com.yalantis.ucrop.MaxBitmapSize", 0));
        this.mGestureCropImageView.setMaxScaleMultiplier(intent.getFloatExtra("com.yalantis.ucrop.MaxScaleMultiplier", 10.0F));
        this.mGestureCropImageView.setImageToWrapCropBoundsAnimDuration(intent.getIntExtra("com.yalantis.ucrop.ImageToCropBoundsAnimDuration", 500));

        this.mOverlayView.setDimmedColor(intent.getIntExtra("com.yalantis.ucrop.DimmedLayerColor", getResources().getColor(R.color.ucrop_color_default_dimmed)));
        this.mOverlayView.setOvalDimmedLayer(intent.getBooleanExtra("com.yalantis.ucrop.OvalDimmedLayer", false));

        this.mOverlayView.setShowCropFrame(intent.getBooleanExtra("com.yalantis.ucrop.ShowCropFrame", true));
        this.mOverlayView.setCropFrameColor(intent.getIntExtra("com.yalantis.ucrop.CropFrameColor", getResources().getColor(R.color.ucrop_color_default_crop_frame)));
        this.mOverlayView.setCropFrameStrokeWidth(intent.getIntExtra("com.yalantis.ucrop.CropFrameStrokeWidth", getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_frame_stoke_width)));

        this.mOverlayView.setShowCropGrid(intent.getBooleanExtra("com.yalantis.ucrop.ShowCropGrid", true));
        this.mOverlayView.setCropGridRowCount(intent.getIntExtra("com.yalantis.ucrop.CropGridRowCount", 2));
        this.mOverlayView.setCropGridColumnCount(intent.getIntExtra("com.yalantis.ucrop.CropGridColumnCount", 2));
        this.mOverlayView.setCropGridColor(intent.getIntExtra("com.yalantis.ucrop.CropGridColor", getResources().getColor(R.color.ucrop_color_default_crop_grid)));
        this.mOverlayView.setCropGridStrokeWidth(intent.getIntExtra("com.yalantis.ucrop.CropGridStrokeWidth", getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_grid_stoke_width)));
    }

    private void setupViews(Intent intent) {
        this.mActiveWidgetColor = intent.getIntExtra("com.yalantis.ucrop.UcropColorWidgetActive", getResources().getColor(R.color.ucrop_color_widget_active));
        this.mToolbarTextColor = intent.getIntExtra("com.yalantis.ucrop.UcropToolbarTitleColor", getResources().getColor(R.color.ucrop_color_title));
        this.mToolbarTitle = intent.getStringExtra("com.yalantis.ucrop.UcropToolbarTitleText");
        this.mToolbarTitle = (!TextUtils.isEmpty(this.mToolbarTitle) ? this.mToolbarTitle : getResources().getString(R.string.ucrop_label_edit_photo));
        this.mLogoColor = intent.getIntExtra("com.yalantis.ucrop.UcropLogoColor", getResources().getColor(R.color.ucrop_color_default_logo));

        ActionBar actionBar = getActionBar();
        if (actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        initiateRootViews();
        setupAspectRatioWidget();
        setupRotateWidget();
        setupScaleWidget();
        setupStatesWrapper();
    }



    private void initiateRootViews() {
        this.mUCropView = ((UCropView) findViewById(R.id.ucrop));
        this.mGestureCropImageView = this.mUCropView.getCropImageView();
        this.mOverlayView = this.mUCropView.getOverlayView();

        this.mGestureCropImageView.setTransformImageListener(this.mImageListener);

        this.mWrapperStateAspectRatio = ((ViewGroup) findViewById(R.id.state_aspect_ratio));
        this.mWrapperStateAspectRatio.setOnClickListener(this.mStateClickListener);
        this.mWrapperStateRotate = ((ViewGroup) findViewById(R.id.state_rotate));
        this.mWrapperStateRotate.setOnClickListener(this.mStateClickListener);
        this.mWrapperStateScale = ((ViewGroup) findViewById(R.id.state_scale));
        this.mWrapperStateScale.setOnClickListener(this.mStateClickListener);

        this.mLayoutAspectRatio = ((ViewGroup) findViewById(R.id.layout_aspect_ratio));
        this.mLayoutRotate = ((ViewGroup) findViewById(R.id.layout_rotate_wheel));
        this.mLayoutScale = ((ViewGroup) findViewById(R.id.layout_scale_wheel));

        ((ImageView) findViewById(R.id.image_view_logo)).setColorFilter(this.mLogoColor, PorterDuff.Mode.SRC_ATOP);
    }

    private void setupStatesWrapper() {
        ImageView stateScaleImageView = (ImageView) findViewById(R.id.image_view_state_scale);
        ImageView stateRotateImageView = (ImageView) findViewById(R.id.image_view_state_rotate);
        ImageView stateAspectRatioImageView = (ImageView) findViewById(R.id.image_view_state_aspect_ratio);

        stateScaleImageView.setImageDrawable(new SelectedStateListDrawable(stateScaleImageView.getDrawable(), this.mActiveWidgetColor));
        stateRotateImageView.setImageDrawable(new SelectedStateListDrawable(stateRotateImageView.getDrawable(), this.mActiveWidgetColor));
        stateAspectRatioImageView.setImageDrawable(new SelectedStateListDrawable(stateAspectRatioImageView.getDrawable(), this.mActiveWidgetColor));
    }

    @TargetApi(21)
    private void setStatusBarColor(int color) {
        if ((Build.VERSION.SDK_INT >= 21) &&
                (getWindow() != null))
            getWindow().setStatusBarColor(color);
    }

    private void setupAspectRatioWidget() {
        ((AspectRatioTextView) ((ViewGroup) findViewById(R.id.crop_aspect_ratio_1_1)).getChildAt(0)).setActiveColor(this.mActiveWidgetColor);
        ((AspectRatioTextView) ((ViewGroup) findViewById(R.id.crop_aspect_ratio_3_4)).getChildAt(0)).setActiveColor(this.mActiveWidgetColor);
        ((AspectRatioTextView) ((ViewGroup) findViewById(R.id.crop_aspect_ratio_original)).getChildAt(0)).setActiveColor(this.mActiveWidgetColor);
        ((AspectRatioTextView) ((ViewGroup) findViewById(R.id.crop_aspect_ratio_3_2)).getChildAt(0)).setActiveColor(this.mActiveWidgetColor);
        ((AspectRatioTextView) ((ViewGroup) findViewById(R.id.crop_aspect_ratio_16_9)).getChildAt(0)).setActiveColor(this.mActiveWidgetColor);

        this.mCropAspectRatioViews.add((ViewGroup) findViewById(R.id.crop_aspect_ratio_1_1));
        this.mCropAspectRatioViews.add((ViewGroup) findViewById(R.id.crop_aspect_ratio_3_4));
        this.mCropAspectRatioViews.add((ViewGroup) findViewById(R.id.crop_aspect_ratio_original));
        this.mCropAspectRatioViews.add((ViewGroup) findViewById(R.id.crop_aspect_ratio_3_2));
        this.mCropAspectRatioViews.add((ViewGroup) findViewById(R.id.crop_aspect_ratio_16_9));
        ((ViewGroup) this.mCropAspectRatioViews.get(2)).setSelected(true);

        for (ViewGroup cropAspectRatioView : this.mCropAspectRatioViews)
            cropAspectRatioView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UCropActivity.this.mGestureCropImageView.setTargetAspectRatio(((AspectRatioTextView) ((ViewGroup) v).getChildAt(0)).getAspectRatio(v.isSelected()));

                    UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
                    if (!v.isSelected())
                        for (ViewGroup cropAspectRatioView : UCropActivity.this.mCropAspectRatioViews)
                            cropAspectRatioView.setSelected(cropAspectRatioView == v);
                }
            });
    }

    private void setupRotateWidget() {
        this.mTextViewRotateAngle = ((TextView) findViewById(R.id.text_view_rotate));
        ((HorizontalProgressWheelView) findViewById(R.id.rotate_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
            public void onScroll(float delta, float totalDistance) {
                UCropActivity.this.mGestureCropImageView.postRotate(delta / 42.0F);
            }

            public void onScrollEnd() {
                UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
            }

            public void onScrollStart() {
                UCropActivity.this.mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView) findViewById(R.id.rotate_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);

        findViewById(R.id.wrapper_reset_rotate).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UCropActivity.this.resetRotation();
            }
        });
        findViewById(R.id.wrapper_rotate_by_angle).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UCropActivity.this.rotateByAngle(90);
            }
        });
    }

    private void setupScaleWidget() {
        this.mTextViewScalePercent = ((TextView) findViewById(R.id.text_view_scale));
        ((HorizontalProgressWheelView) findViewById(R.id.scale_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
            public void onScroll(float delta, float totalDistance) {
                if (delta > 0.0F) {
                    UCropActivity.this.mGestureCropImageView.zoomInImage(UCropActivity.this.mGestureCropImageView.getCurrentScale() + delta * ((UCropActivity.this.mGestureCropImageView.getMaxScale() - UCropActivity.this.mGestureCropImageView.getMinScale()) / 15000.0F));
                } else
                    UCropActivity.this.mGestureCropImageView.zoomOutImage(UCropActivity.this.mGestureCropImageView.getCurrentScale() + delta * ((UCropActivity.this.mGestureCropImageView.getMaxScale() - UCropActivity.this.mGestureCropImageView.getMinScale()) / 15000.0F));
            }

            public void onScrollEnd() {
                UCropActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
            }

            public void onScrollStart() {
                UCropActivity.this.mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView) findViewById(R.id.scale_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
    }

    private void setAngleText(float angle) {
        if (this.mTextViewRotateAngle != null)
            this.mTextViewRotateAngle.setText(String.format("%.1f°", new Object[]{Float.valueOf(angle)}));
    }

    private void setScaleText(float scale) {
        if (this.mTextViewScalePercent != null)
            this.mTextViewScalePercent.setText(String.format("%d%%", new Object[]{Integer.valueOf((int) (scale * 100.0F))}));
    }

    private void resetRotation() {
        this.mGestureCropImageView.postRotate(-this.mGestureCropImageView.getCurrentAngle());
        this.mGestureCropImageView.setImageToWrapCropBounds();
    }

    private void rotateByAngle(int angle) {
        this.mGestureCropImageView.postRotate(angle);
        this.mGestureCropImageView.setImageToWrapCropBounds();
    }

    private void setInitialState() {
        if (this.mWrapperStateAspectRatio.getVisibility() == View.VISIBLE)
            setWidgetState(R.id.state_aspect_ratio);
        else
            setWidgetState(R.id.state_scale);
    }

    private void setWidgetState(int stateViewId) {
        this.mWrapperStateAspectRatio.setSelected(stateViewId == R.id.state_aspect_ratio);
        this.mWrapperStateRotate.setSelected(stateViewId == R.id.state_rotate);
        this.mWrapperStateScale.setSelected(stateViewId == R.id.state_scale);

        this.mLayoutAspectRatio.setVisibility(stateViewId == R.id.state_aspect_ratio ? View.VISIBLE : View.GONE);
        this.mLayoutRotate.setVisibility(stateViewId == R.id.state_rotate ? View.VISIBLE : View.GONE);
        this.mLayoutScale.setVisibility(stateViewId == R.id.state_scale ? View.VISIBLE : View.GONE);

        if (stateViewId == R.id.state_scale)
            setAllowedGestures(0);
        else if (stateViewId == R.id.state_rotate)
            setAllowedGestures(1);
        else
            setAllowedGestures(2);
    }

    private void setAllowedGestures(int tab) {
        this.mGestureCropImageView.setScaleEnabled((this.mAllowedGestures[tab] == 3) || (this.mAllowedGestures[tab] == 1));
        this.mGestureCropImageView.setRotateEnabled((this.mAllowedGestures[tab] == 3) || (this.mAllowedGestures[tab] == 2));
    }

    private void cropAndSaveImage() {
        OutputStream outputStream = null;
        try {
            Bitmap croppedBitmap = this.mGestureCropImageView.cropImage();
            if (croppedBitmap != null) {
                outputStream = getContentResolver().openOutputStream(this.mOutputUri);
                croppedBitmap.compress(this.mCompressFormat, this.mCompressQuality, outputStream);
                croppedBitmap.recycle();

                setResultUri(this.mOutputUri, this.mGestureCropImageView.getTargetAspectRatio());
                finish();
            } else {
                setResultException(new NullPointerException("CropImageView.cropImage() returned null."));
            }
        } catch (Exception e) {
            setResultException(e);
            finish();
        } finally {
            BitmapLoadUtils.close(outputStream);
        }
    }

    private void setResultUri(Uri uri, float resultAspectRatio) {
        setResult(-1, new Intent().putExtra("com.yalantis.ucrop.OutputUri", uri).putExtra("com.yalantis.ucrop.CropAspectRatio", resultAspectRatio));
    }

    private void setResultException(Throwable throwable) {
        setResult(96, new Intent().putExtra("com.yalantis.ucrop.Error", throwable));
    }

    @Retention(RetentionPolicy.SOURCE)
    public static @interface GestureTypes {
    }
}