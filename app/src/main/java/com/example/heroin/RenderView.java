package com.example.heroin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedList;
import java.util.List;


public class RenderView extends View {
    private final static int SCALE_FACTOR = 4;
    private final static int FRAMES = 10;
    private final int SPRITE_WIDTH;
    private final int SPRITE_HEIGHT;

    private final Handler handler;
    private final Runnable drawingRunnable;
    private final Paint paint;
    private final List<Bitmap> bitmaps = new LinkedList<>();
    private final Rect src;
    private final Rect dest;

    private int frame = 0;

    public RenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(false);
        paint.setFilterBitmap(false);
        paint.setDither(false);
        bitmaps.add(scaleBitmap(R.drawable.spritesheet_color));
        bitmaps.add(scaleBitmap(R.drawable.spritesheet_shading));
        bitmaps.add(scaleBitmap(R.drawable.spritesheet_borders));
        bitmaps.add(scaleBitmap(R.drawable.tailsheet_color));
        bitmaps.add(scaleBitmap(R.drawable.tailsheet_shading));
        bitmaps.add(scaleBitmap(R.drawable.tailsheet_borders));
        SPRITE_WIDTH = bitmaps.get(0).getWidth() / FRAMES;
        SPRITE_HEIGHT = bitmaps.get(0).getHeight();
        src = new Rect(0, 0, SPRITE_WIDTH, SPRITE_HEIGHT);
        dest = new Rect(0, 0, SPRITE_WIDTH, SPRITE_HEIGHT);
        drawingRunnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        handler = new Handler();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        src.left = frame * SPRITE_WIDTH;
        src.right = frame * SPRITE_WIDTH + SPRITE_WIDTH;

        for (Bitmap bitmap : bitmaps) {
            canvas.drawBitmap(bitmap, src, dest, paint);
        }

        handler.postDelayed(drawingRunnable, 1000);
        frame = (frame + 1) % FRAMES;
    }

    private Bitmap scaleBitmap(int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
        return Bitmap.createScaledBitmap(bitmap,
                bitmap.getWidth() * SCALE_FACTOR,
                bitmap.getHeight() * SCALE_FACTOR, false);
    }
}
