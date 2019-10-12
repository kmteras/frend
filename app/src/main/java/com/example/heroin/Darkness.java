package com.example.heroin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;


public class Darkness extends View {
    public static Darkness darkness;

    public static int opacity = 0;

    public Darkness(Context context, AttributeSet attrs) {
        super(context, attrs);
        darkness = this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.argb(opacity, 0, 0, 0));
        opacity = (opacity + 255 / 10) % 255;
    }
}
