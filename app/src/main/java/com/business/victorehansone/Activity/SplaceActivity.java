package com.business.victorehansone.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Method;

public class SplaceActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;
    private PrefManager prefManager;
    Method method;
    LottieAnimationView lottieLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splace);
        fullscreen();
        method=new Method(SplaceActivity.this);
        prefManager = new PrefManager(this);

        lottieLogo=findViewById(R.id.lottieLogo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (prefManager.isFirstTimeLaunch()) {
                    launchHomeScreen();
                    finish();
                }
                else {
                    if (method.pref.getBoolean(method.pref_login, false)) {
                        Intent i = new Intent(SplaceActivity.this, MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                        finish();
                    } else {
                        Intent i = new Intent(SplaceActivity.this, LoginActivity.class);

                        startActivity(i);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                        finish();
                    }
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    public void fullscreen() {

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(SplaceActivity.this, LoginActivity.class));
        finish();
    }
}