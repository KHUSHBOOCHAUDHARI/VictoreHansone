package com.business.agrocoin.Activity;

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
import android.text.Html;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.business.agrocoin.Util.Apis;
import com.business.agrocoin.Util.ConfigNew;
import com.business.agrocoin.Util.Method;

import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.business.agrocoin.R;
import com.business.agrocoin.databinding.ActivityLoginBinding;
import cz.msebera.android.httpclient.Header;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    String EmailId, Password;
    Method method;
    public static final String mypreference = "mypref";
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    ProgressBar progressBardialog;
    public String regId;
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
        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        method = new Method(LoginActivity.this);
        pref = getSharedPreferences(mypreference, 0);
        editor = pref.edit();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(ConfigNew.SHARED_PREF, 0);
        context = LoginActivity.this;
        regId = pref.getString("regId", null);
        auth = FirebaseAuth.getInstance();
        String udata = "Sign Up";
        binding.signuptagTxt.setText(Html.fromHtml("Don't have account?"));
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
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
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
        EmailId = binding.emailEdt.getText().toString();
        Password = binding.passwordEdt.getText().toString();
        binding.emailEdt.setError(null);
        binding.passwordEdt.setError(null);
        if (EmailId.isEmpty() || EmailId.equalsIgnoreCase("")) {
            binding.emailEdt.requestFocus();
            binding.emailEdt.setError(getResources().getString(R.string.enter_email_id));
        }
        else if (Password.equals("") || Password.isEmpty()) {
            binding.passwordEdt.requestFocus();
            binding.passwordEdt.setError(getResources().getString(R.string.enter_password_txt));
        } else {
            if (method.isNetworkAvailable(LoginActivity.this)) {
                login(EmailId, Password);
            } else {
                method.alertBox(getResources().getString(R.string.internet_connection));
            }
        }
    }
    //Login Api
    public void login(String email, String sendPassword) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = Apis.ROOT_URLL + getString(R.string.login_user) + "username=" + email + "&password=" + sendPassword;
        Log.e("Url------->", url);
        client.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("statusCode");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String id = jsonObject1.getString("userId");
                        String balance = jsonObject1.getString("balance");
                        JSONArray itemArray = jsonObject1.getJSONArray("walletAddress");
                        //data(id);
                        method.editor.putString(method.Id, id);
                        method.editor.putString(method.account_balance, balance);
                        method.editor.putString(method.Address_Id, itemArray.toString());
                        method.editor.putString(method.UserName, email.toString());
                        method.editor.putString(method.Password, binding.passwordEdt.getText().toString());
                        method.editor.putBoolean(method.pref_login, true);
                        method.editor.commit();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                        String message = jsonObject.getString("statusMsg");
                        Toast.makeText(LoginActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("statusMsg");
                        method.alertBox(message);
                    }
                    binding.progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
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
        progressBardialog = dialog.findViewById(R.id.progressBardialog);
        txtSend = dialog.findViewById(R.id.send_btn);
        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = txtDialogMessage.getText().toString();
                if (phone.isEmpty() || phone.equalsIgnoreCase("")) {
                    txtDialogMessage.requestFocus();
                    txtDialogMessage.setError(getResources().getString(R.string.enter_email_id));
                } else if (!Patterns.EMAIL_ADDRESS.matcher(phone).matches()) {
                    txtDialogMessage.requestFocus();
                    txtDialogMessage.setError(getResources().getString(R.string.enter_validemail_id));
                } else {
                    progressBardialog.setVisibility(View.VISIBLE);

                    if (Method.isNetworkAvailable(LoginActivity.this)) {
                        //ForgetPassword(phone, dialog);
                        dialog.dismiss();
                    } else {

                        method.alertBox(getResources().getString(R.string.internet_connection));
                    }

                }
            }
        });
        dialog.show();
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