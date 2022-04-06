package com.business.victorehansone.Fragment;

import static com.business.victorehansone.Activity.MainActivity.drawer;
import static com.business.victorehansone.Activity.MainActivity.txt_toolbartitle;
import static com.business.victorehansone.Util.Apis.ROOT_URLL;
import static com.business.victorehansone.Util.Apis.ROOT_URLLL;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.business.victorehansone.Activity.CategoryPostActivity;
import com.business.victorehansone.Activity.MainActivity;
import com.business.victorehansone.Activity.SearchActivity;
import com.business.victorehansone.Adapter.HomeAdapter;
import com.business.victorehansone.Adapter.SliderAdapter;
import com.business.victorehansone.Model.HomeModel;
import com.business.victorehansone.Model.SliderItems;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Method;
import com.business.victorehansone.Util.OnBackPressedListener;
import com.business.victorehansone.databinding.HomeFragmentBinding;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment implements OnLikeListener, OnAnimationEndListener {
    public static final int ITEMS_PER_AD = 9;
    public static ProgressBar progressBar;
    public static String TYTPE = "";
    HomeFragmentBinding binding;
    Method method;
    List<HomeModel> list = new ArrayList<>();
    List<AdView> list1 = new ArrayList<AdView>();
    List<SliderItems> slide = new ArrayList<>();
    HomeAdapter adapter;
    private Boolean isOver = false;
    public static String ShareLink;
    public static String newUltra = "";
    private int oldPosition = 0;
    private int pagination_index = 1;
    HomeModel homeModel;

    boolean doubleBackToExitPressedOnce = false;
    boolean disableEvent = false;
    public static String LAYOUT="1";
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        method = new Method(getActivity());
        txt_toolbartitle.setText("Home");
        progressBar = binding.progressBar;
        Log.e("LAYOUT",LAYOUT.toString());
        binding.buttonSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SearchActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });
        setRecyclerView();
        FeaturePostAPI();
        PostAPI();
        Pagination();
        return binding.getRoot();
    }


    private void Post(String page, String per_page) {
        if (adapter == null) {
            list.clear();
            binding.nestedScrollViewHomeFragment.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        AsyncHttpClient client = new AsyncHttpClient();
        String Url = ROOT_URLL + getString(R.string.posts_api) + "page=" + page + "&per_page=" + per_page;
        client.addHeader("Authorization", "Basic dmhhZG1pbjpWSEBBZG1pbjEyMyE=");
        Log.e("**********************", "" + Url.toString());
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (getActivity() != null) {
                    Log.d("ResponseNew", new String(responseBody));
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
                            JSONObject long_news_object = jsonObject.getJSONObject("content");
                            String Long_News = long_news_object.getString("rendered");
                            JSONObject link_object = jsonObject.getJSONObject("guid");
                            String Link = link_object.getString("rendered");
                            JSONObject json_title = jsonObject.getJSONObject("title");
                            String Title = json_title.getString("rendered");
                            JSONObject json_object = jsonObject.getJSONObject("yoast_head_json");
                            String rendered = "";
                            if (json_object.has("og_description")) {
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
                            list.add(new HomeModel(id, Image, newUltra, Date, HomeAdapter.tot_comments.toString(), Title, rendered, Link, Long_News));
                            String jsonString = jsonArray.toString();
                            method.editor.putString(method.JsonString, jsonString.toString());
                            method.editor.commit();
                            Log.e("Commenstssss", HomeAdapter.tot_comments.toString());
                            binding.nestedScrollViewHomeFragment.setVisibility(View.VISIBLE);
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
                                    adapter = new HomeAdapter(getActivity(), list);
                                    binding.homeRecyclerview.setAdapter(adapter);
                                    binding.textViewCategory.setVisibility(View.GONE);
                                } else {
                                    binding.textViewCategory.setVisibility(View.VISIBLE);
                                }
                            } else {
                                adapter.notifyItemMoved(oldPosition, list.size());
                                adapter.notifyDataSetChanged();
                                binding.progressbarLoadmore.setVisibility(View.VISIBLE);
                                binding.nestedScrollViewHomeFragment.setVisibility(View.VISIBLE);
                            }
                        }
                        binding.progressBar.setVisibility(View.GONE);
                        binding.progressbarLoadmore.setVisibility(View.GONE);
                        binding.linearLayoutHomeAdapter.setVisibility(View.VISIBLE);
                        binding.nestedScrollViewHomeFragment.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        isOver = true;
                        binding.progressBar.setVisibility(View.GONE);
                        binding.progressbarLoadmore.setVisibility(View.GONE);
                        binding.textViewCategory.setVisibility(View.VISIBLE);
                        binding.nestedScrollViewHomeFragment.setVisibility(View.VISIBLE);
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
                binding.nestedScrollViewHomeFragment.setVisibility(View.VISIBLE);
            }
        });
    }

    private void FeaturePostList(String Category) {
        if (adapter == null) {
            list.clear();
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        AsyncHttpClient client = new AsyncHttpClient();
        String Url = ROOT_URLLL + "wp-json/wp/v2/" + getString(R.string.posts_api) + "categories=" + Category;
        client.addHeader("Authorization", "Basic dmhhZG1pbjpWSEBBZG1pbjEyMyE=");
        Log.e("**********************", "" + Url.toString());
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (getActivity() != null) {
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
                            JSONObject long_news_object = jsonObject.getJSONObject("content");
                            String Long_News = long_news_object.getString("rendered");
                            JSONObject link_object = jsonObject.getJSONObject("guid");
                            String Link = link_object.getString("rendered");
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
                            slide.add(new SliderItems(id, Image, newUltra, Date, "", Title, rendered, Link, Long_News));
                            method.editor.putString(method.featurepost, jsonArray.toString());
                            method.editor.commit();
                        }
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

    @Override
    public void liked(LikeButton likeButton) {
        //  Toast.makeText(getActivity(), "Liked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        //    Toast.makeText(getActivity(), "Disliked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAnimationEnd(LikeButton likeButton) {
        Log.d("fjdgfjkdgdsk", "Animation End for %s" + likeButton);
    }

    public void FeaturePostAPI() {
        if (Method.isNetworkAvailable(getActivity())) {
            FeaturePostList("1149");
            SliderAdapter adapterr = new SliderAdapter(slide, getActivity());
            binding.viewPager.setHasFixedSize(true);
            binding.viewPager.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            binding.viewPager.setAdapter(adapterr);
        } else {
            if (!TextUtils.isEmpty(method.pref.getString(method.featurepost, null))) {
                try {
                    JSONArray jsonArray = new JSONArray(method.pref.getString(method.featurepost, null));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String Date = jsonObject.getString("date");
                        String titlee = jsonObject.getString("title");
                        Log.e("djksahdjksad", String.valueOf(Html.fromHtml(titlee.toString())));
                        String Image = jsonObject.getString("jetpack_featured_media_url");
                        JSONObject long_news_object = jsonObject.getJSONObject("content");
                        String Long_News = long_news_object.getString("rendered");

                        JSONObject link_object = jsonObject.getJSONObject("guid");
                        String Link = link_object.getString("rendered");
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
                        method.editor.putString(method.featurepost, jsonArray.toString());
                        method.editor.commit();
                        slide.add(new SliderItems(id, Image, newUltra, Date, "", Title, rendered, Link, Long_News));
                    }
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
            } else {
                Log.e("second", "second");
                binding.progressBar.setVisibility(View.GONE);
                binding.nestedScrollViewHomeFragment.setVisibility(View.GONE);
            }
        }
    }

    public void PostAPI() {
        if (Method.isNetworkAvailable(getActivity())) {
            Post(String.valueOf(pagination_index), "10");
        } else {
            binding.textViewCategory.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(method.pref.getString(method.JsonString, null))) {
                Log.e("First", "First");
                binding.textViewCategory.setVisibility(View.GONE);
                binding.nestedScrollViewHomeFragment.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);
                try {
                    JSONArray jsonArray = new JSONArray(method.pref.getString(method.JsonString, null));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        binding.textViewCategory.setVisibility(View.GONE);
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
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
                        JSONObject json_title = jsonObject.getJSONObject("title");
                        String Title = json_title.getString("rendered");
                        JSONObject json_object = jsonObject.getJSONObject("yoast_head_json");
                        String rendered = "";
                        if (json_object.has("og_description")) {
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
                        list.add(new HomeModel(id, Image, newUltra, Date, "6 Comments", Title, rendered, Link, Long_News));
                        String jsonString = jsonArray.toString();
                        method.editor.putString(method.JsonString, jsonString.toString());
                        method.editor.commit();
                        if (jsonArray.length() == 0) {
                            if (adapter != null) {
                                isOver = true;
                                binding.progressBar.setVisibility(View.GONE);
                            }
                        }
                        if (adapter == null) {
                            if (list.size() != 0) {
                                binding.progressBar.setVisibility(View.GONE);
                                adapter = new HomeAdapter(getActivity(), list);
                                binding.homeRecyclerview.setAdapter(adapter);
                                binding.textViewCategory.setVisibility(View.GONE);
                            } else {
                            }
                        } else {

                            adapter.notifyItemMoved(oldPosition, list.size());
                            adapter.notifyDataSetChanged();
                            binding.progressbarLoadmore.setVisibility(View.GONE);
                            binding.progressBar.setVisibility(View.GONE);
                            binding.textViewCategory.setText("No Post Found");
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
                    binding.textViewCategory.setText("No Post Found");
                    if (adapter != null) {
                        isOver = true;
                        binding.progressbarLoadmore.setVisibility(View.GONE);
                        binding.textViewCategory.setVisibility(View.GONE);
                    }
                }
            } else {
                Log.e("second", "second");
                binding.progressBar.setVisibility(View.GONE);
                binding.nestedScrollViewHomeFragment.setVisibility(View.GONE);
                binding.textViewCategory.setVisibility(View.VISIBLE);
                binding.textViewCategory.setText(getResources().getString(R.string.internet_connection));
                method.alertBox(getResources().getString(R.string.internet_connection));
            }

        }
    }

    public void Pagination() {


    }


    public void setRecyclerView() {

        if (!TextUtils.isEmpty(method.pref.getString(method.layoutvalue,null))) {
            if (method.pref.getString(method.layoutvalue,null).equals("1"))
            {
                binding.homeRecyclerview.setNestedScrollingEnabled(false);
                binding.homeRecyclerview.setHasFixedSize(false);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                binding.homeRecyclerview.setLayoutManager(layoutManager);
                binding.homeRecyclerview.setFocusable(false);
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

                                                if (Method.isNetworkAvailable(getActivity())) {
                                                    binding.nestedScrollViewHomeFragment.setVisibility(View.VISIBLE);
                                                    Post(String.valueOf(pagination_index), "15");
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
            else if (method.pref.getString(method.layoutvalue,null).equals("2")) {
                binding.homeRecyclerview.setNestedScrollingEnabled(false);
                binding.homeRecyclerview.setHasFixedSize(false);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                binding.homeRecyclerview.setLayoutManager(gridLayoutManager);
                binding.homeRecyclerview.setFocusable(false);
                binding.nestedScrollViewHomeFragment.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        if (v.getChildAt(v.getChildCount() - 1) != null) {
                            if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                                    scrollY > oldScrollY) {

                                int visibleItemCount = gridLayoutManager.getChildCount();
                                int totalItemCount = gridLayoutManager.getItemCount();
                                int pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                    if (!isOver) {
                                        oldPosition = list.size();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                pagination_index++;
                                                binding.progressbarLoadmore.setVisibility(View.VISIBLE);

                                                if (Method.isNetworkAvailable(getActivity())) {
                                                    binding.nestedScrollViewHomeFragment.setVisibility(View.VISIBLE);
                                                    Post(String.valueOf(pagination_index), "15");
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

        }
        else
        {
            binding.homeRecyclerview.setNestedScrollingEnabled(false);
            binding.homeRecyclerview.setHasFixedSize(false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            binding.homeRecyclerview.setLayoutManager(layoutManager);
            binding.homeRecyclerview.setFocusable(false);
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

                                            if (Method.isNetworkAvailable(getActivity())) {
                                                binding.nestedScrollViewHomeFragment.setVisibility(View.VISIBLE);
                                                Post(String.valueOf(pagination_index), "15");
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
    }




}
