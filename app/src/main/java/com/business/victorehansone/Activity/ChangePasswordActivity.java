package com.business.victorehansone.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.business.victorehansone.R;
import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.Method;
import com.business.victorehansone.databinding.ActivityChangePasswordBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ChangePasswordActivity extends AppCompatActivity {
    Method method;
    private ActivityChangePasswordBinding binding;

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

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ChangePasswordActivity.this, R.layout.activity_change_password);
        method = new Method(ChangePasswordActivity.this);
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form();
            }
        });
        fullscreen();

    }
    //Validation and api callong
    public void form() {
        String old = binding.oldTxt.getText().toString();
        String password = binding.newTxt.getText().toString();
        String confiram = binding.confiramTxt.getText().toString();

        binding.oldTxt.setError(null);
        binding.newTxt.setError(null);
        binding.confiramTxt.setError(null);


        if (old.isEmpty() || old.equalsIgnoreCase("")) {
            binding.oldTxt.requestFocus();
            binding.oldTxt.setError("Please enter old password");
        } else if (!old.equals(method.pref.getString(method.Password, null))) {
            binding.oldTxt.requestFocus();
            binding.oldTxt.setError("Invalid old password");
        } else if (password.equals("") || password.isEmpty()) {
            binding.newTxt.requestFocus();
            binding.newTxt.setError(getResources().getString(R.string.enter_password_txt));
            binding.passwordValidTxt.setVisibility(View.GONE);
        } else if (!isValidPassword(password)) {
            binding.newTxt.requestFocus();
            binding.newTxt.setError(getResources().getString(R.string.strong_password_validation));
            binding.passwordValidTxt.setVisibility(View.VISIBLE);
        } else if (confiram.equals("") || confiram.isEmpty()) {
            binding.confiramTxt.requestFocus();
            binding.confiramTxt.setError("Plsease enter confiram password");
        } else if (!confiram.equals(password)) {
            binding.confiramTxt.requestFocus();
            binding.confiramTxt.setError("Password do not match");
        } else {
            ChangePassword(method.pref.getString(method.Id, null), method.pref.getString(method.Email_id, null), old, password);
        }

    }
    //Full Screen
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
    //Change Password Api
    private void ChangePassword(String ic_user_id, String email, String oldpassword, String newpassword) {

        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("ic_user_id", ic_user_id);
        params.add("email", email);
        params.add("oldpassword", oldpassword);
        params.add("newpassword", newpassword);


        Log.e("Parms", params.toString());

        String url = Apis.ROOT_URL + getString(R.string.change_password);
        Log.e("Url------->", url);
//        client.addHeader("API-TOKEN", getString(R.string.API_TOKEN));
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                if (getApplicationContext() != null) {
                    String res = new String(responseBody);
                    Log.e("Url------->", res.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            String Message =String.valueOf(Html.fromHtml(jsonObject.getString("message")));
                            Toast.makeText(ChangePasswordActivity.this, Message.toString(), Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(ChangePasswordActivity.this, MainActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            String Message = String.valueOf(Html.fromHtml(jsonObject.getString("message")));
                            method.alertBox(Message.toString());
                        }
                        binding.progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.progressBar.setVisibility(View.GONE);
                        method.alertBox(getResources().getString(R.string.failed_try_again));
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }


        });
    }
    //Valid Password
    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}