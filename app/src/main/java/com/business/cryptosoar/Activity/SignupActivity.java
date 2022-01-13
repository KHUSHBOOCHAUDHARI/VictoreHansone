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
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.business.cryptosoar.R;
import com.business.cryptosoar.Util.Apis;
import com.business.cryptosoar.Util.ConfigNew;
import com.business.cryptosoar.Util.Method;
import com.business.cryptosoar.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    String FirstName,LastName,Emailid,Password,MobileNo,ReferralCode;
    Method method;
    private String phone_code;
    Pattern ptrn = Pattern.compile("(0/91)?[7-9][0-9]{9}");
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
        binding= DataBindingUtil.setContentView(SignupActivity.this, R.layout.activity_signup);
        method=new Method(SignupActivity.this);
        pref = getSharedPreferences(mypreference, 0);
        editor = pref.edit();

        SharedPreferences pref = getApplicationContext().getSharedPreferences(ConfigNew.SHARED_PREF, 0);
        context = SignupActivity.this;
        regId = pref.getString("regId", null);
        auth = FirebaseAuth.getInstance();
        String udata="Login Now";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        binding.loginTxt.setText(content);
        binding.firstnameEdt.setFilters(new InputFilter[]{getEditTextFilter()});
        binding.lastnameEdt.setFilters(new InputFilter[]{getEditTextFilter()});


        binding.passwordEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.passwordEdt.onTouchEvent(event);
                binding.passwordEdt.setSelection(binding.passwordEdt.getText().length());
                return true;
            }
        });
//        binding.passwordEdt.append(binding.passwordEdt.length());
        binding.loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);

            }
        });
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form();
            }
        });

        binding.passwordVisibleLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.passwordVisibleLogo.setVisibility(View.GONE);
                binding.passwordInvisibleLogo.setVisibility(View.VISIBLE);
                binding.passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                binding.passwordEdt.setSelection(binding.passwordEdt.getText().length());
            }
        });
        binding.passwordInvisibleLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.passwordVisibleLogo.setVisibility(View.VISIBLE);
                binding.passwordInvisibleLogo.setVisibility(View.GONE);
                binding.passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                binding.passwordEdt.setSelection(binding.passwordEdt.getText().length());
            }
        });
        phone_code = binding.ccp.getSelectedCountryCode();

        binding.ccp.registerCarrierNumberEditText(binding.mobilenoEdt);
        binding.ccp.setNumberAutoFormattingEnabled(false);
        fullscreen();
    }
    public void form() {
        String Code=phone_code;
        FirstName=binding.firstnameEdt.getText().toString();
        LastName=binding.lastnameEdt.getText().toString();
        Emailid=binding.emailEdt.getText().toString();
        Password=binding.passwordEdt.getText().toString();
       MobileNo=binding.mobilenoEdt.getText().toString();
       ReferralCode=binding.refralcodeEdt.getText().toString();

       binding.firstnameEdt.setError(null);
       binding.lastnameEdt.setError(null);
       binding.emailEdt.setError(null);
        binding.mobilenoEdt.setError(null);
       binding.passwordEdt.setError(null);

       binding.refralcodeEdt.setError(null);
        if ( FirstName.isEmpty() || FirstName.equalsIgnoreCase("")) {
            binding.firstnameEdt.requestFocus();
            binding.firstnameEdt.setError(getResources().getString(R.string.firstname_txt));
            binding.passwordValidTxt.setVisibility(View.GONE);
        }
        else if ( LastName.isEmpty() || LastName.equalsIgnoreCase("")) {
            binding.lastnameEdt.requestFocus();
            binding.lastnameEdt.setError(getResources().getString(R.string.lastname_txt));
            binding.passwordValidTxt.setVisibility(View.GONE);
        }
        else if ( Emailid.isEmpty() || Emailid.equalsIgnoreCase("")) {
            binding.emailEdt.requestFocus();
            binding.emailEdt.setError(getResources().getString(R.string.enter_email_id));
            binding.passwordValidTxt.setVisibility(View.GONE);
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(Emailid).matches()) {
            binding.emailEdt.requestFocus();
            binding.emailEdt.setError(getResources().getString(R.string.enter_validemail_id));
            binding.passwordValidTxt.setVisibility(View.GONE);
        }
        else if (MobileNo.equals("") || MobileNo.isEmpty()) {
            binding.mobilenoEdt.requestFocus();
            binding.mobilenoEdt.setError(getResources().getString(R.string.mobile_txt));
            binding.passwordValidTxt.setVisibility(View.GONE);
        }

        else if(!binding.ccp.isValidFullNumber())
        {
            binding.mobilenoEdt.requestFocus();
            binding.mobilenoEdt.setError(getResources().getString(R.string.enter_validephone_no));

        }

        /*else if(Code.length() > 0 && MobileNo.length() > 0){
            }*/

        else if (Password.equals("") || Password.isEmpty()) {
            binding.passwordEdt.requestFocus();
            binding.passwordEdt.setError(getResources().getString(R.string.enter_password_txt));
            binding.passwordValidTxt.setVisibility(View.GONE);
        }
        else if(!isValidPassword(Password))
        {
            binding.passwordEdt.requestFocus();
            binding.passwordEdt.setError(getResources().getString(R.string.strong_password_validation));
            binding.passwordValidTxt.setVisibility(View.VISIBLE);
        }



        else {
            if (method.isNetworkAvailable(SignupActivity.this)) {


                    Log.e("FirstName",binding.firstnameEdt.toString());
                    Log.e("LastName",binding.lastnameEdt.toString());
                    Log.e("Emailid",binding.emailEdt.toString());
                    Log.e("Password",binding.passwordEdt.toString());
                    Log.e("MobileNo",binding.mobilenoEdt.toString());
                    Log.e("ReferralCode",binding.refralcodeEdt.toString());

                    registration(FirstName,LastName,Emailid,Password,MobileNo,ReferralCode,regId);
                    Log.e("RegToken....",regId.toString());


            } else {
                method.alertBox(getResources().getString(R.string.internet_connection));
            }
        }
    }


    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    private boolean validateUsing_libphonenumber(String countryCode, String phNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
            phoneNumber = phoneNumberUtil.parse(phNumber, isoCode);
        } catch (NumberParseException e) {
            System.err.println(e);
        }

        boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
        if (isValid) {
            String internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            Toast.makeText(this, "Phone Number is Valid " + internationalFormat, Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(this, "Phone Number is Invalid " + phoneNumber, Toast.LENGTH_LONG).show();
            return false;
        }
    }




    private void registration(String FirstName, String LastName, String EmailId, String Password,
                              String MobileNo, String ReferalCode,String DevideToken){

        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("first_name", FirstName);
        params.add("last_name", LastName);
        params.add("phone_number", MobileNo);
        params.add("email", EmailId);
        params.add("password", Password);
        params.add("referralcode", ReferalCode);
        params.add("device_token",DevideToken);
        params.add("app_type","android");

        Log.e("Parms",params.toString());

        String url = Apis.ROOT_URL+ getString(R.string.register_user);
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
                            String otp=jsonObject1.getString("otp");

                            Intent i = new Intent(SignupActivity.this, EmailVerificationActivity.class);
                            i.putExtra("OTP",otp.toString());
                            i.putExtra("MobileNo",MobileNo.toString());
                            i.putExtra("EmailId",EmailId.toString());
                            startActivity(i);

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







    //Email Id Validation
    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    //MobileNo Validation
    public static boolean isValidMobileNo(String str)
    {

        Pattern ptrn = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        Matcher match = ptrn.matcher(str);
        return (match.find() && match.group().equals(str));
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

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static InputFilter getEditTextFilter() {
        return new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
                Matcher ms = ps.matcher(String.valueOf(c));
                return ms.matches();
            }
        };
    }

}