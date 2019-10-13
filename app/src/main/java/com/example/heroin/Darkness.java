package com.example.heroin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import java.time.Instant;


public class Darkness extends View {
    public static Darkness darkness;

    private static final int SLEEP_TIME = 30; // in seconds
    private static final int WAKE_TIME = 90; // in seconds

    private static Instant resetTime = Instant.now();
    public static boolean sleeping = false;
    public static int opacity = 0;
    public static boolean modal = false;

    public Darkness(Context context, AttributeSet attrs) {
        super(context, attrs);
        darkness = this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!modal) {
            canvas.drawColor(Color.argb(opacity, 0, 0, 0));
        }
        updateOpacity();
    }

    private void updateOpacity() {
        if (sleeping) {
            if (resetRequired()) {
                sleeping = false;
                resetTime = Instant.now();
                opacity = 0;
            } else if (wakingUp()) {
                reduceOpacity();
            }
            RenderView.renderView.currentState = RenderView.AnimationState.SLEEP;
        } else {
            if (sleepRequired()) {
                sleeping = true;
                opacity = 255;
            } else if (goingToSleep()) {
                increaseOpacity();
                RenderView.renderView.currentState = RenderView.AnimationState.SLEEP_TRANSITION;
            }
            RenderView.renderView.currentState = RenderView.AnimationState.IDLE;
        }
    }

    private void increaseOpacity() {
        // TODO: Modify to have more gradual darkening?
        opacity = Math.min (255, opacity + 1);
    }

    private void reduceOpacity() {
        // TODO: Modify to have more gradual lightening?
        opacity = Math.max(0, opacity-2);
    }

    private boolean goingToSleep() {
        long passedTime = getPassedTime();
        return passedTime > WAKE_TIME * 0.9; // TODO: Move multiplier to const ?
    }

    private boolean sleepRequired() {
        return getPassedTime() > WAKE_TIME;
    }

    private boolean wakingUp() {
        // is time close to end of full cycle time
        return getPassedTime() > getFullCycleTime() * 0.9; // TODO: Move multiplier to const ?
    }

    private int getFullCycleTime() {
        return WAKE_TIME + SLEEP_TIME;
    }

    private boolean resetRequired() {
        // Is the passed time since last reset greater than full cycle time.
        return getPassedTime() > (getFullCycleTime());
    }

    private long getPassedTime() {
        return Instant.now().getEpochSecond() - resetTime.getEpochSecond();
    }

}
