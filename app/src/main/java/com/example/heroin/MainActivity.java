package com.example.heroin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Intent service;

    private static final String correctPin = "1111";

    private int PIN_RESULT_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_main);

        Button exitButton = findViewById(R.id.exit);

        service = new Intent(MainActivity.this, Overlay.class);
        startForegroundService(service);

        Executor executor = Executors.newSingleThreadExecutor();

        exitButton.setOnClickListener((v -> {
            Darkness.modal = true;

            BiometricPrompt biometricPrompt = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                biometricPrompt = new BiometricPrompt.Builder(this)
                        .setTitle("Use finger to close app")
                        .setSubtitle("Please use it")
                        .setNegativeButton("Cancel", executor, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).build();

                biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        stopService(service);
                        finishAffinity();
                        finishAndRemoveTask();
                    }
                });
            } else {
                KeyguardManager mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                if (mKeyguardManager == null) {
                    stopService(service);
                    finishAffinity();
                    finishAndRemoveTask();
                    return;
                }
                Intent intent = mKeyguardManager
                        .createConfirmDeviceCredentialIntent(
                                "Unlock to close",
                                "Please input PIN code");

                startActivityForResult(intent, 111);
            }
        }));


        RecyclerView recyclerView = findViewById(R.id.RView);
        RAdapter radapter = new RAdapter(this);
        recyclerView.setAdapter(radapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(30), true));
    }

    @Override
    protected void onUserLeaveHint() {
        Log.d("onUserLeaveHint", "User left the app");
        super.onUserLeaveHint();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111) {
            if (resultCode == RESULT_OK) {
                stopService(service);
                finishAffinity();
                finishAndRemoveTask();
            }
        }
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
