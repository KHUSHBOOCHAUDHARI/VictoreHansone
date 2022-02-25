package com.business.agrocoin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.business.agrocoin.R;
import com.business.agrocoin.Util.Apis;
import com.business.agrocoin.Util.Method;
import com.business.agrocoin.databinding.ActivityTermandConditionBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TermandCondition extends AppCompatActivity {
    private ActivityTermandConditionBinding binding;
    Method method;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(TermandCondition.this,R.layout.activity_termand_condition);
        binding.titleTxt.setText("Terms & Condition");
        method=new Method(TermandCondition.this);
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        login(method.pref.getString(method.Id,null));

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

    public void login(String user_id) {

        binding.progress.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", user_id);
        String url = Apis.ROOT_URL + getString(R.string.api);
        Log.e("Url------->",url);
           client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->",res.toString());
                           try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("true")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String privacy_policy = jsonObject1.getString("privacy_policy");
                        String terms_conditions = jsonObject1.getString("terms_conditions");
                        String faq = jsonObject1.getString("faq");
                        binding.textviewPrivacyPolicy.setText(HtmlCompat.fromHtml(privacy_policy,0));
                    }

                    else {
                        binding.progress.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                        Toast.makeText(TermandCondition.this, message.toString(), Toast.LENGTH_SHORT).show();
                    }
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