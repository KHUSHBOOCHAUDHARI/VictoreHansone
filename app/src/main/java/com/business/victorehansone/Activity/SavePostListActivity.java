package com.business.victorehansone.Activity;

import static com.business.victorehansone.Util.Apis.ROOT_URLL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.business.victorehansone.Adapter.HomeAdapter;
import com.business.victorehansone.Adapter.SavePostAdapter;
import com.business.victorehansone.Model.HomeModel;
import com.business.victorehansone.Model.SavePostModel;
import com.business.victorehansone.Model.SliderItems;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Apis;
import com.business.victorehansone.Util.Method;
import com.business.victorehansone.databinding.ActivitySavePostListBinding;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class SavePostListActivity extends AppCompatActivity {
    private ActivitySavePostListBinding binding;
    public static ProgressBar progressBar;
    Method method;
    List<SavePostModel> list = new ArrayList<>();
    SavePostAdapter adapter;
    public static String CategoryName = "";
    private Boolean isOver = false;
    public static String jkjk;
    public static String newUltra = "";
    private int oldPosition = 0;
    private int pagination_index = 1;
    String s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(SavePostListActivity.this, R.layout.activity_save_post_list);
        method = new Method(SavePostListActivity.this);
        binding.homeRecyclerview.setNestedScrollingEnabled(false);
        binding.homeRecyclerview.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SavePostListActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.homeRecyclerview.setLayoutManager(layoutManager);
        binding.homeRecyclerview.setFocusable(false);
        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                finish();
            }
        });
        getlikepost("get_postlike", method.pref.getString(method.Id, null));

        //  s = TextUtils.join(",", Collections.singleton());
        // Log.e("NewArraList",method.pref.getString(method.post_list_add, null));

        if (method.pref.getBoolean(method.pref_login, false)) {
            if (method.pref.getString(method.current_plane, null).equals("free_subscription") || method.pref.getString(method.current_plane,null).equals("")) {
                binding.adView.setVisibility(View.VISIBLE);
                MobileAds.initialize(SavePostListActivity.this, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                      //  Toast.makeText(SavePostListActivity.this, " sucesfull ", Toast.LENGTH_SHORT).show();
                    }
                });


                AdRequest adRequest = new AdRequest.Builder().build();
                binding.adView.loadAd(adRequest);
            } else {
                binding.adView.setVisibility(View.GONE);
                binding.adView.destroy();
                binding.adView.setVisibility(View.GONE);
            }
        }

        fullscreen();

        binding.nestedScrollViewHomeFragment.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {

                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            if (!isOver) {
                                oldPosition = list.size();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pagination_index++;
                                        binding.progressbarLoadmore.setVisibility(View.VISIBLE);

                                        if (Method.isNetworkAvailable(SavePostListActivity.this)) {
                                            PodcastLoad(String.valueOf(pagination_index), "10", method.pref.getString(method.post_list_add, null));
                                        } else {
                                            method.alertBox(getResources().getString(R.string.internet_connection));
                                            binding.progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                }, 1000);
                            } else {
                            }
                        }
                    }
                }
            }
        });


    }

    public void getlikepost(String f, String userid) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("f", f);
        params.add("user_id", userid);

        String url = Apis.ROOT_URLLL + getString(R.string.login_ic_user);
        String someData = "{\"f\":\"" + f + "\",\"user_id\":\"" + userid + "\"}";
        StringEntity entity = null;
        try {
            entity = new StringEntity(someData);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        } catch (Exception e) {

        }

        client.addHeader("api-token", "4db248bc10877bef6f3008ef64e5c76a");
        Log.e("Url------->", url);
        client.post(SavePostListActivity.this, url, entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                Log.e("Url------->", res.toString());
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        String postid_string = jsonObject.getString("data");
                        Log.e("postposu", postid_string.toString());
                        if (Method.isNetworkAvailable(SavePostListActivity.this)) {
                            PodcastLoad(String.valueOf(pagination_index), "10", postid_string);

                        } else {

                            if (!TextUtils.isEmpty(method.pref.getString(method.categorypostobjectnew, null))) {

                                binding.textViewCategory.setVisibility(View.GONE);
                                binding.nestedScrollViewHomeFragment.setVisibility(View.VISIBLE);


                                try {
                                    JSONArray jsonArray = new JSONArray(method.pref.getString(method.categorypostobjectnew, null));
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String id = jsonObject1.getString("id");
                                        String Date = jsonObject1.getString("date");
                                        String titlee = jsonObject1.getString("title");
                                        Log.e("djksahdjksad", String.valueOf(Html.fromHtml(titlee.toString())));
                                        String Image = jsonObject1.getString("jetpack_featured_media_url");
                                        binding.nestedScrollViewHomeFragment.setVisibility(View.VISIBLE);
                                        JSONObject link_object = jsonObject1.getJSONObject("guid");
                                        String Link = link_object.getString("rendered");
                                        JSONObject long_news_object = jsonObject.getJSONObject("content");
                                        String Long_News = long_news_object.getString("rendered");
                                        // ShareLink=Link;


                                        JSONObject json_title = jsonObject.getJSONObject("title");
                                        String Title = json_title.getString("rendered");
                                        JSONObject json_object = jsonObject.getJSONObject("excerpt");
                                        String rendered = json_object.getString("rendered");
                                        Log.e("llll", String.valueOf(Html.fromHtml(Title.toString())));
                                        String str_new;
                                        String get_string = String.valueOf(Html.fromHtml(Title.toString()));
                                        Log.e("sorry", get_string.toString());
                                        String str = "VDH Ultra";

                                        if (get_string.startsWith(str)) {
                                            str_new = str;
                                            newUltra = "VDH Ultra";
                                            Log.e("Get exact String ", get_string.toString());
                                        } else {
                                            String new_string = get_string.replaceAll("", str);
                                            str_new = new_string;
                                            newUltra = "sorry";
                                            Log.e("without ultra vdh", get_string.toString());
                                        }
                                        list.add(new SavePostModel(id, Image, newUltra, Date, SavePostAdapter.tot_comments.toString(), Title, rendered, Link, Long_News));
                                        String jsonString = jsonArray.toString();
                                        method.editor.putString(method.categorypostobjectnew, jsonString.toString());
                                        method.editor.commit();

                                        if (jsonArray.length() == 0) {
                                            if (adapter != null) {
                                                isOver = true;
                                                binding.progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                        if (adapter == null) {
                                            binding.textViewCategory.setVisibility(View.VISIBLE);

                                            if (list.size() != 0) {
                                                binding.progressBar.setVisibility(View.GONE);
                                                adapter = new SavePostAdapter(SavePostListActivity.this, list);
                                                binding.homeRecyclerview.setAdapter(adapter);
                                                binding.textViewCategory.setVisibility(View.GONE);

                                            } else {
                                                binding.textViewCategory.setVisibility(View.VISIBLE);

                                            }
                                        } else {

                                            adapter.notifyItemMoved(oldPosition, list.size());
                                            adapter.notifyDataSetChanged();
                                            binding.progressbarLoadmore.setVisibility(View.VISIBLE);


                                        }
                                    }

                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.progressbarLoadmore.setVisibility(View.GONE);
                                    binding.linearLayoutHomeAdapter.setVisibility(View.VISIBLE);

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

                            } else {
                                binding.progressBar.setVisibility(View.GONE);
                                binding.nestedScrollViewHomeFragment.setVisibility(View.GONE);
                                binding.textViewCategory.setVisibility(View.VISIBLE);
                                binding.textViewCategory.setText("No Post Found");
                            }

                        }
                    }
                    else {
                        binding.progressBar.setVisibility(View.GONE);
                        String message = jsonObject.getString("message");
                       // method.alertBox(message);
                        binding.textViewCategory.setVisibility(View.VISIBLE);
                        binding.textViewCategory.setText(message.toString());
                    }


                } catch (JSONException e) {
                    binding.progressBar.setVisibility(View.GONE);
                    e.printStackTrace();

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                binding.progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.something_went_wrong));
            }
        });
    }

    private void PodcastLoad(String page, String per_page, String Include) {
        if (adapter == null) {
            list.clear();
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Basic dmhhZG1pbjpWSEBBZG1pbjEyMyE=");
        String Url = ROOT_URLL + getString(R.string.posts_api) + "page=" + page + "&per_page=" + per_page + "&include=" + Include;


        Log.e("**********************", "" + Url.toString());
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (getApplicationContext() != null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);

                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String Date = jsonObject.getString("date");
                            String titlee = jsonObject.getString("title");
                            Log.e("djksahdjksad", String.valueOf(Html.fromHtml(titlee.toString())));
                            String Image = jsonObject.getString("jetpack_featured_media_url");
                            JSONObject json_title = jsonObject.getJSONObject("title");
                            String Title = json_title.getString("rendered");
                            JSONObject json_object = jsonObject.getJSONObject("yoast_head_json");
                            String rendered="";
                            if(json_object.has("og_description")) {
                                rendered = json_object.getString("og_description");
                            }
                            JSONObject link_object = jsonObject.getJSONObject("guid");
                            String Link = link_object.getString("rendered");

                            Log.e("llll", String.valueOf(Html.fromHtml(Title.toString())));
                            String str_new;
                            String get_string = String.valueOf(Html.fromHtml(Title.toString()));
                            Log.e("sorry", get_string.toString());
                            String str = "VDH Ultra";
                            String jsonString = jsonArray.toString();
                            JSONObject long_news_object = jsonObject.getJSONObject("content");
                            String Long_News = long_news_object.getString("rendered");
                            method.editor.putString(method.categorypostobjectnew, jsonString.toString());
                            method.editor.commit();
                            binding.progressBar.setVisibility(View.GONE);
                            if (get_string.startsWith(str)) {
                                str_new = str;
                                newUltra = "VDH Ultra";
                                Log.e("Get exact String ", get_string.toString());
                            } else {
                                String new_string = get_string.replaceAll("", str);
                                str_new = new_string;
                                newUltra = "sorry";
                                Log.e("without ultra vdh", get_string.toString());
                            }
                            list.add(new SavePostModel(id, Image, newUltra, Date, SavePostAdapter.tot_comments.toString(), Title, rendered, Link, Long_News));

                            if (jsonArray.length() == 0) {
                                if (adapter != null) {
                                    isOver = true;
                                    binding.progressBar.setVisibility(View.GONE);
                                }
                            }
                            if (adapter == null) {
                                binding.progressBar.setVisibility(View.GONE);
                                binding.textViewCategory.setVisibility(View.VISIBLE);

                                if (list.size() != 0) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    adapter = new SavePostAdapter(SavePostListActivity.this, list);
                                    binding.homeRecyclerview.setAdapter(adapter);
                                    binding.textViewCategory.setVisibility(View.GONE);

                                } else {
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.textViewCategory.setVisibility(View.VISIBLE);

                                }
                            } else {

                                adapter.notifyItemMoved(oldPosition, list.size());
                                adapter.notifyDataSetChanged();
                                binding.progressbarLoadmore.setVisibility(View.VISIBLE);


                            }
                        }

                        binding.progressBar.setVisibility(View.GONE);
                        binding.progressbarLoadmore.setVisibility(View.GONE);
                        binding.linearLayoutHomeAdapter.setVisibility(View.VISIBLE);

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