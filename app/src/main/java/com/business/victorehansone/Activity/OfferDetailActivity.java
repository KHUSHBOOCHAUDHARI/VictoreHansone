package com.business.victorehansone.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.business.victorehansone.R;
import com.business.victorehansone.databinding.ActivityOfferBinding;
import com.business.victorehansone.databinding.ActivityOfferDetailBinding;

public class OfferDetailActivity extends AppCompatActivity {
    private ActivityOfferDetailBinding binding;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(OfferDetailActivity.this,R.layout.activity_offer_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && OfferDetailActivity.this.isDestroyed()) {
            return;
        } else {
            Glide.with(getApplicationContext()).load(getIntent().getStringExtra("image")).into(binding.imageview);
        }
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        binding.titleTxt.setText(getIntent().getStringExtra("title"));
        binding.detail.setText(getIntent().getStringExtra("detail"));
        fullscreen();
    }
    //Full Screen
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
}