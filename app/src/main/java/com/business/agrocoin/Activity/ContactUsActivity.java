package com.business.agrocoin.Activity;

import static com.business.agrocoin.Activity.SignupActivity.getEditTextFilter;

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
import android.widget.Toast;


import com.business.agrocoin.Util.Apis;
import com.business.agrocoin.Util.Method;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import com.business.agrocoin.R;
import com.business.agrocoin.databinding.ActivityContactUsBinding;

public class ContactUsActivity extends AppCompatActivity {
    private ActivityContactUsBinding binding;
    Method method;
    private String phone_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(ContactUsActivity.this,R.layout.activity_contact_us);
        method=new Method(ContactUsActivity.this);
        binding.firstnameTxt.setFilters(new InputFilter[]{getEditTextFilter()});
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form();
            }
        });
        binding.mobilenoTxt.setText(method.pref.getString(method.Phone_No,null));
        fullscreen();
        phone_code = binding.ccp.getSelectedCountryCode();

        binding.ccp.registerCarrierNumberEditText(binding.mobilenoTxt);
        binding.ccp.setNumberAutoFormattingEnabled(false);
    }

    //Validation and Api Call
    public void form() {
        String firstName=binding.firstnameTxt.getText().toString();
        String emailid=binding.emailTxt.getText().toString();
        String mobileno=binding.mobilenoTxt.getText().toString();
        String message=binding.messageTxt.getText().toString();
        binding.firstnameTxt.setError(null);

        binding.emailTxt.setError(null);

        binding.mobilenoTxt.setError(null);
        binding.messageTxt.setError(null);
        if ( firstName.isEmpty() || firstName.equalsIgnoreCase("")) {
            binding.firstnameTxt.requestFocus();
            binding.firstnameTxt.setError(getResources().getString(R.string.firstname_txt));
        }

        else if ( emailid.isEmpty() || emailid.equalsIgnoreCase("")) {
            binding.emailTxt.requestFocus();
            binding.emailTxt.setError(getResources().getString(R.string.enter_email_id));
        }
        else if (!isValidMail(emailid)) {
            binding.emailTxt.requestFocus();
            binding.emailTxt.setError(getResources().getString(R.string.enter_validemail_id));
        }


        else if (mobileno.equals("") || mobileno.isEmpty()) {
            binding.mobilenoTxt.requestFocus();
            binding.mobilenoTxt.setError(getResources().getString(R.string.mobile_txt));
        }
        else if(!binding.ccp.isValidFullNumber())
        {
            binding.mobilenoTxt.requestFocus();
            binding.mobilenoTxt.setError(getResources().getString(R.string.enter_validephone_no));

        }

        else if (message.equals("") || message.isEmpty()) {
            binding.messageTxt.requestFocus();
            binding.messageTxt.setError("Please enter message");
        }
        else
        {
            ContactUs(method.pref.getString(method.Id,null),firstName,emailid,mobileno,message);
        }

    }

    //Contact Us Api Call
    private void ContactUs(String user_id, String name, String email, String phone_number,String message){

        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", user_id);
        params.add("name", name);
        params.add("email", email);
        params.add("phone_number",phone_number);
        params.add("message",message);

        Log.e("Parms",params.toString());

        String url = Apis.ROOT_URL+ getString(R.string.contact_us);
        Log.e("Url------->",url);
//        client.addHeader("API-TOKEN", getString(R.string.API_TOKEN));
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                if (getApplicationContext() != null) {
                    String res = new String(responseBody);
                    Log.e("Url------->",res.toString());
                     try {
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            String Message=jsonObject.getString("message");
                            Toast.makeText(ContactUsActivity.this, Message.toString()        , Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ContactUsActivity.this, MainActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                        }  else {
                            binding.progressBar.setVisibility(View.GONE);
                            String Message=jsonObject.getString("message");
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
    //Email Id Validation
    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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