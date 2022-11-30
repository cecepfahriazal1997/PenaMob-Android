package com.nita.penamob.activity;

import android.os.Bundle;
import android.os.Handler;

import com.nita.penamob.R;

public class SplashScreen extends BaseController {
    private static final int SPLASH_TIME_OUT  = 3000;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        initial();
    }

    private void initial() {
        handler             = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (helper.getSession("key") != null) {
                    checkIsLogin();
                    finish();
                } else {
                    helper.startIntent(Login.class, true, null);
                }
            }
        }, SPLASH_TIME_OUT);
    }

    public void onStop()
    {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    public void onDestroy()
    {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}