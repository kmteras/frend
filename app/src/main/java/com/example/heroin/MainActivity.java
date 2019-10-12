package com.example.heroin;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button exitButton;
    private Intent service;
    private AlertDialog.Builder exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.startButton);
        exitButton = findViewById(R.id.exit);
        exitDialog = new AlertDialog.Builder(this);


        //startForegroundService(new Intent(MainActivity.this, Overlay.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service = new Intent(MainActivity.this, Overlay.class);
                startForegroundService(service);
            }
        });

        exitButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(service);

                finishAndRemoveTask();
            }
        }));

        findViewById(R.id.openSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("Test", "Pressed button:" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d("Test", "Back button pressed!");
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_MOVE_HOME) {
            Log.d("Test", "Home button pressed!");
            return false;
        } else {
            Log.d("service", "Pressed button: " + keyCode);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onUserLeaveHint() {
        Log.d("onUserLeaveHint", "User left the app");
        super.onUserLeaveHint();
    }

    @Override
    public void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
        Log.d("onPause", "- ON PAUSE -");
    }
}
