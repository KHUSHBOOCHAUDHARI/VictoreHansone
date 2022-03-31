package com.business.victorehansone.Activity;

import static com.business.victorehansone.Util.Apis.ROOT_URLL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import com.bumptech.glide.Glide;
import com.business.victorehansone.Adapter.CommentAdapter;
import com.business.victorehansone.Model.CommentModel;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.Method;
import com.business.victorehansone.databinding.ActivityBlogDetailBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class BlogDetailActivity extends AppCompatActivity {
    private ActivityBlogDetailBinding binding;
    public static String newUltra="";
    CommentAdapter adapter;
    List<CommentModel> list;
    Method method;
    public static String tot_comments="";
    private Boolean isOver = false;
    public static int textSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(BlogDetailActivity.this,R.layout.activity_blog_detail);
        method=new Method(BlogDetailActivity.this);
        list=new ArrayList<>();
        binding.commentlist.setNestedScrollingEnabled(false);
        binding.commentlist.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(BlogDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.commentlist.setLayoutManager(layoutManager);
        binding.commentlist.setFocusable(false);
        if(method.pref.getBoolean(method.pref_login, false))
        {
            if(method.pref.getString(method.current_plane,null).equals("free_subscription") || method.pref.getString(method.current_plane,null).equals(""))
            {
                binding.adView.setVisibility(View.VISIBLE);
                MobileAds.initialize(BlogDetailActivity.this, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                      //  Toast.makeText(BlogDetailActivity.this, " sucesfull ", Toast.LENGTH_SHORT).show();
                    }
                });


                AdRequest adRequest = new AdRequest.Builder().build();
                binding.adView.loadAd(adRequest);
            }
            else {
                binding.adView.setVisibility(View.GONE);
                binding.adView.destroy();
                binding.adView.setVisibility(View.GONE);
            }
        }
        textSize= (int) binding.newsTxt.getTextSize();
        if (method.pref.getInt(String.valueOf(method.inc),0) == 0)
        {
            binding.newsTxt.setTextSize(12);

        }
        else
        {
            binding.newsTxt.setTextSize(method.pref.getInt(String.valueOf(method.inc),0));

        }



        if (method.pref.getInt(String.valueOf(method.dec),0) == 0)
        {
            binding.headlineTxt.setTextSize(14);
        }
        else
        {
            binding.headlineTxt.setTextSize(method.pref.getInt(String.valueOf(method.dec),0));
        }

        CommentListAPI();

            binding.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(Intent.ACTION_SEND);
                    myIntent.setType("text/plain");
                    String shareBody = getIntent().getStringExtra("link");
                    String shareSub = "Your subject";
                    myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                    myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(myIntent, "Share using"));
                }
            });
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                finish();
            }
        });
        PostComentAPI();
        BlodDetailAPI();


        fullscreen();
    }
    public void  BlodDetailpager(String Id) {
        binding.UDHLayout.setVisibility(View.GONE);
        binding.scrollview.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String Url = ROOT_URLL + getString(R.string.posts_detail_api)+ Id;
        client.addHeader("Authorization","Basic dmhhZG1pbjpWSEBBZG1pbjEyMyE=");
        Log.e("fkjklfkfjdslk",""+Url.toString());
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                        binding.scrollview.setVisibility(View.VISIBLE);
                        String id = jsonObject.getString("id");
                        String Date = jsonObject.getString("date");
                        String titlee = jsonObject.getString("title");
                        Log.e("djksahdjksad",String.valueOf(Html.fromHtml (titlee.toString())));
                        String Image=jsonObject.getString("jetpack_featured_media_url");
                        JSONObject json_title=jsonObject.getJSONObject("title");
                        String Title=json_title.getString("rendered");
                        JSONObject json_object = jsonObject.getJSONObject("content");
                        String rendered = json_object.getString("rendered");
                        JSONObject link_object=jsonObject.getJSONObject("guid");
                        String Link=link_object.getString("rendered");


                    binding.share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent myIntent = new Intent(Intent.ACTION_SEND);
                            myIntent.setType("text/plain");
                            String shareBody = Link;
                            String shareSub = "Your subject";
                            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                            myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(myIntent, "Share using"));
                        }
                    });

                    binding.share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent myIntent = new Intent(Intent.ACTION_SEND);
                            myIntent.setType("text/plain");
                            String shareBody = Link.toString();
                            String shareSub = "Your subject";
                            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                            myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(myIntent, "Share using"));
                        }
                    });






                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && BlogDetailActivity.this.isDestroyed()) {
                        return;
                    } else {
                        Glide.with(getApplicationContext()).load(Image).into(binding.image);

                    }



                          binding.vdhTxt.setText(newUltra.toString());
                        binding.dateTxt.setText(date_formatnew(Date.toString()));
                        binding.newsTxt.setText(Html.fromHtml(rendered.toString()));
                        binding.headlineTxt.setText(Html.fromHtml(Title.toString()));

                    binding.progressBar.setVisibility(View.GONE);


                    method.editor.putString(method.detailJsonObject, jsonObject.toString());
                    method.editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }
        });
    }
    //Comeent List
    private void CommentList(String Id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        String Url = ROOT_URLL + getString(R.string.comment_list_api) + "post=" + Id;
        client.addHeader("Authorization","Basic dmhhZG1pbjpWSEBBZG1pbjEyMyE=");
        Log.e("kfjdkfdsjkl",""+Url.toString());
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (BlogDetailActivity.this != null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);

                    try {
                        JSONArray jsonArray = new JSONArray(res);

                        if (jsonArray != null) {
                            tot_comments = String.valueOf(jsonArray.length());
                            Log.e("commenttxt", String.valueOf(tot_comments));
                            binding.commentTxt.setText(tot_comments+" Comments");
                            //holder.comment_txt.setText(tot_comments);
                        } else {
                            tot_comments = String.valueOf(0);
                            //holder.comment_txt.setText(tot_comments);
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String post = jsonObject.getString("post");
                            String author_name = jsonObject.getString("author_name");
                            String date_gmt=jsonObject.getString("date_gmt");
                            String date=jsonObject.getString("date");
                            JSONObject comment_object=jsonObject.getJSONObject("content");
                            String comment=comment_object.getString("rendered");
                            JSONObject author_avatar_urls_obj=jsonObject.getJSONObject("author_avatar_urls");
                            String author_avatar_urls=author_avatar_urls_obj.getString("48");
                            list.add(new CommentModel(id,author_name,date,author_avatar_urls,comment));


                        }
                        binding.progressBar.setVisibility(View.GONE);
                            adapter = new CommentAdapter(BlogDetailActivity.this,list );
                            binding.commentlist.setAdapter(adapter);
                            binding.textViewCategory.setVisibility(View.GONE);
                            binding.progressBar.setVisibility(View.GONE);
                            binding.progressbarLoadmore.setVisibility(View.GONE);
                            binding.commentlist.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        isOver = true;
                        binding.progressBar.setVisibility(View.GONE);
                        binding.progressbarLoadmore.setVisibility(View.GONE);
                        binding.textViewCategory.setVisibility(View.VISIBLE);
                        if (adapter != null) {
                            isOver = true;
                            binding.progressbarLoadmore.setVisibility(View.GONE);
                            binding.textViewCategory.setVisibility(View.GONE);
                        }
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressbarLoadmore.setVisibility(View.GONE);
            }
        });
    }
    //Post Api
    public void PostComment(String user_id, String post_id,String F,String comment_data) {
        binding.Progressbar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", user_id);
        params.add("post_id", post_id);
        params.add("f", F);
        params.add("comment_data",comment_data);
        Log.e("pastfsdf",params.toString());
        String url = Apis.ROOT_URLLL + getString(R.string.login_ic_user);
        String someData = "{\"user_id\":\""+user_id+"\",\"post_id\":\""+post_id+"\",\"f\":\""+F+"\",\"comment_data\":\""+ URLEncoder.encode(comment_data)+"\"}";

        StringEntity entity = null;
        try {
            entity = new StringEntity(someData);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch(Exception e) {

        }
        Log.e("lollo",entity.toString());
        client.addHeader("api-token","4db248bc10877bef6f3008ef64e5c76a");
        Log.e("Url------->", url);
        client.post(BlogDetailActivity.this,url, entity,"application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String res = new String(responseBody);
                Log.e("********************", "hUrrya :::::::::::::::::"+statusCode);
                Log.d("Response", new String(responseBody));
                Log.e("Urllll------->", res.toString());

                    if (statusCode == 200) {
                        Toast.makeText(getApplicationContext(),"Thank you for comment , we will check and approved the comment shortly.",Toast.LENGTH_LONG).show();
                        binding.msgTxt.setText("");
                        }
                    else {
                        binding.Progressbar.setVisibility(View.GONE);

                    }
                    binding.Progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.Progressbar.setVisibility(View.GONE);

                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }
        });
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
    public void fullscreen() {

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }
    public String date_formatnew(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date varDate = dateFormat.parse(strDate);

            dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            System.out.println("Date :" + dateFormat.format(varDate));
            return dateFormat.format(varDate);
        } catch (Exception e) {
            e.printStackTrace();
            return strDate;
        }


    }
    public void BlodDetailAPI() {
        if (getIntent().getStringExtra("Type").equals("Search"))
        {
            if (Method.isNetworkAvailable(BlogDetailActivity.this)) {
                BlodDetailpager(getIntent().getStringExtra("Id"));
                binding.titleTxt.setText(getIntent().getStringExtra("titlename"));
            }
            else
            {
                try {
                    if (!method.pref.getString(method.detailJsonObject, null).equals("") || method.pref.getString(method.detailJsonObject, null).isEmpty()) {
                        JSONObject jsonObject = new JSONObject(method.pref.getString(method.detailJsonObject, null));
                        binding.scrollview.setVisibility(View.VISIBLE);
                        String id = jsonObject.getString("id");
                        String Date = jsonObject.getString("date");
                        String titlee = jsonObject.getString("title");
                        Log.e("djksahdjksad", String.valueOf(Html.fromHtml(titlee.toString())));
                        String Image = jsonObject.getString("jetpack_featured_media_url");
                        JSONObject json_title = jsonObject.getJSONObject("title");
                        String Title = json_title.getString("rendered");
                        JSONObject json_object = jsonObject.getJSONObject("content");
                        String rendered = json_object.getString("rendered");
                        JSONObject link_object = jsonObject.getJSONObject("guid");
                        String Link = link_object.getString("rendered");
                        binding.share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (Method.isNetworkAvailable(BlogDetailActivity.this)) {
                                    Intent myIntent = new Intent(Intent.ACTION_SEND);
                                    myIntent.setType("text/plain");
                                    String shareBody = Link.toString();
                                    String shareSub = "Your subject";
                                    myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                                    myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                                    startActivity(Intent.createChooser(myIntent, "Share using"));
                                } else {
                                    method.alertBox(getResources().getString(R.string.internet_connection));
                                }
                            }
                        });
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && BlogDetailActivity.this.isDestroyed()) {
                            return;
                        } else {
                            Glide.with(getApplicationContext()).load(Image).into(binding.image);
                        }
                        method.editor.putString(method.detailJsonObject, jsonObject.toString());
                        method.editor.commit();
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

            }
        }
        else {
            binding.titleTxt.setText(Html.fromHtml(getIntent().getStringExtra("titlename")));
            binding.vdhTxt.setText(getIntent().getStringExtra("VDH"));
            binding.dateTxt.setText(getIntent().getStringExtra("date"));
            binding.newsTxt.setText(Html.fromHtml(getIntent().getStringExtra("news")));
            binding.headlineTxt.setText(Html.fromHtml(getIntent().getStringExtra("titlename")));
            binding.progressBar.setVisibility(View.GONE);
            if (binding.vdhTxt.getText().toString().equals("sorry")) {
                binding.UDHLayout.setVisibility(View.GONE);
            } else {
                binding.UDHLayout.setVisibility(View.VISIBLE);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && BlogDetailActivity.this.isDestroyed()) {
                return;
            } else {
                Glide.with(getApplicationContext()).load(getIntent().getStringExtra("image")).into(binding.image);
            }
        }
    }
    public void PostComentAPI() {
        binding.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(method.pref.getBoolean(method.pref_login, true))
                {
                    String Message = binding.msgTxt.getText().toString();
                    if (Message.equals("") || Message.isEmpty()) {
                        method.alertBox("Please enter Comment");
                    }
                    else {
                        if (Method.isNetworkAvailable(BlogDetailActivity.this)) {
                            PostComment(method.pref.getString(method.Id, null), getIntent().getStringExtra("Id"), "post_comment", binding.msgTxt.getText().toString());
                        }
                        else
                        {
                            Toast.makeText(BlogDetailActivity.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(BlogDetailActivity.this, "opps ! sorry. you cant comment .please login into app.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void CommentListAPI()
    {
        if (Method.isNetworkAvailable(BlogDetailActivity.this)) {
            CommentList(getIntent().getStringExtra("Id"));
        }

    }

}