package com.business.cryptosoar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
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
import com.business.cryptosoar.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    String EmailId,Password;
    Method method;
    public static final String mypreference = "mypref";
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    ProgressBar progressBardialog;
    public  String regId;
    TextView dialogTitle, txtDialogMessage, txtSend, txtOk1;
    Context context;
    FirebaseAuth auth;
    private static final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        method=new Method(LoginActivity.this);
        pref = getSharedPreferences(mypreference, 0);
        editor = pref.edit();

        SharedPreferences pref = getApplicationContext().getSharedPreferences(ConfigNew.SHARED_PREF, 0);
        context = LoginActivity.this;
        regId = pref.getString("regId", null);
        auth = FirebaseAuth.getInstance();

        String udata="Signup Now";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        binding.signupTxt.setText(content);


        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              form();
            }
        });
        binding.signupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });


        binding.paswordVisibleLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.paswordVisibleLogo.setVisibility(View.GONE);
                binding.paswordInvisibleLogo.setVisibility(View.VISIBLE);
                binding.passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                binding.passwordEdt.setSelection(binding.passwordEdt.getText().length());
            }
        });

        binding.paswordInvisibleLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.paswordVisibleLogo.setVisibility(View.VISIBLE);
                binding.paswordInvisibleLogo.setVisibility(View.GONE);
                binding.passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                binding.passwordEdt.setSelection(binding.passwordEdt.getText().length());
            }
        });
        binding.forgetpasswordTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertForgetPassword();
            }
        });


        fullscreen();

    }

    //Validation For Login
    public void form() {

        EmailId=binding.emailEdt.getText().toString();
        Password=binding.passwordEdt.getText().toString();
        binding.emailEdt.setError(null);
        binding.passwordEdt.setError(null);
        if ( EmailId.isEmpty() || EmailId.equalsIgnoreCase("")) {
            binding.emailEdt.requestFocus();
            binding.emailEdt.setError(getResources().getString(R.string.enter_email_id));
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(EmailId).matches()) {
            binding.emailEdt.requestFocus();
            binding.emailEdt.setError(getResources().getString(R.string.enter_validemail_id));
        }

        else if (Password.equals("") || Password.isEmpty()) {
            binding.passwordEdt.requestFocus();
            binding.passwordEdt.setError(getResources().getString(R.string.enter_password_txt));

        }
        else {
            if (method.isNetworkAvailable(LoginActivity.this)) {
                login(EmailId,Password,regId);
                Log.e("deviceToken",regId.toString());
            } else {
                method.alertBox(getResources().getString(R.string.internet_connection));
            }
        }
    }

    //Login Api
    public void login(String email, String sendPassword,String device_tocken) {

        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("email", email);
        params.add("password", sendPassword);
        params.add("device_token", device_tocken);
        String url = Apis.ROOT_URL + getString(R.string.login_user);
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
                        String id = jsonObject1.getString("id");
                        String fname = jsonObject1.getString("fname");
                        String lname = jsonObject1.getString("lname");
                        String email = jsonObject1.getString("email");
                        String phone_number = jsonObject1.getString("phone_number");
                        String password = jsonObject1.getString("password");
                        String referred_from = jsonObject1.getString("referred_from");
                        String unique_id = jsonObject1.getString("unique_id");
                        String otp = jsonObject1.getString("otp");
                        String profile_image = jsonObject1.getString("profile_image");
                        String device_token = jsonObject1.getString("device_token");
                        String app_type = jsonObject1.getString("app_type");
                        String is_active = jsonObject1.getString("is_active");
                        String created = jsonObject1.getString("created");
                        String updated = jsonObject1.getString("updated");


                        method.editor.putString(method.Id, id);
                        method.editor.putString(method.First_Name, fname);
                        method.editor.putString(method.Last_Name, lname);
                        method.editor.putString(method.Email_id, email);
                        method.editor.putString(method.Phone_No, phone_number);
                        method.editor.putString(method.referred_from, referred_from);
                        method.editor.putString(method.device_token, device_tocken);
                        method.editor.putString(method.unique_id, unique_id);
                        method.editor.putString(method.profile_image, profile_image);
                        method.editor.putString(method.Password,binding.passwordEdt.getText().toString());
                        method.editor.putBoolean(method.pref_login, true);

                        method.editor.commit();

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);


                    }
//                    String state = jsonObject.getString("state");

                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                        method.alertBox(message);
                        String state = jsonObject.getString("state");
                        if (state.equalsIgnoreCase("verify_user"))
                        {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String otp = jsonObject1.getString("otp");
                            Intent i = new Intent(LoginActivity.this, EmailVerificationActivity.class);
                            i.putExtra("OTP",otp.toString());
                            i.putExtra("MobileNo",binding.emailEdt.getText().toString());
                            i.putExtra("EmailId",binding.emailEdt.getText().toString());
                            startActivity(i);
                            overridePendingTransition(R.anim.right_enter, R.anim.left_out);


                        }



                    }
                    binding.progressBar.setVisibility(View.GONE);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }
        });
    }



    //FogetPassword AlertBox
    public void AlertForgetPassword() {
        final Dialog dialog = new Dialog(LoginActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dialog_forgetpassword);

        txtDialogMessage = dialog.findViewById(R.id.for_email_edt);
        progressBardialog=dialog.findViewById(R.id.progressBardialog);
        txtSend = dialog.findViewById(R.id.send_btn);



        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = txtDialogMessage.getText().toString();
                if(phone.isEmpty() || phone.equalsIgnoreCase(""))
                {
                    txtDialogMessage.requestFocus();
                    txtDialogMessage.setError(getResources().getString(R.string.enter_email_id));
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(phone).matches()) {
                    txtDialogMessage.requestFocus();
                    txtDialogMessage.setError(getResources().getString(R.string.enter_validemail_id));
                }
                else
                {
                    progressBardialog.setVisibility(View.VISIBLE);

                        if (Method.isNetworkAvailable(LoginActivity.this)) {
                            ForgetPassword(phone,dialog);
                            dialog.dismiss();
                        } else {

                            method.alertBox(getResources().getString(R.string.internet_connection));
                        }

                }
            }
        });
        dialog.show();
    }

    //ForgetPassword

    //Forget Password Api Call
    private void ForgetPassword(String email, Dialog dialog) {


        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("email", email);

        String url = Apis.ROOT_URL+ getString(R.string.forget_password);
        Log.e("Parms",params.toString());
        Log.e("Url---->",url.toString());
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
                            dialog.dismiss();
                             Toast.makeText(getApplicationContext(), "Please check your mail on registered email for password.", Toast.LENGTH_LONG).show();
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


    //Forget Password Api Call
    private void VerifyPassword(String email,String otp) {


        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("email", email);
        params.add("otp", otp);

        String url = Apis.ROOT_URL+ getString(R.string.verify_user);
        Log.e("Parms",params.toString());
        Log.e("Url---->",url.toString());
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
                            Toast.makeText(getApplicationContext(), "Please check your mail on registered email for password.", Toast.LENGTH_LONG).show();
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
    //Forget Password Api Call


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
    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}