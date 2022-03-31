package com.business.victorehansone.Activity;

import static com.business.victorehansone.Util.Apis.ROOT_URLL;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filterable;

import com.business.victorehansone.Adapter.HomeAdapter;
import com.business.victorehansone.Adapter.SearchAdapter;
import com.business.victorehansone.Model.HomeModel;
import com.business.victorehansone.Model.SearechModel;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Method;
import com.business.victorehansone.databinding.ActivitySearchBinding;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;
    List<SearechModel> list = new ArrayList<>();
    Method method;
    SearchAdapter adapter;
    private Boolean isOver = false;
    public static String ShareLink;
    public static String newUltra="";
    private int oldPosition = 0;
    private int pagination_index = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(SearchActivity.this,R.layout.activity_search);
        method = new Method(SearchActivity.this);

        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                finish();
            }
        });
        binding.searchlistener.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        fullscreen();
        binding.searchlistener.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                // When user changed the Text

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int before,int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!binding.searchlistener.getText().toString().equals("")) {
                    adapter = new SearchAdapter(SearchActivity.this,list);
                    binding.homeRecyclerview.setAdapter(adapter);


                    binding.textViewCategory.setVisibility(View.GONE);
                    binding.homeRecyclerview.setVisibility(View.VISIBLE);
                    binding.homeRecyclerview.setNestedScrollingEnabled(false);
                    binding.homeRecyclerview.setHasFixedSize(false);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    binding.homeRecyclerview.setLayoutManager(layoutManager);
                    binding.homeRecyclerview.setFocusable(false);
                    if (Method.isNetworkAvailable(SearchActivity.this)) {

                        SearchLoade(String.valueOf(pagination_index), "10",binding.searchlistener.getText().toString());

                        Log.e("lllll",binding.searchlistener.getText().toString());
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

                                                        if (Method.isNetworkAvailable(SearchActivity.this)) {
                                                            SearchLoade(String.valueOf(pagination_index), "10",binding.searchlistener.getText().toString());
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
                        adapter.getFilter().filter(s);

                    } else {

                        method.alertBox(getResources().getString(R.string.internet_connection));


                    }
                }
                else
                {
                    binding.textViewCategory.setVisibility(View.VISIBLE);
                    binding.homeRecyclerview.setVisibility(View.GONE);
                }



            }
        });








    }

    private void SearchLoade(String page, String per_page, String name) {
        if (adapter == null) {
            list.clear();
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        AsyncHttpClient client = new AsyncHttpClient();
        String Url = ROOT_URLL + getString(R.string.search_api) + "page=" + page  + "&per_page=" + per_page + "&search=" + URLEncoder.encode(name);

        client.addHeader("Authorization","Basic dmhhZG1pbjpWSEBBZG1pbjEyMyE=");
        Log.e("**********************",""+Url.toString());
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (getApplicationContext() != null) {
                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);

                    try {
                        JSONArray jsonArray = new JSONArray(res);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String Title = jsonObject.getString("title");
                            String get_string = String.valueOf(Html.fromHtml(Title.toString()));
                            Log.e("sorry",get_string.toString());
                            String str = "VDH Ultra";
                            String str_new;
                            if (get_string.startsWith(str)) {
                                str_new = str ;
                                newUltra="VDH Ultra";
                                Log.e("Get exact String ",get_string.toString());
                            }
                            else
                            {
                                String new_string = get_string.replaceAll("",str);
                                str_new = new_string;
                                newUltra="sorry";
                                Log.e("without ultra vdh",get_string.toString());
                            }



                            list.add(new SearechModel(id, Title,newUltra));
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

                                    binding.textViewCategory.setVisibility(View.GONE);

                                } else {
                                    binding.textViewCategory.setVisibility(View.VISIBLE);

                                }
                            }
                            else {

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