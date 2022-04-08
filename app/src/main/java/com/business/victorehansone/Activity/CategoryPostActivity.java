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

import com.business.victorehansone.Adapter.CategoryAdapter;
import com.business.victorehansone.Adapter.HomeAdapter;
import com.business.victorehansone.Model.CategoryModel;
import com.business.victorehansone.Model.HomeModel;
import com.business.victorehansone.Model.SliderItems;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Method;
import com.business.victorehansone.databinding.ActivityCategoryPostBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CategoryPostActivity extends AppCompatActivity {
    private ActivityCategoryPostBinding binding;
    public static ProgressBar progressBar;
    Method method;
    List<HomeModel> list = new ArrayList<>();
    List<SliderItems> slide = new ArrayList<>();
    HomeAdapter adapter;
    public static String CategoryName = "";
    private Boolean isOver = false;
    public static String jkjk;
    public static String newUltra = "";
    private int oldPosition = 0;
    private int pagination_index = 1;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(CategoryPostActivity.this, R.layout.activity_category_post);

        method = new Method(CategoryPostActivity.this);
        binding.titleTxt.setText(getIntent().getStringExtra("name"));
        CategoryName = getIntent().getStringExtra("name");
        binding.homeRecyclerview.setNestedScrollingEnabled(false);
        binding.homeRecyclerview.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CategoryPostActivity.this, LinearLayoutManager.VERTICAL, false);
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




        if(method.pref.getBoolean(method.pref_login, false))
        {
            if(method.pref.getString(method.current_plane,null).equals("free_subscription") || method.pref.getString(method.current_plane,null).equals(""))
            {
                binding.adView.setVisibility(View.VISIBLE);
                MobileAds.initialize(CategoryPostActivity.this, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                       // Toast.makeText(CategoryPostActivity.this, " sucesfull ", Toast.LENGTH_SHORT).show();
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

        if (Method.isNetworkAvailable(CategoryPostActivity.this)) {
            CategoryPostList(String.valueOf(pagination_index), "10", getIntent().getStringExtra("iddd"));

        } else {

            if (getIntent().getStringExtra("iddd").equals("1125")) {
                if (!TextUtils.isEmpty(method.pref.getString(method.categorypostobject_1125, null))) {

                    SessionArray(method.pref.getString(method.categorypostobject_1125, null));

                }
            } else if (getIntent().getStringExtra("iddd").equals("1121")) {
                if (!TextUtils.isEmpty(method.pref.getString(method.categorypostobject_1121, null))) {


                    SessionArray(method.pref.getString(method.categorypostobject_1121, null));


                }
                else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.nestedScrollViewHomeFragment.setVisibility(View.GONE);
                    binding.textViewCategory.setVisibility(View.VISIBLE);
                    binding.textViewCategory.setText(getResources().getString(R.string.internet_connection));
                }

            } else if (getIntent().getStringExtra("iddd").equals("1122")) {
                if (!TextUtils.isEmpty(method.pref.getString(method.categorypostobject_1122, null))) {
                    SessionArray(method.pref.getString(method.categorypostobject_1122, null));

                }
                else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.nestedScrollViewHomeFragment.setVisibility(View.GONE);
                    binding.textViewCategory.setVisibility(View.VISIBLE);
                    binding.textViewCategory.setText(getResources().getString(R.string.internet_connection));
                }

            } else if (getIntent().getStringExtra("iddd").equals("1123")) {
                if (!TextUtils.isEmpty(method.pref.getString(method.categorypostobject_1123, null))) {

                    SessionArray(method.pref.getString(method.categorypostobject_1123, null));


                }
                else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.nestedScrollViewHomeFragment.setVisibility(View.GONE);
                    binding.textViewCategory.setVisibility(View.VISIBLE);
                    binding.textViewCategory.setText(getResources().getString(R.string.internet_connection));
                }

            } else if (getIntent().getStringExtra("iddd").equals("1124")) {
                if (!TextUtils.isEmpty(method.pref.getString(method.categorypostobject_1124, null))) {

                    SessionArray(method.pref.getString(method.categorypostobject_1124, null));


                }
                else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.nestedScrollViewHomeFragment.setVisibility(View.GONE);
                    binding.textViewCategory.setVisibility(View.VISIBLE);
                    binding.textViewCategory.setText(getResources().getString(R.string.internet_connection));
                }

            } else if (getIntent().getStringExtra("iddd").equals("1126")) {
                if (!TextUtils.isEmpty(method.pref.getString(method.categorypostobject_1126, null))) {

                    SessionArray(method.pref.getString(method.categorypostobject_1126, null));


                }
                else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.nestedScrollViewHomeFragment.setVisibility(View.GONE);
                    binding.textViewCategory.setVisibility(View.VISIBLE);
                    binding.textViewCategory.setText(getResources().getString(R.string.internet_connection));
                }

            } else if (getIntent().getStringExtra("iddd").equals("1131")) {
                if (!TextUtils.isEmpty(method.pref.getString(method.categorypostobject_1131, null))) {

                    SessionArray(method.pref.getString(method.categorypostobject_1131, null));


                }
                else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.nestedScrollViewHomeFragment.setVisibility(View.GONE);
                    binding.textViewCategory.setVisibility(View.VISIBLE);
                    binding.textViewCategory.setText(getResources().getString(R.string.internet_connection));
                }

            } else if (getIntent().getStringExtra("iddd").equals("1132")) {
                if (!TextUtils.isEmpty(method.pref.getString(method.categorypostobject_1132, null))) {

                    SessionArray(method.pref.getString(method.categorypostobject_1132, null));


                }
                else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.nestedScrollViewHomeFragment.setVisibility(View.GONE);
                    binding.textViewCategory.setVisibility(View.VISIBLE);
                    binding.textViewCategory.setText(getResources().getString(R.string.internet_connection));
                }

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

                                        if (Method.isNetworkAvailable(CategoryPostActivity.this)) {
                                            CategoryPostList(String.valueOf(pagination_index), "10", getIntent().getStringExtra("iddd"));
                                        } else {
                                            method.alertBox(getResources().getString(R.string.internet_connection));
                                            binding.progressBar.setVisibility(View.GONE);
                                            binding.progressbarLoadmore.setVisibility(View.GONE);
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
    private void CategoryPostList(String page, String per_page, String Category) {
        if (adapter == null) {
            list.clear();
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        AsyncHttpClient client = new AsyncHttpClient();
        String Url = ROOT_URLL + getString(R.string.posts_api) + "page=" + page + "&per_page=" + per_page + "&categories=" + Category;

        client.addHeader("Authorization", "Basic dmhhZG1pbjpWSEBBZG1pbjEyMyE=");
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
                              if (getIntent().getStringExtra("iddd").equals("1125")) {
                                method.editor.putString(method.categorypostobject_1125, jsonString.toString());

                              }  if (getIntent().getStringExtra("iddd").equals("1121")) {
                                method.editor.putString(method.categorypostobject_1121, jsonString.toString());

                            }  if (getIntent().getStringExtra("iddd").equals("1122")) {
                                method.editor.putString(method.categorypostobject_1122, jsonString.toString());

                            }  if (getIntent().getStringExtra("iddd").equals("1123")) {
                                method.editor.putString(method.categorypostobject_1123, jsonString.toString());

                            }  if (getIntent().getStringExtra("iddd").equals("1124")) {
                                method.editor.putString(method.categorypostobject_1124, jsonString.toString());

                            }  if (getIntent().getStringExtra("iddd").equals("1126")) {
                                method.editor.putString(method.categorypostobject_1126, jsonString.toString());

                            }  if (getIntent().getStringExtra("iddd").equals("1131")) {
                                method.editor.putString(method.categorypostobject_1131, jsonString.toString());

                            }  if (getIntent().getStringExtra("iddd").equals("1132")) {
                                method.editor.putString(method.categorypostobject_1132, jsonString.toString());

                            }
                            method.editor.commit();


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
                            list.add(new HomeModel(id, Image, newUltra, Date, "", Title, rendered, Link, Long_News));

                            if (jsonArray.length() == 0) {
                                if (adapter != null) {
                                    isOver = true;
                                    binding.textViewCategory.setVisibility(View.VISIBLE);
                                    binding.progressBar.setVisibility(View.GONE);
                                }
                            }
                            if (adapter == null) {
                                binding.textViewCategory.setVisibility(View.VISIBLE);

                                if (list.size() != 0) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    adapter = new HomeAdapter(CategoryPostActivity.this, list);
                                    binding.homeRecyclerview.setAdapter(adapter);
                                    binding.textViewCategory.setVisibility(View.GONE);
                                   // binding.textViewCategory.setVisibility(View.VISIBLE);
                                   // binding.textKeywordHide.setVisibility(View.VISIBLE);

                                } else {
                                    binding.textViewCategory.setVisibility(View.VISIBLE);

                                }
                            }
                            else {

                                adapter.notifyItemMoved(oldPosition, list.size());
                                adapter.notifyDataSetChanged();
                                binding.progressbarLoadmore.setVisibility(View.VISIBLE);
                                binding.textViewCategory.setVisibility(View.GONE);

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
                            binding.textViewCategory.setVisibility(View.VISIBLE);
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
    public void SessionArray(String jsonArray) {
        binding.textViewCategory.setVisibility(View.GONE);
        binding.nestedScrollViewHomeFragment.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);

        try {
            JSONArray jsonArrayy = new JSONArray(jsonArray);
            for (int i = 0; i < jsonArrayy.length(); i++) {
                JSONObject jsonObject = jsonArrayy.getJSONObject(i);
                String id = jsonObject.getString("id");
                String Date = jsonObject.getString("date");
                String titlee = jsonObject.getString("title");
                Log.e("djksahdjksad", String.valueOf(Html.fromHtml(titlee.toString())));
                String Image = jsonObject.getString("jetpack_featured_media_url");
                binding.nestedScrollViewHomeFragment.setVisibility(View.VISIBLE);
                JSONObject link_object = jsonObject.getJSONObject("guid");
                String Link = link_object.getString("rendered");
                JSONObject long_news_object = jsonObject.getJSONObject("content");
                String Long_News = long_news_object.getString("rendered");
                // ShareLink=Link;


                JSONObject json_title = jsonObject.getJSONObject("title");
                String Title = json_title.getString("rendered");
                JSONObject json_object = jsonObject.getJSONObject("yoast_head_json");
                String rendered="";
                if(json_object.has("og_description")) {
                    rendered = json_object.getString("og_description");
                }

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
                list.add(new HomeModel(id, Image, newUltra, Date, "", Title, rendered, Link, Long_News));
                String jsonString = jsonArray.toString();
//                        method.editor.putString(method.categorypostobject, jsonString.toString());
//                        method.editor.commit();

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
                        adapter = new HomeAdapter(CategoryPostActivity.this, list);
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
    }
}