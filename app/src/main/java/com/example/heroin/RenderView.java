package com.example.heroin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


public class RenderView extends View {

    private final Paint paint;

    public RenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.RED);
        canvas.drawRGB(255, 128, 128);
        canvas.drawLine(0, 0, 100, 100, paint);
    }
}
