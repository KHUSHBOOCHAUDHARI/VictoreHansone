package com.business.agrocoin.Activity;

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


import com.business.agrocoin.Util.Apis;
import com.business.agrocoin.Util.Method;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import com.business.agrocoin.R;
import com.business.agrocoin.databinding.ActivityEditProfileBinding;

public class EditProfile extends AppCompatActivity {
    Method method;
    String FirstName, LastName;
    private ActivityEditProfileBinding binding;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(EditProfile.this, R.layout.activity_edit_profile);
        method = new Method(this);
        binding.emailEdt.setText(method.pref.getString(method.Email_id, null));
        binding.mobilenoEdt.setText(method.pref.getString(method.Phone_No, null));
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form();
            }
        });
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });
        binding.firstnameEdt.setText(method.pref.getString(method.First_Name, null));
        binding.lastnameEdt.setText(method.pref.getString(method.Last_Name, null));
        fullscreen();
    }

    //Validation and Api Call..........
    public void form() {
        FirstName = binding.firstnameEdt.getText().toString();
        LastName = binding.lastnameEdt.getText().toString();


        binding.firstnameEdt.setError(null);
        binding.lastnameEdt.setError(null);


        if (FirstName.isEmpty() || FirstName.equalsIgnoreCase("")) {
            binding.firstnameEdt.requestFocus();
            binding.firstnameEdt.setError(getResources().getString(R.string.firstname_txt));
            binding.passwordValidTxt.setVisibility(View.GONE);
        } else if (LastName.isEmpty() || LastName.equalsIgnoreCase("")) {
            binding.lastnameEdt.requestFocus();
            binding.lastnameEdt.setError(getResources().getString(R.string.lastname_txt));
            binding.passwordValidTxt.setVisibility(View.GONE);
        } else {
            if (method.isNetworkAvailable(EditProfile.this)) {

                EditProfile(method.pref.getString(method.Id, null), FirstName, LastName);
            } else {
                method.alertBox(getResources().getString(R.string.internet_connection));
            }
        }
    }

    //Edit Profile Api..............
    private void EditProfile(String user_id, String FirstName, String LastName) {

        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", user_id);
        params.add("first_name", FirstName);
        params.add("last_name", LastName);
        Log.e("Parms", params.toString());

        String url = Apis.ROOT_URL + getString(R.string.edit_profile);
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
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String Id = jsonObject1.getString("id");
                            String fname = jsonObject1.getString("fname");
                            String lname = jsonObject1.getString("lname");
                            method.editor.putString(method.Id, Id);
                            method.editor.putString(method.First_Name, fname);
                            method.editor.putString(method.Last_Name, lname);
                            method.editor.putBoolean(method.pref_login, true);
                            method.editor.commit();
                            Intent i = new Intent(EditProfile.this, MainActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            String Message = jsonObject.getString("message");
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

    //Full Screen..........
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