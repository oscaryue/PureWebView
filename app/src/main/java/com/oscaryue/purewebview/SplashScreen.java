package com.oscaryue.purewebview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

public class SplashScreen extends Activity {

    private static final long DELAY_MILLIS = 1000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        startTimer();
    }

    private void startTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launch();
            }
        }, DELAY_MILLIS);
    }

    private void launch() {
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);

        finish();
    }

}
