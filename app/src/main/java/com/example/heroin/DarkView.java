package com.example.heroin;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class DarkView extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dark);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Toast.makeText(getBaseContext(), "onTouchEvent", Toast.LENGTH_LONG).show();

        return super.onTouchEvent(event);
    }
}
