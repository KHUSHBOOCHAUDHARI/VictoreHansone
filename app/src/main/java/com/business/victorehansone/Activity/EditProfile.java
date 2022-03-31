package com.business.victorehansone.Activity;

import static cz.msebera.android.httpclient.extras.Base64.encodeToString;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.Method;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import com.business.victorehansone.R;
import com.business.victorehansone.databinding.ActivityEditProfileBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class EditProfile extends AppCompatActivity {
    Method method;
    String FirstName, LastName;
    private boolean is_profile;
    Uri image_profile;
    private ActivityEditProfileBinding binding;
    Uri uri;
    Bitmap bitmap;
    public  static  String encodeString = null;
    String pt,uriString;
    byte[] bytes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(EditProfile.this, R.layout.activity_edit_profile);
        method = new Method(this);


        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                form();
            }
        });

        binding.imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(EditProfile.this)
                        .cropSquare()
                        .maxResultSize(800, 800)
                        .start();

            }
        });
        Glide.with(EditProfile.this).load(method.pref.getString(method.profile_image,null)).placeholder(R.drawable.user).into(binding.imgprofile);
        binding.firstnameEdt.setText(getIntent().getStringExtra("first_name"));
        binding.lastnameEdt.setText(getIntent().getStringExtra("last_name"));
        binding.emailEdt.setText(getIntent().getStringExtra("email_id"));

        fullscreen();
    }
    //Validation and Api Call..........
    @SuppressLint("LongLogTag")
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
//                if (!image_profile.equals("")) {
                    Log.e("image_profile_checked=","image_profile_checked");


                    EditProfile(FirstName, LastName, "edit_profile", method.pref.getString(method.Id, null), encodeString);

//                }
//                else
//                {
//                    Log.e("image_profile_checked_no=","image_profile_checked_no");
//                    EditProfile(FirstName, LastName, "edit_profile", method.pref.getString(method.Id, null),method.pref.getString(method.profile_image,null));
//                }
            } else {
                method.alertBox(getResources().getString(R.string.internet_connection));
            }
        }
    }

    public void EditProfile(String FirstName, String LastName,String F,String UserId,String profile_image) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("first_name", FirstName);
        params.add("last_name", LastName);
        params.add("user_id", UserId);
        params.add("f", F);

        params.add("profile_image", profile_image);
        Log.e("dsdsads",params.toString());

        String url = Apis.ROOT_URLLL + getString(R.string.login_ic_user);
        String someData = "{\"first_name\":\""+FirstName+"\",\"last_name\":\""+LastName+"\",\"f\":\""+F+"\",\"user_id\":\""+UserId+"\",\"profile_image\":\""+profile_image+"\"}";
        StringEntity entity = null;
        try {
            entity = new StringEntity(someData);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch(Exception e) {

        }

        client.addHeader("api-token","4db248bc10877bef6f3008ef64e5c76a");
        Log.e("Url------->", url);
        client.post(EditProfile.this,url, entity,"application/json", new AsyncHttpResponseHandler() {
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
                        String profile_image = jsonObject1.getString("social_profile_image");
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

                        if(!jsonObject1.getString("members_data").equals(""))
                        {
                            JSONObject jsonObject2=jsonObject1.getJSONObject("members_data");
                            String m_id=jsonObject2.getString("id");
                            String txn_id=jsonObject2.getString("txn_id");
                            String u_id=jsonObject2.getString("u_id");
                            JSONObject jsonObject3=jsonObject2.getJSONObject("payment_data");
                            String transaction_subject=jsonObject3.getString("transaction_subject");
                            method.editor.putString(method.m_id, m_id.toString());
                            method.editor.putString(method.txn_id,txn_id.toString());
                            method.editor.putString(method.u_id, u_id.toString());
                            method.editor.putString(method.transaction_subject,transaction_subject.toString());
                        }



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
                        method.editor.putString(method.display_name, display_name.toString());
                        method.editor.putString(method.profile_image, profile_image.toString());
                        method.editor.putBoolean(method.pref_login, true);
                        method.editor.commit();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                        String message = String.valueOf(Html.fromHtml(jsonObject.getString("message")));
                        Toast.makeText(EditProfile.this, message.toString(), Toast.LENGTH_SHORT).show();
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



    //Full Screen..........
    //Full Screen
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
    public void fullscreen() {

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(EditProfile.this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
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
    //For Background Image Select.....................
    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {
                is_profile = true;
                  image_profile = data.getData();
//                Bitmap bm = BitmapFactory.decodeFile(image_profile);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bm.compress(Bitmap.CompressFormat.JPEG, 10, baos); // bm is the bitmap object
//
//                byte[] b = baos.toByteArray();
//                 encodeString = Base64.encodeToString(b, Base64.DEFAULT);
//                 Log.e("uwgdusagdasjk",encodeString.toString());
               ConvertToString(image_profile);

                uri = Uri.fromFile(new File(data.getData().getPath()));
                Glide.with(EditProfile.this).load(image_profile).placeholder(R.drawable.ic_user).into(binding.imgprofile);
            }

    }


    public void ConvertToString(Uri uri){
        uriString = uri.toString();
        Log.d("data", "onActivityResult: uri"+uriString);

        try {
            InputStream in = getContentResolver().openInputStream(uri);
            bytes=getBytes(in);
            Log.d("data", "onActivityResult: bytes size="+bytes.length);
            Log.d("data", "onActivityResult: Base64string="+Base64.encodeToString(bytes,Base64.DEFAULT));
            encodeString = Base64.encodeToString(bytes,Base64.DEFAULT);
            pt=Base64.encodeToString(bytes,Base64.DEFAULT);
            Log.e("encodedd",encodeString.toString());
           // p=URLEncoder.encode(profile_image).replace("%2F","/").replaceAll("%0A","\\\r\\\n").replaceAll("\\%3D\\%3D","==");
            encodeString= encodeString.replaceAll("\\s+"," ");
           // encodeString= encodeString.replaceAll("%20","");
            Log.e("encodeddhhhh",encodeString.toString());
;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.d("error", "onActivityResult: " + e.toString());
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.e("dshdjksahdsjka",path.toString());
        return Uri.parse(path);
    }




}