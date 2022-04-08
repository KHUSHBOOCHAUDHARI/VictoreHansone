package com.business.victorehansone.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.multidex.BuildConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.ConfigNew;
import com.business.victorehansone.Util.Method;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.business.victorehansone.R;
import com.business.victorehansone.databinding.ActivityLoginBinding;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
   private ActivityLoginBinding binding;
    String EmailId, Password;
    Method method;
    String google_firstname, google_lastname, google_email, google_account_name, google_id_token, google_photo, google_id,devicetoken,deviceid;

    private GoogleSignInClient mGoogleSignInClient;
    private TwitterAuthClient client;
    public static final String mypreference = "mypref";
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    ProgressBar progressBardialog;
    public String regId;
    TextView dialogTitle, txtDialogMessage, txtSend, txtOk1;
    Context context;
    FirebaseAuth auth;
    ImageView customLoginButton;
    String userName,remembered_type="unchecked";
    long userId;
    private static final int RC_SIGN_IN = 9001;
    String TwitterId,TwitterEmail;
    private static final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
    CallbackManager callbackManager;
    AccessToken access_token;
    GraphRequest request;
    private String email,facebook_uid,first_name,last_name,social_id,name,picture;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig mTwitterAuthConfig = new TwitterAuthConfig(getString(R.string.Twitter_CONSUMER_KEY),
                getString(R.string.Twitter_CONSUMER_SECRET));
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(mTwitterAuthConfig)
                .build();
        Twitter.initialize(twitterConfig);
        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        method = new Method(LoginActivity.this);
        customLoginButton = (ImageView) findViewById(R.id.orders_image);
        pref = getSharedPreferences(mypreference, 0);
        editor = pref.edit();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(ConfigNew.SHARED_PREF, 0);
        context = LoginActivity.this;
        regId = pref.getString("regId", null);

        auth = FirebaseAuth.getInstance();
        String udata = "Sign Up";
        client= new TwitterAuthClient();
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        binding.signupTxt.setText(content);

        binding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
            }
        });

        String forget = "Forgot password?";
        binding.signuptagTxt.setText(Html.fromHtml("<font color='#888888'>Don't have any account?</font>"));
        SpannableString forgetcontent = new SpannableString(forget);
        forgetcontent.setSpan(new UnderlineSpan(), 0, forget.length(), 0);
        binding.forgetpasswordTxt.setText(forgetcontent);


        binding.getstartedTxt.setText(Html.fromHtml("<font >Login to <b>THE BLADE FOR PERSEUS</b>"));


        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                form();
            }
        });
        binding.linearGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHashDetail:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {

        } catch (NoSuchAlgorithmException e) {

        }

        binding.signupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SubscribeActivity.class);
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
        //TwitterLogin
        customLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.authorize(LoginActivity.this, new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {
                        // Do something with result, which provides a TwitterSession for making API calls

                        fetchTwitterImage();
//                        User user = result.data;
//                        imageProfileUrl = user.profileImageUrl;
//                        Log.e(TAG, "Data : " + imageProfileUrl);
//                        imageProfileUrl = imageProfileUrl.replace("_normal", "");
//                        Log.e("imagegeeeee",imageProfileUrl.toString());
//                        userId=result.data.getId();
//                        TwitterId=String.valueOf(userId);
//                        userName=result.data.name;
//                        String Twitteremail=result.data.email;
//                        Log.e("Name",userName.toString());
//                        Log.e("UserId", String.valueOf(userId));
//                        Log.e("Twitter", TwitterId.toString());
//                        Log.e("imageProfileUrli",imageProfileUrl);
//                        Log.e("imail",Twitteremail.toString());
//                        TwitterAuthentication("social_login",userName, String.valueOf(TwitterId),regId,"twitter","",userName,userName,"");




                        //AuthenticationcheckTwitter(TwitterId,"twitter");
                    }

                    @Override
                    public void failure(TwitterException e) {

                        Toast toast = Toast.makeText(getApplicationContext(), "Login fail", Toast.LENGTH_LONG);
                        // Here we can set the Gravity to Top and Right
                        toast.setGravity(Gravity.TOP , 0, 100);
                        toast.show();


                    }
                });
            }
        });
        binding.linearfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLoginSignup();
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
                login(EmailId, Password,"login",regId);
            } else {
                method.alertBox(getResources().getString(R.string.internet_connection));
            }
        }
    }
    public void TwitterAuthentication(String f, String useremail,String user_auth_key,String firebase_token,String type,String profile_image,String user_name,String first_name,String last_name) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("f", f);
        params.add("useremail", useremail);
        params.add("user_auth_key", user_auth_key);
        params.add("firebase_token", firebase_token);
        params.add("type", type);
        params.add("profile_image", profile_image);
        params.add("user_name", user_name);
        params.add("first_name", user_name);
        params.add("last_name", user_name);
        String url = Apis.ROOT_URLLL + getString(R.string.login_ic_user);
        String someData = "{\"f\":\""+f+"\",\"useremail\":\""+useremail+"\",\"user_auth_key\":\""+user_auth_key+"\",\"firebase_token\":\""+firebase_token+"\"," +
                "\"type\":\""+type+"\",\"profile_image\":\""+profile_image+"\",\"user_name\":\""+user_name+"\",\"first_name\":\""+first_name+"\",\"last_name\":\""+last_name+"\"}";


        StringEntity entity = null;
        try {
            entity = new StringEntity(someData);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch(Exception e) {

        }

        client.addHeader("api-token","4db248bc10877bef6f3008ef64e5c76a");
        Log.e("Url------->", url);
        client.post(LoginActivity.this,url, entity,"application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String first_name = jsonObject1.getString("first_name");
                        String last_name = jsonObject1.getString("last_name");
                        String social_profile_image = jsonObject1.getString("social_profile_image");
                        JSONObject jsonObject11=jsonObject1.getJSONObject("user_data");
                        String id = jsonObject11.getString("ID");
                        String user_login = jsonObject11.getString("user_login");
                        String user_pass = jsonObject11.getString("user_pass");
                        String user_nicename = jsonObject11.getString("user_nicename");
                        String user_email = jsonObject11.getString("user_email");
                        String user_url = jsonObject11.getString("user_url");
                        String user_registered = jsonObject11.getString("user_registered");
                        String user_activation_key = jsonObject11.getString("user_activation_key");
                        String user_status = jsonObject11.getString("user_status");
                        String display_name = jsonObject11.getString("display_name");
                        JSONObject jsonObjectt=jsonObject1.getJSONObject("current_plan");
                        String current_plant_name=jsonObjectt.getString("name");
                        String transaction_subject=jsonObjectt.getString("label");
                        method.editor.putString(method.Id, id.toString());
                        method.editor.putString(method.First_Name, first_name.toString());
                        method.editor.putString(method.Last_Name, last_name.toString());
                        method.editor.putString(method.user_login, user_login.toString());
                        method.editor.putString(method.user_pass,user_pass.toString());
                        method.editor.putString(method.user_nicename, user_nicename.toString());
                        method.editor.putString(method.user_email, display_name.toString());
                        method.editor.putString(method.user_url,user_url.toString());
                        method.editor.putString(method.user_registered, user_registered.toString());
                        method.editor.putString(method.user_activation_key, user_activation_key.toString());
                        method.editor.putString(method.user_status,user_status.toString());
                        method.editor.putString(method.display_name, display_name.toString());
                        method.editor.putString(method.profile_image,social_profile_image.toString());
                        method.editor.putString(method.transaction_subject,transaction_subject.toString());
                        method.editor.putString(method.current_plane,current_plant_name.toString());

                        method.editor.putBoolean(method.pref_login, true);
                        method.editor.commit();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                        String message = String.valueOf(Html.fromHtml(jsonObject.getString("message")));
                        //Toast.makeText(LoginActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = String.valueOf(Html.fromHtml(jsonObject.getString("message")));
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
    //For Facebook Login
    private void facebookLoginSignup() {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback((com.facebook.CallbackManager) callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("response Success", "Login");
                        access_token = loginResult.getAccessToken();
                        Log.d("response access_token", access_token.toString());

                        request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                JSONObject json = response.getJSONObject();
                                try {
                                    if (json != null) {
                                        Log.d("response", json.toString());
                                        try {
                                            email = json.getString("email");
                                            Log.e("email",email+"");
                                        } catch (Exception e) {

                                            Toast toast = Toast.makeText(getApplicationContext(), "sorry!!! Your email is not verified on facebook.", Toast.LENGTH_LONG);
                                            // Here we can set the Gravity to Top and Right
                                            toast.setGravity(Gravity.TOP , 0, 100);
                                            toast.show();

                                            return;
                                        }
                                        facebook_uid = json.getString("id");
                                        social_id = json.getString("id");
                                        first_name = json.getString("first_name");
                                        last_name = json.getString("last_name");
                                        name = json.getString("name");
                                        email = json.getString("email");
                                        Log.e("name",name+"");
                                        Log.e("email",email+"");
                                        Log.e("first_name",first_name+"");
                                        Log.e("last_name",last_name+"");

                                        picture = "https://graph.facebook.com/" + facebook_uid + "/picture?type=large";


                                        Log.d("response",  picture);
                                        // Picasso.with(context).load(picture).placeholder(R.mipmap.ic_launcher).into(userIv);
                                        //AuthenticationcheckFacebook(facebook_uid,"facebook")       ;                                 //mPb.setVisibility(View.GONE);
                                        if (method.isNetworkAvailable(LoginActivity.this)) {
                                            FacebookAuthentication("social_login",email,facebook_uid,regId,"facebook",picture,name,first_name,last_name);                                        } else {
                                            method.alertBox(getResources().getString(R.string.internet_connection));
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("response problem", "problem" + e.getMessage());
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,first_name,last_name,link,email,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(context, "l", Toast.LENGTH_LONG).show();
                        Toast toast = Toast.makeText(getApplicationContext(), "Login Cancel", Toast.LENGTH_LONG);
                        // Here we can set the Gravity to Top and Right
                        toast.setGravity(Gravity.TOP , 0, 100);
                        toast.show();

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast toast = Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG);
                        // Here we can set the Gravity to Top and Right
                        toast.setGravity(Gravity.TOP , 0, 100);
                        toast.show();
                    }


                });
    }
    public void FacebookAuthentication(String f, String useremail,String user_auth_key,String firebase_token,String type,String profile_image,String user_name,String first_name,String last_name) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("f", f);
        params.add("useremail", useremail);
        params.add("user_auth_key", user_auth_key);
        params.add("firebase_token", firebase_token);
        params.add("type", type);
        params.add("profile_image", profile_image);
        params.add("user_name", user_name);
        params.add("first_name", user_name);
        params.add("last_name", user_name);
        String url = Apis.ROOT_URLLL + getString(R.string.login_ic_user);
        String someData = "{\"f\":\""+f+"\",\"useremail\":\""+useremail+"\",\"user_auth_key\":\""+user_auth_key+"\",\"firebase_token\":\""+firebase_token+"\"," +
                "\"type\":\""+type+"\",\"profile_image\":\""+profile_image+"\",\"user_name\":\""+user_name+"\",\"first_name\":\""+first_name+"\",\"last_name\":\""+last_name+"\"}";


        StringEntity entity = null;
        try {
            entity = new StringEntity(someData);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch(Exception e) {

        }

        client.addHeader("api-token","4db248bc10877bef6f3008ef64e5c76a");
        Log.e("Url------->", url);
        client.post(LoginActivity.this,url, entity,"application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String first_name = jsonObject1.getString("first_name");
                        String last_name = jsonObject1.getString("last_name");
                        String social_profile_image = jsonObject1.getString("social_profile_image");
                        JSONObject jsonObject11=jsonObject1.getJSONObject("user_data");
                        String id = jsonObject11.getString("ID");
                        String user_login = jsonObject11.getString("user_login");
                        String user_pass = jsonObject11.getString("user_pass");
                        String user_nicename = jsonObject11.getString("user_nicename");
                        String user_email = jsonObject11.getString("user_email");
                        String user_url = jsonObject11.getString("user_url");
                        String user_registered = jsonObject11.getString("user_registered");
                        String user_activation_key = jsonObject11.getString("user_activation_key");
                        String user_status = jsonObject11.getString("user_status");
                        String display_name = jsonObject11.getString("display_name");
                        JSONObject jsonObjectt=jsonObject1.getJSONObject("current_plan");
                        String current_plant_name=jsonObjectt.getString("name");
                        String transaction_subject=jsonObjectt.getString("label");

//


                        method.editor.putString(method.Id, id.toString());
                        method.editor.putString(method.First_Name, first_name.toString());
                        method.editor.putString(method.Last_Name, last_name.toString());
                        method.editor.putString(method.user_login, user_login.toString());
                        method.editor.putString(method.user_pass,user_pass.toString());
                        method.editor.putString(method.user_nicename, user_nicename.toString());
                        method.editor.putString(method.user_email, user_email.toString());
                        method.editor.putString(method.user_url,user_url.toString());
                        method.editor.putString(method.user_registered, user_registered.toString());
                        method.editor.putString(method.user_activation_key, user_activation_key.toString());
                        method.editor.putString(method.user_status,user_status.toString());
                        method.editor.putString(method.display_name, name.toString());
                        method.editor.putString(method.profile_image,social_profile_image.toString());
//
                        method.editor.putString(method.transaction_subject,transaction_subject.toString());
                        method.editor.putString(method.current_plane,current_plant_name.toString());

                        method.editor.putBoolean(method.pref_login, true);
                        method.editor.commit();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                        String message = String.valueOf(Html.fromHtml(jsonObject.getString("message")));
                        //Toast.makeText(LoginActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = String.valueOf(Html.fromHtml(jsonObject.getString("message")));
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
    //Login Api
    public void login(String email, String sendPassword,String F,String firebase_token ) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_login", email);
        params.add("user_password", sendPassword);
        params.add("f", F);
        params.add("firebase_token",firebase_token );
        String url = Apis.ROOT_URLLL + getString(R.string.login_ic_user);
        String someData = "{\"user_login\":\""+email+"\",\"user_password\":\""+sendPassword+"\",\"f\":\""+F+"\",\"firebase_token\":\""+firebase_token +"\"}";
        StringEntity entity = null;
        try {
            entity = new StringEntity(someData);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch(Exception e) {

        }

        client.addHeader("api-token","4db248bc10877bef6f3008ef64e5c76a");
        Log.e("Url------->", url);
        client.post(LoginActivity.this,url, entity,"application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String first_name = jsonObject1.getString("first_name");
                        String last_name = jsonObject1.getString("last_name");
                        String social_profile_image = jsonObject1.getString("social_profile_image");
                        JSONObject jsonObject11=jsonObject1.getJSONObject("user_data");
                        String id = jsonObject11.getString("ID");
                        String user_login = jsonObject11.getString("user_login");
                        String user_pass = jsonObject11.getString("user_pass");
                        String user_nicename = jsonObject11.getString("user_nicename");
                        String user_email = jsonObject11.getString("user_email");
                        String user_url = jsonObject11.getString("user_url");
                        String user_registered = jsonObject11.getString("user_registered");
                        String user_activation_key = jsonObject11.getString("user_activation_key");
                        String user_status = jsonObject11.getString("user_status");
                        String display_name = jsonObject11.getString("display_name");

                        JSONObject jsonObjectt=jsonObject1.getJSONObject("current_plan");
                        String current_plant_name=jsonObjectt.getString("name");
                        String transaction_subject=jsonObjectt.getString("label");

//                        JSONObject jsonObject2=jsonObject1.getJSONObject("members_data");
//                       // String m_id=jsonObject2.getString("id");
//                        //String txn_id=jsonObject2.getString("txn_id");
//                        String u_id=jsonObject2.getString("u_id");

//                        JSONObject jsonObject3=jsonObject2.getJSONObject("payment_data");
//                        String transaction_subject=jsonObject3.getString("transaction_subject");
//


                        method.editor.putString(method.Id, id.toString());
                        method.editor.putString(method.First_Name, first_name.toString());
                        method.editor.putString(method.Last_Name, last_name.toString());
                        method.editor.putString(method.profile_image,social_profile_image.toString());
                        method.editor.putString(method.user_login, user_login.toString());
                        method.editor.putString(method.user_pass,user_pass.toString());
                        method.editor.putString(method.user_nicename, user_nicename.toString());
                        method.editor.putString(method.user_email, user_email.toString());
                        method.editor.putString(method.user_url,user_url.toString());
                        method.editor.putString(method.user_registered, user_registered.toString());
                        method.editor.putString(method.user_activation_key, user_activation_key.toString());
                        method.editor.putString(method.user_status,user_status.toString());
                        method.editor.putString(method.display_name, display_name.toString());
//                        method.editor.putString(method.m_id, m_id.toString());
//                        method.editor.putString(method.txn_id,txn_id.toString());
//                        method.editor.putString(method.u_id, u_id.toString());
                        method.editor.putString(method.transaction_subject,transaction_subject.toString());
                        method.editor.putString(method.current_plane,current_plant_name.toString());

                        method.editor.putBoolean(method.pref_login, true);
                        method.editor.commit();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                        String message = String.valueOf(Html.fromHtml(jsonObject.getString("message")));
                        //Toast.makeText(LoginActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = String.valueOf(Html.fromHtml(jsonObject.getString("message")));
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
        ImageView cancle=dialog.findViewById(R.id.cancle);
        progressBardialog = dialog.findViewById(R.id.progressBardialog);
        txtSend = dialog.findViewById(R.id.send_btn);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


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
                        ForgetPassword(phone, "forgot_password");
                        dialog.dismiss();
                    } else {

                        method.alertBox(getResources().getString(R.string.internet_connection));
                    }

                }
            }
        });
        dialog.show();
    }
    //Login Api
    public void ForgetPassword(String email,String F) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("email_id", email);
        params.add("f", F);
        String url = Apis.ROOT_URLLL + getString(R.string.login_ic_user);
        String someData = "{\"email_id\":\""+email+"\",\"f\":\""+F+"\"}";
        StringEntity entity = null;
        try {
            entity = new StringEntity(someData);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch(Exception e) {

        }

        client.addHeader("api-token","4db248bc10877bef6f3008ef64e5c76a");
        Log.e("Url------->", url);
        client.post(LoginActivity.this,url, entity,"application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        String data = String.valueOf(Html.fromHtml(jsonObject.getString("data")));
                        String message = String.valueOf(Html.fromHtml(jsonObject.getString("message")));
                        method.alertBox(message.toString());

                    }
                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = String.valueOf(Html.fromHtml(jsonObject.getString("message")));
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
    //SOCIAL LOGIN
    //Google Login
    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("GOOGLE_LOGIN", "onConnectionFailed:" + connectionResult);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            google_email = account.getEmail();
            google_firstname = account.getDisplayName();
            google_id = account.getId();
            if (account.getPhotoUrl() != null) {
                google_photo = String.valueOf(account.getPhotoUrl());
            } else {
                google_photo = "";
            }

            if (account.getEmail() != null) {
                google_email = String.valueOf(account.getEmail());
            } else {
                google_email= "";
            }
            google_id_token = account.getIdToken();
            google_account_name = account.getGivenName();
            Log.e("GOOGLEID","++++++++"+google_id);
            Log.e("GOOGLENAME","++++++++"+account.getGivenName());
            Log.e("GOOGLENAME","++++++++"+account.getFamilyName());
            Log.e("GOOGLEEMAIL","++++++++"+google_email);
            Log.e("GOOGLEAPHOTO","++++++++"+google_photo);
            Log.e("Response from google","Google response : " + google_id_token);
            if (method.isNetworkAvailable(LoginActivity.this)) {
                GmailAuthentication("social_login",google_email,google_id,regId,"google",google_photo,google_firstname,account.getGivenName(),account.getFamilyName());
            } else {
                method.alertBox(getResources().getString(R.string.internet_connection));
            }




//            AuthenticationcheckGoogle(google_id,"google");
        } catch (ApiException e) {
            Log.e("Show Error status code", "Result:" + e.getStatusCode());
            e.printStackTrace();

            Toast toast = Toast.makeText(getApplicationContext(), "Google Login Failed, Please Try Again", Toast.LENGTH_LONG);
            // Here we can set the Gravity to Top and Right
            toast.setGravity(Gravity.TOP , 0, 100);
            toast.show();

        }
    }
    @SuppressLint("RestrictedApi")
    private void signIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.revokeAccess();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

        Log.e("Google Interpretation","Interpret Data : " + mGoogleSignInClient);
        Log.e("Google data","DatFa : " +RC_SIGN_IN);
    }
    public void GmailAuthentication(String f, String useremail,String user_auth_key,String firebase_token,String type,String profile_image,String user_name,String first_name,String last_name) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("f", f);
        params.add("useremail", useremail);
        params.add("user_auth_key", user_auth_key);
        params.add("firebase_token", firebase_token);
        params.add("type", type);
        params.add("profile_image", profile_image);
        params.add("user_name", user_name);
        params.add("first_name", user_name);
        params.add("last_name", user_name);
        String url = Apis.ROOT_URLLL + getString(R.string.login_ic_user);
        String someData = "{\"f\":\""+f+"\",\"useremail\":\""+useremail+"\",\"user_auth_key\":\""+user_auth_key+"\",\"firebase_token\":\""+firebase_token+"\"," +
                "\"type\":\""+type+"\",\"profile_image\":\""+profile_image+"\",\"user_name\":\""+user_name+"\",\"first_name\":\""+first_name+"\",\"last_name\":\""+last_name+"\"}";


        StringEntity entity = null;
        try {
            entity = new StringEntity(someData);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch(Exception e) {

        }

        client.addHeader("api-token","4db248bc10877bef6f3008ef64e5c76a");
        Log.e("Url------->", url);
        client.post(LoginActivity.this,url, entity,"application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String first_name = jsonObject1.getString("first_name");
                        String last_name = jsonObject1.getString("last_name");
                        String social_profile_image = jsonObject1.getString("social_profile_image");
                        JSONObject jsonObject11=jsonObject1.getJSONObject("user_data");
                        String id = jsonObject11.getString("ID");
                        String user_login = jsonObject11.getString("user_login");
                        String user_pass = jsonObject11.getString("user_pass");
                        String user_nicename = jsonObject11.getString("user_nicename");
                        String user_email = jsonObject11.getString("user_email");
                        String user_url = jsonObject11.getString("user_url");
                        String user_registered = jsonObject11.getString("user_registered");
                        String user_activation_key = jsonObject11.getString("user_activation_key");
                        String user_status = jsonObject11.getString("user_status");
                        String display_name = jsonObject11.getString("display_name");

                        JSONObject jsonObjectt=jsonObject1.getJSONObject("current_plan");
                        String current_plant_name=jsonObjectt.getString("name");
                        String transaction_subject=jsonObjectt.getString("label");

//


                        method.editor.putString(method.Id, id.toString());
                        method.editor.putString(method.First_Name, first_name.toString());
                        method.editor.putString(method.Last_Name, last_name.toString());
                        method.editor.putString(method.user_login, user_login.toString());
                        method.editor.putString(method.user_pass,user_pass.toString());
                        method.editor.putString(method.user_nicename, user_nicename.toString());
                        method.editor.putString(method.user_email, user_email.toString());
                        method.editor.putString(method.user_url,user_url.toString());
                        method.editor.putString(method.user_registered, user_registered.toString());
                        method.editor.putString(method.user_activation_key, user_activation_key.toString());
                        method.editor.putString(method.user_status,user_status.toString());
                        method.editor.putString(method.display_name, google_firstname);
                        method.editor.putString(method.transaction_subject,transaction_subject.toString());
                        method.editor.putString(method.current_plane,current_plant_name.toString());
                        method.editor.putString(method.profile_image,social_profile_image.toString());

                        method.editor.putBoolean(method.pref_login, true);
                        method.editor.commit();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);

                        String message = String.valueOf(Html.fromHtml(jsonObject.getString("message")));
                       // Toast.makeText(LoginActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = String.valueOf(Html.fromHtml(jsonObject.getString("message")));
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN) {
            @SuppressLint("RestrictedApi") Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            handleSignInResult(task);
            Log.e("Show Error status code", "***********************" + statusCode);
            Log.e("Showing Error", "Error Handler : " + statusCode);
        }
//        mTwitterBtn.onActivityResult(requestCode, resultCode, data);
        else if(requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE)
        {
            client.onActivityResult(requestCode, resultCode, data);
        }
        else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
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
    public void fetchTwitterImage() {
        //check if user is already authenticated or not
        if (getTwitterSession() != null) {
            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
            Call<User> call = twitterApiClient.getAccountService().verifyCredentials(true, false, true);
            call.enqueue(new Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    User user = result.data;

                    String imageProfileUrl = user.profileImageUrl;
                    Log.e(TAG, "Data : " + imageProfileUrl);
                    TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                    TwitterAuthToken authToken = session.getAuthToken();
                    imageProfileUrl = imageProfileUrl.replace("_normal", "");
                    Log.e("imagegeeeee",imageProfileUrl.toString());
                 //   fetchTwitterEmail(session);

                    userId=session.getUserId();
                    TwitterId=String.valueOf(userId);
                    userName=session.getUserName();

                    String FullName=user.name;
                    String[] NewName=FullName.split(" ");
                    String FirstName=NewName[0];
                    String LastName=NewName[1];

                    Log.e("Name",userName.toString());
                    Log.e("firstname",FirstName.toString());
                    Log.e("lastname",LastName.toString());
                    Log.e("UserId", String.valueOf(userId));
                    Log.e("Twitter", TwitterId.toString());



                    if (method.isNetworkAvailable(LoginActivity.this)) {
                        TwitterAuthentication("social_login",userName, String.valueOf(TwitterId),regId,"twitter",imageProfileUrl,userName,FirstName,LastName);


                    } else {
                        method.alertBox(getResources().getString(R.string.internet_connection));
                    }



                }
                @Override
                public void failure(TwitterException exception) {
                    Toast.makeText(LoginActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //if user is not authenticated first ask user to do authentication
            Toast.makeText(this, "First to Twitter auth to Verify Credentials.", Toast.LENGTH_SHORT).show();
        }

    }
    public void fetchTwitterEmail(final TwitterSession twitterSession) {
        client.requestEmail(twitterSession, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                //here it will give u only email and rest of other information u can get from TwitterSession
                TwitterEmail = ("User Id : " + twitterSession.getUserId() + "\nScreen Name : " + twitterSession.getUserName() + "\nEmail Id : " + result.data);
                Log.e("twitterscreennn",TwitterEmail.toString());
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private TwitterSession getTwitterSession() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        //NOTE : if you want to get token and secret too use uncomment the below code
        /*TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;*/

        return session;
    }

}