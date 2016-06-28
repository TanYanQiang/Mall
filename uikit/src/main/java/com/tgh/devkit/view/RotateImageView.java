package com.tgh.devkit.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tgh.devkit.core.utils.WeakRefHandler;

/**
 * 旋转的ImageView，可以记住旋转的位置
 * Created by albert on 16/1/21.
 */
public class RotateImageView extends ImageView {

    private static final int DURATION  = 20000;
    private static final int INTERVAL  = 10;
    private static final int MSG_RORATE = 0x9001;

    private static float mDegree = 0;
    private _Handler mHandler = new _Handler(this);
    private boolean mRunning;
    private boolean mStarted;
    private boolean mUserPresent = true;
    private boolean mVisible;
    private int mDuration = DURATION;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                mUserPresent = false;
                updateRunning();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                mUserPresent = true;
                updateRunning();
            }
        }
    };


    private void updateRunning() {
        boolean running = mVisible && mStarted && mUserPresent;
        if (running != mRunning) {
            if (running) {
                mHandler.sendEmptyMessageDelayed(MSG_RORATE, INTERVAL);
            } else {
                mHandler.removeMessages(MSG_RORATE);
            }
            mRunning = running;
        }
    }

    public RotateImageView(Context context) {
        super(context);
    }

    public RotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void startRotating() {
        mStarted = true;
        updateRunning();
    }

    public void stopRotating(){
        mStarted = false;
        updateRunning();
    }

    public void resetRotateDegree(){
        mDegree = 0;
        invalidate();
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float cx = (getMeasuredWidth()-getPaddingLeft()-getPaddingRight())/2f;
        float cy = (getMeasuredHeight()-getPaddingTop()-getPaddingBottom())/2f;
        canvas.rotate(mDegree,cx,cy);
        super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        getContext().registerReceiver(mReceiver, filter, null, mHandler);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        getContext().unregisterReceiver(mReceiver);
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility==VISIBLE;
        updateRunning();
    }

    private static final class _Handler extends WeakRefHandler<RotateImageView>{

        public _Handler(RotateImageView rotateImageView) {
            super(rotateImageView);
        }

        @Override
        protected void handleMessage(RotateImageView rotateImageView, Message msg) {
            if (msg.what == RotateImageView.MSG_RORATE){
                rotateImageView.onRotate();
            }
        }
    }

    private void onRotate() {
        if (mRunning){
            mDegree += getDxDegree();
            invalidate();
            mHandler.sendEmptyMessageDelayed(MSG_RORATE, INTERVAL);
        }
    }

    private float getDxDegree(){
        return 360.0f*INTERVAL/mDuration;
    }
}
