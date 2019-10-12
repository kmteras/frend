package com.example.heroin;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // TODO: For each app we want to display in the launcher loop:
        //   - create button with image
        //   - for the button add onClick method that launches the app UNDERNEATH current one
        //   - AND changes app overlay view
//        button = findViewById(R.id.stopButton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO: Stop service
//            }
//        });

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = this.getPackageManager().queryIntentActivities( mainIntent, 0);
        pkgAppsList.forEach(r -> Log.d("Test", r.activityInfo.parentActivityName));








        ImageView chromeIcon = findViewById(R.id.chromeButton);
        chromeIcon.setImageDrawable(getActivityIcon(this, "com.android.chrome", "com.google.android.apps.chrome.Main"));

        chromeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForegroundService(new Intent(MainActivity.this, Overlay.class));
            }
        });
    }

    public static Drawable getActivityIcon(Context context, String packageName, String activityName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityName));
        ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);

        return resolveInfo.loadIcon(pm);
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

//        activityManager.moveTaskToFront(getTaskId(), 0);
        Log.d("onPause", "- ON PAUSE -");
    }
}
