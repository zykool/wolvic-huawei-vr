package com.igalia.wolvic.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import com.igalia.wolvic.R;

public class PitchControl extends FrameLayout implements SeekBar.OnSeekBarChangeListener {
    private SeekBar mSeekBar;
    private float mPitch;
    private boolean mTouching;
    private Delegate mDelegate;

    // Pitch range: -90 to +90 degrees
    private static final float MIN_PITCH = -90.0f;
    private static final float MAX_PITCH = 90.0f;
    private static final float DEFAULT_PITCH = 0.0f;

    public PitchControl(Context context) {
        super(context);
        initialize();
    }

    public PitchControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public PitchControl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public PitchControl(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initialize() {
        inflate(getContext(), R.layout.pitch_control, this);
        mSeekBar = findViewById(R.id.pitchSeekBar);
        // Set progress to middle (0 degrees) by default
        mSeekBar.setProgress(mSeekBar.getMax() / 2);
        mSeekBar.setOnSeekBarChangeListener(this);
        mSeekBar.setOnTouchListener((v, event) -> {

            if ((event.getAction() == MotionEvent.ACTION_UP) || (event.getAction() == MotionEvent.ACTION_CANCEL)) {
                if ((event.getAction() == MotionEvent.ACTION_CANCEL) && (mDelegate != null)) {
                    mDelegate.onSeekBarActionCancelled();
                }

                return true;
            }
            return false;
        });
        mPitch = DEFAULT_PITCH;
    }

    public void setDelegate(Delegate aDelegate) {
        mDelegate = aDelegate;
    }

    public void setPitch(float aPitch) {
        mPitch = aPitch;
        if (!mTouching) {
            updateProgress();
        }
    }

    public float getPitch() {
        return mPitch;
    }

    private void updateProgress() {
        // Convert pitch (-90 to +90) to progress (0 to max)
        // 0 pitch = max/2, -90 pitch = 0, +90 pitch = max
        int progress = (int) ((mPitch - MIN_PITCH) / (MAX_PITCH - MIN_PITCH) * mSeekBar.getMax());
        mSeekBar.setProgress(progress);
    }

    private float progressToPitch(int progress) {
        // Convert progress (0 to max) to pitch (-90 to +90)
        return MIN_PITCH + (progress / (float) mSeekBar.getMax()) * (MAX_PITCH - MIN_PITCH);
    }

    // SeekBar.OnSeekBarChangeListener
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mPitch = progressToPitch(progress);
            if (mDelegate != null) {
                mDelegate.onPitchChange(mPitch);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mTouching = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mTouching = false;
    }

    public interface Delegate {
        void onPitchChange(float aPitch);

        void onSeekBarActionCancelled();
    }
}