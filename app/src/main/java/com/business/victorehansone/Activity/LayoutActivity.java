package com.business.victorehansone.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;

import com.business.victorehansone.Fragment.HomeFragment;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Method;
import com.business.victorehansone.databinding.ActivityLayoutBinding;

public class LayoutActivity extends AppCompatActivity {
    private ActivityLayoutBinding binding;
    String str = "1";
    Method method;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(LayoutActivity.this,R.layout.activity_layout);
        binding.cash.isChecked();
     method= new Method(LayoutActivity.this);
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str.equals("1")) {
                    binding.cash.isChecked();
                    HomeFragment.LAYOUT="1";
                    method.editor.putString(method.layoutvalue, "1");
                    method.editor.putBoolean(method.pref_login, true);
                    method.editor.commit();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                    finish();
                }
                else if (str.equals("2"))
                {
                    binding.bank.isChecked();
                    HomeFragment.LAYOUT="2";
                    method.editor.putString(method.layoutvalue, "2");
                    method.editor.putBoolean(method.pref_login, true);
                    method.editor.commit();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                    finish();
                }
            }
        });
        if (!TextUtils.isEmpty(method.pref.getString(method.layoutvalue,null))) {
            if (method.pref.getString(method.layoutvalue, null).equals("2")) {
                binding.bank.setChecked(true);
                binding.cash.setChecked(false);
            } else if (method.pref.getString(method.layoutvalue, null).equals("1")) {
                binding.cash.setChecked(true);
                binding.bank.setChecked(false);
            }
        }
        else
        {
            binding.bank.setChecked(true);
            binding.cash.setChecked(false);
        }


        fullscreen();
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

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.cash:
                if (checked)

                str = "1";
                Log.e("dfdfd",str.toString());
                break;
            case R.id.bank:
                if (checked)

                str = "2";
                Log.e("dfdfdfff",str.toString());
                break;

        }
    }

}