package com.business.victorehansone.Fragment;

import static com.business.victorehansone.Util.Apis.ROOT_URLL;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.business.victorehansone.Activity.BlogDetailActivity;
import com.business.victorehansone.Activity.MainActivity;
import com.business.victorehansone.Activity.SubscribeActivity;
import com.business.victorehansone.Adapter.CategoryAdapter;
import com.business.victorehansone.Adapter.HomeAdapter;
import com.business.victorehansone.Adapter.SubscriptiobAdapter;
import com.business.victorehansone.Model.CategoryModel;
import com.business.victorehansone.Model.HomeModel;
import com.business.victorehansone.Model.SubscriptionModel;
import com.business.victorehansone.R;
import com.business.victorehansone.Util.Method;
import com.business.victorehansone.databinding.CategoryFragmentBinding;
import com.google.api.services.people.v1.model.Url;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CategoryFragment extends Fragment {
    CategoryFragmentBinding binding;
    private List<HomeModel> transactionModels;
    HomeAdapter TransactionHistoryAdapter;
    Method method;
    private Boolean isOver = false;
    public static String catid, keyword, discount_by, ic_user_id;
    private int oldPosition = 0;
    private int pagination_index = 1;
    String str = "1";
    List<CategoryModel> list = new ArrayList<>();
    CategoryAdapter adapter;
    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.category_fragment, container, false);
        MainActivity.txt_toolbartitle.setText(getResources().getString(R.string.list_category_txt));

        method = new Method(getActivity());

        binding.categoryRecyclerview.setHasFixedSize(true);
        binding.categoryRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        binding.categoryRecyclerview.setAdapter(adapter);
        String CatId="1125,1121,1122,1123,1124,1126,1131,1132";
        String NewEncode=URLEncoder.encode(CatId);

        if (method.pref.getInt(String.valueOf(method.dec),0) == 0)
        {
            binding.tranCategoryTxt.setTextSize(14);
        }
        else
        {
            binding.tranCategoryTxt.setTextSize(method.pref.getInt(String.valueOf(method.dec),0));
        }
        if (Method.isNetworkAvailable(getActivity())) {
            CaregoryList("view","1","20",NewEncode,"desc");
        } else {

            if (!TextUtils.isEmpty(method.pref.getString(method.categoryobject,null)))
            {

                binding.textViewCategory.setVisibility(View.GONE);
                binding.nestedScrollViewHomeFragment.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);
                try {
                    JSONArray jsonArray = new JSONArray(method.pref.getString(method.categoryobject,null));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");



                        Log.e("dadsa",name.toString());




                        list.add(new CategoryModel(id,name));
                    }
                    binding.progressBar.setVisibility(View.GONE);
                    Collections.reverse(list);
                    adapter= new CategoryAdapter(getActivity(), list);
                    binding.categoryRecyclerview.setAdapter(adapter);
                    binding.textViewCategory.setVisibility(View.GONE);

                    method.editor.putString(method.categoryobject, jsonArray.toString());
                    method.editor.commit();



                    binding.progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            }
            else
            {
                binding.progressBar.setVisibility(View.GONE);
                binding.nestedScrollViewHomeFragment.setVisibility(View.GONE);
                binding.textViewCategory.setVisibility(View.VISIBLE);
                binding.textViewCategory.setText(getResources().getString(R.string.internet_connection));
                method.alertBox(getResources().getString(R.string.internet_connection));
            }

        }



        return binding.getRoot();
    }

    public void CaregoryList(String context,String page,String per_page,String include,String order) {
        binding.progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
         String Url = ROOT_URLL + getString(R.string.category_api) + "context=" + context + "&page=" + page  + "&per_page=" + per_page +"&include=" + include + "&order="+order;

        client.addHeader("Authorization","Basic dmhhZG1pbjpWSEBBZG1pbjEyMyE=");
        Log.e("**********************",""+Url.toString());
        client.get(Url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String res = new String(responseBody);
                Log.e("Array Response","Response : " +res);
                try {
                    JSONArray jsonArray = new JSONArray(res);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        Log.e("dadsa",name.toString());
                        list.add(new CategoryModel(id,name));
                    }
                    if (list.size()==0)
                    {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.textViewCategory.setVisibility(View.VISIBLE);
                        binding.textViewCategory.setText("No Category Found");
                    }
                    else
                    {
                        binding.progressBar.setVisibility(View.GONE);
                        Collections.reverse(list);
                        adapter= new CategoryAdapter(getActivity(), list);
                        binding.categoryRecyclerview.setAdapter(adapter);
                        binding.textViewCategory.setVisibility(View.GONE);
                        method.editor.putString(method.categoryobject, jsonArray.toString());
                        method.editor.commit();
                    }




                    binding.progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                method.alertBox(getResources().getString(R.string.something_went_wrong));
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }





}
