package com.example.heroin;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {

    public static Intent service;

    private int PIN_RESULT_CODE = 111;
    private NotificationManager notificationManager;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE);
        preferences.edit().putBoolean(WindowChangeDetectingService.ACTIVE, true);
        hideActionBar();
        setContentView(R.layout.activity_main);
        startService();
        configureExitButton();
        createApplicationsList();
        setNotification();
    }

    private void startService() {
        if (service == null) {
            // Start service!
            service = new Intent(MainActivity.this, Overlay.class);
            startForegroundService(service);
        }
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void createApplicationsList() {
        RecyclerView recyclerView = findViewById(R.id.RView);
        RAdapter radapter = new RAdapter(this);
        recyclerView.setAdapter(radapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(30), true));
    }

    private void configureExitButton() {
        Button exitButton = findViewById(R.id.exit);

        exitButton.setOnClickListener((v -> {
            Darkness.modal = true;

            KeyguardManager mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (mKeyguardManager == null) {
                closeApp();
                return;
            }
            Intent intent = mKeyguardManager
                    .createConfirmDeviceCredentialIntent(
                            "Unlock to close",
                            "Please input PIN code");

            startActivityForResult(intent, PIN_RESULT_CODE);
        }));
    }

    private void setNotification() {
        String CHANNEL_ID = "my_channel_01";

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = getActivity(this, 0, notificationIntent, 0);

        Intent settingsIntent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Frend")
                .setContentText("Go back to your friend")
                .setSmallIcon(R.drawable.heroicon)
                .setContentIntent(pendingIntent)
                .addAction(
                        0, // TODO:  Need to change this?
                        "SETTINGS",
                        getActivity(this, 0, settingsIntent, 0)
                )
                .build();
        notificationManager.notify(1, notification);
    }

    @Override
    protected void onUserLeaveHint() {
        Log.d("onUserLeaveHint", "User left the app");
        super.onUserLeaveHint();
    }

    @Override
    protected void onDestroy() {
        Log.d("Test", "In onDestroy!  Closing app forceably!");
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TEST", "Got activity result and will check code");
        if (requestCode == PIN_RESULT_CODE) {
            Log.d("Test", "Correct code, will check result");
            if (resultCode == RESULT_OK) {
                Log.d("Test", "Correct result, closing app!");
                closeApp();
            }
        }
    }

    private void closeApp() {
        stopService(service);
        finishAffinity();
        finishAndRemoveTask();
        preferences.edit().putBoolean(WindowChangeDetectingService.ACTIVE, true);
        notificationManager.cancelAll();
        finish();
    }

    /**
     * USING BORROWED CODE FROM: https://www.androidhive.info/2016/05/android-working-with-card-view-and-recycler-view/?utm_source=recyclerview&utm_medium=site&utm_campaign=refer_article
     * <p>
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
