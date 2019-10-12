package com.example.heroin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

public class ClosePopup extends Activity {

    private PopupWindow popUp;
    private EditText pin;
    private Button cancel;
    private Button confirm;

    private static final String correctPin = "1111";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pin = findViewById(R.id.pin);
        popUp = new PopupWindow(this);

        cancel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Cancel button", "CLicked on cancel button");
            }
        }));

        confirm.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ClosePopup", "PIN input: " + pin.getText().toString());
            }
        }));
    }
}
