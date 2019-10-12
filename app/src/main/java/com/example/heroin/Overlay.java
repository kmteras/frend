package com.example.heroin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class Overlay extends Service {
    private View mView;
    private View darkView;
    private WindowManager wm;
    private Handler handler;
    private Runnable updateRunnable;

    @Override
    public void onCreate() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
                PixelFormat.TRANSLUCENT);

        final WindowManager.LayoutParams darkParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        params.gravity = Gravity.BOTTOM | Gravity.START;
        params.x = 0;
        params.y = 0;

        mView = LayoutInflater.from(this).inflate(R.layout.overlay, null);
        darkView = LayoutInflater.from(this).inflate(R.layout.dark, null);

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(darkView, darkParams);
        wm.addView(mView, params);

        mView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float Xdiff = Math.round(event.getRawX() - initialTouchX);
                        float Ydiff = Math.round(event.getRawY() - initialTouchY);


                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) Xdiff;
                        params.y = initialY - (int) Ydiff;

                        //Update the layout with new X & Y coordinates
                        wm.updateViewLayout(mView, params);


                        return true;
                }
                return false;
            }
        });

        String CHANNEL_ID = "my_channel_01";

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Frend")
                .setContentText("Go back to your friend")
                .setSmallIcon(R.drawable.heroicon)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        updateRunnable = () -> {
            RenderView.renderView.invalidate();
            Darkness.darkness.invalidate();
            handler.postDelayed(updateRunnable, 1000);

        };
        handler = new Handler();
        handler.postDelayed(updateRunnable, 1000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wm.removeViewImmediate(mView);
    }
}
