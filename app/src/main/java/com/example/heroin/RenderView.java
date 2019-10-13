package com.example.heroin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedList;
import java.util.List;


public class RenderView extends View {

    public enum AnimationState {
        IDLE,
        SLEEP_TRANSITION,
        SLEEP
    }

    // 429 x 300
    // Idle: 264 - 305

    public static RenderView renderView;

    private final static int IDLE_FRAMES = 155;
    private final static int SLEEP_FRAMES = 305;
    private final static int SLEEP_TRANSITION_FRAME = 265;

    private final static int SPRITE_WIDTH = 429;
    private final static int SPRITE_HEIGHT = 300;

    private final Paint paint;
    private final List<Bitmap> bitmaps = new LinkedList<>();
    private final Bitmap idleSpritemap;
    private final Bitmap sleepSpritemap;
    private final Rect src;
    private final Rect dest;

    private int frame = 0;
    private int frameIndex = 0;

    public AnimationState currentState = AnimationState.SLEEP;
    private int[] idleFrames = {0, 1, 2, 3, 4, 5};
    private int[] transitionFrames = {6, 7, 8, 9, 10, 11, 12, 13};
    private int[] sleepFrames = {14, 15, 16, 17, 18, 19, 20};

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
        idleSpritemap = BitmapFactory.decodeResource(getResources(), R.drawable.idle);
        sleepSpritemap = BitmapFactory.decodeResource(getResources(), R.drawable.sleep);
        src = new Rect(0, 0, SPRITE_WIDTH, SPRITE_HEIGHT);
        dest = new Rect(0, 0, SPRITE_WIDTH, SPRITE_HEIGHT);
        renderView = this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        src.left = frame * SPRITE_WIDTH + 1;
        src.right = frame * SPRITE_WIDTH + SPRITE_WIDTH - 1;

        canvas.drawBitmap(idleSpritemap, src, dest, paint);

        switch (currentState) {
            case IDLE:
                frameIndex = (frameIndex + 1) % idleFrames.length;
                frame = idleFrames[frameIndex];
                break;
            case SLEEP_TRANSITION:
                frameIndex = (frameIndex + 1) % transitionFrames.length;
                frame = transitionFrames[frameIndex];
                break;
            case SLEEP:
                frameIndex = (frameIndex + 1) % sleepFrames.length;
                frame = sleepFrames[frameIndex];
                break;
        }
    }

    private Bitmap scaleBitmap(int id) {
        return BitmapFactory.decodeResource(getResources(), id);
    }
}
