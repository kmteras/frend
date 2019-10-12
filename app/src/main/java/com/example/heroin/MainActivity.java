package com.example.heroin;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.startButton);

        startForegroundService(new Intent(MainActivity.this, Overlay.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForegroundService(new Intent(MainActivity.this, Overlay.class));

            }
        });
    }
    @Override public boolean onKeyDown ( int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d("Test", "Back button pressed!");
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_MOVE_HOME) {
            Log.d("Test", "Home button pressed!");
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onUserLeaveHint()
    {
        Log.d("onUserLeaveHint","User left the app");
        super.onUserLeaveHint();
    }
}
