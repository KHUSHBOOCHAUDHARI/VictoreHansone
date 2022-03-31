package com.business.victorehansone.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.business.victorehansone.R;
import com.business.victorehansone.Util.Method;
import com.business.victorehansone.databinding.ActivitySetCustomFontBinding;
import com.business.victorehansone.databinding.ActivitySettingBinding;

public class SetCustomFontActivity extends AppCompatActivity {
    private ActivitySetCustomFontBinding binding;
    private int ourFontsize = 1;
    String str = "1";
    public static int ans;
    Method method;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(SetCustomFontActivity.this,R.layout.activity_set_custom_font);
        method=new Method(SetCustomFontActivity.this);

        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                finish();
            }
        });
        binding.inc.setProgress(method.pref.getInt(String.valueOf(method.inc),0));
        if (method.pref.getInt(String.valueOf(method.inc),0)==0) {
            binding.txtsizee.setText(String.valueOf("Value:" + "12"));
        }
        else
        {
            binding.txtsizee.setText(String.valueOf("Text Size:" + method.pref.getInt(String.valueOf(method.inc), 0)));
        }
        binding.inc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int p=0;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                // Toast.makeText(getApplicationContext(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();

                binding.txtsizee.setText("Value:"+String.valueOf(progress));


                binding.saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                        int neww= progress +2;
                        method.editor.putInt(String.valueOf(method.inc), progress);

                        method.editor.putBoolean(method.pref_login, true);
                        method.editor.commit();
                        Log.e("-----ok---", String.valueOf(progress));

                        Log.e("-----oknew-", String.valueOf(method.pref.getInt(String.valueOf(method.inc),0)));

                        method.editor.putInt(String.valueOf(method.dec), neww);
                        method.editor.putBoolean(method.pref_login, true);
                        method.editor.commit();
                        Log.e("-----okk---", String.valueOf(neww));
                        Log.e("-----okknew---", String.valueOf(method.pref.getInt(String.valueOf(method.dec),0)));
                    }
                });


            }
        });
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

}