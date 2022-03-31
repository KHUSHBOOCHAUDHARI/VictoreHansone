package com.business.victorehansone.Activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;

import com.business.victorehansone.R;
import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.Method;
import com.business.victorehansone.databinding.ActivityTermandConditionBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import cz.msebera.android.httpclient.Header;


public class TermandCondition extends AppCompatActivity {
    private ActivityTermandConditionBinding binding;
    Method method;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(TermandCondition.this,R.layout.activity_termand_condition);
        binding.titleTxt.setText("About Us");
        method=new Method(TermandCondition.this);
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                finish();
            }
        });

        if (Method.isNetworkAvailable(TermandCondition.this)) {
            AboutUs("2");
        }
        else
        {
            if (!TextUtils.isEmpty(method.pref.getString(method.JsonString,null))) {
                binding.textViewCategory.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(method.pref.getString(method.aboutusobject, null));
                    String id = jsonObject.getString("id");
                    String Date = jsonObject.getString("date");
                    String titlee = jsonObject.getString("title");
                    Log.e("djksahdjksad", String.valueOf(Html.fromHtml(titlee.toString())));

                    JSONObject json_title = jsonObject.getJSONObject("title");
                    String Title = json_title.getString("rendered");
                    JSONObject json_object = jsonObject.getJSONObject("content");
                    String rendered = json_object.getString("rendered");
                    binding.webView.loadData(rendered.toString(), "text/html", "UTF-8");
                    method.editor.putString(method.aboutusobject, jsonObject.toString());
                    method.editor.commit();
                    binding.progress.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.progress.setVisibility(View.GONE);

                }
            }
            else
            {
                Log.e("second","second");
                binding.progress.setVisibility(View.GONE);
                binding.textViewCategory.setVisibility(View.VISIBLE);
                binding.textViewCategory.setText(getResources().getString(R.string.internet_connection));
            }
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
    public void AboutUs(String id) {

        binding.progress.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();

        String url = Apis.ROOT_URLL + getString(R.string.about_us);
        Log.e("Url------->",url);
           client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->",res.toString());
                           try {
                              JSONObject jsonObject = new JSONObject(res);
                               String id = jsonObject.getString("id");
                               String Date = jsonObject.getString("date");
                               String titlee = jsonObject.getString("title");
                               Log.e("djksahdjksad",String.valueOf(Html.fromHtml (titlee.toString())));

                               JSONObject json_title=jsonObject.getJSONObject("title");
                               String Title=json_title.getString("rendered");
                               JSONObject json_object = jsonObject.getJSONObject("content");
                               String rendered = json_object.getString("rendered");
                               binding.webView.loadData(rendered.toString(), "text/html", "UTF-8");
                               method.editor.putString(method.aboutusobject, jsonObject.toString());
                               method.editor.commit();
                    binding.progress.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.progress.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.something_went_wrong));
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progress.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });
    }



}
