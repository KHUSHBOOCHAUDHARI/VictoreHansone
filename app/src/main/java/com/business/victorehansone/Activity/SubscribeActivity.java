package com.business.victorehansone.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.business.victorehansone.Adapter.SubscriptiobAdapter;
import com.business.victorehansone.Model.SubscriptionModel;
import com.business.victorehansone.R;
import com.business.victorehansone.databinding.ActivitySubscribeBinding;

import java.util.ArrayList;
import java.util.List;

public class SubscribeActivity extends AppCompatActivity {
    private ActivitySubscribeBinding binding;
    List<SubscriptionModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(SubscribeActivity.this,R.layout.activity_subscribe);
        list = getData();

        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });



        SubscriptiobAdapter adapter = new SubscriptiobAdapter(list,SubscribeActivity.this);
        binding.subscribeRecyclerview.setHasFixedSize(true);
        binding.subscribeRecyclerview.setLayoutManager(new GridLayoutManager(this,2));
        binding.subscribeRecyclerview.setAdapter(adapter);
        fullscreen();
    }
    private List<SubscriptionModel> getData()
    {
        List<SubscriptionModel> list = new ArrayList<>();
        list.add(new SubscriptionModel("1", "Monthly","Plan" ,"$5", "This is a recurring one month paymentof a small fee."));
        list.add(new SubscriptionModel("2", "Yearly","Plan" ,"$50", "It is a one year payment of a small fee. Just have a test."));
        list.add( new SubscriptionModel("3", "One Time", "One Month", "$5", "This subscription offers a one-time, one-month payment for those who want to try out the subscription and for those who would like to pay by the month."));
        list.add( new SubscriptionModel("4","One Time","One Year","$50","This subscription offers a one-time annual payment for those who want to try out the subscription and for those who would like to pay by the year."));
        list.add(new SubscriptionModel("5", "Free","Subscription", "Free","This subscription offers email notification whenever a new article is published but without access to the “VDH Ultra” content."));

        return list;
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