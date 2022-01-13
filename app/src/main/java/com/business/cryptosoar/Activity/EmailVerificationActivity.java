package com.business.cryptosoar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.ConfigNew;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.ActivityEmailVerificationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import cz.msebera.android.httpclient.Header;

public class EmailVerificationActivity extends AppCompatActivity {
    private ActivityEmailVerificationBinding binding;
    String Otp,MobileNo,EmailId;
    Method method;
    String Ottp;
    public static final String mypreference = "mypref";
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    ProgressBar progressBardialog;
    public  String regId;
    String Code;
    String otp;
    Context context;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(EmailVerificationActivity.this, R.layout.activity_email_verification);
        method=new Method(EmailVerificationActivity.this);
        String terms = "<font color='#FFFFFF'>By proceeding you agree to the </font><font color='#BF953F'>Terms & Conditions</font>";

        pref = getSharedPreferences(mypreference, 0);
        editor = pref.edit();

        SharedPreferences pref = getApplicationContext().getSharedPreferences(ConfigNew.SHARED_PREF, 0);
        context = EmailVerificationActivity.this;
        regId = pref.getString("regId", null);
        auth = FirebaseAuth.getInstance();
        binding.termandconditionTxt.setText(Html.fromHtml(terms), TextView.BufferType.SPANNABLE);
        String rules = "<font color='#FFFFFF'>By proceeding you agree to the </font><font color='#BF953F'>Capital Withdrawal Rules Terms & Conditions</font>";
        binding.capitalwithdrTxt.setText(Html.fromHtml(rules), TextView.BufferType.SPANNABLE);
        String udata="RESEND";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        binding.resendTxt.setText(content);
        binding.termandconditionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EmailVerificationActivity.this, TermandCondition.class);
                startActivity(intent);
                EmailVerificationActivity.this.overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        binding.capitalwithdrTxt                                                                                        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EmailVerificationActivity.this, TermandCondition.class);
                startActivity(intent);
                EmailVerificationActivity.this.overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });



        Otp=getIntent().getStringExtra("OTP");
        MobileNo=getIntent().getStringExtra("MobileNo");
        EmailId=getIntent().getStringExtra("EmailId");
        Toast.makeText(EmailVerificationActivity.this, Otp.toString(), Toast.LENGTH_SHORT).show();


        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.donthaveLayout.setVisibility(View.GONE);
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                binding.waitingTime.setText(f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {

                binding.waitingTime.setText("00:00");
                binding.donthaveLayout.setVisibility(View.VISIBLE);
            }
        }.start();



        binding.resendTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Resend(EmailId);
            }
        });

        binding.registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              otp=binding.otpEdt.getText().toString();

                if ( otp.isEmpty() || otp.equalsIgnoreCase("")) {
                    binding.otpEdt.requestFocus();
                    binding.otpEdt.setError(getResources().getString(R.string.enter_otp_id));
                }

                else if (!binding.termandconditionBox.isChecked())
                {
                    method.alertBox("Please Select Terms & Conditions");

                }
                else if (!binding.capitalwithdrBox.isChecked())
                {
                    method.alertBox("Please Select Capital Withdrawal Rules Terms & Conditions");
                }

                else
                {

                    Verification(EmailId,otp);

                }

            }
        });
        fullscreen();


    }

    private void Verification(String MobileNo,String Otp){

        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("email", MobileNo);
        params.add("otp", Otp);

        String url = Apis.ROOT_URL+ getString(R.string.verification);
        Log.e("Url------->",url);
//        client.addHeader("API-TOKEN", getString(R.string.API_TOKEN));
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                if (getApplicationContext() != null) {
                    String res = new String(responseBody);
                    Log.e("Url------->",res.toString());
                    // Toast.makeText(getApplicationContext(), res.toString(), Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            String Id=jsonObject1.getString("id");
                            String fname=jsonObject1.getString("fname");
                            String lname=jsonObject1.getString("lname");
                            String email=jsonObject1.getString("email");
                            String phone_number=jsonObject1.getString("phone_number");
                            String password=jsonObject1.getString("password");
                            String referred_from=jsonObject1.getString("referred_from");
                            String unique_id=jsonObject1.getString("unique_id");
                            String otp=jsonObject1.getString("otp");
                            String profile_image=jsonObject1.getString("profile_image");
                            String device_token=jsonObject1.getString("lname");
                            String app_type=jsonObject1.getString("app_type");
                            String is_active=jsonObject1.getString("is_active");
                            String created=jsonObject1.getString("created");
                            String updated=jsonObject1.getString("updated");
                            SettingApi(Id);
                            method.editor.putString(method.Id, Id);
                            method.editor.putString(method.First_Name, fname);
                            method.editor.putString(method.Last_Name, lname);
                            method.editor.putString(method.Email_id, email);
                            method.editor.putString(method.Phone_No, phone_number);
                            method.editor.putString(method.Password, password);
                            method.editor.putString(method.referred_from, referred_from);
                            method.editor.putString(method.unique_id, unique_id);
                            method.editor.putString(method.profile_image, profile_image);
                            method.editor.putBoolean(method.pref_login, true);
                            method.editor.commit();
                            binding.progressBar.setVisibility(View.GONE);
                            Intent i = new Intent(EmailVerificationActivity.this, MainActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                            finish();
                        }  else {
                            binding.progressBar.setVisibility(View.GONE);
                            String Message = jsonObject.getString("message");

                            method.alertBox(Message.toString());
                        }
                        binding.progressBar.setVisibility(View.GONE);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        binding.progressBar.setVisibility(View.GONE);
                        method.alertBox(getResources().getString(R.string.failed_try_again));                                           }

                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.something_went_wrong));

            }


        });
    }
    private void Resend(String EmailId){

        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("email", EmailId);


        String url = Apis.ROOT_URL+ getString(R.string.resendotp);
        Log.e("Url------->",url);
//        client.addHeader("API-TOKEN", getString(R.string.API_TOKEN));
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                if (getApplicationContext() != null) {
                    String res = new String(responseBody);
                    Log.e("Url------->",res.toString());
                    // Toast.makeText(getApplicationContext(), res.toString(), Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {

                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            Ottp=jsonObject1.getString("otp");

                            Toast.makeText(EmailVerificationActivity.this,Ottp.toString(),Toast.LENGTH_LONG).show();
                            new CountDownTimer(30000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    binding.donthaveLayout.setVisibility(View.GONE);
                                    // Used for formatting digit to be in 2 digits only
                                    NumberFormat f = new DecimalFormat("00");
                                    long hour = (millisUntilFinished / 3600000) % 24;
                                    long min = (millisUntilFinished / 60000) % 60;
                                    long sec = (millisUntilFinished / 1000) % 60;
                                    binding.waitingTime.setText(f.format(min) + ":" + f.format(sec));
                                }
                                // When the task is over it will print 00:00:00 there
                                public void onFinish() {

                                    binding.waitingTime.setText("00:00");
                                    binding.donthaveLayout.setVisibility(View.VISIBLE);
                                }
                            }.start();


                        }

                        else {
                            binding.progressBar.setVisibility(View.GONE);
                            String Message = jsonObject.getString("message");
                            method.alertBox(Message.toString());
                        }
                        String Message=jsonObject.getString("message");
                        Toast.makeText(EmailVerificationActivity.this, Message.toString(), Toast.LENGTH_SHORT).show();

                        String Otp=jsonObject.getString("data");
                        Toast.makeText(EmailVerificationActivity.this, Otp.toString(), Toast.LENGTH_SHORT).show();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                    catch (JSONException e) {

                        e.printStackTrace();
                        binding.progressBar.setVisibility(View.GONE);
                        method.alertBox(getResources().getString(R.string.failed_try_again));                                           }

                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.something_went_wrong));

            }


        });
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

    private void SettingApi(String UserId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", UserId);

        String url = Apis.ROOT_URL+ getString(R.string.setting_api);
        Log.e("Parms",params.toString());
        Log.e("Url---->",url.toString());
        // client.addHeader("API-TOKEN", getString(R.string.API_TOKEN));
        client.post(url,params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (getApplicationContext()!= null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);
                    try {
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            String addfund_minimum_diposit=jsonObject1.getString("addfund_minimum_diposit");
                            String monthly_interest_percentage=jsonObject1.getString("monthly_interest_percentage");
                            method.editor.putString(method.addfund_minimum_diposit, addfund_minimum_diposit);
                            method.editor.putString(method.monthly_interest_percentage, monthly_interest_percentage);


                        }

                        else
                        {
                            String Message = jsonObject.getString("message");
                            method.alertBox(Message);

                        }

                        binding.progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                method.alertBox(getResources().getString(R.string.something_went_wrong));
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

}