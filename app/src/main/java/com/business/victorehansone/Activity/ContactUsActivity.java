package com.business.victorehansone.Activity;

import static com.business.victorehansone.Activity.SignupActivity.getEditTextFilter;
import static com.business.victorehansone.Fragment.HomeFragment.progressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.Method;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import com.business.victorehansone.R;
import com.business.victorehansone.databinding.ActivityContactUsBinding;

public class ContactUsActivity extends AppCompatActivity {
    private ActivityContactUsBinding binding;
    Method method;
    private String phone_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(ContactUsActivity.this,R.layout.activity_contact_us);
        method=new Method(ContactUsActivity.this);
        binding.titleTxt.setText("Contact Prof. Hanson");
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                finish();
            }
        });
        binding.progress.setVisibility(View.VISIBLE);
        fullscreen();

        binding.webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                binding.progress.setProgress(progress);
                if (progress == 100) {
                    binding.progress.setVisibility(View.GONE);

                } else {
                    binding.progress.setVisibility(View.VISIBLE);

                }
            }
        });
        String url ="https://victorhanson.com/contact-us/";
      //  binding.webview.loadUrl(url);
        binding.progress.setVisibility(View.GONE);
        binding.webview.loadUrl(url);

        // this will enable the javascript.
        binding.webview.getSettings().setJavaScriptEnabled(true);

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        binding.webview.setWebViewClient(new WebViewClient());
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
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